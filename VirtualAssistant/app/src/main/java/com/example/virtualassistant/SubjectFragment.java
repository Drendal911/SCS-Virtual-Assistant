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
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import DAO.AsgmtIncDAO;
import DAO.SubjectDAO;
import DAO.TeacherDAO;
import Database.VADatabase;
import Entity.AsgmtIncomplete;


public class SubjectFragment extends Fragment {
    private SubjectDAO dbSubject;
    private TeacherDAO dbTeacher;
    private AsgmtIncDAO dbAsgmtInc;
    private int temp, subjectID;
    TextView subjectStartTime, subjectTeacher, textViewTeacherNameDesc,
            textViewIncAssDesc, textViewStartTimeDesc, subjectNameTextView;
    EditText editTextSubjectTextMultiLine;
    Button buttonDoneSubject;
    private List<AsgmtIncomplete> subjectAssignmentsList;

    public SubjectFragment() {
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
        return inflater.inflate(R.layout.fragment_subject, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setItemIDs();
        buildSubjectAssignmentsList();
        buildButtons();
    }



    /*METHODS **************************************************************************************
     ************************************************************************************************
     ***********************************************************************************************/


    private void checkNull() {
        if (getArguments() != null) {
            VADatabase database = Room.databaseBuilder(Objects.requireNonNull(getContext()), VADatabase.class, "Database")
                    .allowMainThreadQueries().build();
            temp = getArguments().getInt("USER_ID");
            subjectID = getArguments().getInt("SUBJECT_ID");

            dbSubject = database.getSubjectDAO();
            dbTeacher = database.getTeacherDAO();
            dbAsgmtInc = database.getAsgmtIncDAO();
        }
    }

    private void setItemIDs() {
        int tID = dbSubject.getSubjectTeacherID(temp, subjectID);
        subjectNameTextView = Objects.requireNonNull(getView()).findViewById(R.id.subjectNameTextView);
        subjectStartTime = getView().findViewById(R.id.subjectStartTime);
        subjectTeacher = getView().findViewById(R.id.subjectTeacher);
        editTextSubjectTextMultiLine = getView().findViewById
                (R.id.editTextSubjectTextMultiLine);
        textViewStartTimeDesc = getView().findViewById(R.id.textViewStartTimeDesc);
        textViewTeacherNameDesc = getView().findViewById(R.id.textViewTeacherNameDesc);
        textViewIncAssDesc = getView().findViewById(R.id.textViewIncAssDesc);
        buttonDoneSubject = getView().findViewById(R.id.buttonDoneSubject);
        subjectAssignmentsList = dbAsgmtInc.getAllAsgmtInc(temp);

        subjectNameTextView.setText(dbSubject.getSubjectName(temp, subjectID));
        subjectStartTime.setText(dbSubject.getSubjectTime(temp, subjectID));
        subjectTeacher.setText(dbTeacher.getTeacherName(tID));
    }

    private void buildSubjectAssignmentsList() {
        int count = subjectAssignmentsList.size();
        if (count > 0) {
            StringBuilder aNamesDisplay = new StringBuilder();
            while (count > 0) {
                aNamesDisplay.append(subjectAssignmentsList.get(count - 1).getAsgmtName()).append(", ");
                count--;
            }
            String adjustedString = aNamesDisplay.substring(0,aNamesDisplay.length()-2);
            editTextSubjectTextMultiLine.setText(adjustedString);
        }
    }

    private void buildButtons() {
        buttonDoneSubject.setOnClickListener(v -> {
            Fragment mFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", temp);
            bundle.putInt("SUBJECT_ID", subjectID);
            mFragment.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mFragment).commit();
        });
    }

}