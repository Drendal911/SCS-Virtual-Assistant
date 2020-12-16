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

import java.util.Objects;

import DAO.AsgmtIncDAO;
import DAO.SubjectDAO;
import Database.VADatabase;

public class AssignmentFragment extends Fragment {
    private AsgmtIncDAO dbAsgmtInc;
    private SubjectDAO dbSubject;
    private String asgmtName;
    private int temp, assignmentID;

    EditText editTextEditAssignmentName, editTextEditAssignmentSubject, editTextEditAssignmentStatus,
            editTextEditAssignmentDueDate, editTextEditAssignmentDueTime, editTextTextMultiLine;
    Button buttonAssignmentDone;

    public AssignmentFragment() {
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
        return inflater.inflate(R.layout.fragment_assignment, container, false);
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
            assignmentID = getArguments().getInt("ASSIGNMENT_ID");
            asgmtName = getArguments().getString("ASSIGNMENT_NAME");
            VADatabase database = Room.databaseBuilder(Objects.requireNonNull(getContext()), VADatabase.class, "Database")
                    .allowMainThreadQueries().build();
            dbAsgmtInc = database.getAsgmtIncDAO();
            dbSubject = database.getSubjectDAO();
        }
    }

    private void setItemIDs() {
        editTextEditAssignmentName = Objects.requireNonNull(getView()).findViewById(R.id.editTextEditAssignmentName);
        editTextEditAssignmentSubject = getView().findViewById(R.id.editTextEditAssignmentSubject);
        editTextEditAssignmentDueDate = getView().findViewById(R.id.editTextEditAssignmentDueDate);
        editTextEditAssignmentDueTime = getView().findViewById(R.id.editTextEditAssignmentDueTime);
        editTextEditAssignmentStatus = getView().findViewById(R.id.editTextEditAssignmentStatus);
        editTextTextMultiLine = getView().findViewById(R.id.editTextTextMultiLine);
        buttonAssignmentDone = getView().findViewById(R.id.buttonAssignmentDone);

        editTextEditAssignmentName.setText(asgmtName);
        editTextEditAssignmentSubject.setText(dbSubject.getSubjectName(temp, dbAsgmtInc.
                getAsgmtIncSubjectID(temp, assignmentID)));
        editTextEditAssignmentDueDate.setText(dbAsgmtInc.getAsgmtIncDate(temp, assignmentID));
        editTextEditAssignmentDueTime.setText(dbAsgmtInc.getAsgmtIncTime(temp, assignmentID));
        editTextTextMultiLine.setText(dbAsgmtInc.getAsgmtIncNotes(temp, assignmentID));
        try {
            int compPercent = dbAsgmtInc.getAsgmtInc(temp, assignmentID, asgmtName).
                    getAsgmtCompPercent();
            if (!String.valueOf(compPercent).isEmpty()) {
                editTextEditAssignmentStatus.setText(compPercent + "% complete");
            }else {
                editTextEditAssignmentStatus.setText("0% complete");
            }

        }catch (NullPointerException e) {
            editTextEditAssignmentStatus.setText("100% complete");
        }
    }

    private void buildButtons() {
        buttonAssignmentDone.setOnClickListener(v -> {
            Fragment frag = new AssignmentsFragment();
            Bundle bun = new Bundle();
            bun.putInt("USER_ID", temp);
            frag.setArguments(bun);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    frag).commit();
        });
    }
}