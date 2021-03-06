package cs.ecs.dacatoref;

import co.ecso.dacato.config.ApplicationConfig;
import co.ecso.dacato.database.CachedDatabaseEntity;
import co.ecso.dacato.database.ColumnList;
import co.ecso.dacato.database.DatabaseEntity;
import co.ecso.dacato.database.cache.Cache;
import co.ecso.dacato.database.querywrapper.DatabaseField;
import co.ecso.dacato.database.querywrapper.DatabaseResultField;
import co.ecso.dacato.database.querywrapper.SingleColumnQuery;
import java.sql.Types;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * CachedCustomer.
 *
 * @author Christian Scharmach (cs@e-cs.co)
 * @version $Id:$
 * @since 17.09.16
 */
@SuppressWarnings("unused")
final class CachedCustomer implements CachedDatabaseEntity<Long> {

    private final ApplicationConfig config;
    private final Long id;
    private static final String TABLE_NAME = "customer";
    private static final String QUERY = String.format("SELECT %%s FROM %s WHERE id = ?", TABLE_NAME);
    private final AtomicBoolean objectValid = new AtomicBoolean(true);

    CachedCustomer(final ApplicationConfig config, final Long id) {
        this.config = config;
        this.id = id;
    }

    @Override
    public ApplicationConfig config() {
        return config;
    }

    @Override
    public Long primaryKey() {
        return id;
    }

    @Override
    public CompletableFuture<DatabaseEntity<Long>> save(final ColumnList values) {
        return null;
    }

    @Override
    public Cache cache() {
        return RefApplicationConfig.CACHE;
    }

    public CompletableFuture<DatabaseResultField<String>> firstName() {
        return this.findOne(new SingleColumnQuery<>(QUERY, Fields.FIRST_NAME, Fields.ID, this.primaryKey()), () ->
        this.objectValid);
    }

    public CompletableFuture<DatabaseResultField<String>> lastName() {
        return this.findOne(new SingleColumnQuery<>(QUERY, Fields.LAST_NAME, Fields.ID, this.primaryKey()), () -> this.objectValid);
    }

    public CompletableFuture<DatabaseResultField<Long>> number() {
        return this.findOne(new SingleColumnQuery<>(QUERY, Fields.NUMBER, Fields.ID, this.primaryKey()), () ->
        this.objectValid);
    }

    static final class Fields {
        static final DatabaseField<Long> ID = new DatabaseField<>("id", Long.class, Types.BIGINT);
        static final DatabaseField<Long> NUMBER =
                new DatabaseField<>("customer_number", Long.class, Types.BIGINT);
        static final DatabaseField<String> FIRST_NAME =
                new DatabaseField<>("customer_first_name", String.class, Types.VARCHAR);
        static final DatabaseField<String> LAST_NAME =
                new DatabaseField<>("customer_last_name", String.class, Types.VARCHAR);

        private Fields() {
            //unused
        }
    }
}
