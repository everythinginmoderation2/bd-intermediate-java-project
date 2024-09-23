package com.amazon.ata.deliveringonourpromise;

import com.amazon.ata.deliveringonourpromise.types.Promise;

public interface PromiseClient {
    Promise getDeliveryPromiseByOrderItemId(String customerOrderItemId);
}