package no.hiof.bjornap.pilocker;


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
import android.widget.Button;


public class InstallServiceModeInstructionsFragment extends Fragment {

    /**
     * Fragment for displaying installation instructions.
     */

    private String doorName;
    private String side;

    private Boolean firstTime;
    private String password;

    private Button nextBtn;

    private NavController navController;


    public InstallServiceModeInstructionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_install_service_mode_instructions, container, false);
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

        nextBtn = view.findViewById(R.id.installation_service_nextBtn);
        navController = Navigation.findNavController(view);



        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("side", side);
                b.putString("doorName", doorName);
                b.putString("password", password);
                b.putBoolean("firstTime", firstTime);

                navController.navigate(R.id.action_installServiceModeInstructionsFragment_to_installFragment, b);
            }
        });
    }
}
