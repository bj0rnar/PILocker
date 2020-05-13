package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.Utility.EncryptedSharedPref;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ConnectFragment extends Fragment {

    private Button connectBtn;

    public ConnectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        /**
         * TODO: Store key + ip to EncryptedSharedPreferences
         * TODO: Check if encrypted SharedPreferences has a key
         * TODO: Scan for SSID.
         * TODO: If key exists, connect to SSH via JSCH library, if NO KEY => InstallFragment
         * TODO: Validate IP address
         * TODO: Use jsch.addIdentity for SSH keybased authentication
         * TODO: Use jsch.addIdentity for SSH keybased authentication
         */


        //SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);

        //int key = 1;

        /*
        if (pref.getString("key_ip", null) != null){
            key = 0;
        }
        */



        if (EncryptedSharedPref.readString(EncryptedSharedPref.KEY_IP, null) != null &&
                EncryptedSharedPref.readString(EncryptedSharedPref.RSAPUB, null) != null) {
            navController.navigate(R.id.action_connectFragment2_to_unlockFragment2);
        }
        else {
            navController.navigate(R.id.action_connectFragment2_to_installWelcomeFragment);
        }


        

        //Basically slekk en ska ha dæ.
        /*
        if (key == 0){
            navController.navigate(R.id.action_connectFragment2_to_unlockFragment2);
        }
        else if (key == 1){

            navController.navigate(R.id.action_connectFragment2_to_installWelcomeFragment);
        }
         */
    }
}
