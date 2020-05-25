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

    /**
     * Starting fragment, originally planned as a splash screen in case slower encryption methods were used
     * It's kept for checking keys, in the edge case that the end user has a really slow device.
     */


    public ConnectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_connect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);


        //If RPI IP and public key is stored, assume that the installation has been completed and send user to Unlock screen.

        if (EncryptedSharedPref.readString(EncryptedSharedPref.KEY_IP, null) != null &&
                EncryptedSharedPref.readString(EncryptedSharedPref.RSAPUB, null) != null) {
            navController.navigate(R.id.action_connectFragment2_to_unlockFragment2);
        }
        else {
            navController.navigate(R.id.action_connectFragment2_to_installWelcomeFragment);
        }

    }
}
