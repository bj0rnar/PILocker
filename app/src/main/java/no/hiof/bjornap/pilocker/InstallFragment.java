package no.hiof.bjornap.pilocker;


import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import static android.content.Context.WIFI_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstallFragment extends Fragment{




    private NavController navController;


    private Button installBtn;


    private String doorName;
    private String side;

    private String wlanIp;

    private Boolean firstTime;
    private String password;

    public InstallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_install, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){
            doorName = getArguments().getString("doorName");
            side = getArguments().getString("side");
            firstTime = getArguments().getBoolean("firstTime");
            password = getArguments().getString("password");

        }

        //Initialize
        navController = Navigation.findNavController(view);
        installBtn = view.findViewById(R.id.installation_ap_installBtn);


        installBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wlanIp = getIPAddressTwo();

                if (wlanIp.equals("10.0.60.1")) {

                    Bundle b = new Bundle();
                    b.putString("side", side);
                    b.putString("doorName", doorName);
                    b.putString("password", password);
                    b.putBoolean("firstTime", firstTime);
                    b.putString("wlanIp", wlanIp);
                    navController.navigate(R.id.action_installFragment_to_IPExtractionFragment, b);
                }
                else {
                    Toast.makeText(getContext().getApplicationContext(), "Are you connected to piDOOR?", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //Use this is progressfragment, check if getIp matches ethernetIp.
    private String getIPAddressTwo() {
        final WifiManager manager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        final String address = Formatter.formatIpAddress(dhcp.gateway);

        return address;
    }

}