package com.ajain.securediary;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

class StoreData {
    private static StoreData ourInstance;

    static StoreData getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new StoreData(context);
        }
        return ourInstance;
    }

    private SharedPreferences shared;
    private String json;
    private ArrayList<Entry> dataEntries;
    private final String passKey = "password";
    private final String dataEntryKey = "dataEntry";

    private StoreData(Context context) {
        dataEntries = new ArrayList<>();
        final String SharedKey = "share";
        shared = context.getSharedPreferences(SharedKey, Context.MODE_PRIVATE);
    }

    void saveMasterPassword(String password) {
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(passKey, password);
        editor.apply();
    }

    void saveDataEntries(ArrayList list) {
        Gson gson = new Gson();
        json = gson.toJson(list);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(dataEntryKey, json);
        editor.apply();
    }

    String getMasterPassword() {
        return shared.getString(passKey, null);
    }

    ArrayList<Entry> getDataEntries() {
        Type type = new TypeToken<ArrayList<Entry>>() {
        }.getType();
        json = shared.getString(dataEntryKey, null);
        Gson gson = new Gson();
        dataEntries = gson.fromJson(json, type);
        PasswordCard.getInstance().setDataEntries(dataEntries);
        return dataEntries;
    }
}
