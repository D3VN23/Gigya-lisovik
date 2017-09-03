package com.example.alexanderlisovik.gigya;

import java.lang.String;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONException;

import android.os.StrictMode;
import android.widget.TextView;

import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;

public class MainActivity extends AppCompatActivity {

    Button saveButton;
    Button readOnlyButton;

    EditText defaultLanguageEditText;
    EditText loginIdentifiersEditText;

    Spinner loginIdentifiersSpinner;

    CheckBox allowUnverifiedLoginCheckBox;
    CheckBox preventLoginCheckBox;
    CheckBox sendAccountDeletedEmailCheckBox;
    CheckBox sendWelcomeEmailCheckBox;
    CheckBox verifyEmailCheckbox;
    CheckBox verifyProviderEmailCheckBox;

    JSONObject oldAccountOptions;

    TextView loginIdentifiersTextView;

    public enum LoginIdentifier{
        ignore, failOnSiteConflictingIdentity, failOnAnyConflictingIdentity
    }

    //NetworkManager networkManager = new NetworkManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Thread Policy

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Set UI properties

        this.defaultLanguageEditText = (EditText) findViewById(R.id.defaultLanguageEditTextId);
        this.loginIdentifiersEditText = (EditText) findViewById(R.id.loginIdentifiersEditTextId);
        this.loginIdentifiersSpinner = (Spinner) findViewById(R.id.loginIdentifiersSpinnerId);
        this.loginIdentifiersTextView = (TextView) findViewById(R.id.loginIdentifiersTextViewId);
        this.allowUnverifiedLoginCheckBox = (CheckBox) findViewById(R.id.allowUnverifiedCheckBoxId);
        this.preventLoginCheckBox = (CheckBox) findViewById(R.id.preventLoginCheckBoxId);
        this.sendAccountDeletedEmailCheckBox = (CheckBox) findViewById(R.id.sendAccountDeletedEmailCheckBoxId);
        this.sendWelcomeEmailCheckBox = (CheckBox) findViewById(R.id.sendWelcomeEmailCheckBoxId);
        this.verifyEmailCheckbox = (CheckBox) findViewById(R.id.verifyEmailCheckBoxId);
        this.verifyProviderEmailCheckBox = (CheckBox) findViewById(R.id.verifyProviderEmailCheckBoxId);
        this.saveButton = (Button) findViewById(R.id.saveButtonId);
        this.readOnlyButton = (Button) findViewById(R.id.readOnlyButtonId);

        //Set spinner data source

        final String[] spinnerDataSource = new String[] {
                LoginIdentifier.ignore.name(),
                LoginIdentifier.failOnAnyConflictingIdentity.name(),
                LoginIdentifier.failOnSiteConflictingIdentity.name()
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, spinnerDataSource);
        loginIdentifiersSpinner.setAdapter(adapter);

        //Spinner action

        loginIdentifiersSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                loginIdentifiersTextView.setText(spinnerDataSource[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        //Button action

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject accountOptions = createAccountObjectJSON();
                System.out.println(accountOptions);
                setAccountOptions();
            }
        });

        //Readonly action

        readOnlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableReadOnlyMode();
            }
        });

        //Create POST request (get)

        NetworkManager.getAccountOptions(Constants.baseUrl + Constants.getPolicies, new Callback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                Toast.makeText(getApplicationContext(), "Loaded", Toast.LENGTH_LONG).show();
                oldAccountOptions = response;
                setFields(response);
            }

            @Override
            public void onFailure(JSONObject response) throws JSONException {
                Toast.makeText(getApplicationContext(), response.getString("errorMessage").toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void enableReadOnlyMode() {
        this.allowUnverifiedLoginCheckBox.setEnabled(!this.allowUnverifiedLoginCheckBox.isEnabled());
        this.preventLoginCheckBox.setEnabled(!this.preventLoginCheckBox.isEnabled());
        this.sendAccountDeletedEmailCheckBox.setEnabled(!this.sendAccountDeletedEmailCheckBox.isEnabled());
        this.sendWelcomeEmailCheckBox.setEnabled(!this.sendWelcomeEmailCheckBox.isEnabled());
        this.verifyEmailCheckbox.setEnabled(!this.verifyEmailCheckbox.isEnabled());
        this.verifyProviderEmailCheckBox.setEnabled(!this.verifyProviderEmailCheckBox.isEnabled());

        this.loginIdentifiersSpinner.setEnabled(!this.loginIdentifiersSpinner.isEnabled());

        this.defaultLanguageEditText.setEnabled(!this.defaultLanguageEditText.isEnabled());
        this.loginIdentifiersEditText.setEnabled(!this.loginIdentifiersEditText.isEnabled());
    }

    public JSONObject createAccountObjectJSON() {
        try {
            JSONObject accountOptions = new JSONObject();
            accountOptions.put("defaultLanguage", this.defaultLanguageEditText.getText().toString());
            accountOptions.put("loginIdentifiers", this.loginIdentifiersEditText.getText().toString());
            accountOptions.put("loginIdentifierConflict", this.loginIdentifiersTextView.getText().toString());
            accountOptions.put("allowUnverifiedLogin", this.allowUnverifiedLoginCheckBox.isChecked());
            accountOptions.put("preventLoginIDHarvesting", this.preventLoginCheckBox.isChecked());
            accountOptions.put("sendAccountDeletedEmail", this.sendAccountDeletedEmailCheckBox.isChecked());
            accountOptions.put("sendWelcomeEmail", this.sendWelcomeEmailCheckBox.isChecked());
            accountOptions.put("verifyEmail", this.verifyEmailCheckbox.isChecked());
            accountOptions.put("verifyProviderEmail", this.verifyProviderEmailCheckBox.isChecked());
            return accountOptions;
        } catch (JSONException exception) {
            exception.printStackTrace();
            return new JSONObject();
        }
    }

    private void setAccountOptions() {
        final JSONObject newAccountOptions = createAccountObjectJSON();
        NetworkManager.setAccountOptions(Constants.baseUrl + Constants.setPolicies, newAccountOptions, new Callback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                String statusReason = response.get("statusReason").toString();
                oldAccountOptions = newAccountOptions;
                Toast.makeText(getApplicationContext(), statusReason, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(JSONObject response) throws JSONException {
                setFields(oldAccountOptions);
                String errorMessage = response.get("errorMessage").toString();
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    void setFields(JSONObject accountOptions) throws JSONException {
        this.verifyEmailCheckbox.setChecked((Boolean) accountOptions.get("verifyEmail"));
        this.verifyProviderEmailCheckBox.setChecked((Boolean) accountOptions.get("verifyProviderEmail"));
        this.allowUnverifiedLoginCheckBox.setChecked((Boolean) accountOptions.get("allowUnverifiedLogin"));
        this.preventLoginCheckBox.setChecked((Boolean) accountOptions.get("preventLoginIDHarvesting"));
        this.sendWelcomeEmailCheckBox.setChecked((Boolean) accountOptions.get("sendWelcomeEmail"));
        this.sendAccountDeletedEmailCheckBox.setChecked((Boolean) accountOptions.get("sendAccountDeletedEmail"));
        this.defaultLanguageEditText.setText(accountOptions.get("defaultLanguage").toString());
        this.loginIdentifiersEditText.setText(accountOptions.get("loginIdentifiers").toString());
        this.loginIdentifiersTextView.setText(LoginIdentifier.valueOf(accountOptions.get("loginIdentifierConflict").toString()).name());
    }
}
