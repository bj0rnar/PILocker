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


/**
 * A simple {@link Fragment} subclass.
 */
public class InstallWelcomeFragment extends Fragment {

    private Button newInstallButton;
    private Button oldInstallButton;

    private NavController navController;

    public InstallWelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_install_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        newInstallButton = view.findViewById(R.id.installation_welcome_btn_first_time_installation);
        oldInstallButton = view.findViewById(R.id.installation_welcome_btn_nth_time_installation);

        newInstallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putBoolean("firstTime", true);
                navController.navigate(R.id.action_installWelcomeFragment_to_installPasswordFragment, b);
            }
        });

        oldInstallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putBoolean("firstTime", false);
                navController.navigate(R.id.action_installWelcomeFragment_to_installPasswordFragment, b);
            }
        });


    }
}
