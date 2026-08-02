#!/usr/bin/env bash
# Download the latest S&S Companion native image for this machine and make it
# runnable. Usage: install.sh [target-directory]   (defaults to the current one)
set -euo pipefail

repo="spies-and-spiders/companion"
dir="${1:-$PWD}"

case "$(uname -s)" in
  Darwin) os=macos ;;
  Linux)  os=linux ;;
  *)      echo "Unsupported OS: $(uname -s). On Windows, use install.ps1." >&2; exit 1 ;;
esac

case "$(uname -m)" in
  arm64|aarch64)  arch=arm64 ;;
  x86_64|amd64)   arch=x64 ;;
  *)              echo "Unsupported architecture: $(uname -m)" >&2; exit 1 ;;
esac

[ -d "$dir" ] || { echo "No such directory: $dir" >&2; exit 1; }
dest="$(cd "$dir" && pwd)/companion"

release="$(curl -fsSL "https://api.github.com/repos/${repo}/releases/latest")"
version="$(printf '%s' "$release" | grep -o '"tag_name": *"[^"]*"' | cut -d'"' -f4)"
url="$(printf '%s' "$release" \
  | grep -o '"browser_download_url": *"[^"]*"' | cut -d'"' -f4 \
  | grep -E "companion-[^/]*-${os}-${arch}\.gz$" | head -1 || true)"
[ -n "$url" ] || { echo "No ${os}-${arch} asset in release ${version}." >&2; exit 1; }

tmp="$(mktemp -d)"
trap 'rm -rf "$tmp"' EXIT

echo "Downloading ${version} (${os}-${arch})..."
curl -fsSL --retry 3 -o "$tmp/companion.gz" "$url"
gunzip -c "$tmp/companion.gz" > "$dest"
chmod 755 "$dest"

# Only set when a browser did the download, but harmless to clear either way.
if [ "$os" = macos ]; then
  xattr -d com.apple.quarantine "$dest" 2>/dev/null || true
fi

echo "Installed ${version} to ${dest}"
