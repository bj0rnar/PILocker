package no.hiof.bjornap.pilocker;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.SSHConnection.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.SSHConnection.SSHExecuter;
import no.hiof.bjornap.pilocker.Utility.EncryptedSharedPref;
import no.hiof.bjornap.pilocker.Utility.InputValidator;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements AsyncResponseInterface{


    public SettingsFragment() {
        // Required empty public constructor
    }

    private Toolbar _toolbar;
    private NavController navController;

    private TextView currentSignedInEmailText;
    private TextView uptimeTextView;
    private LinearLayout sendLogToMailBtn;
    private LinearLayout deleteMailBtn;
    private LinearLayout changeLoginMethodBtn;
    private LinearLayout shutDownBtn;
    private LinearLayout factoryResetBtn;
    private LinearLayout setupEmailBtn;
    private LinearLayout changeHandleBtn;
    private Switch changeHandleSwitch;

    private String prefMail;
    private String prefPub;
    private String prefPriv;
    private String prefHost;
    private String prefPass;
    private String prefHandleSide;

    private SettingsViewModel model;

    private AsyncResponseInterface thisInterface = this;

    private boolean settingUptime = true;

    //Booleans for methods that leave this fragment
    private boolean sendEmail = false;
    private boolean factoryReset = false;
    private boolean shutdown = false;
    private boolean deleteEmail = false;


    //PIN fields in prompt
    private EditText pin;
    private EditText repeat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Set NavController
        navController = Navigation.findNavController(view);

        //Load and initialize
        //Textview for updating
        currentSignedInEmailText = view.findViewById(R.id.settings_email_textview);
        uptimeTextView = view.findViewById(R.id.settings_uptime_textview);


        //Buttons for settings
        sendLogToMailBtn = view.findViewById(R.id.settings_send_email_btn);
        deleteMailBtn = view.findViewById(R.id.settings_delete_email_btn);
        changeLoginMethodBtn = view.findViewById(R.id.settings_login_method_btn);
        shutDownBtn = view.findViewById(R.id.settings_shutdown_rpi_btn);
        factoryResetBtn = view.findViewById(R.id.settings_factory_reset_btn);
        setupEmailBtn = view.findViewById(R.id.settings_setup_email_btn);
        changeHandleSwitch = view.findViewById(R.id.settings_change_handle_switch);
        changeHandleBtn = view.findViewById(R.id.settings_change_handle_side_btn);

        //Load from storage.
        prefHost = EncryptedSharedPref.readString(EncryptedSharedPref.KEY_IP, null);
        prefPriv = EncryptedSharedPref.readString(EncryptedSharedPref.RSAPRIV, null);
        prefPub = EncryptedSharedPref.readString(EncryptedSharedPref.RSAPUB, null);
        prefPass = EncryptedSharedPref.readString(EncryptedSharedPref.PASSWORD, null);
        prefMail = EncryptedSharedPref.readString(EncryptedSharedPref.EMAIL, null);
        prefHandleSide = EncryptedSharedPref.readString(EncryptedSharedPref.SIDE, null);


        //Set temp text
        uptimeTextView.setText(R.string.settings_calculating_uptime);

        //Initialize ViewModel
        model = new ViewModelProvider(this).get(SettingsViewModel.class);

        //Set observer
        final Observer<String> uptimeObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                uptimeTextView.setText(newName);
            }
        };

        //Set observer
        final Observer<String> emailObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                currentSignedInEmailText.setText(newName);
            }
        };

        //Bind observer
        model.getUptime().observe(getViewLifecycleOwner(), uptimeObserver);
        model.getEmail().observe(getViewLifecycleOwner(), emailObserver);


        //#1 Get uptime
        SSHExecuter executer = new SSHExecuter();
        executer.response = thisInterface;
        executer.execute("ubuntu", prefHost, "uptime -p", prefPriv, prefPub);

        //Toolbar
        _toolbar = view.findViewById(R.id.settings_toolbar);
        _toolbar.setTitle("Settings");

        _toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_settingsFragment3_to_unlockFragment2);
            }
        });


        //Set email if found
        if (prefMail != null) {
            currentSignedInEmailText.setText(prefMail);
            setupEmailBtn.setVisibility(View.GONE);

        }
        else {
            model.getEmail().setValue("No user signed in");
            //currentSignedInEmailText.setText(R.string.settings_no_email_found);
            greyOutButton(sendLogToMailBtn);
            greyOutButton(deleteMailBtn);
        }

        setupEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_settingsFragment3_to_installLoggingSetup);
            }
        });
        //Default handle side
        if(prefHandleSide == null){
            EncryptedSharedPref.writeString(EncryptedSharedPref.SIDE, "right");
        }
        if(prefHandleSide.equals("right")){
            changeHandleSwitch.setChecked(true);
        }else{
            changeHandleSwitch.setChecked(false);
        }


        //Listener for send log to email
        sendLogToMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //#2 Send mail
                sendEmail = true;
                greyOutButton(sendLogToMailBtn);
                SSHExecuter executer = new SSHExecuter();
                executer.response = thisInterface;
                executer.execute("ubuntu", prefHost, "./sendMail.sh", prefPriv, prefPub);
            }
        });

        deleteMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //#3 Delete mail
                deleteEmail = true;
                model.getEmail().setValue("Deleting..");
                greyOutButton(deleteMailBtn);
                SSHExecuter executer = new SSHExecuter();
                executer.response = thisInterface;
                executer.execute("ubuntu", prefHost, "./deleteMail.sh", prefPriv, prefPub);
                EncryptedSharedPref.delete(EncryptedSharedPref.EMAIL);
                EncryptedSharedPref.writeBool(EncryptedSharedPref.LOGGINGENABLED, false);

            }
        });

        //Listener for handleside
        changeHandleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getContext(), "Changed to right", Toast.LENGTH_SHORT).show();
                    EncryptedSharedPref.writeString(EncryptedSharedPref.SIDE, "right");
                }else{
                    Toast.makeText(getContext(), "Changed to left", Toast.LENGTH_SHORT).show();
                    EncryptedSharedPref.writeString(EncryptedSharedPref.SIDE, "left");
                }
            }
        });

        changeHandleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHandleSwitch.performClick();
            }
        });

        changeLoginMethodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create prompt, change encryptedSharedPref
                buildLoginSelectionPrompt();
            }
        });

        shutDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //#4 Shutdown
                shutdown = true;
                model.getUptime().setValue("Turning off RPI..");
                greyOutButton(shutDownBtn);
                SSHExecuter executer = new SSHExecuter();
                executer.response = thisInterface;
                executer.execute("ubuntu", prefHost, "./shutdown.sh", prefPriv, prefPub);

            }
        });

        factoryResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                factoryReset = true;
                //Deletes everything in storage
                for(int i = 0; i < EncryptedSharedPref.ALLKEYWORDS.length; i++){
                    EncryptedSharedPref.delete(EncryptedSharedPref.ALLKEYWORDS[i]);
                }
                SSHExecuter executer = new SSHExecuter();
                executer.response = thisInterface;
                executer.execute("ubuntu", prefHost, "./wipeAllData.sh", prefPriv, prefPub);

            }
        });


    }

    private void greyOutButton(LinearLayout button){
        button.setClickable(false);
        button.setEnabled(false);
        button.setFocusable(false);
        int childCount = button.getChildCount();
        for(int i = 0; i < childCount; i++){
            TextView child = (TextView) button.getChildAt(i);
            child.setTextColor(getResources().getColor(R.color.greyedOutButtonTextColor));
        }
        //button.setBackgroundColor(getResources().getColor(R.color.greyedOutButtonBackgroundColor));
    }

    private void reverseGreyOutButton(LinearLayout button){
        button.setClickable(true);
        button.setEnabled(true);
        button.setFocusable(true);
        int childCount = button.getChildCount();
        for(int i = 0; i < childCount; i++){
            TextView child = (TextView) button.getChildAt(i);
            child.setTextColor(getResources().getColor(R.color.primaryTextColor));
        }
        //button.setBackgroundColor(getResources().getColor(R.color.greyedOutButtonBackgroundColor));
    }

    private void buildLoginSelectionPrompt(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity().getApplicationContext(), R.layout.fragment_settings_dialog, null);
        alertDialog.setView(view);

        alertDialog.setCancelable(false);
        Button cancelButton = view.findViewById(R.id.fragment_settings_dialog_cancel);
        Button saveButton = view.findViewById(R.id.fragment_settings_dialog_save);

        pin = view.findViewById(R.id.fragment_settings_dialog_pin);
        repeat = view.findViewById(R.id.fragment_settings_dialog_pin_repeat);

        final RadioGroup radioGroup = view.findViewById(R.id.fragment_settings_dialog_radioGroup);

        switchVisibility(false);

        final AlertDialog alertDialog1 = alertDialog.show();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.cancel();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.fragment_settings_dialog_radio_biometric:
                        switchVisibility(false);
                        break;
                    case R.id.fragment_settings_dialog_radio_pin:
                        switchVisibility(true);
                        break;
                    case R.id.fragment_settings_dialog_radio_nothing:
                        switchVisibility(false);
                        break;

                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(radioGroup.getCheckedRadioButtonId() != -1){
                    int selectedButton = radioGroup.getCheckedRadioButtonId();

                    if (selectedButton == R.id.fragment_settings_dialog_radio_biometric){
                        EncryptedSharedPref.writeString(EncryptedSharedPref.APPLOGINMETHOD, "biometric");
                        alertDialog1.cancel();
                    }
                    if (selectedButton == R.id.fragment_settings_dialog_radio_pin){
                        Pair<Boolean, String> inputCheck = InputValidator.isPinGood(pin.getText().toString(), repeat.getText().toString());
                        if (inputCheck.first) {
                            EncryptedSharedPref.writeString(EncryptedSharedPref.APPLOGINMETHOD, "pin");
                            EncryptedSharedPref.writeInt(EncryptedSharedPref.PINCODE, Integer.parseInt(pin.getText().toString()));
                            alertDialog1.cancel();
                        }
                        else {
                            Toast.makeText(getContext().getApplicationContext(), inputCheck.second, Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (selectedButton == R.id.fragment_settings_dialog_radio_nothing) {
                        EncryptedSharedPref.writeString(EncryptedSharedPref.APPLOGINMETHOD, "nothing");
                        alertDialog1.cancel();
                    }
                }
                else {
                    alertDialog1.cancel();
                }

            }
        });

    }

    private void switchVisibility(boolean visible){
        if (visible){
            pin.setVisibility(View.VISIBLE);
            repeat.setVisibility(View.VISIBLE);
        }
        else {
            //Turn invisble
            pin.setVisibility(View.GONE);
            repeat.setVisibility(View.GONE);

            //Clear text
            pin.getText().clear();
            repeat.getText().clear();

            //Hide keyboard
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(pin.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(repeat.getWindowToken(), 0);
        }
    }


    @Override
    public void onComplete(String result) {
        //Response is always a blank space in case of connection.
        if (result != null) {
            //#1: only set uptime once.
            if (settingUptime) {
                model.getUptime().setValue(result);
                settingUptime = false;
            }
            //#2 Send mail
            if (sendEmail) {
                reverseGreyOutButton(sendLogToMailBtn);
            }

            //#3 Delete mail
            if (deleteEmail) {
                model.getEmail().setValue("No user signed in");
                //currentSignedInEmailText.setText(R.string.settings_no_email_found);
                setupEmailBtn.setVisibility(View.VISIBLE);
                greyOutButton(sendLogToMailBtn);
                greyOutButton(deleteMailBtn);
                Toast.makeText(getContext().getApplicationContext(), "Email successfully deleted", Toast.LENGTH_LONG).show();
                deleteEmail = false;
            }

            //#4 Shutdown
            if (shutdown) {
                navController.navigate(R.id.action_settingsFragment3_to_unlockFragment2);
            }
            //#5 Factory reset
            if (factoryReset) {
                navController.navigate(R.id.action_settingsFragment3_to_connectFragment2);

            }



        }
        //If result != null, SSH connection failed.
        else {
            model.getUptime().setValue("No connection established");
        }
    }

}
