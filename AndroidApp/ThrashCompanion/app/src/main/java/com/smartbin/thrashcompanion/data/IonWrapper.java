package com.smartbin.thrashcompanion.data;

import android.app.Activity;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.Date;
import java.util.HashMap;

import servlet.entities.ContextEntity;
import servlet.entities.SmartbinEntity;

/**
 * Created by Ivan on 28-Nov-16.
 */

public class IonWrapper {
    final static String TAG = "IonWrapper";
    final static String remoteIP = "http://smartbinapi.appspot.com";

    static HashMap<String, SmartbinEntity> cache_smartbin = new HashMap<>();
    static HashMap<String, ContextEntity[]> cache_contexts = new HashMap<>();
    static HashMap<String, Date> timestamps = new HashMap<>();

    public static SmartbinEntity getBin(String name) {
        return cache_smartbin.get(name);
    }

    public static ContextEntity[] getBinCtx(String name) {
        return cache_contexts.get(name);
    }

    public static interface ContextConsumer {
        void post(ContextEntity[] responseData, SmartbinEntity source);
    }
    public static interface SmartbinConsumer {
        void post(SmartbinEntity[] responseData);
    }

    public static void getBins(Activity context, final SmartbinConsumer consumer) throws Exception {
                Ion.with(context)
                .load(remoteIP+"/r/bins")
                .asJsonArray().setCallback(new com.koushikdutta.async.future.FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        Gson gson = new Gson();
                        int i = 0;
                        SmartbinEntity[] responseData = new SmartbinEntity[result.size()];
                        for (JsonElement sbe : result) {
                            JsonObject element = sbe.getAsJsonObject();
                            responseData[i] = gson.fromJson(element, SmartbinEntity.class);
                            cache_smartbin.put( responseData[i].name, responseData[i++] );
                        }
                        Log.i(TAG, "Received "+responseData.length+" bins");
                        if(consumer != null) consumer.post(responseData);
                    }
                });
    }

    public static void getContextsForBin(Activity context, final ContextConsumer consumer, final SmartbinEntity source) {
        Date now = new Date();
        if(!timestamps.containsKey(source.name) || (now.getTime() - timestamps.get(source.name).getTime() ) > (DateUtils.MINUTE_IN_MILLIS * 5) ) {
            Log.i(TAG, "Ctx timestamp more than 5 minutes old");
            Ion.with(context)
                    .load(remoteIP+"/r/ctx?bin="+source.name)
                    .asJsonArray().setCallback(new com.koushikdutta.async.future.FutureCallback<JsonArray>() {
                @Override
                public void onCompleted(Exception e, JsonArray result) {
                    Gson gson = new Gson();
                    int i = 0;
                    ContextEntity[] responseData = new ContextEntity[result.size()];
                    for (JsonElement sbe : result) {
                        JsonObject element = sbe.getAsJsonObject();
                        responseData[i] = gson.fromJson(element, ContextEntity.class);
                        Log.i(TAG, responseData[i].level + " " + responseData[i++].concentration);
                    }
                    cache_contexts.put(source.name, responseData);
                    timestamps.put(source.name, new Date() );
                    if(consumer != null) consumer.post(responseData, source);
                }
            });
        }
        else if(consumer != null) consumer.post( cache_contexts.get( source.name ), source );
    }

}
