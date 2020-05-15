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
import no.hiof.bjornap.pilocker.R;
import no.hiof.bjornap.pilocker.SSHConnection.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.Utility.EncryptedSharedPref;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    private NavController navController;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_general, rootKey);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        //SHUTDOWN
        shutdownPref = (Preference) findPreference("shutDownRaspberryPiPref");
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
        factoryResetPref = (Preference) findPreference("factoryResetPref");
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

        changeHandleSidePref.getEntry();

        //---------------------------CHANGE LOGIN METHOD-------------------------------------
        changeLoginMethodPref = (Preference) findPreference("changeLoginMethodPref");
        changeLoginMethodPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
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
        changeEmailPref = (Preference) findPreference("changeLoginMethodPref");
        changeEmailPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        //User navigates away from settings, save selections made.

        //ChangeHandleSide
        String leavingSelection = changeHandleSidePref.getEntry().toString().toLowerCase();
        EncryptedSharedPref.writeString(EncryptedSharedPref.SIDE, leavingSelection);


        Log.i("SIDESETTINGS", changeHandleSidePref.getEntry().toString().toLowerCase());


    }

    @Override
    public void onComplete(String result) {

    }
}
