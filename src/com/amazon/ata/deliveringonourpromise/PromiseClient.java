package com.amazon.ata.deliveringonourpromise;

import com.amazon.ata.deliveringonourpromise.types.Promise;

/**
 * Implemented by any clients needing to return estimated order delivery details
 * @returns  promise objects stored in order details
 */
public interface PromiseClient {
    Promise getDeliveryPromiseByOrderItemId(String customerOrderItemId);
}
