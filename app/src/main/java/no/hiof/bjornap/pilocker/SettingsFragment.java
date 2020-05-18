package no.hiof.bjornap.pilocker;


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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    private LinearLayout changeHandleSideBtn;
    private LinearLayout changeLoginMethodBtn;
    private LinearLayout shutDownBtn;
    private LinearLayout factoryResetBtn;
    private LinearLayout setupEmailBtn;

    private String prefMail;
    private String prefPub;
    private String prefPriv;
    private String prefHost;
    private String prefPass;

    private UptimeViewModel model;

    private AsyncResponseInterface thisInterface = this;

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
        changeHandleSideBtn = view.findViewById(R.id.settings_change_handle_side_btn);
        changeLoginMethodBtn = view.findViewById(R.id.settings_login_method_btn);
        shutDownBtn = view.findViewById(R.id.settings_shutdown_rpi_btn);
        factoryResetBtn = view.findViewById(R.id.settings_factory_reset_btn);
        setupEmailBtn = view.findViewById(R.id.settings_setup_email_btn);

        //Load from storage.
        prefHost = EncryptedSharedPref.readString(EncryptedSharedPref.KEY_IP, null);
        prefPriv = EncryptedSharedPref.readString(EncryptedSharedPref.RSAPRIV, null);
        prefPub = EncryptedSharedPref.readString(EncryptedSharedPref.RSAPUB, null);
        prefPass = EncryptedSharedPref.readString(EncryptedSharedPref.PASSWORD, null);
        prefMail = EncryptedSharedPref.readString(EncryptedSharedPref.EMAIL, null);

        //Set temp text
        uptimeTextView.setText(R.string.settings_calculating_uptime);

        //Initialize ViewModel
        model = new ViewModelProvider(this).get(UptimeViewModel.class);

        //Set observer
        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                uptimeTextView.setText(newName);
            }
        };

        //Bind observer
        model.getUptime().observe(getViewLifecycleOwner(), nameObserver);


        //Get uptime
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
            currentSignedInEmailText.setText(R.string.settings_no_email_found);
            greyOutButton(sendLogToMailBtn);
            greyOutButton(deleteMailBtn);
        }


        //Listener for send log to email
        sendLogToMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
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

    @Override
    public void onComplete(String result) {
        if (result != null){
            model.getUptime().setValue(result);
        }
        //No connection to RPI
        else {
            model.getUptime().setValue("No connection established");
        }

    }
}
