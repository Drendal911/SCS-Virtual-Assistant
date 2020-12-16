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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import DAO.SubjectDAO;
import DAO.TeacherDAO;
import Database.VADatabase;
import Entity.Subject;


public class TeacherFragment extends Fragment {
    private SubjectDAO dbSubject;
    private TeacherDAO dbTeacher;
    private int temp, tID;
    private ArrayList<Subject> teacherSubjectsList;
    private List<String> names;

    EditText editTextTeacherName2, editTextTeacherTextEmailAddress2, editTextTeacherClasses;
    Button buttonTeacherDone;

    public TeacherFragment() {
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
        return inflater.inflate(R.layout.fragment_teacher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setItemIDs();
        buildLists();
        buildButtons();
    }


    /*METHODS **************************************************************************************
     ************************************************************************************************
     ***********************************************************************************************/


    private void checkNull() {
        if (getArguments() != null) {
            temp = getArguments().getInt("USER_ID");
            tID = getArguments().getInt("TEACHER_ID");

            VADatabase database = Room.databaseBuilder(Objects.requireNonNull(getContext()), VADatabase.class, "Database")
                    .allowMainThreadQueries().build();
            dbSubject = database.getSubjectDAO();
            dbTeacher = database.getTeacherDAO();
        }
    }

    private void setItemIDs() {
        editTextTeacherName2 = Objects.requireNonNull(getView()).findViewById(R.id.editTextTeacherName2);
        editTextTeacherTextEmailAddress2 = getView().findViewById
                (R.id.editTextTeacherTextEmailAddress2);
        editTextTeacherClasses = getView().findViewById(R.id.editTextTeacherClasses);
        buttonTeacherDone = getView().findViewById(R.id.buttonTeacherDone);

        names = new ArrayList<>();
        teacherSubjectsList = new ArrayList<>();

        editTextTeacherName2.setText(dbTeacher.getTeacherName(tID));
        editTextTeacherTextEmailAddress2.setText(dbTeacher.getTeacherEmail(tID));
    }

    private void buildLists() {
        StringBuilder nameString = new StringBuilder();
        String adjustedNameString = "";
        try {
            teacherSubjectsList.addAll(dbSubject.getSubjectWithTeacher(temp, tID));
            for (Subject s: teacherSubjectsList) {
                names.add(s.getSubjectName());
            }
        }catch (NullPointerException e) {
            editTextTeacherClasses.setText("");
        }
        int nameSize = names.size();
        if (nameSize > 1) {
            while (nameSize >= 1) {
                nameString.append(names.get(nameSize - 1)).append(", ");
                nameSize--;
            }
            adjustedNameString = nameString.substring(0, nameString.length()-2);
        }else if (nameSize == 1){
            nameString = new StringBuilder(names.get(0));
            adjustedNameString = nameString.toString();
        }
        editTextTeacherClasses.setText(adjustedNameString);
    }

    private void buildButtons() {
        buttonTeacherDone.setOnClickListener(v -> {
            Fragment mFragment = new TeachersFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", temp);
            mFragment.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mFragment).commit();
        });
    }

}