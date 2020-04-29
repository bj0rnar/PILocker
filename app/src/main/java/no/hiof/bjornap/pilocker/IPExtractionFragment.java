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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class IPExtractionFragment extends Fragment implements AsyncResponseInterface {

    private String doorName = "";
    private String side = "";
    private String wlanIp = "";


    private String actualUser = "ubuntu";
    private String actualPass = "gruppe6";

    private NavController navController;

    private Button nextBtn;

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

        nextBtn = view.findViewById(R.id.ipextraction_next_button);
        nextBtn.setVisibility(View.INVISIBLE);
        nextBtn.setEnabled(false);

        navController = Navigation.findNavController(view);

        if (getArguments() != null) {
            doorName = getArguments().getString("doorName");
            side = getArguments().getString("side");
            wlanIp = getArguments().getString("wlanIp");



            //Run reader method
            SSHReader sshReader = new SSHReader();
            sshReader.response = thisInterface;
            sshReader.execute(actualUser, actualPass, wlanIp, "./getIp.sh");
        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_IPExtractionFragment_to_progressFragment);
            }
        });

    }

    @Override
    public void onComplete(String result) {
        result = result.substring(0, result.length()-1);

        SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("side", side);
        edit.putString("doorName", doorName);
        edit.putString("key_ip", result);
        edit.apply();

        nextBtn.setVisibility(View.VISIBLE);
        nextBtn.setEnabled(true);

        //navController.navigate(R.id.action_IPExtractionFragment_to_progressFragment);
    }
}
