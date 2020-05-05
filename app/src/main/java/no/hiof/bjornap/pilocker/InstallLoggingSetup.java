package no.hiof.bjornap.pilocker;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.Utility.InputValidator;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstallLoggingSetup extends Fragment {

    private boolean loggingEnabled = false;
    private EditText editText;
    private Button nextBtn;

    private NavController navController;

    public InstallLoggingSetup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_install_logging_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_logging_radioGroup);
        editText = view.findViewById(R.id.installation_logging_editText);
        nextBtn = view.findViewById(R.id.installation_logging_nextBtn);

        navController = Navigation.findNavController(view);

        editText.setVisibility(View.INVISIBLE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_logging_no:
                        editText.setVisibility(View.INVISIBLE);
                        loggingEnabled = false;
                        Log.i("RADIOTEST", "Pressed: " + i);
                        Log.i("RADIOTEST", "Radio_logging_no: "  + R.id.radio_logging_no);
                        break;
                    case R.id.radio_logging_yes:
                        editText.setVisibility(View.VISIBLE);
                        loggingEnabled = true;
                        Log.i("RADIOTEST", "Pressed: " + i);
                        Log.i("RADIOTEST", "Radio_logging_yes: "  + R.id.radio_logging_yes);
                        break;
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loggingEnabled){
                    if (!editText.getText().toString().equals("")){
                        String mail = editText.getText().toString();
                        Pair<Boolean, String> result = InputValidator.isMailGood(mail);

                        //Valid email
                        if (result.first){
                            Log.i("INPUTTEST", "Valid email");
                        }
                        //Invalid email, show message to user
                        else {
                            Toast.makeText(getContext().getApplicationContext(), result.second, Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else {
                    navController.navigate(R.id.action_installLoggingSetup_to_unlockFragment2);
                }
            }
        });

    }


}
