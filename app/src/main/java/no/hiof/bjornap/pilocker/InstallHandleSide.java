package no.hiof.bjornap.pilocker;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstallHandleSide extends Fragment {

    //Door selection
    private ConstraintLayout rightDoorLayout;
    private ConstraintLayout leftDoorLayout;

    private Button nextBtn;
    private Button backBtn;
    private NavController navController;
    private String doorName;
    private String selectedDoor;

    private String password;
    private Boolean firstTime;


    public InstallHandleSide() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_install_handle_side, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Check that passed data is received from previous fragment
        if (getArguments() != null){
            doorName = getArguments().getString("doorName");
            firstTime = getArguments().getBoolean("firstTime");
            password = getArguments().getString("password");

            Log.i("BUNDLE", doorName);
        }

        //Initialize
        selectedDoor = "";
        rightDoorLayout = view.findViewById(R.id.installation_side_rightDoorConstraintLayout);
        leftDoorLayout = view.findViewById(R.id.installation_side_leftDoorConstraintLayout);
        navController = Navigation.findNavController(view);

        //Select door
        rightDoorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightDoorLayout.setBackgroundColor(getResources().getColor(R.color.green));
                leftDoorLayout.setBackgroundColor(getResources().getColor(R.color.primaryDarkBackgroundColor));
                selectedDoor = "right";
            }
        });

        leftDoorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftDoorLayout.setBackgroundColor(getResources().getColor(R.color.green));
                rightDoorLayout.setBackgroundColor(getResources().getColor(R.color.primaryDarkBackgroundColor));
                selectedDoor = "left";
            }
        });

        nextBtn = view.findViewById(R.id.installation_side_nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check if either door has not been selected or data from previous fragment didn't come through
                if (!selectedDoor.equals("") && !doorName.equals("")) {
                    Bundle b = new Bundle();
                    b.putString("side", selectedDoor);
                    b.putString("doorName", doorName);
                    b.putString("password", password);
                    b.putBoolean("firstTime", firstTime);
                    navController.navigate(R.id.action_installHandleSide2_to_installFragment, b);
                }
                else {
                    Toast.makeText(getContext().getApplicationContext(), "Select a side to continue", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn = view.findViewById(R.id.installation_side_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("lOL");
            }
        });

    }
}
