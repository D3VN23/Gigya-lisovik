package com.example.alexanderlisovik.gigya;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import java.io.IOException;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)

public class NetwrokManagerUnitTests {

    @Mock
    NetworkManager networkManager;

    @Test
    public void testResponseForTypeAccordance() throws JSONException, IOException, InterruptedException {

        NetworkManager.getAccountOptions("https://accounts.us1.gigya.com/accounts.getPolicies", new Callback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                assertTrue(response.has("defaultLanguage") && response.get("defaultLanguage") instanceof String);
                assertTrue(response.has("loginIdentifiers") && response.get("loginIdentifiers") instanceof String);
                assertTrue(response.has("loginIdentifierConflict") && response.get("loginIdentifierConflict") instanceof String);
                assertTrue(response.has("allowUnverifiedLogin") && response.get("allowUnverifiedLogin") instanceof Boolean);
                assertTrue(response.has("preventLoginIDHarvesting") && response.get("preventLoginIDHarvesting") instanceof Boolean);
                assertTrue(response.has("sendAccountDeletedEmail") && response.get("sendAccountDeletedEmail") instanceof Boolean);
                assertTrue(response.has("sendWelcomeEmail") && response.get("sendWelcomeEmail") instanceof Boolean);
                assertTrue(response.has("verifyEmail") && response.get("verifyEmail") instanceof Boolean);
                assertTrue(response.has("verifyProviderEmail") && response.get("verifyProviderEmail") instanceof Boolean);
            }

            @Override
            public void onFailure(JSONObject response) throws JSONException {
                assertTrue(response.has("errorMessage") && response.get("errorMessage") instanceof String);
                assertTrue(response.has("errorDetails") && response.get("errorDetails") instanceof String);
            }
        });
    }
}