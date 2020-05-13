package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.SSHConnection.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.SSHConnection.SSHReader;
import no.hiof.bjornap.pilocker.Utility.EncryptedSharedPref;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class IPExtractionFragment extends Fragment implements AsyncResponseInterface {

    private String doorName = "";
    private String side = "";
    private String wlanIp = "";


    private String actualUser = "ubuntu";
    private String actualPass = "gruppe6";

    private Boolean firstTime;
    private String password;

    private NavController navController;


    public AsyncResponseInterface thisInterface = this;

    public IPExtractionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ipextraction, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        navController = Navigation.findNavController(view);

        if (getArguments() != null) {
            doorName = getArguments().getString("doorName");
            side = getArguments().getString("side");
            wlanIp = getArguments().getString("wlanIp");
            password = getArguments().getString("password");
            firstTime = getArguments().getBoolean("firstTime");

            //if strings[4] != null;
            //then set password

            if (firstTime) {
                //Run reader method
                SSHReader sshReader = new SSHReader();
                sshReader.response = thisInterface;
                sshReader.execute(actualUser, actualPass, wlanIp, "./getIp.sh", "./changePassword.sh " + password);
            }
            else {
                SSHReader sshReader2 = new SSHReader();
                sshReader2.response = thisInterface;
                sshReader2.execute(actualUser, password, wlanIp, "./getIp.sh", null);
            }
        }



    }

    @Override
    public void onComplete(String result) {
        result = result.substring(0, result.length()-1);

        Toast.makeText(getContext().getApplicationContext(), "Extracted IP: " + result, Toast.LENGTH_LONG).show();

        EncryptedSharedPref.writeString(EncryptedSharedPref.SIDE, side);
        EncryptedSharedPref.writeString(EncryptedSharedPref.DOORNAME, doorName);
        EncryptedSharedPref.writeString(EncryptedSharedPref.KEY_IP, result);
        EncryptedSharedPref.writeString(EncryptedSharedPref.PASSWORD, password);


        navController.navigate(R.id.action_IPExtractionFragment_to_progressFragment);
    }
}
