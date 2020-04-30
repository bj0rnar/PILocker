package no.hiof.bjornap.pilocker;

import android.content.Context;
import android.net.Uri;
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
import android.widget.TextView;


public class InstallPasswordFragment extends Fragment {

    private Boolean firstTime;
    private TextView password_textView;
    private EditText password_editText;
    private Button password_nextBtn;

    private NavController navController;

    private String standardPassword = "gruppe6";

    public InstallPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_install_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        password_editText = view.findViewById(R.id.installation_password_editText);
        password_textView = view.findViewById(R.id.installation_password_textView);
        password_nextBtn = view.findViewById(R.id.installation_password_btn_next);

        navController = Navigation.findNavController(view);



        if (getArguments() != null) {
            firstTime = getArguments().getBoolean("firstTime");

            if (firstTime) {
                password_textView.setText(R.string.installation_password_create_new_password);
                password_editText.setHint(R.string.installation_password_firsttime_edittext_hint);
            }
            else {
                password_textView.setText(R.string.installation_password_write_in_password);
                password_editText.setHint(R.string.installation_password_nthtime_edittext_hint);
            }


        }

        password_nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstTime) {
                    //SSHExecutur.execute(newPassword, bruk standard passord)
                    //SharedPref pref = bla bla, save newPassord
                    navController.navigate(R.id.action_installPasswordFragment_to_installNamingFragment);
                }
                else {
                    //SharedPref pref = overwrite Passord
                    navController.navigate(R.id.action_installPasswordFragment_to_installNamingFragment);
                }
            }
        });

    }
}
