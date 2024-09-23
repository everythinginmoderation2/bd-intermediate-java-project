 package com.amazon.ata.deliveringonourpromise.TCTtest.taskcompletion.mastery.task4;

import com.amazon.ata.deliveringonourpromise.App;
import com.amazon.ata.deliveringonourpromise.dao.PromiseDao;
import com.amazon.ata.deliveringonourpromise.deliverypromiseservice.DeliveryPromiseServiceClient;
import com.amazon.ata.deliveringonourpromise.promisehistoryservice.PromiseHistoryClient;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.deliveringonourpromise.types.PromiseHistory;
import com.amazon.ata.test.helper.AtaTestHelper;
import com.amazon.ata.test.reflect.ClassQuery;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class MasteryTaskFourTests {
    private PromiseHistoryClient client;

    @BeforeMethod
    private void setup() {
        this.client = App.getPromiseHistoryClient();
    }

    @Test
    public void masteryTaskFour_ofsClientClass_existsAndHasASingleMethod() {
        // GIVEN
        Class<?> ofsClientClass =
            ClassQuery.inExactPackage("com.amazon.ata.deliveringonourpromise.orderfulfillmentservice")
            .withExactSimpleName("com.amazon.ata.deliveringonourpromise.orderfulfillmentservice.OrderFulfillmentServiceClient")
            .findClassOrFail();

        // WHEN
        Method[] publicMethods = Arrays.stream(ofsClientClass.getDeclaredMethods())
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .toArray(Method[]::new);

        // THEN
        assertTrue(1 == publicMethods.length, "Expected com.amazon.ata.deliveringonourpromise.orderfulfillmentservice.OrderFulfillmentServiceClient to have a single public method!");

        Class<?> ofsMethodReturnType = publicMethods[0].getReturnType();
        assertTrue(ofsMethodReturnType == Promise.class,
            "Expected com.amazon.ata.deliveringonourpromise.orderfulfillmentservice.OrderFulfillmentServiceClient public method to have Promise as return type!");

        Class<?>[] ofsMethodParameterTypes = publicMethods[0].getParameterTypes();
        assertTrue(1 == ofsMethodParameterTypes.length && ofsMethodParameterTypes[0] == String.class,
            "Expected com.amazon.ata.deliveringonourpromise.orderfulfillmentservice.OrderFulfillmentServiceClient public method to take single parameter of String type!");
    }

    @Test
    public void masteryTaskFour_promiseDao_supportsMultiplePromiseClients() {
        // GIVEN
        Class<?> promiseDaoClass = PromiseDao.class;

        // WHEN
        Constructor<?>[] promiseDaoConstructors = promiseDaoClass.getConstructors();

        // THEN
        boolean containsAList = false;
        for (Constructor<?> promiseDaoConstructor : promiseDaoConstructors) {
            Class<?>[] parameterTypes = promiseDaoConstructor.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                if (parameterType == List.class) {
                    containsAList = true;
                }
            }
        }
        assertTrue(containsAList,
                   String.format("Expected %s class to support using multiple PromiseClients",
                                 PromiseDao.class.getSimpleName())
        );
    }

    @Test
    public void masteryTaskFour_promiseClients_arePolymorphicallyInterchangeable() {
        // GIVEN
        Class<?> ofsClientClass =
            ClassQuery.inExactPackage("com.amazon.ata.deliveringonourpromise.orderfulfillmentservice")
                .withExactSimpleName("com.amazon.ata.deliveringonourpromise.orderfulfillmentservice.OrderFulfillmentServiceClient")
                .findClassOrFail();

        Class<?> dpsClientClass = DeliveryPromiseServiceClient.class;

        // WHEN
        Class<?>[] ofsClientClassInterfaces = ofsClientClass.getInterfaces();
        Class<?>[] dpsClientClassInterfaces = dpsClientClass.getInterfaces();

        // THEN
        assertTrue(1 == ofsClientClassInterfaces.length &&
                1 == dpsClientClassInterfaces.length &&
                ofsClientClassInterfaces[0] == dpsClientClassInterfaces[0],
            "Expected Promise clients classes to be polymorphically interchangeable!");

        Method[] promiseClientMethods = ofsClientClassInterfaces[0].getMethods();

        assertTrue(1 == promiseClientMethods.length,
            "Expected Promise clients to have one common method!");

        assertTrue(Promise.class == promiseClientMethods[0].getReturnType(),
            "Expected Promise clients method to return Promise type!");

        Class<?>[] parameterTypes = promiseClientMethods[0].getParameterTypes();
        assertTrue(1 == parameterTypes.length && String.class == parameterTypes[0],
            "Expected Promise clients method to take single parameter of String type!");
    }

    @Test
    public void masteryTaskFour_orderFulfillmentServiceClient_getsPromise() {
        // GIVEN
        String orderId = "900-3746401-0000002";

        // WHEN
        PromiseHistory promiseHistory = null;

        // Workaround to prevent the test from printing NPE to STDOUT.
        try {
            promiseHistory = client.getPromiseHistoryByOrderId(orderId);
        } catch (Exception e) {
            fail("Expected com.amazon.ata.deliveringonourpromise.orderfulfillmentservice.OrderFulfillmentServiceClient to not throw an exception when retrieving order: " +
                orderId);
        }

        //THEN
        boolean providedByOfs = false;
        for (Promise promise : promiseHistory.getPromises()) {
            if (promise.getPromiseProvidedBy().equals("OFS")) {
                providedByOfs = true;
            }
        }
        assertTrue(providedByOfs,
            "Expected com.amazon.ata.deliveringonourpromise.orderfulfillmentservice.OrderFulfillmentServiceClient to provide promise from OrderFulfillmentService for order: " +
            orderId);
    }
}
