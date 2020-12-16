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


public class EditTeacherFragment extends Fragment {
    private TeacherDAO dbTeacher;
    private int temp, tID;

    EditText editTextEditTeacherName, editTextEditTeacherTextEmailAddress;
    Button buttonEditTeacher, buttonEditTeacherCancel;


    public EditTeacherFragment() {
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
        return inflater.inflate(R.layout.fragment_edit_teacher, container, false);
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
            tID = getArguments().getInt("TEACHER_ID");

            VADatabase database = Room.databaseBuilder(Objects.requireNonNull(getContext()), VADatabase.class,
                    "Database")
                    .allowMainThreadQueries().build();
            dbTeacher = database.getTeacherDAO();
        }
    }

    private void setItemIDs() {
        editTextEditTeacherName = Objects.requireNonNull(getView()).findViewById
                (R.id.editTextEditTeacherName);
        editTextEditTeacherTextEmailAddress = getView().findViewById(R.id.
                editTextEditTeacherTextEmailAddress);
        buttonEditTeacher = getView().findViewById(R.id.buttonEditTeacher);
        buttonEditTeacherCancel = getView().findViewById(R.id.buttonEditTeacherCancel);

        editTextEditTeacherName.setText(dbTeacher.getTeacherName(tID));
        editTextEditTeacherTextEmailAddress.setText(dbTeacher.getTeacherEmail(tID));
    }

    private void buildButtons() {
        buttonEditTeacher.setOnClickListener(v -> {
            Pattern pattern = Pattern.compile(".*" + "@" + "." + ".*");
            Matcher matcher = pattern.matcher(editTextEditTeacherTextEmailAddress.getText().
                    toString());
            if (editTextEditTeacherName.getText().toString().isEmpty() ||
                    editTextEditTeacherTextEmailAddress.getText().toString().isEmpty()) {
                Toast.makeText(getContext(),"Please complete all fields.",
                        Toast.LENGTH_SHORT).show();
            }else if (!matcher.find()) {
                Toast.makeText(getContext(),"Please enter a valid email address.",
                        Toast.LENGTH_SHORT).show();
            }else {
                dbTeacher.updateTeacher(tID, editTextEditTeacherName.getText().toString(),
                        editTextEditTeacherTextEmailAddress.getText().toString());
                Toast.makeText(getContext(), "Teacher updated.", Toast.LENGTH_SHORT).show();
                Fragment mFragment = new TeachersFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("USER_ID", temp);
                mFragment.setArguments(bundle);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mFragment).commit();
            }
        });

        buttonEditTeacherCancel.setOnClickListener(v -> {
            Fragment mFragment = new TeachersFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", temp);
            mFragment.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mFragment).commit();
            Toast.makeText(getContext(),"Cancelled.",Toast.LENGTH_SHORT).show();
        });
    }

}