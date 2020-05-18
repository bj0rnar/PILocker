package no.hiof.bjornap.pilocker.Settings;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import no.hiof.bjornap.pilocker.R;
import no.hiof.bjornap.pilocker.SSHConnection.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.Utility.EncryptedSharedPref;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralSettingsFragment extends PreferenceFragmentCompat implements AsyncResponseInterface {

    private Preference shutdownPref;
    private Preference sendEmailPref;
    private Preference factoryResetPref;
    private ListPreference changeHandleSidePref;
    private Preference changeLoginMethodPref;
    private Preference changeEmailPref;
    private Preference deleteEmailPref;

    private SwitchPreference enableLoggingSwitchPref;

    private NavController navController;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_general, rootKey);
        
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PreferenceScreen x = (PreferenceScreen) findPreference("preferenceScreen");




        //initialize navcontroller
        navController = Navigation.findNavController(view);

        //find references.
        sendEmailPref =  findPreference("sendLogToEmailPref");
        shutdownPref =  findPreference("shutDownRaspberryPiPref");
        factoryResetPref =  findPreference("factoryResetPref");
        changeLoginMethodPref =  findPreference("changeLoginMethodPref");
        enableLoggingSwitchPref = findPreference("enableLoggingSwitchPref");
        changeEmailPref =  findPreference("changeLoginMethodPref");
        deleteEmailPref =  findPreference("deleteEmailPref");

        if (EncryptedSharedPref.readBool(EncryptedSharedPref.LOGGINGENABLED, false)){
            sendEmailPref.setVisible(true);
            changeEmailPref.setVisible(true);
        }
        else {
            sendEmailPref.setVisible(false);
            changeEmailPref.setVisible(false);
        }

        //SHUTDOWN

        shutdownPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext().getApplicationContext(), "Clicked the: " + preference.getKey(), Toast.LENGTH_SHORT).show();
                shutdownPref.setEnabled(false);
                shutdownPref.setShouldDisableView(true);
                return false;
            }
        });

        //FACTORY RESET

        factoryResetPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });

        //-------------------------CHANGE HANDLE SIDE---------------------------------
        changeHandleSidePref = (ListPreference) findPreference("sideSelectionListPref");
        String currentSelection = EncryptedSharedPref.readString(EncryptedSharedPref.SIDE, null);

        //Right = index 0
        //Left = index 1

        if (currentSelection != null){
            //changeHandleSidePref.setSummary("Current side is: " + currentSelection);
            Log.i("SIDESELECTION", currentSelection);
            if (currentSelection.equals("right")){
                changeHandleSidePref.setValueIndex(0);
            }
            else {
                changeHandleSidePref.setValueIndex(1);
            }
        }

        changeHandleSidePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.i("SIDESETTINGS", "NEWVALUE?: " + newValue.toString());

                //Save it to EncryptedSharedPreferences
                EncryptedSharedPref.writeString(EncryptedSharedPref.SIDE, newValue.toString().toLowerCase());

                //Set defaultValue to last selected
                if (newValue.toString().toLowerCase().equals("right")){
                    changeHandleSidePref.setValueIndex(0);
                }
                else {
                    changeHandleSidePref.setValueIndex(1);
                }

                return true;
            }
        });



        //---------------------------CHANGE LOGIN METHOD-------------------------------------

        changeLoginMethodPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return false;
            }
        });

        //ENABLE EMAIL

        enableLoggingSwitchPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.i("LOGGINGSETTINGS", newValue.toString());

                if((boolean) newValue){
                    sendEmailPref.setVisible(true);
                    deleteEmailPref.setVisible(true);
                }
                else {
                    sendEmailPref.setVisible(false);
                    deleteEmailPref.setVisible(false);
                }

                return true;
            }
        });

        //SEND EMAIL
        String currentEmail = EncryptedSharedPref.readString(EncryptedSharedPref.EMAIL, null);
        sendEmailPref = (Preference) findPreference("sendLogToEmailPref");
        if (currentEmail != null){
            sendEmailPref.setSummary("Send log file to " + sendEmailPref);
        }
        else {
            sendEmailPref.setSummary("No email set");
        }


        //DELETE EMAIL
        changeEmailPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });

    }

    @Override
    public void onComplete(String result) {

    }
}
