package com.amazon.ata.deliveringonourpromise.comparators;

import com.amazon.ata.deliveringonourpromise.types.OrderItem;

import java.util.Comparator;

public class OrderItemAsinComparator implements Comparator<OrderItem> {
    @Override
    public int compare(OrderItem orderItem1, OrderItem orderItem2) {
        String orderItemAsin1 = orderItem1.getAsin();
        String orderItemAsin2 = orderItem2.getAsin();
        if (!orderItemAsin1.equals(orderItemAsin2)) {
            return orderItemAsin1.compareTo(orderItemAsin2);
        }
        return 0;
    }
}
