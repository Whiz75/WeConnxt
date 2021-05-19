package com.example.weconnxt.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weconnxt.R;
import com.example.weconnxt.activities.HomePage;
import com.example.weconnxt.interfaces.FragmentClickInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/*import com.weconnect.we_connect.R;
import com.weconnect.we_connect.activities.HomePage;
import com.weconnect.we_connect.interfaces.FragmentClickInterface;*/


public class LoginFragment extends Fragment {

    private TextInputEditText InputEmail;
    private TextInputEditText InputPassword;
    private Context context;
    public String TAG = "LOGIN FRAGMENT";

    //firebase
    FirebaseAuth auth;

    public LoginFragment(FragmentClickInterface clickInterface) {
        // Required empty public constructor
        this.clickInterface = clickInterface;
    }


    private final FragmentClickInterface clickInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        //fragment context
        context = view.getContext();
        //call methods
        ConnectViews(view);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null)
        {
            Intent intent = new Intent(getActivity(), HomePage.class);
            startActivity(intent);
        }
    }

    private void ConnectViews(final View view)
    {
        MaterialButton btnSignup = view.findViewById(R.id.btn_sign_up);
        MaterialButton BtnLogin = view.findViewById(R.id.login_button);
        MaterialTextView BtnForgotPassword = view.findViewById(R.id.reset_password_tv);
        InputEmail = view.findViewById(R.id.input_email);
        InputPassword = view.findViewById(R.id.input_password);


        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = InputEmail.getText().toString().trim();
                String password = InputPassword.getText().toString().trim();

                if(email.isEmpty()){
                    InputEmail.setError("Email cannot be empty");
                    return;
                }
                if(password.isEmpty()){
                    InputPassword.setError("Password cannot be empty");
                    return;
                }
                if (!(email.isEmpty() && password.isEmpty()))
                {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    try
                                    {
                                        Intent intent = new Intent(view.getContext(), HomePage.class);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        });
        btnSignup.setOnClickListener(new MaterialTextView.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                clickInterface.BtnSignUpClick();
            }
        });
    }

}