package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.Utility.SSHConnector;
import no.hiof.bjornap.pilocker.Utility.SSHExecuter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnlockFragment extends Fragment {

    private SSHConnector sshConnector = new SSHConnector();
    private SSHExecuter sshExecuter = new SSHExecuter();
    private TextView statusText;
    private TextView doorNameTxt;

    public UnlockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

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

        SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
        String prefHost = pref.getString("key_ip", null);
        String prefName = pref.getString("doorName", null);
        String prefSide = pref.getString("side", null);

        Log.i("FINALSTAGE", "SHAREDPREFERENCES HAS IP: " + prefHost);
        Log.i("FINALSTAGE", "SHAREDPREFERENCES HAS NAME: " + prefName);
        Log.i("FINALSTAGE", "SHAREDPREFERENCES HAS SIDE: " + prefSide);

        doorNameTxt = view.findViewById(R.id.unlock_status_door_name);
        doorNameTxt.setText(prefName);

        statusText = view.findViewById(R.id.unlock_status_status_textView);

        final NavController navController = Navigation.findNavController(view);

        final Button lockBtn = view.findViewById(R.id.lockBtn);
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusText.setText("LOCKED");

            }
        });

        final Button unlockBtn = view.findViewById(R.id.unlockBtn);
        unlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusText.setText("UNLOCKED");

                //String usr = "bjornar";
                //String host = "192.168.10.153";
                //String cmd = "./lol.sh";

                /*
                SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
                String prefHost = pref.getString("key_ip", null);

                Log.i("SSHREADER", "Fra UnlockFragment: " + prefHost);

                String usr = "ubuntu";
                //String host = getArguments().getString("ip");
                String hardcodedHost = "158.39.162.152";
                String cmd = "./lockTest.sh";

                //Log.i("SSHREADER", "EXECUTOR STATUS: " + sshExecuter.getStatus().toString());
                //Log.i("SSHREADER", "EXECUTOR STATUS: " + sshExecuter.isCancelled());

                SSHExecuter executor = new SSHExecuter();

                executor.execute(usr, prefHost, cmd, "lol");

                 */
            }
        });

    }


}
