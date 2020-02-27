package no.hiof.bjornap.pilocker;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.Model.Door;
import no.hiof.bjornap.pilocker.Utility.AsyncEthernetResponse;
import no.hiof.bjornap.pilocker.Utility.SSHGetIP;
import no.hiof.bjornap.pilocker.Utility.SSHReader;
import no.hiof.bjornap.pilocker.Utility.WiFiConnection;

import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import static android.content.Context.WIFI_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstallFragment extends Fragment implements AsyncEthernetResponse {

    //private String responseMessage;
    private static String TAG = "INSTALLWIFI";
    public static boolean connection = false;
    public String storedIP = "";

    private SSHReader reader = new SSHReader();

    //private PageViewModel pvm = new PageViewModel();


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

        final NavController navController = Navigation.findNavController(view);

        Button installBtn = view.findViewById(R.id.install_button);
        installBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reader.execute();

                /* For Faktisk RPI
                SSHGetIP.EthernetIP ip = new SSHGetIP.EthernetIP() {
                    @Override
                    public void address(String address) {
                        storedIP = address;
                    }
                };

                new SSHGetIP(ip).execute();

                Log.i("JAUDA", storedIP);
                */

                /*
                String x = getIPAddress();
                Log.i("INSTALLGETIP", "Metode 1: " + x);

                String y = getIPAddressTwo();
                Log.i("INSTALLGETIP", "Metode 2: " + y);
                */


            }
        });
    }

    //Check for static IP address when finding correct SSID?
    public String getIPAddressTwo() {
        final WifiManager manager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        final String address = Formatter.formatIpAddress(dhcp.gateway);

        return address;
    }

    private String getIPAddress() {
        final WifiManager manager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        int ipAddress = dhcp.gateway;
        ipAddress = (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) ?
                Integer.reverseBytes(ipAddress) : ipAddress;
        byte[] ipAddressByte = BigInteger.valueOf(ipAddress).toByteArray();
        try {
            InetAddress myAddr = InetAddress.getByAddress(ipAddressByte);
            return myAddr.getHostAddress();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            Log.e("Wifi Class", "Error getting Hotspot IP address ", e);
        }
        return "null";
    }

    @Override
    public void onComplete(String ipAddress) {
        Log.i("WAT", "Fra AsyncEthernetREsponse: " + ipAddress);
    }
}