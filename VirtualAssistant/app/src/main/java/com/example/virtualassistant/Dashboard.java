package com.example.virtualassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {
    private String tempUserName;
    private int tempUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        createNotificationChannel();
        setIntent();

        //Sets the bottom navigation buttons to switch between fragments
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        setFragment();
    }




    /*METHODS **************************************************************************************
    ************************************************************************************************
    ***********************************************************************************************/


    private void setIntent() {
        Intent intent = getIntent();
        tempUserName = intent.getStringExtra("USER_NAME");
        tempUserID = intent.getIntExtra("USER_ID", 0);
    }

    private void setFragment() {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("USER_NAME", tempUserName);
        bundle.putInt("USER_ID", tempUserID);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }

    private void createNotificationChannel() {
        String name = "Assignment Notification Channel";
        String description = "Assignment Notifications";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("Channel_1", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    //Links bottom navigation buttons to the different fragments
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    Bundle bundle = new Bundle();
                    FragmentManager fm = getSupportFragmentManager();

                    switch(item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_assignment:
                            selectedFragment = new AssignmentsFragment();
                            break;
                        case R.id.nav_teacher:
                            selectedFragment = new TeachersFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                    }

                    bundle.putString("USER_NAME", tempUserName);
                    bundle.putInt("USER_ID", tempUserID);
                    assert selectedFragment != null;
                    selectedFragment.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.fragment_container, selectedFragment);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

}

