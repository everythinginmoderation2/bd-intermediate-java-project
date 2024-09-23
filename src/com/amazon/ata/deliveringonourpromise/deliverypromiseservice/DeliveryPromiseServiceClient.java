package com.amazon.ata.deliveringonourpromise.deliverypromiseservice;

import com.amazon.ata.deliveringonourpromise.PromiseClient;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.deliverypromiseservice.service.DeliveryPromise;
import com.amazon.ata.deliverypromiseservice.service.DeliveryPromiseService;

/**
 * Client for accessing the DeliveryPromiseService to retrieve Promises.
 */
public class DeliveryPromiseServiceClient implements PromiseClient {
    private DeliveryPromiseService dpService;

    /**
     * Create new client that calls DPS with the given service object.
     *
     * @param dpService The DeliveryPromiseService that this client will call.
     */
    public DeliveryPromiseServiceClient(DeliveryPromiseService dpService) {
        this.dpService = dpService;
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
