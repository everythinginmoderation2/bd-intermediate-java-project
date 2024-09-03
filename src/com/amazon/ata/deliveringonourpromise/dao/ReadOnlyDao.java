package com.amazon.ata.deliveringonourpromise.dao;

/**
 * DAO interface to abstract calls.
 * @param <I> Input type
 * @param <O> Output type
 */
public interface ReadOnlyDao<I, O> {

    /**
     * Get object method to be implemented.
     * @param orderId Order Id
     * @return Object abstracted object
     */
    O get(I orderId);
}
