package com.example.virtualassistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DAO.TeacherDAO;
import Database.VADatabase;
import Entity.Teacher;


public class AddTeacherFragment extends Fragment {
    private TeacherDAO dbTeacher;
    private int temp;

    EditText editTextTeacherName, editTextTeacherTextEmailAddress;
    Button buttonAddTeacher;
    Button buttonCancel;

    public AddTeacherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNull();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_teacher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setItemIDs();
        buildButtons();
    }



    /*METHODS **************************************************************************************
    ************************************************************************************************
    ***********************************************************************************************/


    private void checkNull() {
        if (getArguments() != null) {
            temp = getArguments().getInt("USER_ID");
            VADatabase database = Room.databaseBuilder(Objects.requireNonNull(getContext()), VADatabase.class, "Database")
                    .allowMainThreadQueries().build();
            dbTeacher = database.getTeacherDAO();
        }
    }

    private void setItemIDs() {
        editTextTeacherName = Objects.requireNonNull(getView()).findViewById(R.id.editTextTeacherName);
        editTextTeacherTextEmailAddress = getView()
                .findViewById((R.id.editTextTeacherTextEmailAddress));
        buttonAddTeacher = getView().findViewById(R.id.buttonAddTeacher);
        buttonCancel = getView().findViewById(R.id.buttonCancel);
    }

    private void buildButtons() {
        //Set click listener for add teacher button
        buttonAddTeacher.setOnClickListener(v -> {
            String teacherName = editTextTeacherName.getText().toString();
            String teacherEmail = editTextTeacherTextEmailAddress.getText().toString();
            if (teacherName.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a teacher name.",
                        Toast.LENGTH_SHORT).show();
            }else if (teacherEmail.isEmpty()) {
                Toast.makeText(getContext(), "Please enter the teacher's email address.",
                        Toast.LENGTH_SHORT).show();
            }else {
                //Check to make sure email address contains "@" and "."
                Pattern pattern = Pattern.compile(".*" + "@" + "." + ".*");
                Matcher matcher = pattern.matcher(teacherEmail);
                if (matcher.find()) {
                    try {
                        if (dbTeacher.getTeacher(temp, teacherName).getTeacherName()
                                .contains(teacherName)) {
                            Toast.makeText(getContext(), "Teacher with that name " +
                                            "already exists.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }catch (NullPointerException e) {
                        Teacher inputTeacher = new Teacher(temp, teacherName, teacherEmail,
                                R.drawable.ic_baseline_teacher_24);
                        dbTeacher.insertTeacher(inputTeacher);
                        if (dbTeacher.getTeacher(temp, teacherName).getTeacherName().contains
                                (teacherName)) {
                            Toast.makeText(getContext(), inputTeacher.getTeacherName() +
                                    " added", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new TeachersFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("USER_ID", temp);
                            fragment.setArguments(bundle);
                            assert getFragmentManager() != null;
                            getFragmentManager().beginTransaction().replace
                                    (R.id.fragment_container, fragment).commit();
                        } else {
                            Toast.makeText(getContext(), "Something went wrong",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(getContext(), "Please enter a valid email address",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Set click listener for cancel button
        buttonCancel.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            Fragment fragment = new TeachersFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", temp);
            fragment.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
        });
    }

}