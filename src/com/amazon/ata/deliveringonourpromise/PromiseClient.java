package com.amazon.ata.deliveringonourpromise;

import com.amazon.ata.deliveringonourpromise.types.Promise;

/**
 * Implemented by any clients needing to return estimated order delivery details.
 */
public interface PromiseClient {
    /**
     * This method should be implemented by applicable clients.
     * @param customerOrderItemId is the id of the order item
     * @return Promise
     */
    Promise getDeliveryPromiseByOrderItemId(String customerOrderItemId);
}
