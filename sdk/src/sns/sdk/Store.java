package sns.sdk;

import java.util.function.Function;

/**
 * Persistence abstraction. Built-in backends are a MySQL-compatible SQL server, a
 * file per collection, or in-memory; a plugin may supply its own by implementing
 * this interface. Mirrors the {@code sns.sdk.protocols/Store} protocol. {@code
 * coll} is a collection keyword and {@code id} a document key.
 */
public interface Store {

    /**
     * Prepare the backend for use (e.g. create a schema, ensure a directory
     * exists). Called once at startup, before any other method; construction
     * itself must stay side-effect-free. Default is a no-op.
     */
    default void setup() {}

    /** Return the document at {@code id}, or {@code null}. */
    Object fetch(Object coll, Object id);

    /** Return documents in {@code coll} matching query map {@code q}. */
    Object query(Object coll, Object q);

    /** Insert or replace the document at {@code id}. Returns {@code doc}. */
    Object put(Object coll, Object id, Object doc);

    /**
     * Atomically apply {@code f} to the document at {@code id} and store the
     * result. Returns the new document. Invoke {@code f} with the current document,
     * e.g. {@code f.invoke(current)}.
     */
    Object update(Object coll, Object id, Function<?, ?> f);
}
