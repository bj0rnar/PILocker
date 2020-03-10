package no.hiof.bjornap.pilocker;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstallHandleSide extends Fragment {

    //Door selection
    private ConstraintLayout rightDoorLayout;
    private ConstraintLayout leftDoorLayout;


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

        //Add green door border
        rightDoorLayout = view.findViewById(R.id.installation_side_rightDoorConstraintLayout);
        leftDoorLayout = view.findViewById(R.id.installation_side_leftDoorConstraintLayout);

        rightDoorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext().getApplicationContext(), "RIGHT", Toast.LENGTH_SHORT).show();
            }
        });

        leftDoorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext().getApplicationContext(), "LEFT", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
