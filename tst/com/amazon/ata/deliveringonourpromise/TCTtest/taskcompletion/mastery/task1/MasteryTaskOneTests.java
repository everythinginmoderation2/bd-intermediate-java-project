package com.amazon.ata.deliveringonourpromise.TCTtest.taskcompletion.mastery.task1;

import com.amazon.ata.deliveringonourpromise.App;
import com.amazon.ata.deliveringonourpromise.promisehistoryservice.PromiseHistoryClient;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.deliveringonourpromise.types.PromiseHistory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class MasteryTaskOneTests {
    private PromiseHistoryClient client;

    @BeforeMethod
    private void setup() {
        this.client = App.getPromiseHistoryClient();
    }

    @Test
    public void masteryTaskOne_missedPromiseCli_resolvesCSIssue() {
        // GIVEN
        String nonExistentOrderId = "900-0000000-0000000";
//        String nonExistentOrderId = "111-749023-7630574";

        // WHEN
        PromiseHistory promiseHistory = null;

        // Workaround to prevent the test from printing NPE to STDOUT.
        try {
            promiseHistory = client.getPromiseHistoryByOrderId(nonExistentOrderId);
        } catch (Exception e) {
            fail("Expected Missed Promise CLI to not throw an exception!");
        }

        //THEN
        assertNull(promiseHistory.getOrder(), "Expected Missed Promise CLI to provide result!");
        List<Promise> promises = promiseHistory.getPromises();
        assertTrue(null != promises && 0 == promises.size(),
            "Expected Missed Promise CLI to not print promise history!");
    }
}
