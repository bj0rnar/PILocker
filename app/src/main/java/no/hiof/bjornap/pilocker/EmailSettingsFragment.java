package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.SSHConnection.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.SSHConnection.SSHExecuter;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class EmailSettingsFragment extends Fragment implements AsyncResponseInterface {

    private AsyncResponseInterface thisInterface = this;

    private NavController navController;
    private Button sendBtn;

    private SharedPreferences pref;

    private String prefHost;
    private String prefPub;
    private String prefPriv;

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

        sendBtn = view.findViewById(R.id.email_fragment_resendEmailBtn);
        navController = Navigation.findNavController(view);

        pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
        prefHost = pref.getString("key_ip", null);
        prefPriv = pref.getString("rsapriv", null);
        prefPub = pref.getString("rsapub", null);

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

        Toast.makeText(getContext().getApplicationContext(), result, Toast.LENGTH_LONG).show();
    }
}
