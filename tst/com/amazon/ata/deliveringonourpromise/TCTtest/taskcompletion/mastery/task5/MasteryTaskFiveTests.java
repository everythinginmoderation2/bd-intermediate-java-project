package com.amazon.ata.deliveringonourpromise.TCTtest.taskcompletion.mastery.task5;

import com.amazon.ata.deliveringonourpromise.activity.GetPromiseHistoryByOrderIdActivity;
import com.amazon.ata.deliveringonourpromise.dao.OrderDao;
import com.amazon.ata.deliveringonourpromise.dao.PromiseDao;
import com.amazon.ata.deliveringonourpromise.TCTtest.wrapper.PromiseAsinComparatorWrapper;
import com.amazon.ata.deliveringonourpromise.types.Order;
import com.amazon.ata.deliveringonourpromise.types.OrderItem;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.deliveringonourpromise.types.PromiseHistory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Comparators;
import com.google.common.collect.Multimap;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.amazon.ata.test.assertions.IntrospectionAssertions.assertDoesNotImplementInterface;
import static com.amazon.ata.test.assertions.IntrospectionAssertions.assertImplementsInterface;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MasteryTaskFiveTests {

    private OrderDao mockOrderDao;
    private PromiseDao mockPromiseDao;
    private GetPromiseHistoryByOrderIdActivity activity;

    @BeforeMethod
    private void setup() {
        mockOrderDao = mock(OrderDao.class);
        mockPromiseDao = mock(PromiseDao.class);
        activity = new GetPromiseHistoryByOrderIdActivity(mockOrderDao, mockPromiseDao);
    }

    @Test
    public void masteryTaskFive_getPromiseHistoryByOrderId_returnsOnePromiseForOneItemWithOnePromisePerItem() {
        int numberOfItems = 1;
        int numberOfPromisesPerItem = 1;

        runTestForConfiguration(numberOfItems, numberOfPromisesPerItem);
    }

    @Test
    public void masteryTaskFive_getPromiseHistoryByOrderId_returnsTwoPromisesForOneItemWithTwoPromisesPerItem() {
        int numberOfItems = 1;
        int numberOfPromisesPerItem = 2;

        runTestForConfiguration(numberOfItems, numberOfPromisesPerItem);
    }

    @Test
    public void masteryTaskFive_getPromiseHistoryByOrderId_returnsTwoPromisesForTwoItemsWithOnePromisePerItem() {
        int numberOfItems = 2;
        int numberOfPromisesPerItem = 1;

        runTestForConfiguration(numberOfItems, numberOfPromisesPerItem);
    }

    @Test
    public void masteryTaskFive_getPromiseHistoryByOrderId_returnsFourPromisesForTwoItemsWithTwoPromisesPerItem() {
        int numberOfItems = 2;
        int numberOfPromisesPerItem = 2;

        runTestForConfiguration(numberOfItems, numberOfPromisesPerItem);
    }

    @Test
    void masteryTaskFiveMilestone2_promise_isNotComparable() {
        assertDoesNotImplementInterface(Promise.class, Comparable.class);
    }

    @Test
    void masteryTaskFiveMilestone2_promiseAsinComparator_existsAndImplementsComparator() {
        // GIVEN + WHEN
        Class<?> comparatorClass = PromiseAsinComparatorWrapper.findPromiseAsinComparatorWrapperClass();

        // THEN
        assertImplementsInterface(comparatorClass, Comparator.class);
    }

    @Test
    void masteryTaskFiveMilestone2_promiseAsinComparatorCompare_withEqualPromise_returnsZero() {
        // GIVEN
        String asin = "A123456789";
        Promise promise1 = Promise.builder().withAsin(asin).withCustomerOrderItemId("123").build();
        Promise promise2 = Promise.builder().withAsin(asin).withCustomerOrderItemId("456").build();
        PromiseAsinComparatorWrapper promiseComparatorWrapper = new PromiseAsinComparatorWrapper();

        // WHEN
        int result = promiseComparatorWrapper.compare(promise1, promise2);

        // THEN
        assertEquals(
            0,
            result,
            String.format("Expected %s and %s to compare equal to each other",
                          promise1.toString(),
                          promise2.toString())
        );
    }

    @Test
    void masteryTaskFiveMilestone2_promiseAsinComparatorCompare_withPromiseWithLaterAsin_returnsPositive() {
        // GIVEN
        String earlierAsin = "A123456789";
        String laterAsin = "A234567890";
        Promise earlierPromise = Promise.builder().withAsin(earlierAsin).withCustomerOrderItemId("123").build();
        Promise laterPromise = Promise.builder().withAsin(laterAsin).withCustomerOrderItemId("456").build();
        PromiseAsinComparatorWrapper promiseComparatorWrapper = new PromiseAsinComparatorWrapper();

        // WHEN
        int result = promiseComparatorWrapper.compare(earlierPromise, laterPromise);

        // THEN
        assertTrue(result < 0,
            String.format("Expected %s to order before %s but was not",
                          earlierPromise.toString(),
                          laterPromise.toString())
        );
    }

    @Test
    void masteryTaskFiveMilestone2_promiseAsinComparatorCompare_withPromiseWithEarlierAsin_returnsNegative() {
        // GIVEN
        String earlierAsin = "A123456789";
        String laterAsin = "A234567890";
        Promise earlierPromise = Promise.builder().withAsin(earlierAsin).withCustomerOrderItemId("123").build();
        Promise laterPromise = Promise.builder().withAsin(laterAsin).withCustomerOrderItemId("456").build();
        PromiseAsinComparatorWrapper promiseComparatorWrapper = new PromiseAsinComparatorWrapper();

        // WHEN
        int result = promiseComparatorWrapper.compare(earlierPromise, laterPromise);

        // THEN
        assertTrue(result < 0,
                   String.format("Expected %s to order after %s but was not", laterPromise, earlierPromise));
    }

    @Test
    void masteryTaskFiveMilestone2_getPromiseHistoryByOrderId_returnsPromisesSortedByAsin() {
        // GIVEN
        String orderId = "111-7497023-2960775";
        Order order = createOrder(orderId, 5);

        Multimap<String, Promise> orderItemIdToPromises =
            createOrderItemIdToPromisesMap(order, 1);

        List<String> promises = orderItemIdToPromises.values()
                                    .stream()
                                    .map(Promise::toString)
                                    .collect(Collectors.toList());

        initializeMocks(order, orderItemIdToPromises);

        // WHEN
        PromiseHistory promiseHistory = activity.getPromiseHistoryByOrderId(orderId);

        // THEN
        // all promises are present
        assertOrdersAndPromisesMatch(order, promises, promiseHistory);
        List<Promise> returnedPromises = promiseHistory.getPromises();
        String promiseAsinFirst = returnedPromises.get(0).getAsin();
        String promiseAsinLast = returnedPromises.get(promises.size() - 1).getAsin();
        // promises are in ascending ASIN order.
        assertTrue(Comparators.isInOrder(
            returnedPromises,
            (x, y) -> x.getAsin().compareTo(y.getAsin())), String.format("%s should be listed before %s", promiseAsinFirst, promiseAsinLast)
        );
    }

    private void runTestForConfiguration(int numberOfItems, int numberOfPromisesPerItem) {
        // GIVEN
        String orderId = "111-7497023-2960775";
        Order order = createOrder(orderId, numberOfItems);

        Multimap<String, Promise> orderItemIdToPromises =
            createOrderItemIdToPromisesMap(order, numberOfPromisesPerItem);

        List<String> promises = orderItemIdToPromises.values()
            .stream()
            .map(Promise::toString)
            .collect(Collectors.toList());

        initializeMocks(order, orderItemIdToPromises);

        // WHEN
        PromiseHistory promiseHistory = activity.getPromiseHistoryByOrderId(orderId);

        // THEN
        assertOrdersAndPromisesMatch(order, promises, promiseHistory);
    }

    private Order createOrder(String orderId, int numOrderItems) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < numOrderItems; i++) {
            OrderItem orderItem = OrderItem.builder()
                .withCustomerOrderItemId("" + i)
                // required since it is used in GetPromiseHistoryByOrderIdActivity
                .withIsConfidenceTracked(false)
                .withAsin("A12345678" + i)
                .build();
            orderItems.add(orderItem);
        }
        Collections.shuffle(orderItems, new Random(1));
        return Order.builder()
            .withOrderId(orderId)
            .withCustomerOrderItemList(orderItems)
            .build();
    }

    @SuppressFBWarnings(value = "RV_RETURN_VALUE_IGNORED", justification = "FindBugs issue with Guava")
    private Multimap<String, Promise> createOrderItemIdToPromisesMap(Order order, int numPromisesPerItem) {
        Multimap<String, Promise> orderItemIdToPromises = ArrayListMultimap.create();
        for (OrderItem orderItem : order.getCustomerOrderItemList()) {
            String customerOrderItemId = orderItem.getCustomerOrderItemId();
            for (int i = 0; i < numPromisesPerItem; i++) {
                Promise promise = Promise.builder()
                    .withAsin(orderItem.getAsin())
                    .withCustomerOrderItemId(customerOrderItemId)
                    .withPromiseProvidedBy(0 == i ? "DPS" : "OFS")
                    .build();
                orderItemIdToPromises.put(customerOrderItemId, promise);
            }
        }
        return orderItemIdToPromises;
    }

    private void initializeMocks(Order order, Multimap<String, Promise> orderItemIdToPromises) {
        when(mockOrderDao.get(anyString())).thenReturn(order);
        for (String orderItemId : orderItemIdToPromises.keys()) {
            when(mockPromiseDao.get(matches(orderItemId)))
                .thenReturn(new ArrayList<>(orderItemIdToPromises.get(orderItemId)));
        }
    }

    private void assertOrdersAndPromisesMatch(Order order, List<String> promises, PromiseHistory promiseHistory) {
        assertEquals(promiseHistory.getOrder().toString(), order.toString(),
            "Expected PromiseHistory returned by GetPromiseHistoryByOrderIdActivity " +
            "to contain order returned by OrderDao!");
        List<String> returnedPromises = promiseHistory.getPromises()
            .stream()
            .map(Promise::toString)
            .collect(Collectors.toList());

        assertEquals(returnedPromises.size(), promises.size(),
            "Expected PromiseHistory returned by GetPromiseHistoryByOrderIdActivity " +
             "to contain same number of Promises as returned by PromiseDao!");
        assertTrue(returnedPromises.containsAll(promises),
            "Expected PromiseHistory returned by GetPromiseHistoryByOrderIdActivity " +
            "to contain exactly the same Promises as returned by PromiseDao!");
    }

}
