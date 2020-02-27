package no.hiof.bjornap.pilocker;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.Model.Door;
import no.hiof.bjornap.pilocker.Utility.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.Utility.SSHReader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment implements AsyncResponseInterface {

    private String ethernetIP;

    public ProgressFragment() {
        // Required empty public constructor
    }

    private SSHReader reader = new SSHReader();

    private String defaultuser = "bjornar";
    private String defaultpass = "toor";

    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        reader.response = this;

        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        if (getArguments() != null) {
            ethernetIP = getArguments().getString("ip");
            Log.i("SSHREADER", "Progressfragment: " + ethernetIP);

            String testIp = "192.168.10.153";
            String cmd = "./fakersa.sh";

            reader.execute(defaultuser, defaultpass,testIp, cmd);
        }

    }

    @Override
    public void onComplete(String result) {
        Log.i("SSHREADER", "Progressfragment FAKERSA: " + result);

        /**
         * At this point, don't send it via bundle. The key and the IP belongs in
         * EncryptedSharedPreference
         */

        Bundle b = new Bundle();
        b.putString("rsa", result);
        b.putString("ip", ethernetIP);
        navController.navigate(R.id.action_progressFragment_to_unlockFragment2, b);
    }
}
