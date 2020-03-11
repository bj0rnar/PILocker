package no.hiof.bjornap.pilocker;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.Model.Door;
import no.hiof.bjornap.pilocker.Utility.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.Utility.SSHReader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment implements AsyncResponseInterface {

    private String ethernetIP;

    public ProgressFragment() {
        // Required empty public constructor
    }

    private SSHReader reader = new SSHReader();

    private String defaultuser = "bjornar";
    private String defaultpass = "toor";

    private String actualUser = "ubuntu";
    private String actualPass = "gruppe6";
    private String wlanIP = "10.0.60.1";
    private String cmd = "./getIp.sh";

    private NavController navController;
    private String doorName = "";
    private String side = "";

    private Button btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        reader.response = this;

        //keyReader.response = this;


        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        
        //SPIN THE WHEEL FOR NOW, MIGRATE INSTALLFRAGMENT LOGIC TO THIS CLASS.

        if (getArguments() != null){
            doorName = getArguments().getString("doorName");
            side = getArguments().getString("side");
            Log.i("BUNDLE PROGRESS", doorName);
            Log.i("BUNDLE PROGRESS", side);

            //Run reader method
            reader.execute(actualUser, actualPass, wlanIP, cmd);


        }


        /*
        if (getArguments() != null) {
            ethernetIP = getArguments().getString("ip");
            Log.i("SSHREADER", "Progressfragment: " + ethernetIP);




            String testIp = "192.168.10.153";
            String cmd = "./fakersa.sh";

            //for prototyping: burde stå ./getRsa.sh
            //reader.execute(actualUser, actualPass, ethernetIP, "ls -l");

            //reader.execute(defaultuser, defaultpass,testIp, cmd);
        }

        Button btn = view.findViewById(R.id.progress_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //reader.execute(actualUser, actualPass, ethernetIP, "ifconfig");

                reader.execute(actualUser, actualPass, ethernetIP, "ls -l");
               /*
                if (reader.getStatus() != AsyncTask.Status.RUNNING) {
                    keyReader.execute(actualUser, actualPass, ethernetIP, "ls -l");
                }
                else {
                    Log.i("SSHREADER", "PROGRESSFRAGMENT, READER: " + reader.getStatus().toString());
                }

                */
        /*
            }
        });
        */


    }

    @Override
    public void onComplete(String result) {
        result = result.substring(0, result.length()-1);
        Log.i("SSHREADER", "Progressfragment FAKERSA: " + result);

        /**
         * At this point, don't send it via bundle. The key and the IP belongs in
         * EncryptedSharedPreference
         */

        Bundle b = new Bundle();
        b.putString("ip", result);
        navController.navigate(R.id.action_progressFragment_to_unlockFragment2, b);
    }
}
