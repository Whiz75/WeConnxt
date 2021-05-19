package com.example.weconnxt.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weconnxt.R;
import com.example.weconnxt.fragments.LoginFragment;
import com.example.weconnxt.fragments.SignupFragment;
import com.example.weconnxt.interfaces.FragmentClickInterface;

public class LoginSignupActivity extends AppCompatActivity implements FragmentClickInterface {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        if(savedInstanceState == null){
            LoginFragment frag = new LoginFragment((FragmentClickInterface) this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.FragHost, frag)
                    .commit();
        }

    }

    @Override
    public void BtnSignUpClick() {
        SignupFragment frag = new SignupFragment(this);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.right_in, R.anim.right_out)
                .replace(R.id.FragHost, frag, frag.TAG)
                .commit();
    }

    @Override
    public void LoginClick() {
        LoginFragment frag = new LoginFragment(this);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                .replace(R.id.FragHost, frag, frag.TAG)
                .commit();
    }



}