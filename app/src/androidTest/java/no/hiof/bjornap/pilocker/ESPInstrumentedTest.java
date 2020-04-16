package no.hiof.bjornap.pilocker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ESPInstrumentedTest {

    String masterKeyAlias;
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;



    long start;
    long end;

    @Before
    public void setup(){


        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            sharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    appContext,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        }
        catch (GeneralSecurityException | IOException e ){
            System.out.println(e.getMessage());
        }

        editor = sharedPreferences.edit();
        editor.putString("02", "hay").apply();


    }

    @Test
    public void getStuffFromESP(){
        start = System.currentTimeMillis();
        String storedString = sharedPreferences.getString("02", "");
        assertEquals("hay", storedString);
        end = System.currentTimeMillis();
        long elapsedSeconds = end-start;
        Log.i("TimeTest", String.valueOf(elapsedSeconds));
    }



    @Test
    public void measureTimeTakenWithNewInstance() {

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences pref = appContext.getSharedPreferences("secret_shared_prefs", 0);
        String storedString = pref.getString("02", "");
        assertEquals("hay", storedString);
    }
}
