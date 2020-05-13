package no.hiof.bjornap.pilocker;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.SSHConnection.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.SSHConnection.SSHExecuter;
import no.hiof.bjornap.pilocker.Utility.EncryptedSharedPref;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class RPISettingsFragment extends Fragment implements AsyncResponseInterface {

    private AsyncResponseInterface thisInterface = this;

    private SharedPreferences pref;

    private String prefHost;
    private String prefPub;
    private String prefPriv;
    private String prefPass;

    private Button shutdownBtn;
    private Button factoryResetBtn;

    private Boolean settingUpdateTime = true;
    private Boolean factoryReset = false;

    private NavController navController;

    private TextView uptimeTextView;

    private UptimeViewModel model;

    public RPISettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rpisettings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize navigationcomponent
        navController = Navigation.findNavController(view);

        //initialize UI elements
        shutdownBtn = view.findViewById(R.id.fragment_rpisettings_shutdownBtn);
        factoryResetBtn = view.findViewById(R.id.fragment_rpisettings_factoryResetBtn);
        uptimeTextView = view.findViewById(R.id.fragment_rpisettings_uptime_actual_textView);
        uptimeTextView.setText(R.string.rpisettings_uptime_placeholder);

        //Initialize ViewModel
        model = new ViewModelProvider(this).get(UptimeViewModel.class);

        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                uptimeTextView.setText(newName);
            }
        };

        model.getUptime().observe(getViewLifecycleOwner(), nameObserver);
        
        prefHost = EncryptedSharedPref.readString(EncryptedSharedPref.KEY_IP, null);
        prefPriv = EncryptedSharedPref.readString(EncryptedSharedPref.RSAPRIV, null);
        prefPub = EncryptedSharedPref.readString(EncryptedSharedPref.RSAPUB, null);
        prefPass = EncryptedSharedPref.readString(EncryptedSharedPref.PASSWORD, null);


        SSHExecuter executer = new SSHExecuter();
        executer.response = thisInterface;
        executer.execute("ubuntu", prefHost, "uptime -p", prefPriv, prefPub);



        shutdownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.getUptime().setValue("Turning off RPI..");
                SSHExecuter executer = new SSHExecuter();
                executer.response = thisInterface;
                executer.execute("ubuntu", prefHost, "./shutdown.sh", prefPriv, prefPub);

            }
        });

        factoryResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Prompt password.
                buildAlertDialog();
            }
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    navController.navigate(R.id.action_RPISettingsFragment_to_unlockFragment2);
                    return true;
                }
                return false;
            }
        });

    }

    private void buildAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Factory reset");
        alertDialog.setMessage("Enter password to permanently reset RPI and application");

        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        //Transformation not as good as XML inputtype, consider changing this.
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (input.getText().toString().equals(prefPass)){
                    wipeAndReturnToStart();
                }
                else {
                    Toast.makeText(getContext().getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                    dialogInterface.cancel();
                }
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });

        alertDialog.show();

    }

    private void wipeAndReturnToStart() {
        //factoryReset = true;

        //Wipe all RPI data
        SSHExecuter executer = new SSHExecuter();
        executer.response = thisInterface;
        executer.execute("ubuntu", prefHost, "./wipeAllData.sh", prefPriv, prefPub);

        //Wipe SharedPreferences.
        //pref.edit().clear().apply();
        navController.navigate(R.id.action_RPISettingsFragment_to_installWelcomeFragment);

    }

    @Override
    public void onComplete(String result) {
        if(settingUpdateTime){
            //RPI not turned off
            if (result != null){
                model.getUptime().setValue(result);
            }
            //No connection to RPI
            else {
                model.getUptime().setValue("No connection established");
            }
            settingUpdateTime = false;
        }
        else {
            if (!factoryReset) {
                navController.navigate(R.id.action_RPISettingsFragment_to_unlockFragment2);
            }
            else {
                navController.navigate(R.id.action_RPISettingsFragment_to_installWelcomeFragment);
            }
        }

    }
}
