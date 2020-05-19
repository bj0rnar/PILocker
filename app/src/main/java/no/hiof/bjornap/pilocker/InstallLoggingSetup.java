package no.hiof.bjornap.pilocker;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstallLoggingSetup extends Fragment implements AsyncResponseInterface{

    private AsyncResponseInterface thisInterface = this;

    private boolean loggingEnabled = false;
    private EditText mailEditText;
    private EditText passEditText;
    private Button nextBtn;



    private NavController navController;

    private SharedPreferences pref;

    private String prefHost;
    private String prefPub;
    private String prefPriv;
    private String hostName = "ubuntu";

    private String mail;

    public InstallLoggingSetup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_install_logging_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Initialize
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.fragment_settings_dialog_radioGroup);
        mailEditText = view.findViewById(R.id.installation_logging_editText);
        passEditText = view.findViewById(R.id.installation_logging_editText_password);
        nextBtn = view.findViewById(R.id.installation_logging_nextBtn);
        navController = Navigation.findNavController(view);

        //Set invisble per default
        mailEditText.setVisibility(View.INVISIBLE);
        passEditText.setVisibility(View.INVISIBLE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_logging_no:
                        //Set invisible and clear text
                        mailEditText.setVisibility(View.INVISIBLE);
                        passEditText.setVisibility(View.INVISIBLE);
                        mailEditText.getText().clear();
                        passEditText.getText().clear();

                        //If radio button "no" is clicked, removes keyboard from screen and changes focus back.
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mailEditText.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(passEditText.getWindowToken(), 0);

                        loggingEnabled = false;
                        Log.i("RADIOTEST", "Pressed: " + i);
                        Log.i("RADIOTEST", "Radio_logging_no: "  + R.id.radio_logging_no);
                        break;
                    case R.id.radio_logging_yes:
                        //Set visible
                        mailEditText.setVisibility(View.VISIBLE);
                        passEditText.setVisibility(View.VISIBLE);
                        loggingEnabled = true;
                        Log.i("RADIOTEST", "Pressed: " + i);
                        Log.i("RADIOTEST", "Radio_logging_yes: "  + R.id.radio_logging_yes);
                        break;
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loggingEnabled){
                    if (!mailEditText.getText().toString().equals("") && !passEditText.getText().toString().equals("")){
                        mail = mailEditText.getText().toString();
                        String appPassword = passEditText.getText().toString();
                        Pair<Boolean, String> result = InputValidator.isMailGood(mail);

                        //Valid email, assume that password is also correct
                        if (result.first){

                            //Retrieve necessary data from sharedpreferences.

                            prefHost = EncryptedSharedPref.readString(EncryptedSharedPref.KEY_IP, null);
                            prefPriv = EncryptedSharedPref.readString(EncryptedSharedPref.RSAPRIV, null);
                            prefPub = EncryptedSharedPref.readString(EncryptedSharedPref.RSAPUB, null);

                            nextBtn.setEnabled(false);
                            nextBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

                            String emailAndPassword = mail + ":" + appPassword;

                            Log.i("LOGTEST", emailAndPassword);


                            String command = "./setupMail.sh [smtp.gmail.com]:587 " + emailAndPassword;

                            Log.i("LOGTEST", command);

                            SSHExecuter executer = new SSHExecuter();
                            executer.response = thisInterface;
                            executer.execute(hostName, prefHost, command, prefPriv, prefPub);





                        }
                        //Invalid email, show message to user
                        else {
                            Toast.makeText(getContext().getApplicationContext(), result.second, Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else {
                    navController.navigate(R.id.action_installLoggingSetup_to_unlockFragment2);
                }
            }
        });

    }


    @Override
    public void onComplete(String result) {

        nextBtn.setEnabled(true);
        nextBtn.getBackground().setColorFilter(null);

        EncryptedSharedPref.writeString(EncryptedSharedPref.EMAIL, mail);
        EncryptedSharedPref.writeBool(EncryptedSharedPref.LOGGINGENABLED, true);

        Toast.makeText(getContext().getApplicationContext(), "Email setup successful", Toast.LENGTH_SHORT).show();
        navController.navigate(R.id.action_installLoggingSetup_to_unlockFragment2);
        /*

        //Just to be helpful, result sometimes returns a blank space. The assumption is this a newline from the shell exec channel.
        if (result == null || result.equals(" ") || result.equals("")){



        }
        else {
            Log.i("LOGTEST", result);
            Toast.makeText(getContext().getApplicationContext(), "Could not connect to RPI, make sure you're on eduroam and app password is valid", Toast.LENGTH_LONG).show();
        }

         */
    }
}
