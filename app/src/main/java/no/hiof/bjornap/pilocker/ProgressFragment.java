package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.SSHConnection.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.SSHConnection.SSHInstaller;
import no.hiof.bjornap.pilocker.SSHConnection.SSHReader;
import no.hiof.bjornap.pilocker.Utility.Tuples;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static no.hiof.bjornap.pilocker.Utility.RSAGenerator.generateRSAPairs;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment implements AsyncResponseInterface {

    private String ethernetIP;

    public ProgressFragment() {
        // Required empty public constructor
    }

    //public SSHInstaller sshInstaller;

    public AsyncResponseInterface thisInterface = this;

    private String defaultuser = "bjornar";
    private String defaultpass = "toor";

    private String actualUser = "ubuntu";
    private String actualPass = "gruppe6";
    private String wlanIP = "10.0.60.1";
    private String cmd = "./getIp.sh";

    private NavController navController;
    private String doorName = "";
    private String side = "";
    private String ip = "";

    private String pub;
    private String priv;

    private SharedPreferences pref;

    private String prefHost;
    private String prefSide;
    private String prefName;
    private String prefPub;
    private String prefPriv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        //keyReader.response = this;
        //sshInstaller.response = this;


        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        Log.i("SSHREADER", "PROGRESSFRAGMENT STARTED");

        //SPIN THE WHEEL FOR NOW

        pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
        prefHost = pref.getString("key_ip", null);
        prefName = pref.getString("doorName", null);
        prefSide = pref.getString("side", null);

        Tuples tuples = generateRSAPairs();
        priv = (String)tuples.priv;
        pub = (String)tuples.pub;

        Log.i("SSHREADER", "priv key" + priv);
        Log.i("SSREADER", "pub key " + pub);


        SSHInstaller sshInstaller = new SSHInstaller();
        sshInstaller.response = thisInterface;
        sshInstaller.execute(pub, actualUser, prefHost, actualPass);

        /*
        if (getArguments() != null){
            doorName = getArguments().getString("doorName");
            side = getArguments().getString("side");
            ip = getArguments().getString("ip");

            Log.i("BUNDLE PROGRESS", doorName);
            Log.i("BUNDLE PROGRESS", side);
            Log.i("BUNDLE PROGRESS", ip);

            //Run reader method
            //reader.execute(actualUser, actualPass, wlanIP, cmd);

            Tuples tuples = generateRSAPairs();
            priv = (String)tuples.priv;
            pub = (String)tuples.pub;

            Log.i("RSATEST", priv);
            Log.i("RSATEST", pub);


            SSHInstaller sshInstaller = new SSHInstaller();
            sshInstaller.response = thisInterface;
            sshInstaller.execute(pub, actualUser, ip, actualPass);


        }
        */

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

        /**
         * At this point, don't send it via bundle. The key and the IP belongs in
         * EncryptedSharedPreference
         */

        Log.i("SSHREADER", "onComplete i progressfragment?!");

        //Save to sharedpreferences, switch to encryptedsharedpreferences later.
        SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("rsapub", pub);
        edit.putString("rsapriv", priv);
        edit.apply();


        navController.navigate(R.id.action_progressFragment_to_unlockFragment2);
    }
}
