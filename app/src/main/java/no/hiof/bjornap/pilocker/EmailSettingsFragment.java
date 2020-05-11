package no.hiof.bjornap.pilocker;


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

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class EmailSettingsFragment extends Fragment implements AsyncResponseInterface {

    private AsyncResponseInterface thisInterface = this;

    private NavController navController;
    private Button sendBtn;
    private Button deleteEmailBtn;


    private SharedPreferences pref;

    private String prefHost;
    private String prefPub;
    private String prefPriv;
    private String prefMail;
    private Boolean prefLog;
    private TextView emailTextView;

    public EmailSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendBtn = view.findViewById(R.id.fragment_rpisettings_shutdownBtn);
        deleteEmailBtn = view.findViewById(R.id.fragment_rpisettings_factoryResetBtn);
        navController = Navigation.findNavController(view);
        emailTextView = view.findViewById(R.id.fragment_rpisettings_uptime_actual_textView);

        pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
        prefHost = pref.getString("key_ip", null);
        prefPriv = pref.getString("rsapriv", null);
        prefPub = pref.getString("rsapub", null);
        prefMail = pref.getString("email", null);


        if (prefMail != null) {
            emailTextView.setText(prefMail);
        }
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendBtn.setEnabled(false);
                sendBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

                SSHExecuter executer = new SSHExecuter();
                executer.response = thisInterface;
                executer.execute("ubuntu", prefHost, "./sendMail.sh", prefPriv, prefPub);
            }
        });

        deleteEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteEmailBtn.setEnabled(false);
                deleteEmailBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

                SSHExecuter executer = new SSHExecuter();
                executer.response = thisInterface;
                executer.execute("ubuntu", prefHost, "./deleteMail.sh", prefPriv, prefPub);
                pref.edit().remove("email").apply();
                pref.edit().putBoolean("isLoggingEnabled", false).apply();

                Toast.makeText(getContext().getApplicationContext(), "Email successfully deleted", Toast.LENGTH_LONG).show();
                navController.navigate(R.id.action_emailSettingsFragment_to_unlockFragment2);
            }
        });




        //Instead of using onBackPressed, implement it locally on (back button) key release.
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    navController.navigate(R.id.action_emailSettingsFragment_to_unlockFragment2);
                    return true;
                }
                return false;
            }
        });

    }


    @Override
    public void onComplete(String result) {
        sendBtn.setEnabled(true);
        sendBtn.getBackground().setColorFilter(null);

        deleteEmailBtn.setEnabled(true);
        deleteEmailBtn.getBackground().setColorFilter(null);

    }
}
