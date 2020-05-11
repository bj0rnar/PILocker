package no.hiof.bjornap.pilocker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.text.ParseException;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstallLoginSelection extends Fragment {

    private EditText pinEditText, repeatPinEditText;
    private RadioGroup radioGroup;
    private Button nextButton;
    private int selection;
    private NavController navController;

    private SharedPreferences pref;

    public InstallLoginSelection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_install_login_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioGroup = view.findViewById(R.id.radio_logging_radioGroup);
        pinEditText = view.findViewById(R.id.installation_authSelection_editText_pin);
        repeatPinEditText = view.findViewById(R.id.installation_authSelection_editText_repeatPin);
        nextButton = view.findViewById(R.id.installation_logging_nextBtn);
        navController = Navigation.findNavController(view);

        final View viewForPrompt = view;

        selection = -1;

        pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);

        //Set edittext to invisible
        pinEditText.setVisibility(View.INVISIBLE);
        repeatPinEditText.setVisibility(View.INVISIBLE);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radio_authSelection_biometric:
                        //Set invisible and clear text
                        pinEditText.setVisibility(View.INVISIBLE);
                        repeatPinEditText.setVisibility(View.INVISIBLE);
                        pinEditText.getText().clear();
                        repeatPinEditText.getText().clear();

                        clearKeyBoard();

                        selection = 1;
                        break;
                    case R.id.radio_authSelection_pin:
                        //Set visible
                        pinEditText.setVisibility(View.VISIBLE);
                        repeatPinEditText.setVisibility(View.VISIBLE);

                        selection = 2;

                        break;
                    case R.id.radio_authSelection_nothing:
                        //Set invisible and clear text
                        pinEditText.setVisibility(View.INVISIBLE);
                        repeatPinEditText.setVisibility(View.INVISIBLE);
                        pinEditText.getText().clear();
                        repeatPinEditText.getText().clear();

                        clearKeyBoard();

                        selection = 0;
                        break;
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = pref.edit();
                switch (selection){
                    case 0:
                        edit.putString("authMethod", "nothing");
                        edit.apply();
                        navController.navigate(R.id.action_installLoginSelection_to_installFragment);
                        break;
                    case 1:
                        edit.putString("authMethod", "biometric");
                        edit.apply();
                        navController.navigate(R.id.action_installLoginSelection_to_installFragment);
                        break;
                    case 2:
                        int pin, repeatPin;
                        try{
                            pin = Integer.parseInt(pinEditText.getText().toString());
                            repeatPin = Integer.parseInt(repeatPinEditText.getText().toString());
                        }catch (NumberFormatException e){
                            Toast.makeText(getContext().getApplicationContext(), "Please enter a number", Toast.LENGTH_LONG).show();
                            break;
                        }

                        if(pinEditText.getText().length() >= 4){
                            if(pin == repeatPin){
                                edit.putString("authMethod", "pin");
                                edit.putInt("pinCode", pin);
                                edit.apply();
                                navController.navigate(R.id.action_installLoginSelection_to_installFragment);
                            }else{
                                Toast.makeText(getContext().getApplicationContext(), "The pin codes dosn't match", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getContext().getApplicationContext(), "The pin code needs to be at least 4 characters long", Toast.LENGTH_LONG).show();
                        }

                        break;
                    default:
                        Snackbar.make(viewForPrompt, "Please choose an option!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        break;

                }
            }
        });

    }

    private void clearKeyBoard(){
        //If radio button "no" is clicked, removes keyboard from screen and changes focus back.
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pinEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(repeatPinEditText.getWindowToken(), 0);
    }

}
