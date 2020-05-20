package no.hiof.bjornap.pilocker;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import no.hiof.bjornap.pilocker.Utility.InputValidator;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class InstallPasswordFragment extends Fragment {

    private Boolean firstTime;
    private TextView password_textView;
    private EditText password_editText;
    private EditText password_editText_repeat;
    private Button password_nextBtn;
    private ImageView viewPassword_imageView;



    private NavController navController;


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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        password_editText = view.findViewById(R.id.installation_password_editText);
        password_textView = view.findViewById(R.id.installation_password_textView);
        password_editText_repeat = view.findViewById(R.id.installation_password_editText_repeat);
        password_nextBtn = view.findViewById(R.id.installation_password_btn_next);
        viewPassword_imageView = view.findViewById(R.id.installation_password_viewPassword_imageView);

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

        viewPassword_imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    password_editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password_editText_repeat.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    return true;
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    password_editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password_editText_repeat.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    return true;
                }
                return false;
            }
        });

        password_nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!password_editText.getText().toString().equals("")) {

                    Pair<Boolean, String> validator = InputValidator.isServiceModePasswordGood(password_editText.getText().toString(), password_editText_repeat.getText().toString());

                    if (validator.first) {
                        String nPassword = password_editText.getText().toString();

                        Bundle b = new Bundle();
                        b.putString("password", nPassword);
                        b.putBoolean("firstTime", firstTime);
                        navController.navigate(R.id.action_installPasswordFragment_to_installNamingFragment, b);
                    }
                    else {
                        Toast.makeText(getContext().getApplicationContext(), validator.second, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
