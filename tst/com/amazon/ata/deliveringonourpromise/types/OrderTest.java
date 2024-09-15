package com.amazon.ata.deliveringonourpromise.types;

import com.amazon.ata.ordermanipulationauthority.OrderCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderTest {
    private String orderId = "1";
    private String customerId = "2";
    private String marketplaceId = "3";
    private OrderCondition condition;
    private List<OrderItem> customerOrderItemList = new ArrayList<>();
    private String shipOption;
    private ZonedDateTime orderDate;


    @Test
    public void addItemTo_CustomerOrderItemList_returnsUnmodifiedList() {
        //GIVEN a customer adds an item to their order item list
        OrderItem wantedItem = OrderItem.builder()
                .withCustomerOrderItemId("1").build();

        //This list will be hacked and have one too many items
        List<OrderItem> unExpectedList = new ArrayList<>();
        unExpectedList.add(wantedItem);

        //This list should only contain the one desired item
        List<OrderItem> expectedList = new ArrayList<>();
        expectedList.add(wantedItem);

        Order.Builder orderInProcess = getGenericOrderBuilder()
                .withCustomerOrderItemList(unExpectedList);

        //WHEN an attempt is made to add an item to the CustomerOrderItemList field
        //using the list object's add method (creating an unintended addition)
        OrderItem unWantedItem = OrderItem.builder()
                .withCustomerOrderItemId("2").build();

        unExpectedList.add(unWantedItem);

        //THEN an error is returned if the CustomerOrderItemList copy was not made,
        //and if the CustomerOrderList was modified directly
        Order unwantedCompletedOrder = orderInProcess.build();
        List<OrderItem> customerOrderItemList = unwantedCompletedOrder.getCustomerOrderItemList();

        Assertions.assertTrue(
                expectedList.equals(customerOrderItemList), "Expected lists in to be equal.");
    }



    @Test
    public void addItemTo_CustomerOrderItemList_verifiesUnchangedListSize() {
        //GIVEN a customer adds an item to their order item list
        OrderItem wantedItem = OrderItem.builder()
                .withCustomerOrderItemId("1").build();

        List<OrderItem> wantedList = new ArrayList<>();
        wantedList.add(wantedItem);

        Order.Builder orderInProcess = getGenericOrderBuilder()
                .withCustomerOrderItemList(wantedList);

        //WHEN an attempt is made to add an item to the CustomerOrderItemList field
        //using the list object's add method (creating an unintended addition)
        OrderItem unWantedItem = OrderItem.builder()
                .withCustomerOrderItemId("2").build();

        wantedList.add(unWantedItem);

        ///THEN an error is returned if the CustomerOrderItemList size demonstrates
        //that an order item was added to the CustomerOrderItemList field instead of the copy
        Order unwantedCompletedOrder = orderInProcess.build();
        List<OrderItem> customerOrderItemList = unwantedCompletedOrder.getCustomerOrderItemList();
        Assertions.assertEquals(
                1,
                customerOrderItemList.size(),
                "Expected only original OrderItem to exist in Order, but found: " + customerOrderItemList);
    }




    @Test
    public void removeItemFrom_CustomerOrderItemList_failsToRemoveItem() {
        //GIVEN a customer adds an item to their order item list
        OrderItem wantedItem = OrderItem.builder()
                .withCustomerOrderItemId("1").build();

        List<OrderItem> wantedList = new ArrayList<>();
        wantedList.add(wantedItem);

        Order order = getGenericOrderBuilder()
                .withCustomerOrderItemList(wantedList).build();

        //WHEN an attempt is made to remove an item from the finalized OrderList
        order.getCustomerOrderItemList().remove(0);

        //THEN
        Assertions.assertEquals(1,
                order.getCustomerOrderItemList().size(),
                "Order items cannot be removed from this order list object using this method.");
    }

    @Test
    public void addItemTo_usingGetCustomerOrderItemList_failsToAddItem() {
        //GIVEN a customer adds an item to their order item list
        OrderItem wantedItem = OrderItem.builder()
                .withCustomerOrderItemId("1").build();

        List<OrderItem> wantedList = new ArrayList<>();
        wantedList.add(wantedItem);

        Order order = getGenericOrderBuilder()
                .withCustomerOrderItemList(wantedList).build();


        //WHEN an attempt is made to add an item to the list
        OrderItem unwantedItem = OrderItem.builder()
                .withCustomerOrderItemId("2").build();

        order.getCustomerOrderItemList().add(unwantedItem);

        //THEN
        Assertions.assertEquals(1,
                order.getCustomerOrderItemList().size(),
                "Order items cannot be added to this order list using this method.");
    }


    private Order.Builder getGenericOrderBuilder() {
        return Order.builder()
                .withOrderId(orderId)
                .withCustomerId(customerId)
                .withMarketplaceId(marketplaceId)
                .withCondition(condition)
                .withCustomerOrderItemList(customerOrderItemList)
                .withShipOption(shipOption)
                .withOrderDate(orderDate);

    }

}
