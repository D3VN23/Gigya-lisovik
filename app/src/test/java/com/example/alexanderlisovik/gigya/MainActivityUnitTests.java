package com.example.alexanderlisovik.gigya;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import java.io.IOException;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)

public class MainActivityUnitTests {

    private MainActivity mainActivity;

    @Before
    public void setup() {
        this.mainActivity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void testAccountOptionsTypeAccorodance() throws JSONException, IOException, InterruptedException {
        JSONObject accountOptions = new JSONObject();
        accountOptions = this.mainActivity.createAccountObjectJSON();
        assertTrue(accountOptions.get("defaultLanguage") instanceof String);
        assertTrue(accountOptions.get("loginIdentifiers") instanceof String);
        assertTrue(accountOptions.get("loginIdentifierConflict") instanceof String);
        assertTrue(accountOptions.get("allowUnverifiedLogin") instanceof Boolean);
        assertTrue(accountOptions.get("preventLoginIDHarvesting") instanceof Boolean);
        assertTrue(accountOptions.get("sendAccountDeletedEmail") instanceof Boolean);
        assertTrue(accountOptions.get("sendWelcomeEmail") instanceof Boolean);
        assertTrue(accountOptions.get("verifyEmail") instanceof Boolean);
        assertTrue(accountOptions.get("verifyProviderEmail") instanceof Boolean);
    }

    @Test
    public void testReadOnlyMode() {

        this.mainActivity.enableReadOnlyMode();

        assertFalse(this.mainActivity.allowUnverifiedLoginCheckBox.isEnabled());
        assertFalse(this.mainActivity.preventLoginCheckBox.isEnabled());
        assertFalse(this.mainActivity.sendAccountDeletedEmailCheckBox.isEnabled());
        assertFalse(this.mainActivity.sendWelcomeEmailCheckBox.isEnabled());
        assertFalse(this.mainActivity.verifyEmailCheckbox.isEnabled());
        assertFalse(this.mainActivity.verifyProviderEmailCheckBox.isEnabled());

        assertFalse(this.mainActivity.loginIdentifiersSpinner.isEnabled());

        assertFalse(this.mainActivity.defaultLanguageEditText.isEnabled());
        assertFalse(this.mainActivity.loginIdentifiersEditText.isEnabled());
    }
}
