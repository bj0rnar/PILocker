package no.hiof.bjornap.pilocker;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import no.hiof.bjornap.pilocker.Utility.EncryptedSharedPref;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnlockFragment extends Fragment implements AsyncResponseInterface {

    /**
     * Main fragment for user to control the doorlock
     * Uses SSHExecutur with AsyncResponseInterface to connect to RPI
     * Creates and display it's own toolbar
     *
     */

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

    private String prefStatus;
    private String prefDate;
    private String prefTime;

    private SharedPreferences pref;

    private ConstraintLayout unlockBtn;
    private ConstraintLayout lockBtn;

    private ImageView unlockButtonImage, lockButtonImage;

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

        exec = ContextCompat.getMainExecutor(getActivity().getApplicationContext());
        bioPrompt = new BiometricPrompt(UnlockFragment.this, exec, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                getActivity().finish();
                Toast.makeText(getActivity().getApplicationContext(),
                        "ERROR: " + errString, Toast.LENGTH_SHORT)
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

        setHasOptionsMenu(true);


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

        prefStatus = EncryptedSharedPref.readString(EncryptedSharedPref.LASTCOMMAND_STATUS, null);
        prefDate = EncryptedSharedPref.readString(EncryptedSharedPref.LASTCOMMAND_DATE, null);
        prefTime = EncryptedSharedPref.readString(EncryptedSharedPref.LASTCOMMAND_TIME, null);

        navController = Navigation.findNavController(view);


        model = new ViewModelProvider(this).get(StatusViewModel.class);


        //Initialize toolbar only for this fragment.
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(prefName);
        toolbar.inflateMenu(R.menu.unlockmenu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Send user to settings fragment
                if (item.getItemId() == R.id.menu_unlock_settings_selectable) {
                    navController.navigate(R.id.action_unlockFragment2_to_settingsFragment3);
                    return true;
                }
                return false;
            }
        });

        //Initialize
        dateText = view.findViewById(R.id.unlock_status_date_textView);
        timeText = view.findViewById(R.id.unlock_status_time_textView);

        lockImage = view.findViewById(R.id.unlock_status_status_locked);
        unlockImage = view.findViewById(R.id.unlock_status_status_unlocked);
        unknownImage = view.findViewById(R.id.unlock_status_status_unknown);

        lockBtn = view.findViewById(R.id.lockBtn);
        unlockBtn = view.findViewById(R.id.unlockBtn);

        lockButtonImage = view.findViewById(R.id.lockButtonImage);
        unlockButtonImage = view.findViewById(R.id.unlockButtonImage);

        //LAST COMMAND SENT LOGIC
        if (prefDate != null && prefTime != null) {
            dateText.setText(prefDate);
            timeText.setText(prefTime);
        }
        if (prefStatus != null) {
            if (prefStatus.equals("UNLOCKED")) {
                lockImage.setVisibility(View.INVISIBLE);
                unlockImage.setVisibility(View.VISIBLE);
                unknownImage.setVisibility(View.INVISIBLE);
            } else if (prefStatus.equals("LOCKED")) {
                lockImage.setVisibility(View.VISIBLE);
                unlockImage.setVisibility(View.INVISIBLE);
                unknownImage.setVisibility(View.INVISIBLE);
            }
        }

        //Set all observers
        SetObservers();

        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locking = true;
                model.getStatus().setValue("LOCKING..");

                lockButtonImage.setColorFilter(Color.argb(150,200,200,200));
                //lockBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                lockBtn.setEnabled(false);
                unlockButtonImage.setColorFilter(Color.argb(150,200,200,200));
                //unlockBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                unlockBtn.setEnabled(false);

                String command = "";

                if (prefSide == "right"){
                    command = "./turnCounterClockwise.sh;";
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
                    unlockButtonImage.setColorFilter(null);
                    unlockBtn.setEnabled(true);
                    lockButtonImage.setColorFilter(null);
                    lockBtn.setEnabled(true);
                }
            }
        });



        unlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locking = false;
                model.getStatus().setValue("UNLOCKING..");

                lockButtonImage.setColorFilter(Color.argb(150,200,200,200));
                lockBtn.setEnabled(false);
                unlockButtonImage.setColorFilter(Color.argb(150,200,200,200));
                unlockBtn.setEnabled(false);


                String command = "";

                if (prefSide == "right"){
                    command = "./turnClockwise.sh;";
                }
                else {
                    command = "./turnClockwise.sh;";
                }


                SSHExecuter executer = new SSHExecuter();
                executer.response = thisInterface;
                try {
                    executer.execute(hostName, prefHost, command, prefPriv, prefPub);
                }
                catch (Exception e){
                    unlockButtonImage.setColorFilter(null);
                    unlockBtn.setEnabled(true);
                    lockButtonImage.setColorFilter(null);
                    lockBtn.setEnabled(true);
                }

            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(this, callback);

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
                timeText.setText(newName);
            }
        };

        final Observer<String> dateObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
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
        unlockBtn.setEnabled(true);
        lockBtn.setEnabled(true);
        //If result means that the async method ran successfully
        if (result != null) {
            //If locking or unlocking, change the ViewModel, ungrey the images and store the last status in EncryptedSharedPreferences.
            if(locking){
                model.getStatus().setValue("LOCKED");
                model.getDate().setValue(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
                model.getTime().setValue(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
                lockButtonImage.setColorFilter(null);
                unlockButtonImage.setColorFilter(null);
                EncryptedSharedPref.writeString(EncryptedSharedPref.LASTCOMMAND_STATUS, "LOCKED");
                EncryptedSharedPref.writeString(EncryptedSharedPref.LASTCOMMAND_TIME, new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
                EncryptedSharedPref.writeString(EncryptedSharedPref.LASTCOMMAND_DATE, new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
            }
            else {
                model.getStatus().setValue("UNLOCKED");
                model.getDate().setValue(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
                model.getTime().setValue(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
                lockButtonImage.setColorFilter(null);
                unlockButtonImage.setColorFilter(null);
                EncryptedSharedPref.writeString(EncryptedSharedPref.LASTCOMMAND_STATUS, "UNLOCKED");
                EncryptedSharedPref.writeString(EncryptedSharedPref.LASTCOMMAND_TIME, new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
                EncryptedSharedPref.writeString(EncryptedSharedPref.LASTCOMMAND_DATE, new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
            }
        }
        //No result means that the connection failed.
        else {
            Toast.makeText(getContext().getApplicationContext(), "Error, no connection to RPI", Toast.LENGTH_SHORT).show();
            lockButtonImage.setColorFilter(null);
            unlockButtonImage.setColorFilter(null);
        }

    }
    
    @Override
    public void onStart() {
        super.onStart();
        Log.d("BIOMETRIC", "onStart called");

        String loginMethod = EncryptedSharedPref.readString(EncryptedSharedPref.APPLOGINMETHOD, "nothing");
        //Check onStart which login method is used, and act accordingly
        switch (loginMethod){
            case "nothing":
                Toast.makeText(getContext().getApplicationContext(), "You should consider getting an authentication method", Toast.LENGTH_LONG).show();
                break;
            case "pin":
                //Alert dialog
                buildAlertDialog();
                break;
            case "biometric":
                //SetDeviceCredientialAllowed(false) means that only biometric will work, no alternative once biometric is activated.
                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Biometric login for PILocker")
                        .setSubtitle("Log in using your biometric credential")
                        .setDeviceCredentialAllowed(false)
                        .setNegativeButtonText("Exit application")
                        .build();
                bioPrompt.authenticate(promptInfo);
                break;
        }

    }



    //Alert Dialog for entering pin code
    private void buildAlertDialog() {

        final int pinCode = EncryptedSharedPref.readInt(EncryptedSharedPref.PINCODE, 0);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity().getApplicationContext(), R.layout.pin_code_dialog, null);
        alertDialog.setView(view);

        alertDialog.setCancelable(false);
        final EditText input = view.findViewById(R.id.pin_code_dialog_code);
        Button button = view.findViewById(R.id.pin_code_btn);


        final AlertDialog alertDialog1 = alertDialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(input.getText().length() >= 4) {
                        int inputCode = Integer.parseInt(input.getText().toString());

                        if (inputCode == pinCode) {
                            Toast.makeText(getContext().getApplicationContext(), "Correct pin", Toast.LENGTH_SHORT).show();
                            alertDialog1.dismiss();
                        } else {
                            Toast.makeText(getContext().getApplicationContext(), "Wrong pin", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext().getApplicationContext(), "Wrong pin", Toast.LENGTH_SHORT).show();
                    }
                }catch (NumberFormatException e){
                    Toast.makeText(getContext().getApplicationContext(), "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}
