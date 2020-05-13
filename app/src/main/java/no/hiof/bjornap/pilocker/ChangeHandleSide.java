package no.hiof.bjornap.pilocker;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeHandleSide extends Fragment {

    private SharedPreferences pref;
    private String oldSide;
    private String selectedDoor;

    private ConstraintLayout rightDoorLayout;
    private ConstraintLayout leftDoorLayout;
    private Button saveButton;

    private NavController navController;

    public ChangeHandleSide() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_handle_side, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rightDoorLayout = view.findViewById(R.id.installation_side_rightDoorConstraintLayout);
        leftDoorLayout = view.findViewById(R.id.installation_side_leftDoorConstraintLayout);
        saveButton = view.findViewById(R.id.change_side_saveBtn);
        navController = Navigation.findNavController(view);

        pref = getContext().getApplicationContext().getSharedPreferences("myPref", 0);
        oldSide = pref.getString("side", null);



        if(oldSide != null){
            switch (oldSide){
                case "left":
                    leftDoorLayout.setBackgroundColor(getResources().getColor(R.color.green));
                    rightDoorLayout.setBackgroundColor(getResources().getColor(R.color.primaryDarkBackgroundColor));
                    selectedDoor = "left";
                    break;
                case "right":
                    rightDoorLayout.setBackgroundColor(getResources().getColor(R.color.green));
                    leftDoorLayout.setBackgroundColor(getResources().getColor(R.color.primaryDarkBackgroundColor));
                    selectedDoor = "right";
                    break;
            }
        }

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDoorChoice();
            }
        });

    }

    private void saveDoorChoice(){
        if(selectedDoor.equals("right") || selectedDoor.equals("left")){
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("side", selectedDoor);
            edit.apply();
            navController.navigate(R.id.action_changeHandleSide_to_unlockFragment2);
        }
    }


}
