package com.example.virtualassistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import DAO.AsgmtCompDAO;
import DAO.AsgmtIncDAO;
import DAO.ReminderDAO;
import Database.VADatabase;
import Entity.Asgmt;
import Entity.AsgmtComplete;
import Entity.AsgmtIncomplete;
import Utility.CardViewAdapter;
import Utility.CardViewItem;


public class AssignmentsFragment extends Fragment {
    private ReminderDAO dbReminder;
    private AsgmtIncDAO dbAsgmtInc;
    private AsgmtCompDAO dbAsgmtComp;
    private int temp;

    FloatingActionButton assignmentFloatingActionButton;

    public AssignmentsFragment() {
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
        return inflater.inflate(R.layout.fragment_assignments, container, false);
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
            dbAsgmtInc = database.getAsgmtIncDAO();
            dbAsgmtComp = database.getAsgmtCompDAO();
            dbReminder = database.getReminderDAO();
        }
    }

    private void setItemIDs() {
        assignmentFloatingActionButton = Objects.requireNonNull(getView()).findViewById
                (R.id.assignmentFloatingActionButton);
    }

    private void buildButtons() {
        assignmentFloatingActionButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Add Assignment", Toast.LENGTH_SHORT).show();
            Fragment fragment = new AddAssignmentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", temp);
            fragment.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
        });
    }

    private void buildArrayAdapter() {
        ArrayList<CardViewItem> cardViewItemArrayList = new ArrayList<>();
        List<AsgmtIncomplete> assignmentsInc = dbAsgmtInc.getAllAsgmtInc(temp);
        for (Asgmt a : assignmentsInc){
            int aID = a.getAsgmtID();
            int aIcon = dbAsgmtInc.getAsgmtIncIcon(aID);
            String aName = dbAsgmtInc.getAsgmtIncName(temp, aID);
            String line2 = dbAsgmtInc.getAsgmtIncDate(temp, aID) + " " +
                    dbAsgmtInc.getAsgmtIncTime(temp, aID) + " " + dbAsgmtInc.getAsgmtIncCompPercent
                    (temp, aID) + "% complete";
            cardViewItemArrayList.add(new CardViewItem(aIcon, aName, line2));
        }
        List<AsgmtComplete> assignmentsComp = dbAsgmtComp.getAllAsgmtComp(temp);
        for (Asgmt a : assignmentsComp){
            int aID = a.getAsgmtID();
            int aIcon = dbAsgmtComp.getAsgmtCompIcon(aID);
            String aName = dbAsgmtComp.getAsgmtCompName(temp, aID);
            String line2 = "Completed on " + dbAsgmtComp.getAsgmtCompDate(temp, aID);
            cardViewItemArrayList.add(new CardViewItem(aIcon, aName, line2));
        }

        RecyclerView mRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.assignmentRecyclerView);
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
            CardViewItem pos;
            @Override
            public void onItemClick(int position) {
                pos = cardViewItemArrayList.get(position);
                try {
                    AsgmtIncomplete tempAsgmt = dbAsgmtInc.getAsgmtInc(temp, pos.getText1());
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", temp);
                    bundle.putInt("ASSIGNMENT_ID", tempAsgmt.getAsgmtID());
                    bundle.putString("ASSIGNMENT_NAME", tempAsgmt.getAsgmtName());
                    Fragment fragment = new AssignmentFragment();
                    fragment.setArguments(bundle);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            fragment).commit();
                }catch (NullPointerException e){
                    AsgmtComplete tempAsgmt = dbAsgmtComp.getAsgmtComp(temp, pos.getText1());
                    Fragment fragment = new AssignmentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", temp);
                    bundle.putInt("ASSIGNMENT_ID", tempAsgmt.getAsgmtID());
                    bundle.putString("ASSIGNMENT_NAME", tempAsgmt.getAsgmtName());
                    fragment.setArguments(bundle);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            fragment).commit();
                }

            }

            @Override
            public void onEditClick(int position) {
                pos = cardViewItemArrayList.get(position);
                try {
                    AsgmtComplete tempCompAsgmt = dbAsgmtComp.getAsgmtComp(temp, pos.getText1());
                    if (!tempCompAsgmt.getAsgmtCompleteDate().isEmpty()){
                        Toast.makeText(getContext(), "Completed assignments can't be edited.",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (NullPointerException e) {
                    AsgmtIncomplete tempAsgmt = dbAsgmtInc.getAsgmtInc(temp, pos.getText1());
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", temp);
                    bundle.putInt("ASSIGNMENT_ID", tempAsgmt.getAsgmtID());
                    bundle.putString("ASSIGNMENT_NAME", tempAsgmt.getAsgmtName());
                    Fragment fragment = new EditAssignmentFragment();
                    fragment.setArguments(bundle);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            fragment).commit();
                }
            }

            @Override
            public void onDeleteClick(int position) {
                pos = cardViewItemArrayList.get(position);
                try{
                    AsgmtIncomplete aDelete = dbAsgmtInc.getAsgmtInc(temp, pos.getText1());
                    dbReminder.deleteReminders(temp, aDelete.getAsgmtID());
                    dbAsgmtInc.deleteAsgmtInc(aDelete);
                    Toast.makeText(getContext(),aDelete.getAsgmtName() + " has been deleted.",
                            Toast.LENGTH_SHORT).show();

                    Fragment fragment = new AssignmentsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", temp);
                    fragment.setArguments(bundle);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            fragment).commit();
                }catch (NullPointerException e) {
                    AsgmtComplete aDelete = dbAsgmtComp.getAsgmtComp(temp, pos.getText1());
                    dbReminder.deleteReminders(temp, aDelete.getAsgmtID());
                    dbAsgmtComp.deleteAsgmtComp(aDelete);
                    Toast.makeText(getContext(),aDelete.getAsgmtName() + " has been deleted.",
                            Toast.LENGTH_SHORT).show();

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
    }

}