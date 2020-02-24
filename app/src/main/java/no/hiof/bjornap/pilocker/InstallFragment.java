package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import no.hiof.bjornap.pilocker.Model.Door;
import no.hiof.bjornap.pilocker.Utility.SSHReader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstallFragment extends Fragment {

    private String responseMessage;

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

        //Txt
        final EditText ipText = view.findViewById(R.id.install_edittext_ip);

        final EditText resp = (EditText)view.findViewById(R.id.install_response_message);

        Button installBtn = view.findViewById(R.id.install_button);
        installBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                SSHReader.OutputMessage msg = new SSHReader.OutputMessage() {
                    @Override
                    public void returnMessage(String message) {
                        resp.setText(message);
                    }
                };

                new SSHReader(msg).execute();



                // Saving this for later
                /*String value = ipText.getText().toString();

                if (validateIP(value)){
                    Door newDoor = new Door(value);
                }*/
            }
        });




    }

    private boolean validateIP(String string){
        //TODO: Validation method
        return true;
    }
}
