package no.hiof.bjornap.pilocker;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class InstallNamingFragment extends Fragment {

    /**
     * Fragment for naming the door.
     */

    private Button nextBtn;
    private NavController navController;
    private EditText nameTxt;

    private Boolean firstTime;
    private String password;

    public InstallNamingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_install_naming, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        if (getArguments() != null) {
            firstTime = getArguments().getBoolean("firstTime");
            password = getArguments().getString("password");

        }
        nameTxt = view.findViewById(R.id.installation_name_doorName_editText);

        nextBtn = view.findViewById(R.id.installation_name_nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sNameTxt = nameTxt.getText().toString();

                //Include some form of input validation, create static class to check input.
                if (!sNameTxt.equals("")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("password", password);
                    bundle.putBoolean("firstTime", firstTime);
                    bundle.putString("doorName", sNameTxt);
                    navController.navigate(R.id.action_installNamingFragment_to_installHandleSide2, bundle);
                }
                else {
                    Toast.makeText(getContext().getApplicationContext(), "Missing door name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
