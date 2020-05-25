package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.SSHConnection.AsyncResponseInterface;
import no.hiof.bjornap.pilocker.SSHConnection.SSHInstaller;
import no.hiof.bjornap.pilocker.SSHConnection.SSHReader;
import no.hiof.bjornap.pilocker.Utility.EncryptedSharedPref;
import no.hiof.bjornap.pilocker.Utility.Tuples;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import static no.hiof.bjornap.pilocker.Utility.RSAGenerator.generateRSAPairs;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment implements AsyncResponseInterface {

    /**
     * RSA Installation class. Uses SSHExecutur through AsyncResponseInterface for SSH action
     * Generates RSA keys with RSAGenerator class
     */


    public ProgressFragment() {
        // Required empty public constructor
    }


    public AsyncResponseInterface thisInterface = this;


    private String actualUser = "ubuntu";

    private NavController navController;


    private String pub;
    private String priv;

    private ProgressBar progressBar;
    private Button retryButton;


    private String prefHost;
    private String prefSide;
    private String prefName;
    private String prefPub;
    private String prefPriv;
    private String prefPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        Log.i("SSHREADER", "PROGRESSFRAGMENT STARTED");

        //SPIN THE WHEEL FOR NOW
        progressBar = view.findViewById(R.id.progress_fragment_progressbar);
        retryButton = view.findViewById(R.id.progress_fragment_retrybutton);

        prefHost = EncryptedSharedPref.readString(EncryptedSharedPref.KEY_IP, null);
        prefName = EncryptedSharedPref.readString(EncryptedSharedPref.DOORNAME, null);
        prefSide = EncryptedSharedPref.readString(EncryptedSharedPref.SIDE, null);
        prefPass = EncryptedSharedPref.readString(EncryptedSharedPref.PASSWORD, null);


        Tuples tuples = generateRSAPairs();
        priv = (String)tuples.priv;
        pub = (String)tuples.pub;

        Log.i("SSHREADER", "priv key" + priv);
        Log.i("SSREADER", "pub key " + pub);
        Log.i("PASSWORD", prefPass);


        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                installKeys();
            }
        });


        installKeys();


    }

    //Initialize SSH action and hide + disable retry button
    private void installKeys() {
        progressBar.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.INVISIBLE);
        retryButton.setEnabled(false);

        SSHInstaller sshInstaller = new SSHInstaller();
        sshInstaller.response = thisInterface;
        sshInstaller.execute(pub, actualUser, prefHost, prefPass);
    }

    @Override
    public void onComplete(String result) {

        //If result is null the installation failed, display retry button and let the user try again. This might happen
        //if the user is not connected to the same network as the RPI, which is why this fix exists.
        if (result != null) {

            EncryptedSharedPref.writeString(EncryptedSharedPref.RSAPUB, pub);
            EncryptedSharedPref.writeString(EncryptedSharedPref.RSAPRIV, priv);

            navController.navigate(R.id.action_progressFragment_to_installLoggingSetup);
        }
        else {
            Toast.makeText(getContext().getApplicationContext(), "Could not generate keys. Make sure you're on the same network as RPI", Toast.LENGTH_LONG).show();
            retryButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            retryButton.setEnabled(true);
        }
    }
}
