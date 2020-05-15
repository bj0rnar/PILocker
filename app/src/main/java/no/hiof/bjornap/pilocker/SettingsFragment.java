package no.hiof.bjornap.pilocker;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*
        View v = inflater.inflate(R.layout.custom_preference,container, false);
        Toolbar t = v.findViewById(R.id.settings_toolbar);
        t.setTitle("HEI");
        Log.i("SETTINGSSTUFF", t.toString());
        */

        View v = (View)getActivity().findViewById(R.id.custom_layout);
        Toolbar t = v.findViewById(R.id.settings_toolbar);
        t.setTitle("HEI");



        return super.onCreateView(inflater, container, savedInstanceState);


    }
}
