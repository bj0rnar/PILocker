package no.hiof.bjornap.pilocker.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import no.hiof.bjornap.pilocker.R;
import no.hiof.bjornap.pilocker.Utility.EncryptedSharedPref;

public class ChangeSideSettings extends PreferenceFragmentCompat {

    ListPreference sideSelection;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_side, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String currentSelection = EncryptedSharedPref.readString(EncryptedSharedPref.SIDE, null);

        sideSelection = (ListPreference) findPreference("sideSelectionListPref");

        //Right = index 0
        //Left = index 1

        if (currentSelection != null){
            Log.i("SIDESELECTION", currentSelection);
            if (currentSelection.equals("right")){
                sideSelection.setValueIndex(0);
            }
            else {
                sideSelection.setValueIndex(1);
            }
        }




    }
}
