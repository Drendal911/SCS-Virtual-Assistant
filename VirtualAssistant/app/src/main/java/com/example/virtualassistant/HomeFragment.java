package com.example.virtualassistant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import DAO.AsgmtCompDAO;
import DAO.AsgmtIncDAO;
import DAO.ReminderDAO;
import DAO.SubjectDAO;
import Database.VADatabase;
import Entity.AsgmtComplete;
import Entity.AsgmtIncomplete;
import Entity.Reminder;
import Entity.Subject;
import Utility.CardViewAdapter;
import Utility.CardViewItem;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
    private int temp;
    private SubjectDAO dbSubject;
    private ReminderDAO dbReminder;
    private AsgmtIncDAO dbAsgmtInc;
    private AsgmtCompDAO dbAsgmtComp;

    TextView textViewDate;
    FloatingActionButton addSubjectFloatingActionButton, logoutFloatingActionButton;
    ListView smallAssignmentListView;
    List<AsgmtIncomplete> asgmtListInc;
    List<AsgmtComplete> asgmtListComp;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNull();
        deleteOldReminders();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setItemIDs();
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
            dbSubject = database.getSubjectDAO();
            dbAsgmtInc = database.getAsgmtIncDAO();
            dbReminder = database.getReminderDAO();
            dbAsgmtComp = database.getAsgmtCompDAO();
        }
    }


    private void setItemIDs() {
        addSubjectFloatingActionButton = Objects.requireNonNull(getView()).findViewById
                (R.id.addSubjectFloatingActionButton);
        logoutFloatingActionButton = getView().findViewById
                (R.id.logoutFloatingActionButton);
        smallAssignmentListView = getView().findViewById(R.id.smallAssignmentListView);
        //Sets the date in a small text view
        textViewDate = getView().findViewById(R.id.textViewDate);
    }

    //Delete old assignment reminders
    private void deleteOldReminders() {
        //Get current date
        Calendar cal = Calendar.getInstance();
        //Get list of all reminders in the database
        List<Reminder> reminders = dbReminder.getAllReminders(temp);
        //Get date for each reminder
        for (Reminder r: reminders) {
            //Get date for each reminder
            int year = Integer.parseInt(r.getDate().substring(0,4));
            int day = Integer.parseInt(r.getDate().substring(5));
            //Check if reminder date is older than current date
            if (cal.get(Calendar.YEAR) >= year) {
                if (cal.get(Calendar.YEAR) > year) {
                    dbReminder.deleteReminder(r);
                }
                if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.DAY_OF_YEAR) > day) {
                    //Delete old reminders
                    dbReminder.deleteReminder(r);
                }
            }
        }
    }

    private void buildButtons() {
        addSubjectFloatingActionButton.setOnClickListener(v -> {
            Toast.makeText(getContext(),"Add Class", Toast.LENGTH_SHORT).show();
            Fragment mFragment = new AddSubjectFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", temp);
            mFragment.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mFragment).commit();
        });

        logoutFloatingActionButton.setOnClickListener(v -> {
            Toast.makeText(getContext(),"Logout", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
        });
    }

    private void buildArrayAdapter() {
        Calendar calendar = Calendar.getInstance();
        String cal = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        textViewDate.setText(cal);
        //This is the list that will get passed to the adapter and fill the card view
        ArrayList<CardViewItem> cardViewItemArrayList = new ArrayList<>();
        ArrayList<String> assignmentArrayList = new ArrayList<>();

        List<AsgmtIncomplete> tempAssign = dbAsgmtInc.getAsgmtIncDueToday(temp, cal);
        for (AsgmtIncomplete a: tempAssign) {
            String tString = dbAsgmtInc.getAsgmtIncName(temp, a.getAsgmtID());
            assignmentArrayList.add(tString);
        }

        if (assignmentArrayList.isEmpty()) {
            assignmentArrayList.add("Nothing Due Today");
        }

        ArrayAdapter listViewArrayAdapter = new ArrayAdapter(getContext(),
                R.layout.white_list_view, assignmentArrayList);
        smallAssignmentListView.setAdapter(listViewArrayAdapter);

        List<Subject> subjects = dbSubject.getAllSubjects(temp);
        for (Subject s : subjects){
            int subjectID = s.getSubjectID();
            int tempIcon = dbSubject.getIcon(temp, subjectID);
            String sName = dbSubject.getSubjectName(temp, subjectID);
            String sTime = dbSubject.getSubjectTime(temp, subjectID);
            cardViewItemArrayList.add(new CardViewItem(tempIcon, sName, sTime));
        }

        RecyclerView mRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.subjectRecyclerView);
        //Setting setHasFixedSize true (if recyclerview won't change size) will increase performance
        mRecyclerView.setHasFixedSize(true);
        //Create the layout manager and adapter
        //The layout manager aligns the single items in the list
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        //The adapter is the bridge between the data and the RecyclerView
        CardViewAdapter mAdapter = new CardViewAdapter(cardViewItemArrayList);
        //Pass created layout manager and adapter to the recyclerview
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //Set listener to card view
        mAdapter.setOnItemClickListener(new CardViewAdapter.OnitemClickListener() {
            @Override
            public void onItemClick(int position) {
                CardViewItem pos = cardViewItemArrayList.get(position);
                int subjectID = dbSubject.getSubject(temp, pos.getText1()).getSubjectID();
                Fragment mFragment = new SubjectFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("USER_ID", temp);
                bundle.putInt("SUBJECT_ID", subjectID);
                mFragment.setArguments(bundle);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mFragment).commit();
            }

            @Override
            public void onEditClick(int position) {
                CardViewItem pos = cardViewItemArrayList.get(position);
                int subjectID = dbSubject.getSubject(temp, pos.getText1()).getSubjectID();
                Fragment mFragment = new EditSubjectFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("USER_ID", temp);
                bundle.putInt("SUBJECT_ID", subjectID);
                mFragment.setArguments(bundle);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mFragment).commit();
            }

            @Override
            public void onDeleteClick(int position) {
                CardViewItem pos = cardViewItemArrayList.get(position);
                Subject subject = dbSubject.getSubject(temp, pos.getText1());
                asgmtListInc = new ArrayList<>();
                asgmtListComp = new ArrayList<>();
                try {
                    asgmtListInc = dbAsgmtInc.getAsgmtIncWithSubject(temp, subject.getSubjectID());
                }catch (NullPointerException e) {
                    Log.d(TAG, "onDeleteClick: asgmtListInc null");
                }
                try {
                    asgmtListComp = dbAsgmtComp.getAsgmtCompWithSubject(temp, subject.getSubjectID());
                }catch (NullPointerException e) {
                    Log.d(TAG, "onDeleteClick: asgmtListComp null");
                }

                if (asgmtListInc.size() > 0 || asgmtListComp.size() > 0) {
                    Toast.makeText(getContext(), "Assignments must be removed from " +
                            subject.getSubjectName() + " first.", Toast.LENGTH_LONG).show();
                }else if (asgmtListInc.size() == 0 && asgmtListComp.size() == 0) {
                    Toast.makeText(getContext(), subject.getSubjectName() +
                            " deleted.", Toast.LENGTH_SHORT).show();
                    dbSubject.deleteSubject(subject);
                    Fragment mFragment = new HomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", temp);
                    mFragment.setArguments(bundle);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            mFragment).commit();
                }else {
                    Toast.makeText(getContext(), "Something went wrong.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        smallAssignmentListView.setOnItemClickListener((parent, view, position, id) -> {
            String pos = assignmentArrayList.get(position);
            int aID = dbAsgmtInc.getAsgmtInc(temp,pos).getAsgmtID();
            Toast.makeText(getContext(), pos + " clicked.", Toast.LENGTH_SHORT).show();
            Fragment fragment = new AssignmentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", temp);
            bundle.putInt("ASSIGNMENT_ID", aID);
            bundle.putString("ASSIGNMENT_NAME", dbAsgmtInc.getAsgmtIncName(temp, aID));
            fragment.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
        });
    }

}
