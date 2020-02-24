package no.hiof.bjornap.pilocker;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.Utility.SSHLogin;

import android.se.omapi.Session;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;


public class ConnectFragment extends Fragment {

    private Button connectBtn;
    private PageViewModel pvm = new PageViewModel();
    private SSHLogin login = new SSHLogin();

    public ConnectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        pvm = new ViewModelProvider(this).get(PageViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        /**
         * TODO: Check if encrypted SharedPreferences has a key
         * TODO: If key exists, connect to SSH via JSCH library, if NO KEY => InstallFragment
         * TODO: Create SERVICE to keep SESSION alive, or create new SESSION for each ACTION?
         * NOTE: ViewModel can't keep session alive, network stuff on UI thread == epic failure
         */


        int key = 1;

        //Basically slekk en ska ha dæ.
        if (key == 0){
            navController.navigate(R.id.action_connectFragment2_to_unlockFragment2);
        }
        else if (key == 1){
            navController.navigate(R.id.action_connectFragment2_to_installFragment);
        }


        /*
        connectBtn = view.findViewById(R.id.connectButton);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.execute();

                navController.navigate(R.id.action_connectFragment2_to_unlockFragment2);

            }
        });

         */
    }
}
