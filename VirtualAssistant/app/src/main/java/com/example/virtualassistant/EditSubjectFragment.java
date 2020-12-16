package com.example.virtualassistant;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import DAO.SubjectDAO;
import DAO.TeacherDAO;
import Database.VADatabase;
import Entity.Teacher;


public class EditSubjectFragment extends Fragment {
    private SubjectDAO dbSubject;
    private TeacherDAO dbTeacher;
    private int temp, subjectID;
    TextView editSubjectTextView, editTextEditSubjectTeacher, editTextEditSubjectStartTime,
            editTextEditSubjectName;
    Button buttonEditClass, buttonEditCancel;

    public EditSubjectFragment() {
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
        return inflater.inflate(R.layout.fragment_edit_subject, container, false);
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
            subjectID = getArguments().getInt("SUBJECT_ID");
            VADatabase database = Room.databaseBuilder(Objects.requireNonNull(getContext()), VADatabase.class, "Database")
                    .allowMainThreadQueries().build();
            dbSubject = database.getSubjectDAO();
            dbTeacher = database.getTeacherDAO();
        }
    }

    private void setItemIDs() {
        editSubjectTextView = Objects.requireNonNull(getView()).findViewById(R.id.editSubjectTextView);
        editTextEditSubjectTeacher = getView().findViewById
                (R.id.editTextEditSubjectTeacher);
        editTextEditSubjectStartTime = getView().findViewById
                (R.id.editTextEditSubjectStartTime);
        editTextEditSubjectName = getView().findViewById(R.id.editTextEditSubjectName);
        buttonEditClass = getView().findViewById(R.id.buttonEditClass);
        buttonEditCancel = getView().findViewById(R.id.buttonEditCancel);

        editTextEditSubjectName.setText(dbSubject.getSubjectName(temp, subjectID));
        editTextEditSubjectStartTime.setText(dbSubject.getSubjectTime(temp, subjectID));
        editTextEditSubjectTeacher.setText(dbTeacher.getTeacherName
                (dbSubject.getSubjectTeacherID(temp, subjectID)));
    }

    private void buildButtons() {
        buttonEditClass.setOnClickListener(v -> {
            if (editSubjectTextView.getText().toString().isEmpty() || editTextEditSubjectTeacher
                    .getText().toString().isEmpty() || editTextEditSubjectStartTime.getText()
                    .toString().isEmpty()) {
                Toast.makeText(getContext(),"Please complete all fields.",
                        Toast.LENGTH_SHORT).show();
            }else {
                try {
                    int tID = dbTeacher.getTeacher(temp,editTextEditSubjectTeacher.getText().
                            toString()).getTeacherID();
                    dbSubject.updateSubject(subjectID, tID, editTextEditSubjectName.getText().
                            toString(),editTextEditSubjectStartTime.getText().toString());
                    Toast.makeText(getContext(), dbSubject.getSubjectName(temp, subjectID)
                            + " updated.", Toast.LENGTH_SHORT).show();
                    Fragment mFragment = new HomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", temp);
                    bundle.putInt("SUBJECT_ID", subjectID);
                    mFragment.setArguments(bundle);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            mFragment).commit();
                }catch (NullPointerException e) {
                    Toast.makeText(getContext(), "No teacher with that name in database.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonEditCancel.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            Fragment mFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", temp);
            bundle.putInt("SUBJECT_ID", subjectID);
            mFragment.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mFragment).commit();
        });

        //Set click listener for EditText and show time picker dialog
        editTextEditSubjectStartTime.setOnClickListener(new View.OnClickListener() {
            int hr, min;
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        (view, hourOfDay, minute) -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(0,0,0,hourOfDay,minute);
                            editTextEditSubjectStartTime.setText(DateFormat.format
                                    ("hh:mm aa",calendar));
                        },12,0,false);
                timePickerDialog.updateTime(hr,min);
                timePickerDialog.show();
            }
        });

        editTextEditSubjectTeacher.setOnClickListener(v -> {
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
                editTextEditSubjectTeacher.setText(chosenTeacher);
            });
            builderSingle.show();
        });
    }

}