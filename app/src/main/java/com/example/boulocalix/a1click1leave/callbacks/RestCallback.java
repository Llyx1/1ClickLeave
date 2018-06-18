package com.example.boulocalix.a1click1leave.callbacks;

import com.example.boulocalix.a1click1leave.repository.RestError;
import com.google.api.client.json.Json;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RestCallback<T> implements Callback<T> {
    private static List<RestCallback> mList = new ArrayList<>();

    public abstract void success(T res);

    public abstract void failure(RestError error, String mess);

    public RestCallback() {
        mList.add(this);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        switch (response.code()) {
            case 200:
                success(response.body());
                break;
            case 400:
                try {
                    JSONObject jObjError = new JSONObject(response.errorBody().toString()) ;
                    int codeError = Integer.parseInt(jObjError.getString("code"));
                    failure(new RestError(codeError, jObjError.getString("code")), jObjError.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    failure(new RestError(response.code(), response.message()), response.message());
                }

                break;
            case 401:
            case 500:
            case 403:
            case 405:
            case 406:
                failure(new RestError(response.code(), response.message()), response.message() + " - "+ response.code());
                break;
            default:
                failure(new RestError(RestError.SERVER_ERROR), response.message()+ " - "+ response.code());
                break;

        }
        mList.remove(this);
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        RestError restError;
        String error = throwable.getMessage();
        restError = new RestError(RestError.SERVER_ERROR, "No connexion, please connect your device");
        failure(restError, error);
    }
}
