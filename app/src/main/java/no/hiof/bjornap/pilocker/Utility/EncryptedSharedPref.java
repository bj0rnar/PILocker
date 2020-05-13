package no.hiof.bjornap.pilocker.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.security.GeneralSecurityException;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

public class EncryptedSharedPref {

    /**
     * Singleton class to hold one instance of EncryptedSharedPreferences
     * Initialized in MainActivity, write and read from anywhere.
     */

    //All keywords used for storage
    public static final String SIDE = "side";
    public static final String DOORNAME = "doorName";
    public static final String KEY_IP = "key_ip";
    public static final String PASSWORD = "password";
    public static final String RSAPUB = "rsapub";
    public static final String RSAPRIV = "rsapriv";
    public static final String EMAIL = "email";
    public static final String LOGGINGENABLED = "isLoggingEnabled";
    public static final String APPLOGINMETHOD = "authMethod";
    public static final String PINCODE = "pinCode";


    private static SharedPreferences encryptedSharedPreferences;

    private EncryptedSharedPref() {}

    public static void initESP(Context context){
        if(encryptedSharedPreferences == null){
            try {
                String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

                encryptedSharedPreferences = EncryptedSharedPreferences.create(
                        "secret_shared_prefs",
                        masterKeyAlias,
                        context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
            }
            catch (GeneralSecurityException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void writeString(String key, String value) {
        SharedPreferences.Editor edit = encryptedSharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String readString(String key, String defaultValue){
        return encryptedSharedPreferences.getString(key, defaultValue);
    }

    public static void delete(String key) {
        SharedPreferences.Editor edit = encryptedSharedPreferences.edit();
        edit.remove(key);
        edit.apply();
    }

    public static void writeBool(String key, boolean value) {
        SharedPreferences.Editor edit = encryptedSharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static boolean readBool(String key, boolean defaultValue){
        return encryptedSharedPreferences.getBoolean(key, defaultValue);
    }

    public static void writeInt(String key, Integer value) {
        SharedPreferences.Editor edit = encryptedSharedPreferences.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static Integer readInt(String key, Integer defaultValue) {
        return encryptedSharedPreferences.getInt(key, defaultValue);
    }

}
