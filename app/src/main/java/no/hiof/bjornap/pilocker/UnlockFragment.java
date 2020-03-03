package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.Utility.SSHConnector;
import no.hiof.bjornap.pilocker.Utility.SSHExecuter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnlockFragment extends Fragment {

    private SSHConnector sshConnector = new SSHConnector();
    private SSHExecuter sshExecuter = new SSHExecuter();

    public UnlockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_unlock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        if (getArguments() != null){
            Log.i("SSHREADER", "UNLOCKFRAGMENT HAS IP: " + getArguments().getString("ip"));
            Log.i("SSHREADER", "UNLOCKFRAGMENT HAS RSA: " + getArguments().getString("rsa"));
        }

         */




        final NavController navController = Navigation.findNavController(view);

        final Button unlockBtn = view.findViewById(R.id.unlockBtn);
        unlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String usr = "bjornar";
                //String host = "192.168.10.153";
                //String cmd = "./lol.sh";

                SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
                String prefHost = pref.getString("key_ip", null);

                Log.i("SSHREADER", "Fra UnlockFragment: " + prefHost);

                String usr = "ubuntu";
                //String host = getArguments().getString("ip");
                String hardcodedHost = "158.39.162.152";
                String cmd = "./lockTest.sh";

                //Log.i("SSHREADER", "EXECUTOR STATUS: " + sshExecuter.getStatus().toString());
                //Log.i("SSHREADER", "EXECUTOR STATUS: " + sshExecuter.isCancelled());

                SSHExecuter executor = new SSHExecuter();

                executor.execute(usr, prefHost, cmd, "lol");
                /*
                if (sshExecuter.getStatus() != AsyncTask.Status.RUNNING) {
                    sshExecuter.execute(usr, hardcodedHost, cmd, "lol");
                }
                else {
                    Log.i("SSHREADER", "EXECUTOR STATUS: " + sshExecuter.getStatus().toString());
                }
                */
                //Connect and disconnect in one move
                //sshConnector.execute();

            }
        });

        Button logoutBtn = view.findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_unlockFragment2_to_connectFragment2);
            }
        });
    }


}
