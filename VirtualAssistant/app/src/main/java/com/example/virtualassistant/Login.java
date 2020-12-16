package com.example.virtualassistant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import DAO.UserDAO;
import Database.VADatabase;
import Entity.User;


public class Login extends AppCompatActivity {
    private UserDAO db;

    TextView textView;
    Button loginButton;
    Button registerButton;
    TextInputEditText usernameTextInputEditText, passwordTextInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        VADatabase database = Room.databaseBuilder(this, VADatabase.class, "Database")
                .allowMainThreadQueries().build();
        db = database.getUserDAO();

        setItemIDs();
        buildButtons();
    }



    /*METHODS **************************************************************************************
     ************************************************************************************************
     ***********************************************************************************************/


    private void setItemIDs() {
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        textView = findViewById(R.id.textView);
        usernameTextInputEditText = findViewById(R.id.usernameTextInputEditText);
        passwordTextInputEditText = findViewById(R.id.passwordTextInputEditText);
    }

    private void buildButtons() {
        loginButton.setOnClickListener(v -> {
            //Checks the fields for correct input, then checks the input against the DB
            try {
                String str1 = Objects.requireNonNull(usernameTextInputEditText.getText()).toString().toUpperCase();
                String str2 = Objects.requireNonNull(passwordTextInputEditText.getText()).toString();
                User tempUser = db.getUser(str1, str2);
                String tempUserName = tempUser.getUserName();
                int tempUserID = tempUser.getUserID();
                if (str1.isEmpty() || str2.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill in all fields.",
                            Toast.LENGTH_SHORT).show();
                } else if (tempUser.getPassword().isEmpty()) {
                    Toast.makeText(Login.this, "Username or password incorrect.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (str1.equals(tempUser.getUserName()) && str2.equals(tempUser
                            .getPassword())) {
                        Intent intent = new Intent(Login.this, Dashboard.class);
                        intent.putExtra("USER_NAME", tempUserName);
                        intent.putExtra("USER_ID", tempUserID);
                        startActivity(intent);
                    }
                }
            } catch (NullPointerException e) {
                Toast.makeText(getBaseContext(), "Username or password incorrect.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });
    }

}