package com.amazon.ata.deliveringonourpromise.orderfulfillmentservice;

import com.amazon.ata.deliveringonourpromise.PromiseClient;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.deliverypromiseservice.service.DeliveryPromise;
import com.amazon.ata.deliverypromiseservice.service.DeliveryPromiseService;
import com.amazon.ata.orderfulfillmentservice.OrderFulfillmentService;
import com.amazon.ata.orderfulfillmentservice.OrderPromise;

/**
 * Client for accessing the OrderFulfillmentService to retrieve Promises.
 */
public class OrderFulfillmentServiceClient implements PromiseClient {
    private OrderFulfillmentService ofService;
    private DeliveryPromiseService dpService;

    /**
     * Create new client that calls OFS with the given service object.
     *
     * @param ofService The OrderFulfillmentService that this client will call.
     */
    public OrderFulfillmentServiceClient(OrderFulfillmentService ofService) {
        this.ofService = ofService;
    }

    /**
     * Fetches the Promise for the given order item ID.
     *
     * @param customerOrderItemId String representing the order item ID to fetch the order for.
     * @return the Promise for the given order item ID.
     */
    public Promise getOrderFulfillmentPromiseByOrderItemId(String customerOrderItemId) {
        OrderPromise orderPromise = ofService.getOrderPromise(customerOrderItemId);

        if (null == orderPromise) {
            return null;
        }

        return Promise.builder()
                .withPromiseLatestArrivalDate(orderPromise.getPromiseLatestArrivalDate())
                .withCustomerOrderItemId(orderPromise.getCustomerOrderItemId())
                .withPromiseLatestShipDate(orderPromise.getPromiseLatestShipDate())
                .withPromiseEffectiveDate(orderPromise.getPromiseEffectiveDate())
                .withIsActive(orderPromise.isActive())
                .withPromiseProvidedBy(orderPromise.getPromiseProvidedBy())
                .withAsin(orderPromise.getAsin())
                .build();
    }

    /**
     * Fetches the Promise for the given order item ID.
     *
     * @param customerOrderItemId String representing the order item ID to fetch the order for.
     * @return the Promise for the given order item ID.
     */
    @Override
    public Promise getDeliveryPromiseByOrderItemId(String customerOrderItemId) {
        DeliveryPromise deliveryPromise = dpService.getDeliveryPromise(customerOrderItemId);

        if (null == deliveryPromise) {
            return null;
        }

        return Promise.builder()
                .withPromiseLatestArrivalDate(deliveryPromise.getPromiseLatestArrivalDate())
                .withCustomerOrderItemId(deliveryPromise.getCustomerOrderItemId())
                .withPromiseLatestShipDate(deliveryPromise.getPromiseLatestShipDate())
                .withPromiseEffectiveDate(deliveryPromise.getPromiseEffectiveDate())
                .withIsActive(deliveryPromise.isActive())
                .withPromiseProvidedBy(deliveryPromise.getPromiseProvidedBy())
                .withAsin(deliveryPromise.getAsin())
                .build();
    }
}
