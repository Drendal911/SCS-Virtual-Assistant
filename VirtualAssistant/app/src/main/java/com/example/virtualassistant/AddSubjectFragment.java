package com.example.virtualassistant;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import DAO.SubjectDAO;
import DAO.TeacherDAO;
import Database.VADatabase;
import Entity.Subject;
import Entity.Teacher;


public class AddSubjectFragment extends Fragment {
    private TeacherDAO dbTeacher;
    private SubjectDAO dbSubject;
    private int temp;
    EditText editTextSubjectName, editTextSubjectStartTime, editTextAddSubjectTeacher;
    List<Teacher> teacherList;
    Button buttonCancel;
    Button buttonAddClass;

    public AddSubjectFragment() {
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
        return inflater.inflate(R.layout.fragment_add_subject, container, false);
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
            dbSubject = database.getSubjectDAO();
        }
    }

    private void setItemIDs() {
        teacherList = dbTeacher.getAllTeachers(temp);
        editTextAddSubjectTeacher = Objects.requireNonNull(getView()).findViewById(R.id.editTextAddSubjectTeacher);
        editTextSubjectName = getView().findViewById(R.id.editTextSubjectName);
        editTextSubjectStartTime = getView().findViewById(R.id.editTextSubjectStartTime);
        buttonCancel = getView().findViewById(R.id.buttonCancel);
        buttonAddClass = getView().findViewById(R.id.buttonAddClass);
    }

    private void buildButtons() {
        //Set click listener for EditText and show time picker dialog
        editTextSubjectStartTime.setOnClickListener(new View.OnClickListener() {
            int hr, min;
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        (view, hourOfDay, minute) -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(0,0,0,hourOfDay,minute);
                            editTextSubjectStartTime.setText(DateFormat.format
                                    ("hh:mm aa",calendar));
                        },12,0,false);
                timePickerDialog.updateTime(hr,min);
                timePickerDialog.show();
            }
        });

        //Set click listener for add button
        buttonAddClass.setOnClickListener(v -> {
            String subjectName = editTextSubjectName.getText().toString();
            String subjectTime = editTextSubjectStartTime.getText().toString();
            Teacher selectedTeacher = dbTeacher.getTeacher(temp, editTextAddSubjectTeacher.
                    getText().toString());
            int selectedTeacherID = selectedTeacher.getTeacherID();
            if (subjectName.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a class name.",
                        Toast.LENGTH_SHORT).show();
            }else if (subjectTime.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a class time.",
                        Toast.LENGTH_SHORT).show();
            }else {
                Subject inputSubject = new Subject(temp, selectedTeacherID, subjectName,
                        subjectTime,R.mipmap.class_icon);
                dbSubject.insertSubject(inputSubject);

                if (dbSubject.getSubject(temp,subjectName).getSubjectName().contains
                        (subjectName)) {
                    Toast.makeText(getContext(), "Class added", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Something went wrong",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Set click listener for cancel button
        buttonCancel.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            Fragment fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", temp);
            fragment.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
        });

        //Set click listener for EditText showing dialog choices
        editTextAddSubjectTeacher.setOnClickListener(v -> {
            List<Teacher> teacherObjectList = dbTeacher.getAllTeachers(temp);
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
            builderSingle.setTitle("Select Teacher:");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.select_dialog_singlechoice);

            for (Teacher t: teacherObjectList){
                arrayAdapter.add(t.getTeacherName());
            }

            builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String chosenTeacher = arrayAdapter.getItem(which);
                editTextAddSubjectTeacher.setText(chosenTeacher);
            });
            builderSingle.show();
        });

        //Set click listener for add button
        buttonAddClass.setOnClickListener(v -> {
            String subjectName = editTextSubjectName.getText().toString();
            String subjectTime = editTextSubjectStartTime.getText().toString();

            if (subjectName.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a class name.",
                        Toast.LENGTH_SHORT).show();
            }else if (subjectTime.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a class time.",
                        Toast.LENGTH_SHORT).show();
            }else if (editTextAddSubjectTeacher.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please select a teacher.",
                        Toast.LENGTH_SHORT).show();
            }else {
                Teacher selectedTeacher = dbTeacher.getTeacher(temp, editTextAddSubjectTeacher.
                        getText().toString());
                Subject inputSubject = new Subject(temp, selectedTeacher.getTeacherID(),
                        subjectName, subjectTime,R.mipmap.class_icon);
                dbSubject.insertSubject(inputSubject);

                if (dbSubject.getSubject(temp,subjectName).getSubjectName().contains
                        (subjectName)) {
                    Toast.makeText(getContext(), inputSubject.getSubjectName() + " added",
                            Toast.LENGTH_SHORT).show();
                    Fragment mFragment = new HomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", temp);
                    mFragment.setArguments(bundle);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            mFragment).commit();
                }else {
                    Toast.makeText(getContext(), "Something went wrong",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}