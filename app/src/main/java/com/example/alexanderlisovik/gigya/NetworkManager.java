package com.example.alexanderlisovik.gigya;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.HttpUrl;

public class NetworkManager {
    static void getAccountOptions(String stringUrl, Callback callback) {

        BufferedReader reader = null;

        try {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("apiKey", Constants.apiKey)
                    .appendQueryParameter("secret", Constants.secretKey)
                    .appendQueryParameter("userKey", Constants.userKey);
            String query = builder.build().getEncodedQuery();

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            JSONObject response = new JSONObject(buffer.toString());
            System.out.println("RESPONSE:");
            System.out.println(response);

            if ((Integer)response.get("statusCode") == 200) {
                JSONObject accountOptions = new JSONObject(response.get("accountOptions").toString());
                callback.onSuccess(accountOptions);
            } else {
                callback.onFailure(response);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static void setAccountOptions(String stringUrl, JSONObject accountOptions, Callback callback) {

        BufferedReader reader = null;

        try {
            URL url = new URL("https://accounts.us1.gigya.com/accounts.setPolicies");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("apiKey", Constants.apiKey)
                    .appendQueryParameter("secret", Constants.secretKey)
                    .appendQueryParameter("userKey", Constants.userKey)
                    .appendQueryParameter("accountOptions", accountOptions.toString());
            String query = builder.build().getEncodedQuery();

            System.out.println("NEW:");
            System.out.println(accountOptions.toString());
            System.out.println(query);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            JSONObject response = new JSONObject(buffer.toString());
            System.out.println("RESPONSE:");
            System.out.println(response);

            Integer statusCode = (Integer) response.get("statusCode");

            if (statusCode == 200) {
                callback.onSuccess(response);
            } else {
                callback.onFailure(response);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
