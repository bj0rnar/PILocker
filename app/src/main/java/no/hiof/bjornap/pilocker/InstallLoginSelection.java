package no.hiof.bjornap.pilocker;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstallLoginSelection extends Fragment {

    private EditText pinEditText, repeatPinEditText;
    private RadioGroup radioGroup;
    private Button nextButton;
    private int selection = 0;

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
                switch (selection){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
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
