package com.example.virtualassistant;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import DAO.AsgmtIncDAO;
import DAO.SubjectDAO;
import Database.VADatabase;
import Entity.AsgmtIncomplete;
import Entity.Subject;

public class AddAssignmentFragment extends Fragment {
    private AsgmtIncDAO dbAsgmtInc;
    private SubjectDAO dbSubject;
    private int temp;
    private List<Subject> subjectList;
    private List<String> subjectNamesList;

    EditText editTextAssignmentName, editTextAssignmentDueDate, editTextAssignmentDueTime,
            editTextNotes;
    Button buttonAddAssignment, buttonCancel;
    Spinner classSpinner;


    public AddAssignmentFragment() {
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
        return inflater.inflate(R.layout.fragment_add_assignment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setItemIDs();
        buildSubjectList();
        buildArrayAdapter();
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
            dbAsgmtInc = database.getAsgmtIncDAO();
            dbSubject = database.getSubjectDAO();
        }
    }

    private void setItemIDs() {
        editTextAssignmentName = Objects.requireNonNull(getView()).findViewById(R.id.editTextAssignmentName);
        editTextAssignmentDueDate = getView().findViewById(R.id.editTextAssignmentDueDate);
        editTextAssignmentDueTime = getView().findViewById(R.id.editTextAssignmentDueTime);
        editTextNotes = getView().findViewById(R.id.editTextNotes);
        classSpinner = getView().findViewById(R.id.classSpinner);
        buttonAddAssignment = getView().findViewById(R.id.buttonAddAssignment);
        buttonCancel = getView().findViewById(R.id.buttonCancel);
        subjectList = dbSubject.getAllSubjects(temp);
        subjectNamesList = new ArrayList<>();
    }

    private void buildSubjectList() {
        //Fill subjectList with subject
        for (Subject s: subjectList) {
            subjectNamesList.add(s.getSubjectName());
        }
    }

    private void buildArrayAdapter() {
        //Create an ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,subjectNamesList);
        //Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        classSpinner.setAdapter(spinnerAdapter);
    }

    private void buildButtons() {
        buttonCancel.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            Fragment fragment = new AssignmentsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", temp);
            fragment.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        });

        buttonAddAssignment.setOnClickListener(v -> {
            String aName = editTextAssignmentName.getText().toString();
            String dueDate = editTextAssignmentDueDate.getText().toString();
            String dueTime = editTextAssignmentDueTime.getText().toString();
            String aNotes = editTextNotes.getText().toString();
            String sName = (String) classSpinner.getSelectedItem();
            int sID = dbSubject.getSubject(temp, sName).getSubjectID();
            if (aName.isEmpty()) {
                Toast.makeText(getContext(), "Please enter an assignment name.",
                        Toast.LENGTH_SHORT).show();
            }else if (dueDate.isEmpty()) {
                Toast.makeText(getContext(), "Please enter due date.",
                        Toast.LENGTH_SHORT).show();
            }else if (dueTime.isEmpty()) {
                Toast.makeText(getContext(), "Please enter due time.",
                        Toast.LENGTH_SHORT).show();
            }else {
                try {
                    if (dbAsgmtInc.getAsgmtInc(temp, aName).getAsgmtName()
                            .contains(aName)) {
                        Toast.makeText(getContext(), "Assignment with that name " +
                                        "already exists.",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e) {
                    AsgmtIncomplete inputAssignment;
                    if (aNotes.isEmpty()) {
                        inputAssignment = new AsgmtIncomplete(sID, temp, aName, null,
                                R.drawable.ic_baseline_assignment_24, dueDate, dueTime,
                                0);
                    } else {
                        inputAssignment = new AsgmtIncomplete(sID, temp, aName, aNotes,
                                R.drawable.ic_baseline_assignment_24, dueDate, dueTime,
                                0);
                    }
                    dbAsgmtInc.insertAsgmtInc(inputAssignment);
                    if (dbAsgmtInc.getAsgmtInc(temp, aName).getAsgmtName()
                            .contains(aName)) {
                        Toast.makeText(getContext(), dbAsgmtInc.getAsgmtInc
                                        (temp, aName).getAsgmtName() + " added",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Something went wrong.",
                                Toast.LENGTH_SHORT).show();
                    }
                    Fragment fragment = new AssignmentsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", temp);
                    fragment.setArguments(bundle);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            fragment).commit();
                }
            }
        });

        editTextAssignmentDueDate.setOnClickListener(new View.OnClickListener() {
            final Calendar calendar = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (view, year, month, dayOfMonth) -> {
                            calendar.set(year,month,dayOfMonth);
                            editTextAssignmentDueDate.setText(DateFormat.format
                                    ("MMM d, yyyy", calendar));
                        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        editTextAssignmentDueTime.setOnClickListener(new View.OnClickListener() {
            final Calendar calendar = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        (view, hourOfDay, minute) -> {
                            calendar.set(0,0,0, hourOfDay, minute);
                            editTextAssignmentDueTime.setText(DateFormat.format(
                                    "hh:mm aa",calendar));
                        },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),
                        false);
                timePickerDialog.show();
            }
        });

    }

}

