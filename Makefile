# sns-companion — common dev / build / run commands.
# Run `make` (or `make help`) to see everything available.

.DEFAULT_GOAL := help

VERSION := $(shell cat VERSION)
NATIVE_IMAGE := target/companion-$(VERSION)-standalone
UBERJAR := $(NATIVE_IMAGE).jar

.PHONY: help
help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) \
		| awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-16s\033[0m %s\n", $$1, $$2}'

# --- setup -------------------------------------------------------------------

.PHONY: deps
deps: ## Install npm dependencies (shadow-cljs, playwright)
	npm install

.PHONY: prep
prep: ## Compile the SPI Java interfaces (required before running/building)
	clojure -X:deps prep

# --- run (development) -------------------------------------------------------

.PHONY: run
run: prep ## Run the backend server (API + SPA on http://localhost:8080)
	clojure -M -m sns.server.core

.PHONY: watch
watch: ## Hot-reload the frontend (shadow-cljs dev server; run alongside `make run`)
	npm run watch

.PHONY: repl
repl: prep ## Start a backend REPL with the :dev alias
	clojure -M:dev

# --- build -------------------------------------------------------------------

.PHONY: frontend
frontend: ## Build the optimised CLJS release bundle (resources/public/js)
	npm run release

.PHONY: uber
uber: prep frontend ## Build the standalone uberjar (builds the frontend first)
	clojure -T:build uber

.PHONY: dist
dist: uber ## Full production artifact (alias for `uber`)

.PHONY: spi-jar
spi-jar: ## Build the SPI library jar for plugin authors (target/sns-spi-<version>.jar)
	clojure -T:build spi-jar

.PHONY: config-schema
config-schema: prep ## Generate config.schema.json (JSON Schema for config.json)
	clojure -M -m sns.server.schema-gen

.PHONY: graalvm
graalvm: prep frontend ## Build GraalVM Native Image
	clojure -T:build uber :aliases '[:graalvm]'
	native-image -jar $(UBERJAR) \
	    -classpath target/classes \
	    -o $(NATIVE_IMAGE) \
	    --features=clj_easy.graal_build_time.InitClojureClasses

# --- test --------------------------------------------------------------------

.PHONY: test
test: prep ## Run the Clojure test suite
	clojure -M:test

.PHONY: reflection
reflection: prep ## Fail if any backend namespace compiles with reflection
	clojure -M:reflect

.PHONY: check
check: test reflection ## Run tests + the reflection guard

# --- artifacts ---------------------------------------------------------------

.PHONY: run-jar
run-jar: ## Run the built uberjar (run `make uber` first)
	java -jar $(UBERJAR)

.PHONY: clean
clean: ## Remove build artifacts (target/ and compiled JS)
	clojure -T:build clean
	rm -rf resources/public/js
