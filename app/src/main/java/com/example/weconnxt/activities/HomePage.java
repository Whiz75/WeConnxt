package com.example.weconnxt.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.weconnxt.R;
import com.example.weconnxt.dialogs.LoadingDialogFragment;
import com.example.weconnxt.fragments.ChatFragment;
import com.example.weconnxt.fragments.FriendsFragments;
import com.example.weconnxt.fragments.HomeFragment;
import com.example.weconnxt.fragments.FriendProfileFragment;
import com.example.weconnxt.fragments.ProfileFragment;
import com.example.weconnxt.fragments.RequestsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
//import com.weconnect.we_connect.fragments.HomeFragment;

public class HomePage extends AppCompatActivity {

    MaterialToolbar tool_bar;
    DrawerLayout drawer_layout;
    NavigationView drawer_nav_menu;
    //private ChipNavigationBar bottomnavigation;
    private BottomNavigationView bottomnavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.host_frag, new HomeFragment())//not sure about the resourse host_frag
                .commit();

        //default fragment

        //call methods
        ConnectViews();
        initComponents();
        UpdateLatsSeen();

    }

    private void initComponents() {

        bottomnavigation = findViewById(R.id.bottom_nav_menu);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.host_frag,new HomeFragment())
                .commit();

        //bottomnavigation.setOnNavigationItemSelectedListener(bottom_nav_home, true);

        listenToEvent();
    }
    //support bottom navigation
    private void listenToEvent()
    {
        bottomnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.bottom_nav_home:
                        fragment = new HomeFragment();
                        tool_bar.setTitle("Home");
                        break;
                    case R.id.bottom_nav_friends:
                        fragment = new FriendsFragments();
                        tool_bar.setTitle("Friends");
                        break;
                    /*case R.id.bottom_nav_requests:
                        fragment = new RequestsFragment();
                        tool_bar.setTitle("Requests");
                        break;*/
                    case R.id.bottom_nav_profile:
                        fragment = new ProfileFragment();
                        tool_bar.setTitle("Profile");
                        break;
                    case R.id.bottom_nav_chat:
                        fragment = new ChatFragment();
                        tool_bar.setTitle("Chat");
                        break;
                }

                //get the fragment support
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.host_frag,fragment)
                        .commit();
               return true;
            }
        });
    }

    private void ConnectViews()
    {
        tool_bar = findViewById(R.id.tool_bar);
        drawer_layout = findViewById(R.id.drawer_layout);
        drawer_nav_menu = findViewById(R.id.drawer_nav_menu);

        tool_bar.setNavigationIcon(R.drawable.baseline_reorder_white_24dp);
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.START);

            }
        });
        drawer_nav_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_profile){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.host_frag, new FriendProfileFragment())
                            .commit();
                }
                if (item.getItemId() == R.id.nav_about)
                {
                    //display this message
                    Toast.makeText(getApplicationContext(), "Info about the App", Toast.LENGTH_SHORT).show();
                }

                if (item.getItemId() == R.id.nav_logout)
                {
                    //call method to log out user
                    UserLogout();
                }

                return true;
            }
        });

    }

    private void UpdateLatsSeen()
    {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    SimpleDateFormat dateFormat =new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                    String d = dateFormat.format(Calendar.getInstance().getTime());
                    Thread.sleep(1000);
                    HashMap hashMap = new HashMap();
                    hashMap.put("Last_Seen", d);


                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Online")
                            .child("-MWF5iu9-dv9p0JAPWxR")
                            .setValue(hashMap);
                    UpdateLatsSeen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void UserLogout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        builder.setMessage("Are you sure you want to log out?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LoadingDialogFragment dialogFragment =  new LoadingDialogFragment("Loading...");
                dialogFragment.show(getSupportFragmentManager(), "Loading");
                
                Thread thread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(3000);
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(),LoginSignupActivity.class));
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //dismiss dialog if user doesn't want to logout
                dialog.dismiss();
            }
        });
        builder.show();
    }

}