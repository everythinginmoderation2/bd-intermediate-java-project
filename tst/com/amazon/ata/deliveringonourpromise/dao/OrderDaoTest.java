package com.amazon.ata.deliveringonourpromise.dao;

import com.amazon.ata.deliveringonourpromise.App;
import com.amazon.ata.deliveringonourpromise.ordermanipulationauthority.OrderManipulationAuthorityClient;
import com.amazon.ata.deliveringonourpromise.types.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDaoTest {
    private OrderManipulationAuthorityClient omaClient = App.getOrderManipulationAuthorityClient();
    private OrderDao dao = new OrderDao(omaClient);

    @Test
    //Happy case, verifying that the OrderDao can return an order.
    public void get_forKnownOrderId_returnsOrder() {
        //Given
        //An order ID that we know exist
        String orderId = "111-7497023-2960776";

        // When
        //We call get() with that order ID
        Order result = dao.get(orderId);

        //Then
        //The result is not null
        assertNotNull(result, "An existing order should be returned.");
    }

    @Test
    //Verifying that OrderDao returns a null order if the order cannot be found
    public void get_orderCannotBeFound_returnsNullOrder() {
        //Given
        //An order ID that doesn't exist
        String orderId = "chicken";

        //When
        Order result = dao.get(orderId);

        //Then
        //A null order is returned
        assertNull(result, "This order is null");
    }


}

