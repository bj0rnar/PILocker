package no.hiof.bjornap.pilocker;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    private Toolbar _toolbar;
    private NavController navController;

    private TextView currentSignedInEmailText;
    private TextView uptimeTextView;
    private LinearLayout sendLogToMailBtn;
    private LinearLayout deleteMailBtn;
    private LinearLayout changeHandleSideBtn;
    private LinearLayout changeLoginMethodBtn;
    private LinearLayout shutDownBtn;
    private LinearLayout factoryResetBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        _toolbar = view.findViewById(R.id.settings_toolbar);
        _toolbar.setTitle("Settings");

        _toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_settingsFragment3_to_unlockFragment2);
            }
        });


        //Textview for updating
        currentSignedInEmailText = view.findViewById(R.id.settings_email_textview);
        uptimeTextView = view.findViewById(R.id.settings_uptime_textview);
        //Buttons for settings
        sendLogToMailBtn = view.findViewById(R.id.settings_send_email_btn);
        deleteMailBtn = view.findViewById(R.id.settings_delete_email_btn);
        changeHandleSideBtn = view.findViewById(R.id.settings_change_handle_side_btn);
        changeLoginMethodBtn = view.findViewById(R.id.settings_login_method_btn);
        shutDownBtn = view.findViewById(R.id.settings_shutdown_rpi_btn);
        factoryResetBtn = view.findViewById(R.id.settings_factory_reset_btn);


        //Listener for send log to email
        sendLogToMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }


}
