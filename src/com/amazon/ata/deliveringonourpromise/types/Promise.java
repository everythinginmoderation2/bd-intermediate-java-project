package com.amazon.ata.deliveringonourpromise.types;

import com.amazon.ata.deliveringonourpromise.comparators.PromiseAsinComparator;

import java.time.ZonedDateTime;

/**
 * Represents a customer promise, regardless of which service/stage of the order process
 * the promise was made.
 *
 * Use the Builder pattern to populate a Promise object:
 *
 * <pre>{@code
 *   Promise promise = Promise.builder()
 *                            .withCustomerOrderItemId(orderItemId)
 *                            .withAsin(asin)
 *                            ...
 *                            .withPromiseProvidedBy("OFS")
 *                            .build();
 * }</pre>
 *
 * Explanation of fields:
 * * customerOrderItemId: the ID of the item in the customer's order that this Promise is relevant for
 * * asin: the ASIN that the order item includes
 * * isActive: boolean indicating whether the promise is currently relevant (true)
 *             or superseded by another promise (false)
 * * promiseEffectiveDate: the timestamp that the promise became the relevant/active promise
 * * promiseLatestArrivalDate: timestamp of the latest time item can be delivered and still meet promise made
 *                             to customer
 * * promiseLatestShipDate: timestamp of latest time item can be shipped and meet customer promise
 * * deliveryDate: timestamp of when the item was delivered (or null if it hasn't been delivered yet)
 * * promiseProvidedBy: where the promise came from (which service)
 */
public class Promise {
    private String customerOrderItemId;
    private String asin;
    private boolean active;
    private ZonedDateTime promiseEffectiveDate;
    private ZonedDateTime promiseLatestArrivalDate;
    private ZonedDateTime promiseLatestShipDate;
    private ZonedDateTime deliveryDate;
    private String promiseProvidedBy;
    private int confidence;

    private Promise() {}

    /**
     * Creates a new builder for populating a Promise.
     * @return new builder ready to build.
     */
    public static Builder builder() {
        return new Builder();
    }

    public ZonedDateTime getPromiseLatestArrivalDate() {
        return promiseLatestArrivalDate;
    }

    public String getCustomerOrderItemId() {
        return customerOrderItemId;
    }

    public ZonedDateTime getPromiseEffectiveDate() {
        return promiseEffectiveDate;
    }

    public boolean isActive() {
        return active;
    }

    public ZonedDateTime getPromiseLatestShipDate() {
        return promiseLatestShipDate;
    }

    public ZonedDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public String getPromiseProvidedBy() {
        return promiseProvidedBy;
    }

    public String getAsin() {
        return asin;
    }

    public int getConfidence() {
        return confidence;
    }

    /**
     * Sets the confidence value associated with the item ID this promise corresponds to. Allow setting
     * it after build/instantiation to allow centralization of logic across promise providers, and to
     * accommodate tracking determination.
     * @param isConfidenceTracked Whether this item is one of the items tracked by Global Transportation.
     * @param trackedConfidence The confidence in promises related to this item, if tracked; otherwise a random
     * integer that should be ignored.
     */
    public void setConfidence(boolean isConfidenceTracked, int trackedConfidence) {
        this.confidence = trackedConfidence;
    }

    /**
     * Sets the delivery date associated with the order item ID this promise corresponds to. Allow
     * setting it after build/instantiation to allow centralization of logic across promise providers.
     * @param deliveryDate the ZonedDateTime containing the delivery date for the shipment containing the order ID
     */
    public void setDeliveryDate(ZonedDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return "Promise{" +
               "customerOrderItemId='" + customerOrderItemId + '\'' +
               ", asin='" + asin + '\'' +
               ", isActive=" + active +
               ", promiseEffectiveDate=" + promiseEffectiveDate +
               ", promiseLatestArrivalDate=" + promiseLatestArrivalDate +
               ", promiseLatestShipDate=" + promiseLatestShipDate +
               ", deliveryDate=" + deliveryDate +
               ", promiseProvidedBy='" + promiseProvidedBy + '\'' +
               '}';
    }

    /**
     * Builder for Promises. See Promise documentation.
     */
    public static class Builder {
        private String customerOrderItemId;
        private String asin;
        private boolean active;
        private ZonedDateTime promiseEffectiveDate;
        private ZonedDateTime promiseLatestArrivalDate;
        private ZonedDateTime promiseLatestShipDate;
        private ZonedDateTime deliveryDate;
        private String promiseProvidedBy;

        //CHECKSTYLE:OFF:HiddenField
        //CHECKSTYLE:OFF:JavadocMethod
        public Builder withCustomerOrderItemId(String customerOrderItemId) {
            this.customerOrderItemId = customerOrderItemId;
            return this;
        }

        public Builder withAsin(String asin) {
            this.asin = asin;
            return this;
        }

        public Builder withIsActive(boolean active) {
            this.active = active;
            return this;
        }

        public Builder withPromiseEffectiveDate(ZonedDateTime promiseEffectiveDate) {
            this.promiseEffectiveDate = promiseEffectiveDate;
            return this;
        }

        public Builder withPromiseLatestArrivalDate(ZonedDateTime promiseLatestArrivalDate) {
            this.promiseLatestArrivalDate = promiseLatestArrivalDate;
            return this;
        }

        public Builder withPromiseLatestShipDate(ZonedDateTime promiseLatestShipDate) {
            this.promiseLatestShipDate = promiseLatestShipDate;
            return this;
        }

        public Builder withDeliveryDate(ZonedDateTime deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public Builder withPromiseProvidedBy(String promiseProvidedBy) {
            this.promiseProvidedBy = promiseProvidedBy;
            return this;
        }
        //CHECKSTYLE:ON:JavadocMethod
        //CHECKSTYLE:ON:HiddenField

        /**
         * build the Promise from the provided data.
         * @return the populated Promise
         */
        public Promise build() {
            Promise promise = new Promise();
            promise.customerOrderItemId = customerOrderItemId;
            promise.asin = asin;
            promise.active = active;
            promise.promiseEffectiveDate = promiseEffectiveDate;
            promise.promiseLatestArrivalDate = promiseLatestArrivalDate;
            promise.promiseLatestShipDate = promiseLatestShipDate;
            promise.deliveryDate = deliveryDate;
            promise.promiseProvidedBy = promiseProvidedBy;

            return promise;
        }
    }
}
