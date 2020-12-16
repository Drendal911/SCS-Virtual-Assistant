package com.example.virtualassistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DAO.AsgmtCompDAO;
import DAO.AsgmtIncDAO;
import DAO.SubjectDAO;
import DAO.TeacherDAO;
import Database.VADatabase;
import Entity.AsgmtComplete;
import Entity.AsgmtIncomplete;
import Entity.Subject;
import Entity.Teacher;
import Utility.CardViewAdapter;
import Utility.CardViewItem;

import static android.content.ContentValues.TAG;

public class SearchFragment extends Fragment {
    //The adapter is the bridge between the data and the RecyclerView
    private CardViewAdapter mAdapter;
    VADatabase database;
    TeacherDAO dbTeacher;
    SubjectDAO dbSubject;
    AsgmtCompDAO dbAsgmtComp;
    AsgmtIncDAO dbAsgmtInc;
    int temp;
    EditText editTextSearch;
    ArrayList<CardViewItem> filteredList;
    ArrayList<CardViewItem> cardViewItemArrayList;

    public SearchFragment() {
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextSearch = getView().findViewById(R.id.editTextSearch);

        buildSearchList();
        buildRecyclerView();
        buildButtons();
        cardViewItemArrayList.clear();
        buildAdapter();
    }



    /*METHODS **************************************************************************************
     ************************************************************************************************
     ***********************************************************************************************/


    private void checkNull() {
        if (getArguments() != null) {
            temp = getArguments().getInt("USER_ID");

            database = Room.databaseBuilder(getContext(), VADatabase .class, "Database")
                    .allowMainThreadQueries().build();
            dbTeacher = database.getTeacherDAO();
            dbSubject = database.getSubjectDAO();
            dbAsgmtInc = database.getAsgmtIncDAO();
            dbAsgmtComp = database.getAsgmtCompDAO();
        }
    }

    private void buildButtons() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches("")) {
                    buildSearchList();
                    buildRecyclerView();
                    buildAdapter();
                    cardViewItemArrayList.clear();
                }else {
                    buildSearchList();
                    filter(s.toString());
                }
            }
        });
    }

    private void filter(String string) {
        filteredList = new ArrayList<>();
        for (CardViewItem item : cardViewItemArrayList) {
            if (item.getText1().toUpperCase().contains(string.toUpperCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }

    private void buildRecyclerView() {
        RecyclerView mRecyclerView = getView().findViewById(R.id.assignmentRecyclerView);
        //Create the layout manager and adapter
        //The layout manager aligns the single items in the list
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CardViewAdapter(cardViewItemArrayList);
        //Pass created layout manager and adapter to the recyclerview
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void buildSearchList() {
        cardViewItemArrayList = new ArrayList<>();
        List<Teacher> teachers = dbTeacher.getAllTeachers(temp);
        for (Teacher t : teachers){
            cardViewItemArrayList.add(new CardViewItem(t.getTeacherIcon(), t.getTeacherName(),
                    t.getTeacherEmail()));
        }
        List<Subject> subjects = dbSubject.getAllSubjects(temp);
        for (Subject s : subjects){
            cardViewItemArrayList.add(new CardViewItem(s.getIcon(), s.getSubjectName(),
                    s.getSubjectTime()));
        }
        List<AsgmtIncomplete> assignmentsInc = dbAsgmtInc.getAllAsgmtInc(temp);
        List<AsgmtComplete> assignmentsComp = dbAsgmtComp.getAllAsgmtComp(temp);
        for (AsgmtIncomplete a : assignmentsInc){
            String line2 = a.getAsgmtDueDate() + " " + a.getAsgmtDueTime() + " " +
                    a.getAsgmtCompPercent() + "%";
            cardViewItemArrayList.add(new CardViewItem(a.getAsgmtIcon(), a.getAsgmtName(), line2));
        }
        for (AsgmtComplete a : assignmentsComp){
            String line2 = "Completed on " + a.getAsgmtCompleteDate();
            cardViewItemArrayList.add(new CardViewItem(a.getAsgmtIcon(), a.getAsgmtName(), line2));
        }
    }

    private void buildAdapter() {
        //Set listener to card view
        mAdapter.setOnItemClickListener(new CardViewAdapter.OnitemClickListener() {
            @Override
            public void onItemClick(int position) {
                CardViewItem pos;
                try {
                    pos = filteredList.get(position);
                }catch (NullPointerException exception) {
                    pos = cardViewItemArrayList.get(position);
                }
                try {
                    Teacher teacher = dbTeacher.getTeacher(temp, pos.getText1());
                    if (!teacher.getTeacherName().isEmpty()) {
                        Fragment mFragment = new TeacherFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("USER_ID", temp);
                        bundle.putInt("TEACHER_ID", teacher.getTeacherID());
                        mFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                mFragment).commit();
                    }
                }catch (NullPointerException e) {
                    Log.d(TAG, "onItemClick: Not a Teacher.");
                }
                try {
                    Subject subject = dbSubject.getSubject(temp, pos.getText1());
                    if (!subject.getSubjectName().isEmpty()) {
                        Fragment mFragment = new SubjectFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("USER_ID", temp);
                        bundle.putInt("SUBJECT_ID", subject.getSubjectID());
                        mFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                mFragment).commit();
                    }
                } catch (NullPointerException ex) {
                    Log.d(TAG, "onItemClick: Not a Class.");
                }
                try {
                    AsgmtIncomplete assignment = dbAsgmtInc.getAsgmtInc(temp, pos.getText1());
                    Fragment mFragment = new AssignmentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", temp);
                    bundle.putInt("ASSIGNMENT_ID", assignment.getAsgmtID());
                    bundle.putString("ASSIGNMENT_NAME", assignment.getAsgmtName());
                    mFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            mFragment).commit();
                }catch (NullPointerException exception){
                    Log.d(TAG, "onItemClick: Not an Incomplete Assignment.");
                }
                try {
                    AsgmtComplete assignment = dbAsgmtComp.getAsgmtComp(temp, pos.getText1());
                    Fragment mFragment = new AssignmentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", temp);
                    bundle.putInt("ASSIGNMENT_ID", assignment.getAsgmtID());
                    bundle.putString("ASSIGNMENT_NAME", assignment.getAsgmtName());
                    mFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            mFragment).commit();
                }catch (NullPointerException exc) {
                    Log.d(TAG, "onItemClick: Not a Completed Assignment.");
                }
            }

            @Override
            public void onEditClick(int position) {
                CardViewItem pos;
                try {
                    pos = filteredList.get(position);
                }catch (NullPointerException exception) {
                    pos = cardViewItemArrayList.get(position);
                }
                try {
                    Teacher teacher = dbTeacher.getTeacher(temp, pos.getText1());
                    if (!teacher.getTeacherName().isEmpty()) {
                        Fragment mFragment = new EditTeacherFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("USER_ID", temp);
                        bundle.putInt("TEACHER_ID", teacher.getTeacherID());
                        mFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                mFragment).commit();
                    }
                }catch (NullPointerException e) {
                    try {
                        Subject subject = dbSubject.getSubject(temp, pos.getText1());
                        if (!subject.getSubjectName().isEmpty()) {
                            Fragment mFragment = new EditSubjectFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("USER_ID", temp);
                            bundle.putInt("SUBJECT_ID", subject.getSubjectID());
                            mFragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    mFragment).commit();
                        }
                    } catch (NullPointerException ex) {
                        Log.d(TAG, "onEditClick: Not an Incomplete Assignment.");
                    }
                    try {
                        AsgmtIncomplete assignment = dbAsgmtInc.getAsgmtInc(temp, pos.getText1());
                        Fragment mFragment = new EditAssignmentFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("USER_ID", temp);
                        bundle.putInt("ASSIGNMENT_ID", assignment.getAsgmtID());
                        bundle.putString("ASSIGNMENT_NAME", assignment.getAsgmtName());
                        mFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                mFragment).commit();
                    }catch (NullPointerException exception) {
                        Log.d(TAG, "onEditClick: Not an Incomplete Assignment.");
                    }
                    try {
                        AsgmtComplete assignment = dbAsgmtComp.getAsgmtComp(temp, pos.getText1());
                        Toast.makeText(getContext(), "Completed assignments can't be edited.",
                                Toast.LENGTH_SHORT).show();
                    }catch (NullPointerException exception) {
                        Log.d(TAG, "onEditClick: Not a Completed Assignment.");
                    }
                }
            }

            @Override
            public void onDeleteClick(int position) {
                CardViewItem pos;
                try {
                    pos = filteredList.get(position);
                }catch (NullPointerException exception) {
                    pos = cardViewItemArrayList.get(position);
                }
                try {
                    Teacher teacher = dbTeacher.getTeacher(temp, pos.getText1());
                    List<Subject> subjectList;
                    subjectList = dbSubject.getSubjectWithTeacher(temp, teacher.getTeacherID());
                    if (subjectList.size() > 0) {
                        Toast.makeText(getContext(),"Teacher is currently assigned to a class",
                                Toast.LENGTH_LONG).show();
                    }else if (subjectList.size() == 0) {
                        Toast.makeText(getContext(), teacher.getTeacherName() +
                                " deleted.", Toast.LENGTH_SHORT).show();
                        dbTeacher.deleteTeacher(teacher);
                        Fragment mFragment = new SearchFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("USER_ID", temp);
                        mFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                mFragment).commit();
                    }
                }catch (NullPointerException e) {
                    try {
                        Subject subject = dbSubject.getSubject(temp, pos.getText1());
                        List<AsgmtIncomplete> asgmtListInc;
                        List<AsgmtComplete> asgmtListComp;
                        asgmtListInc = dbAsgmtInc.getAsgmtIncWithSubject
                                (temp,subject.getSubjectID());
                        asgmtListComp = dbAsgmtComp.getAsgmtCompWithSubject
                                (temp,subject.getSubjectID());
                        if (asgmtListInc.size() > 0 || asgmtListComp.size() > 0) {
                            Toast.makeText(getContext(), "Assignments must be removed from " +
                                    subject.getSubjectName() + " first.", Toast.LENGTH_LONG).show();
                        }else if (asgmtListInc.size() == 0 && asgmtListComp.size() == 0) {
                            Toast.makeText(getContext(), subject.getSubjectName() +
                                    " deleted.", Toast.LENGTH_SHORT).show();
                            dbSubject.deleteSubject(subject);
                            Fragment mFragment = new SearchFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("USER_ID", temp);
                            mFragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    mFragment).commit();
                        }
                    }catch (NullPointerException ex) {
                        try{
                            AsgmtIncomplete aDelete = dbAsgmtInc.getAsgmtInc(temp, pos.getText1());
                            dbAsgmtInc.deleteAsgmtInc(aDelete);
                            Toast.makeText(getContext(),aDelete.getAsgmtName() +
                                    " has been deleted.", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new SearchFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("USER_ID", temp);
                            fragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    fragment).commit();
                        }catch (NullPointerException exception) {
                            AsgmtComplete aDelete = dbAsgmtComp.getAsgmtComp(temp, pos.getText1());
                            dbAsgmtComp.deleteAsgmtComp(aDelete);
                            Toast.makeText(getContext(),aDelete.getAsgmtName() +
                                    " has been deleted.", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new SearchFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("USER_ID", temp);
                            fragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    fragment).commit();
                        }

                    }
                }
            }

        });
    }

}