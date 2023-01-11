package com.kamaii.rider.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.ui.models.OfflineDataArrayListModal;

import java.lang.reflect.Type;

/**
 * Created by VARUN on 01/01/19.
 */
public class SharedPrefrence {
    public static SharedPreferences myPrefs;
    public static SharedPreferences.Editor prefsEditor;

    public static SharedPrefrence myObj;

    private SharedPrefrence() {

    }

    public void clearAllPreferences() {
        prefsEditor = myPrefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }


    public static SharedPrefrence getInstance(Context ctx) {
        if (myObj == null) {
            myObj = new SharedPrefrence();
            myPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
            prefsEditor = myPrefs.edit();
        }
        return myObj;
    }

    public void clearPreferences(String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }


    public void setIntValue(String Tag, int value) {
        prefsEditor.putInt(Tag, value);
        prefsEditor.apply();
    }

    public int getIntValue(String Tag) {
        return myPrefs.getInt(Tag, 0);
    }

    public void setLongValue(String Tag, long value) {
        prefsEditor.putLong(Tag, value);
        prefsEditor.apply();
    }

    public long getLongValue(String Tag) {
        return myPrefs.getLong(Tag, 0);
    }


    public void setValue(String Tag, String token) {
        prefsEditor.putString(Tag, token);
        prefsEditor.commit();
    }

    public String getValue(String Tag) {
        if (Tag.equalsIgnoreCase(Consts.LATITUDE))
            return myPrefs.getString(Tag, "");
        else if (Tag.equalsIgnoreCase(Consts.LONGITUDE))
            return myPrefs.getString(Tag, "");
        else if (Tag.equalsIgnoreCase("startdate"))
            return myPrefs.getString(Tag, "");
        return myPrefs.getString(Tag, "");
    }

    public boolean getBooleanValue(String Tag) {
        return myPrefs.getBoolean(Tag, false);

    }

    public void setBooleanValue(String Tag, boolean token) {
        prefsEditor.putBoolean(Tag, token);
        prefsEditor.commit();
    }

    public void setParentUser(UserDTO userDTO, String tag) {

        Gson gson = new Gson();
        String hashMapString = gson.toJson(userDTO);

        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }


    public UserDTO getParentUser(String tag) {
        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new UserDTO();
        } else {
            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<UserDTO>() {
            }.getType();
            UserDTO testHashMap = gson.fromJson(storedHashMapString, type);
            return testHashMap;
        }
    }

    public void setOfflineData(OfflineDataArrayListModal userDTO, String tag) {

        Gson gson = new Gson();
        String arrayList1 = gson.toJson(userDTO);

        prefsEditor.putString(tag, arrayList1);
        prefsEditor.apply();
    }

    public OfflineDataArrayListModal getOfflineData(String tag) {

     //   Gson gson = new Gson();
     //   String arrayListString = db.getString(key, null);
    //    Type type = new TypeToken<ArrayList<ArrayObject>>() {}.getType();
     //   ArrayList<ArrayObject> arrayList = gson.fromJson(arrayListString, type);

        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new OfflineDataArrayListModal();
        } else {
            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<OfflineDataArrayListModal>() {
            }.getType();
            OfflineDataArrayListModal testHashMap = gson.fromJson(storedHashMapString, type);
            return testHashMap;
        }
    }
}
