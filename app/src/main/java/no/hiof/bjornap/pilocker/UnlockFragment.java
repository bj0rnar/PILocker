package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.SSHConnection.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.SSHConnection.SSHExecuter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.concurrent.Executor;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnlockFragment extends Fragment implements AsyncResponseInterface {

    private AsyncResponseInterface thisInterface = this;

    //private SSHExecuter executer = new SSHExecuter();
    private TextView statusText;
    private TextView doorNameTxt;

    private String prefHost;
    private String prefSide;
    private String prefName;
    private String prefPub;
    private String prefPriv;
    private Boolean prefEmailLoggedIn;

    private SharedPreferences pref;

    private Button unlockBtn;
    private Button lockBtn;

    private String hostName = "ubuntu";

    private Executor exec;
    private BiometricPrompt bioPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private NavController navController;

    public UnlockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_unlock, container, false);

        /*
        pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
        prefHost = pref.getString("key_ip", null);
        prefName = pref.getString("doorName", null);
        prefSide = pref.getString("side", null);
        prefPriv = pref.getString("rsapriv", null);
        prefPub = pref.getString("rsapub", null);
        */

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
        /*
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setDeviceCredentialAllowed(true)
                .build();

        bioPrompt.authenticate(promptInfo);

        */
        //Initialize toolbar and set Menu.
        setHasOptionsMenu(true);

        /*
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(prefName);
        toolbar.setOnMenuItemClickListener(this);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //*/
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Initialize sharedpreferences

        pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
        prefHost = pref.getString("key_ip", null);
        prefName = pref.getString("doorName", null);
        prefSide = pref.getString("side", null);
        prefPriv = pref.getString("rsapriv", null);
        prefPub = pref.getString("rsapub", null);
        prefEmailLoggedIn = pref.getBoolean("isLoggingEnabled", false);

        navController = Navigation.findNavController(view);

        //Initialize toolbar only for this fragment.
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(prefName);
        toolbar.inflateMenu(R.menu.unlockmenu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_unlock_email_settings:
                        Log.i("MENUTEST", "Email knappen");
                        //Can navigate using this.
                        if (prefEmailLoggedIn) {
                            navController.navigate(R.id.action_unlockFragment2_to_emailSettingsFragment);
                        }
                        else {
                            navController.navigate(R.id.action_unlockFragment2_to_installLoggingSetup2);
                        }
                        return true;
                    case R.id.menu_unlock_second:
                        Log.i("MENUTEST", "Andre knappen");
                        navController.navigate(R.id.action_unlockFragment2_to_RPISettingsFragment);
                        return true;
                }

                return false;
            }
        });





        Log.i("FINALSTAGE", "SHAREDPREFERENCES HAS IP: " + prefHost);
        Log.i("FINALSTAGE", "SHAREDPREFERENCES HAS NAME: " + prefName);
        Log.i("FINALSTAGE", "SHAREDPREFERENCES HAS SIDE: " + prefSide);

        doorNameTxt = view.findViewById(R.id.unlock_status_door_name);
        doorNameTxt.setText(prefName);

        statusText = view.findViewById(R.id.unlock_status_status_textView);

        statusText.setText("Waiting for command");
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

                String command = "";

                if (prefSide == "right"){
                    command = "./turnCounterClockwise.sh;";
                }
                else {
                    command = "./turnClockwise.sh;";
                }

                //USE prefHost FOR ACCEPTANCETEST
                //String manualHost = "158.39.162.128";

                SSHExecuter executer = new SSHExecuter();
                executer.response = thisInterface;
                try {
                    executer.execute(hostName, prefHost, command, prefPriv, prefPub);
                }
                catch (Exception e){
                    unlockBtn.getBackground().setColorFilter(null);
                    unlockBtn.setEnabled(true);
                    lockBtn.getBackground().setColorFilter(null);
                    lockBtn.setEnabled(true);
                }
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


                String command = "";

                if (prefSide == "left"){
                    command = "./turnClockwise.sh;";
                }
                else {
                    command = "./turnCounterClockwise.sh;";
                }

                //USE prefHost FOR ACCEPTANCETEST
                //String manualHost = "158.39.162.128";

                SSHExecuter executer = new SSHExecuter();
                executer.response = thisInterface;
                try {
                    executer.execute(hostName, prefHost, command, prefPriv, prefPub);
                }
                catch (Exception e){
                    unlockBtn.getBackground().setColorFilter(null);
                    unlockBtn.setEnabled(true);
                    lockBtn.getBackground().setColorFilter(null);
                    lockBtn.setEnabled(true);
                }

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

        if (result != null) {
            if (!result.equals("0")) {
                Log.i("FINALSTAGE", "echo $? gives no output");
            }
        }
        else {
            Toast.makeText(getContext().getApplicationContext(), "Error, no connection to RPI", Toast.LENGTH_SHORT).show();
            statusText.setText("Unknown");
        }

    }
    
    @Override
    public void onStart() {
        super.onStart();
        Log.d("BIOMETRIC", "onStart called");

        /*promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setDeviceCredentialAllowed(true)
                .build();

        bioPrompt.authenticate(promptInfo);
        */
    }



}
