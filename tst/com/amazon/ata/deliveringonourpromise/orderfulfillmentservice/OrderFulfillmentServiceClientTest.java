package com.amazon.ata.deliveringonourpromise.orderfulfillmentservice;

import com.amazon.ata.deliveringonourpromise.App;
import com.amazon.ata.deliveringonourpromise.data.OrderDatastore;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderFulfillmentServiceClientTest {
    private OrderFulfillmentServiceClient client;
    private String orderItemId;

    @BeforeEach
    private void setup() {
        // not mocking: use an actual client calling actual service
        client = App.getOrderFulfillmentServiceClient();
        String orderId = "111-7497023-2960776";
        orderItemId = OrderDatastore.getDatastore()
                .getOrderData(orderId)
                .getCustomerOrderItemList()
                .get(0)
                .getCustomerOrderItemId();
    }

    @Test
    public void getOrderPromiseByOrderItemId_nullOrderItemId_returnsNull() {
        // GIVEN
        orderItemId = null;

        // WHEN
        Promise promise = client.getOrderFulfillmentPromiseByOrderItemId(orderItemId);

        // THEN
        assertNull(promise);
    }

    @Test
    public void getOrderFulfillmentPromiseByOrderItemId_nonexistentOrderItemId_returnsNull() {
        // GIVEN - an invalid/missing order item ID
        orderItemId = "20";

        // WHEN
        Promise promise = client.getOrderFulfillmentPromiseByOrderItemId(orderItemId);

        // THEN
        assertNull(promise);
    }

    @Test
    public void getOrderFulfillmentPromiseByOrderItemId_validItemId_hasCorrectOrderItemId() {
        // WHEN
        Promise promise = client.getOrderFulfillmentPromiseByOrderItemId(orderItemId);

        // THEN
        assertEquals(orderItemId, promise.getCustomerOrderItemId());
    }

    @Test
    public void getOrderFulfillmentPromiseByOrderItemId_validItemId_setsLatestArrivalDate() {
        // WHEN
        Promise promise = client.getOrderFulfillmentPromiseByOrderItemId(orderItemId);

        // THEN
        assertNotNull(promise.getPromiseLatestArrivalDate());
    }

    @Test
    public void getOrderFulfillmentPromiseByOrderItemId_validItemId_setsPromiseLatestShipDate() {
        // WHEN
        Promise promise = client.getOrderFulfillmentPromiseByOrderItemId(orderItemId);

        // THEN
        assertNotNull(promise.getPromiseLatestShipDate());
    }

    @Test
    public void getOrderFulfillmentPromiseByOrderItemId_validItemId_setsEffectiveDate() {
        // WHEN
        Promise promise = client.getOrderFulfillmentPromiseByOrderItemId(orderItemId);

        // THEN
        assertNotNull(promise.getPromiseEffectiveDate());
    }

    @Test
    public void getOrderFulfillmentPromiseByOrderItemId_validItemId_setsPromiseProvidedBy() {
        // WHEN
        Promise promise = client.getOrderFulfillmentPromiseByOrderItemId(orderItemId);

        // THEN
        assertNotNull(promise.getPromiseProvidedBy());
    }

    @Test
    public void getOrderFulfillmentPromiseByOrderItemId_validItemId_setsAsin() {
        // WHEN
        Promise promise = client.getOrderFulfillmentPromiseByOrderItemId(orderItemId);

        // THEN
        assertNotNull(promise.getAsin());
    }
}
