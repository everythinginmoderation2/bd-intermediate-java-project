package com.amazon.ata.deliveringonourpromise;

import com.amazon.ata.deliveringonourpromise.data.OrderDatastore;
import com.amazon.ata.deliveringonourpromise.promisehistoryservice.PromiseHistoryClient;
import com.amazon.ata.deliveringonourpromise.types.Order;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.deliveringonourpromise.types.PromiseHistory;
import com.amazon.ata.input.console.ATAUserHandler;
import com.amazon.ata.string.TextTable;

import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Owns the UI for the DeliveringOnOurPromise app. Provides command-line interface
 * to the user, allowing them to submit order IDs to fetch promise histories for,
 * and displaying the results.
 */
public class Shell {
    public static final String SHOW_FIXTURES_FLAG = "--show-fixtures";
    private static final String CONTINUE_PROMPT = "Would you like to enter another orderId? (y/n)";
    private static final Collection<String> VALID_YES_NO_ANSWERS =
            Collections.unmodifiableList(Arrays.asList("y", "n", "Y", "N"));
    private static final String ORDER_ID_PROMPT =
            "Please enter the orderId you would like to view the Promise History for.";
    private static final String UNKNOWN_ORDER_MESSAGE =
            "Unable to find any order data for orderId: %s. Please check your order id and try again.";

    private static final String INLINE_PROMPT = "> ";

    private PromiseHistoryClient promiseHistoryClient;
    private ATAUserHandler inputHandler;

    /**
     * Constructs a Shell instance that will use the given service client.
     *
     * @param promiseHistoryClient The client to use to communicate with the promise history service.
     * @param userHandler The ATAUserHandler to use for asking user for their input.
     */
    public Shell(PromiseHistoryClient promiseHistoryClient, ATAUserHandler userHandler) {
        this.promiseHistoryClient = promiseHistoryClient;
        this.inputHandler = userHandler;
    }

    //     *FIXED*: I need some code to mess up Checkstyle. I put opening braces on their own line
    /**
     * Command Line Interface entry point. Arguments are ignored.
     *
     * @param args command line args (ignored).
     * */
    public static void main(String[] args) {
        Shell shell = new Shell(App.getPromiseHistoryClient(), new ATAUserHandler());
        shell.processCommandLineArgs(args);

        try {
            do {
                System.out.println(shell.handleUserRequest());
            } while (shell.userHasAnotherRequest());
        } catch (Exception e) {
            System.out.print("Order does not exist. Please try different order ID. \n\n");
        }

        System.out.println("Thank you for using the Promise History CLI. Have a great day!\n\n");
    }

    /**
     * Handles a user request to fetch promise history for order IDs, and returns the text to display
     * to user.
     *
     * @return the user-facing output from the last request.
     */
    @VisibleForTesting
    String handleUserRequest() {
        String response;

        do {
            response = inputHandler.getString(ORDER_ID_PROMPT, INLINE_PROMPT).trim();
        } while ("".equals(response));

        PromiseHistory promiseHistory = promiseHistoryClient.getPromiseHistoryByOrderId(response);
        if (promiseHistory.getOrder() == null) {
            return String.format(UNKNOWN_ORDER_MESSAGE, response);
        }
        return renderOrderTable(promiseHistory.getOrder()) + renderPromiseHistoryTable(promiseHistory);
    }

    /**
     * Generates the user-facing representation of the given promise history.
     *
     * @param promiseHistory The PromiseHistory to render to user-facing String
     * @return The String representation of the promise history to display to user
     */
    private String renderPromiseHistoryTable(PromiseHistory promiseHistory) {
        List<String> columnNames = Arrays.asList(
                "EFFECTIVE DATE",
                "ASIN",
                "ITEM ID",
                "ACTIVE",
                "PROMISED SHIP DATE",
                "PROMISED DELIVERY DATE",
                "DELIVERY DATE",
                "PROVIDED BY",
                "CONFIDENCE"
        );

        List<List<String>> promiseRows = new ArrayList<>();
        for (Promise promise : promiseHistory.getPromises()) {
            List<String> row = new ArrayList<>();
            promiseRows.add(row);

            if (promise.getPromiseEffectiveDate() != null) {
                row.add(promise.getPromiseEffectiveDate().toLocalDateTime().toString());
            } else {
                row.add(null);
            }
            row.add(promise.getAsin());
            row.add(promise.getCustomerOrderItemId());
            row.add(promise.isActive() ? "Y" : "N");
            if (promise.getPromiseLatestShipDate() != null) {
                row.add(promise.getPromiseLatestShipDate().toLocalDateTime().toString());
            } else {
                row.add(null);
            }
            if (promise.getPromiseLatestArrivalDate() != null) {
                row.add(promise.getPromiseLatestArrivalDate().toLocalDateTime().toString());
            } else {
                row.add(null);
            }
            if (promise.getDeliveryDate() != null) {
                row.add(promise.getDeliveryDate().toLocalDateTime().toString());
            } else {
                row.add(null);
            }
            row.add(promise.getPromiseProvidedBy());
            Integer confidence = promise.getConfidence();
            if (confidence != null) {
                row.add(confidence.toString());
            } else {
                row.add(null);
            }
        }

        return new TextTable(columnNames, promiseRows).toString();
    }

    /**
     * Generates the user-facing representation of the given order.
     *
     * @param order The Order to render to String for display in the UI
     * @return The String representation of Order to display to user
     */
    private String renderOrderTable(Order order) {
        List<String> columnNames = Arrays.asList(
                "ORDER DATE", "ORDER ID", "MARKETPLACE", "TIMEZONE", "CONDITION", "SHIP OPTION", "CUSTOMER ID"
        );

        List<String> orderFields = new ArrayList<>();
        if (order != null) {
            if (order.getOrderDate() != null) {
                orderFields.add(order.getOrderDate().toLocalDateTime().toString());
            } else {
                orderFields.add(null);
            }
            orderFields.add(order.getOrderId());
            orderFields.add(order.getMarketplaceId());
            if (order.getOrderDate() != null) {
                orderFields.add(order.getOrderDate().getZone().toString());
            } else {
                orderFields.add(null);
            }
            if (order.getCondition() != null) {
                orderFields.add(order.getCondition().toString());
            } else {
                orderFields.add(null);
            }
            orderFields.add(order.getShipOption());
            orderFields.add(order.getCustomerId());
        }

        return new TextTable(columnNames, Arrays.asList(orderFields)).toString();
    }

    /**
     * Asks user if they want to submit another request and return boolean indicating their answer.
     *
     * @return true if user has another order ID to request; false otherwise
     */
    @VisibleForTesting
    boolean userHasAnotherRequest() {
        String answer = inputHandler.getString(VALID_YES_NO_ANSWERS, CONTINUE_PROMPT, INLINE_PROMPT);
        return answer.equals("y") || answer.equals("Y");
    }

    private void processCommandLineArgs(String[] args) {
        if (args.length > 0 && args[0].equals(SHOW_FIXTURES_FLAG)) {
            System.out.println("\nHere are a few test orders you can use:");
            System.out.println(renderFixtures());
        }
    }

    private String renderFixtures() {
        return OrderDatastore.getDatastore().getOrderFixturesTable();
    }
}
