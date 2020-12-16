package com.example.virtualassistant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DAO.UserDAO;
import Database.VADatabase;
import Entity.User;

public class Register extends AppCompatActivity {
    private UserDAO db;

    Button registrationButton;
    Button backButton;
    EditText editTextTextPassword, editTextTextEmailAddress, editTextTextUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        registrationButton = findViewById(R.id.registrationButton);
        backButton = findViewById(R.id.backButton);
        editTextTextUserName = findViewById(R.id.editTextTextUserName);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
    }

    private void buildButtons() {
        registrationButton.setOnClickListener(v -> {
            String[] details = new String[3];
            details[0] = editTextTextUserName.getText().toString().toUpperCase();
            details[1] = editTextTextPassword.getText().toString();
            details[2] = editTextTextEmailAddress.getText().toString();

            //Check fields for valid input and username uniqueness
            if (details[0].isEmpty() || details[1].isEmpty() || details[2].isEmpty()) {
                Toast.makeText(Register.this, "Please complete all fields",
                        Toast.LENGTH_SHORT).show();
            }else {
                try {
                    if (details[0].equals(db.checkUser(details[0]).getUserName())) {
                        Toast.makeText(Register.this, "Username: " + details[0] +
                                        " already exists. Please choose a new username.",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (NullPointerException e){
                    Pattern pattern = Pattern.compile(".*" + "@" + "." + ".*");
                    Matcher matcher = pattern.matcher(details[2]);
                    if (!matcher.find()) {
                        Toast.makeText(Register.this,
                                "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                    }else {
                        //Create and insert new user into the database after all checks executed
                        User user = new User(details[0], details[1], details[2]);
                        db.insertUser(user);
                        Toast.makeText(Register.this, "New user " + details[0] +
                                " created.", Toast.LENGTH_SHORT).show();
                        //Switch to Login screen
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);

                    }
                }
            }
        });

        backButton.setOnClickListener(v -> {
            //Switch to Login screen
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });
    }

}