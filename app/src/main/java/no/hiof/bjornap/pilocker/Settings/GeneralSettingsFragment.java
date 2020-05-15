package no.hiof.bjornap.pilocker.Settings;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import no.hiof.bjornap.pilocker.R;
import no.hiof.bjornap.pilocker.SSHConnection.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.Utility.EncryptedSharedPref;

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
    private Preference changeHandleSidePref;
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

        //CHANGE HANDLE SIDE
        changeHandleSidePref = (Preference) findPreference("changeHandleSidePref");
        changeHandleSidePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                navController.navigate(R.id.action_settingsFragment_to_changeSideSettings);
                return true;
            }
        });

        //CHANGE LOGIN METHOD
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
    public void onComplete(String result) {

    }
}
