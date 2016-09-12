package cs.ecs.jdaoref;

import co.ecso.jdao.database.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Customers.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 15.03.16
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class Customers implements DatabaseTable<Long, Customer> {

    private final ApplicationConfig config;

    /**
     * Construct.
     *
     * @param config Config.
     */
    public Customers(final ApplicationConfig config) {
        this.config = config;
    }

    /**
     * Remove all entries.
     *
     * @return True if truncating succeeded.
     */
    CompletableFuture<Boolean> removeAll() {
        return truncate("TRUNCATE TABLE customer");
    }

    /**
     * Find one by id.
     *
     * @param id ID
     * @return Found entity.
     */
    @Override
    public CompletableFuture<Customer> findOne(final Long id) {
        return this.findOne(new SingleColumnQuery<>("SELECT %s FROM customer WHERE %s = ?", Customer.Fields.ID,
                Customer.Fields.ID, id)).thenApply(foundId -> new Customer(config, foundId.value()));
    }

    /**
     * Add a new entity.
     *
     * @param customerFirstName First Name.
     * @param customerLastName  Last Name.
     * @param customerNumber    Number.
     * @return Newly created entity.
     */
    public CompletableFuture<Customer> add(final String customerFirstName, final String customerLastName,
                                           final long customerNumber) {
        final InsertQuery<Long> query = new InsertQuery<>("INSERT INTO customer (%s, %s, %s, %s) " +
                "VALUES (null, ?, ?, ?)", Customer.Fields.ID);
        query.add(Customer.Fields.FIRST_NAME, customerFirstName)
                .add(Customer.Fields.LAST_NAME, customerLastName)
                .add(Customer.Fields.NUMBER, customerNumber);
        return add(query).thenApply(id -> new Customer(config, id.value()));
    }

    /**
     * Find all entities.
     *
     * @return List of entities found.
     */
    @Override
    public CompletableFuture<List<Customer>> findAll() {
        return this.findMany(new SingleColumnQuery<>("SELECT %s FROM customer", Customer.Fields.ID))
                .thenApply(list -> list.stream().map(foundId -> new Customer(config, foundId.value()))
                        .collect(Collectors.toList()));
    }

    @Override
    public co.ecso.jdao.config.ApplicationConfig config() {
        return config;
    }
}
