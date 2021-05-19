package com.example.weconnxt.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.weconnxt.R;
import com.example.weconnxt.activities.HomePage;
import com.example.weconnxt.activities.LoginSignupActivity;
import com.example.weconnxt.dialogs.LoadingDialogFragment;
import com.example.weconnxt.interfaces.FragmentClickInterface;
import com.example.weconnxt.model.AppUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
/*import com.weconnect.we_connect.R;
import com.weconnect.we_connect.interfaces.FragmentClickInterface;*/

import java.util.Objects;


public class SignupFragment extends Fragment {

    public String TAG = ":SIGN UP FRAGMENT";
    private final FragmentClickInterface clickInterface;

    TextInputEditText InputName;
    TextInputEditText InputLastName;
    TextInputEditText InputEmail;
    TextInputEditText InputPassword;
    TextInputEditText InputPassword2;

    MaterialButton btnBackToLogin;
    MaterialButton btnRegister;

    //firebase
    FirebaseAuth auth;

    public SignupFragment(LoginSignupActivity clickInterface){
        this.clickInterface = clickInterface;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_signup, container, false);

        //call methods here
        initialize(view);
        ConnectViews(view);

        return view;
    }

    private void initialize(View view)
    {
        btnBackToLogin = view.findViewById(R.id.btn_have_account);
        btnRegister = view.findViewById(R.id.sign_up_button);

        InputName =  view.findViewById(R.id.InputName);
        InputLastName =  view.findViewById(R.id.InputLastname);
        InputEmail = view.findViewById(R.id.InputEmail);
        InputPassword = view.findViewById(R.id.InputPassword);
        InputPassword2 = view.findViewById(R.id.InputConfirmPassword);
    }

    private void ConnectViews(final View view)
    {
        //initialize firebase auth object
        auth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String name = InputName.getText().toString().trim();
                final String lastName = InputLastName.getText().toString().trim();
                final String email = InputEmail.getText().toString().trim();
                String password = InputPassword.getText().toString().trim();
                String confirmPassword = InputPassword2.getText().toString().trim();

                try
                {
                    if (TextUtils.isEmpty(name))
                    {
                        InputName.setError("Cannot be empty");
                    }
                    else if (TextUtils.isEmpty(lastName))
                    {
                        InputLastName.setError("Cannot be empty");
                    }
                    else if (TextUtils.isEmpty(email))
                    {
                        InputEmail.setError("Cannot be empty");
                    }
                    else if (TextUtils.isEmpty(password))
                    {
                        InputPassword.setError("Cannot be empty");
                    } else if (!(TextUtils.equals(password, confirmPassword)))
                    {
                        InputPassword2.setError("Password does not match");
                    }else
                    {
                        auth
                                .createUserWithEmailAndPassword(email, password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {

                                        //get current user's id
                                        String uid = auth.getCurrentUser().getUid();

                                        //User model instance
                                        AppUser user = new AppUser();
                                        user.setId(uid);
                                        user.setName(name);
                                        user.setLastName(lastName);
                                        user.setEmail(email);

                                        //upload user's info to database
                                        FirebaseFirestore
                                                .getInstance()
                                                .collection("Users")
                                                .document(uid)
                                                .set(user);

                                        Toast.makeText(view.getContext(),"User registration was successful...", Toast.LENGTH_LONG).show();

                                        startActivity(new Intent(getActivity(),HomePage.class));

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterface.LoginClick();
                Toast.makeText(view.getContext(),"Clicked on back to login button", Toast.LENGTH_LONG).show();
            }
        });
    }

}