package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import no.hiof.bjornap.pilocker.Utility.EncryptedSharedPref;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnlockFragment extends Fragment implements AsyncResponseInterface {

    private AsyncResponseInterface thisInterface = this;


    private TextView doorNameTxt;
    private TextView timeText;
    private TextView dateText;

    private ConstraintLayout lockImage;
    private ConstraintLayout unlockImage;
    private ConstraintLayout unknownImage;

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

    private StatusViewModel model;

    private boolean locking;

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

        prefHost = EncryptedSharedPref.readString(EncryptedSharedPref.KEY_IP, null);
        prefName = EncryptedSharedPref.readString(EncryptedSharedPref.DOORNAME, null);
        prefSide = EncryptedSharedPref.readString(EncryptedSharedPref.SIDE, null);
        prefPriv = EncryptedSharedPref.readString(EncryptedSharedPref.RSAPRIV, null);
        prefPub = EncryptedSharedPref.readString(EncryptedSharedPref.RSAPUB, null);
        prefEmailLoggedIn = EncryptedSharedPref.readBool(EncryptedSharedPref.LOGGINGENABLED, false);

        navController = Navigation.findNavController(view);


        model = new ViewModelProvider(this).get(StatusViewModel.class);




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
        Log.i("FINALSTAGE", "SHAREDPREFERENCES HAS SIDE: " + prefPriv);
        Log.i("FINALSTAGE", "SHAREDPREFERENCES HAS SIDE: " + prefSide);

        //doorNameTxt = view.findViewById(R.id.unlock_status_door_name);
        //doorNameTxt.setText(prefName);

        dateText = view.findViewById(R.id.unlock_status_date_textView);
        timeText = view.findViewById(R.id.unlock_status_time_textView);

        lockImage = view.findViewById(R.id.unlock_status_status_locked);
        unlockImage = view.findViewById(R.id.unlock_status_status_unlocked);
        unknownImage = view.findViewById(R.id.unlock_status_status_unknown);

        lockBtn = view.findViewById(R.id.lockBtn);
        unlockBtn = view.findViewById(R.id.unlockBtn);

        //Set all observers
        SetObservers();

        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locking = true;
                model.getStatus().setValue("LOCKING..");

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
                locking = false;
                model.getStatus().setValue("UNLOCKING..");

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

    private void SetObservers() {
        final Observer<String> statusObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                if(newName.equals("UNLOCKED")){
                    lockImage.setVisibility(View.INVISIBLE);
                    unlockImage.setVisibility(View.VISIBLE);
                    unknownImage.setVisibility(View.INVISIBLE);
                }else if(newName.equals("LOCKED")){
                    lockImage.setVisibility(View.VISIBLE);
                    unlockImage.setVisibility(View.INVISIBLE);
                    unknownImage.setVisibility(View.INVISIBLE);
                }else{
                    lockImage.setVisibility(View.INVISIBLE);
                    unlockImage.setVisibility(View.INVISIBLE);
                    unknownImage.setVisibility(View.VISIBLE);
                }
            }
        };

        final Observer<String> timeObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                timeText.setText(newName);
            }
        };

        final Observer<String> dateObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                dateText.setText(newName);
            }
        };

        model.getStatus().observe(getViewLifecycleOwner(), statusObserver);
        model.getDate().observe(getViewLifecycleOwner(), dateObserver);
        model.getTime().observe(getViewLifecycleOwner(), timeObserver);
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
            if(locking){
                model.getStatus().setValue("LOCKED");
                model.getDate().setValue(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
                model.getTime().setValue(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
            }
            else {
                model.getStatus().setValue("UNLOCKED");
                model.getDate().setValue(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
                model.getTime().setValue(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
            }
        }
        else {
            Toast.makeText(getContext().getApplicationContext(), "Error, no connection to RPI", Toast.LENGTH_SHORT).show();
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
