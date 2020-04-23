package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import no.hiof.bjornap.pilocker.Utility.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.Utility.RSAGenerator;
import no.hiof.bjornap.pilocker.Utility.SSHConnector;
import no.hiof.bjornap.pilocker.Utility.SSHExecuter;
import no.hiof.bjornap.pilocker.Utility.WheresMyTuples;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

import static no.hiof.bjornap.pilocker.Utility.RSAGenerator.generateRSAPairs;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnlockFragment extends Fragment implements AsyncResponseInterface {

    private SSHConnector sshConnector = new SSHConnector();

    private AsyncResponseInterface thisInterface = this;

    //private SSHExecuter executer = new SSHExecuter();
    private TextView statusText;
    private TextView doorNameTxt;

    private String prefHost;
    private String prefSide;
    private String prefName;

    private SharedPreferences pref;

    private Button unlockBtn;
    private Button lockBtn;

    private String hostName = "ubuntu";

    private Executor exec;
    private BiometricPrompt bioPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    public UnlockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //executer.response = this;
        exec = ContextCompat.getMainExecutor(getActivity().getApplicationContext());
        bioPrompt = new BiometricPrompt(UnlockFragment.this, exec, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getActivity().getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setDeviceCredentialAllowed(true)
                .build();

        bioPrompt.authenticate(promptInfo);


        //Initializing interface for dependency injection

        return inflater.inflate(R.layout.fragment_unlock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Check bundle
        if (getArguments() != null){
            Log.i("FINALSTAGE", "UNLOCKFRAGMENT RECEEVES IP: " + getArguments().getString("ip"));
            Log.i("FINALSTAGE", "UNLOCKFRAGMENT RECEIVES DOORNAME: " + getArguments().getString("doorName"));
            Log.i("FINALSTAGE", "UNLOCKFRAGMENT RECEIVES SIDE: " + getArguments().getString("side"));
        }

        //Initialize sharedpreferences
        pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
        prefHost = pref.getString("key_ip", null);
        prefName = pref.getString("doorName", null);
        prefSide = pref.getString("side", null);

        Log.i("FINALSTAGE", "SHAREDPREFERENCES HAS IP: " + prefHost);
        Log.i("FINALSTAGE", "SHAREDPREFERENCES HAS NAME: " + prefName);
        Log.i("FINALSTAGE", "SHAREDPREFERENCES HAS SIDE: " + prefSide);

        doorNameTxt = view.findViewById(R.id.unlock_status_door_name);
        doorNameTxt.setText(prefName);

        statusText = view.findViewById(R.id.unlock_status_status_textView);

        statusText.setText("UNKNOWN");
        lockBtn = view.findViewById(R.id.lockBtn);
        unlockBtn = view.findViewById(R.id.unlockBtn);

        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusText.setText("LOCKED");

                lockBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                lockBtn.setEnabled(false);
                unlockBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                unlockBtn.setEnabled(false);
                /*
                String command = "";

                if (prefSide == "right"){
                    command = "./turnCounterClockwise.sh; echo $?";
                }
                else {
                    command = "./turnClockwise.sh; echo $?";
                }

                //USE prefHost FOR ACCEPTANCETEST
                //String manualHost = "158.39.162.128";



                SSHExecuter executer = new SSHExecuter();
                executer.response = thisInterface;
                try {
                    executer.execute(hostName, prefHost, command, "rsaplaceholder");
                }
                catch (Exception e){
                    unlockBtn.getBackground().setColorFilter(null);
                    unlockBtn.setEnabled(true);
                    lockBtn.getBackground().setColorFilter(null);
                    lockBtn.setEnabled(true);
                }
                 */
                /*
                for(int i = 0; i < 1000; i++) {
                    SSHConnector rere = new SSHConnector();
                    rere.response = thisInterface;
                    rere.execute();
                }
                */
            }
        });



        unlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusText.setText("UNLOCKED");

                lockBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                lockBtn.setEnabled(false);
                unlockBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                unlockBtn.setEnabled(false);

                /*
                String command = "";

                if (prefSide == "left"){
                    command = "./turnClockwise.sh; echo $?";
                }
                else {
                    command = "./turnCounterClockwise.sh; echo $?";
                }

                //USE prefHost FOR ACCEPTANCETEST
                //String manualHost = "158.39.162.128";

                SSHExecuter executer = new SSHExecuter();
                executer.response = thisInterface;
                try {
                    executer.execute(hostName, prefHost, command, "rsaplaceholder");
                }
                catch (Exception e){
                    unlockBtn.getBackground().setColorFilter(null);
                    unlockBtn.setEnabled(true);
                    lockBtn.getBackground().setColorFilter(null);
                    lockBtn.setEnabled(true);
                }
                */

                WheresMyTuples myTuples = generateRSAPairs();
                String priv = (String)myTuples.priv;
                String pub = (String)myTuples.pub;

                Log.i("RSATEST", priv);
                Log.i("RSATEST", pub);

                SSHConnector rere = new SSHConnector();
                rere.response = thisInterface;

                rere.execute(pub);

            }
        });

    }


    @Override
    public void onComplete(String result) {
        Log.i("FINALSTAGE", "ONCOMPLETE FRA ASYNC MOTTAR: " + result);
        //UNLOCK BUTTONS ONLY ON RESPONSE
        unlockBtn.getBackground().setColorFilter(null);
        unlockBtn.setEnabled(true);
        lockBtn.getBackground().setColorFilter(null);
        lockBtn.setEnabled(true);
        /*
        if (result != null) {
            if (!result.equals("0")) {
                Log.i("FINALSTAGE", "echo $? gives no output");
            }
        }
        else {
            Toast.makeText(getContext().getApplicationContext(), "Error, are you connected to eduroam?", Toast.LENGTH_SHORT).show();
            statusText.setText("Unknown");
        }
         */

    }
    
    @Override
    public void onStart() {
        super.onStart();
        Log.d("BIOMETRIC", "onStart called");
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setDeviceCredentialAllowed(true)
                .build();

        bioPrompt.authenticate(promptInfo);
    }
}
