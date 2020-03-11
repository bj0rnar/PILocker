package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.Utility.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.Utility.SSHReader;


import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import static android.content.Context.WIFI_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstallFragment extends Fragment implements AsyncResponseInterface {



    private SSHReader reader = new SSHReader();

    private NavController navController;

    private String defaultuser = "bjornar";
    private String defaultpass = "toor";

    private String actualUser = "ubuntu";
    private String actualPass = "gruppe6";


    private Button installBtn;
    private Button nextBtn;

    private TextView statusTxt;

    private String doorName;
    private String side;

    public InstallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        reader.response = this;

        return inflater.inflate(R.layout.fragment_install, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){
            doorName = getArguments().getString("doorName");
            side = getArguments().getString("side");
            Log.i("BUNDLE FINAL INSTALL", doorName);
            Log.i("BUNDLE FINAL INSTALL", side);
        }

        /**
         * TODO: Build a DOOR object with recieved IP from logic and arguments from getArguments.
         * TODO: Move "reader.execute" logic from this fragment to progress fragment, handle everything there!
         * TODO: SLEEP
         */

        navController = Navigation.findNavController(view);

        installBtn = view.findViewById(R.id.install_button);
        nextBtn = view.findViewById(R.id.installation_ap_nextBtn);

        statusTxt = view.findViewById(R.id.installation_ap_status_textView);

        installBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String wlanIp = getIPAddressTwo();

                if (wlanIp.equals("10.0.60.1")) {

                    //String testIp = "192.168.10.153";
                    //String cmd = "./readtest.sh";
                    //String cmd = "ip a | grep 'eth0' | grep 'inet' | awk '{ print $2}' | grep -E -o \"([0-9]{1,3}[.]){3}[0-9]{1,3}";
                    String cmd = "./getIp.sh";

                    String forceIp = "10.0.60.1";
                    //for prototyping:
                    //reader.response = getContext().getApplicationContext();
                    reader.execute(actualUser, actualPass, wlanIp, cmd);

                    //reader.execute(defaultuser, defaultpass, testIp, cmd);
                }
                else {
                    Toast.makeText(getContext().getApplicationContext(), "Are you connected to piDOOR?", Toast.LENGTH_SHORT).show();
                    statusTxt.setText("ERROR");
                }

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_installFragment_to_progressFragment);
            }
        });
    }

    //Use this is progressfragment, check if getIp matches ethernetIp.
    public String getIPAddressTwo() {
        final WifiManager manager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        final String address = Formatter.formatIpAddress(dhcp.gateway);

        return address;
    }

    @Override
    public void onComplete(String result) {
        result = result.substring(0, result.length()-1);
        Log.i("SSHREADER", "Fra AsyncEthernetREsponse: " + result);
        SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("key_ip", result);
        edit.apply();
        Bundle bundle = new Bundle();
        bundle.putString("ip", result);
        navController.navigate(R.id.action_installFragment_to_progressFragment, bundle);
    }
}