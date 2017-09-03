package com.example.alexanderlisovik.gigya;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alexander.lisovik on 9/2/17.
 */

public interface Callback  {
    void onSuccess(JSONObject response) throws JSONException;
    void onFailure(JSONObject response) throws JSONException;
}
