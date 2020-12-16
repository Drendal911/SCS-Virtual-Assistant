package com.example.virtualassistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import DAO.SubjectDAO;
import DAO.TeacherDAO;
import Database.VADatabase;
import Entity.Subject;
import Entity.Teacher;
import Utility.CardViewAdapter;
import Utility.CardViewItem;


public class TeachersFragment extends Fragment {
    private SubjectDAO dbSubject;
    private TeacherDAO dbTeacher;
    private int temp;
    private List<Subject> subjectList;
    FloatingActionButton teacherFloatingActionButton;

    public TeachersFragment() {
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
        return inflater.inflate(R.layout.fragment_teachers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        teacherFloatingActionButton = Objects.requireNonNull(getView()).findViewById
                (R.id.teacherFloatingActionButton);
        buildAdapter();
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
            dbTeacher = database.getTeacherDAO();
        }
    }

    private void buildAdapter(){
        //This is the list that will get passed to the adapter and fill the cardview
        ArrayList<CardViewItem> cardViewItemArrayList = new ArrayList<>();

        List<Teacher> teachers = dbTeacher.getAllTeachers(temp);
        for (Teacher t : teachers){
            cardViewItemArrayList.add(new CardViewItem(t.getTeacherIcon(), t.getTeacherName(),
                    t.getTeacherEmail()));
        }

        RecyclerView mRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.teacherRecyclerView);
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
                Teacher teacher = dbTeacher.getTeacher(temp, pos.getText1());
                Fragment mFragment = new TeacherFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("USER_ID", temp);
                bundle.putInt("TEACHER_ID", teacher.getTeacherID());
                mFragment.setArguments(bundle);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mFragment).commit();
            }

            @Override
            public void onEditClick(int position) {
                CardViewItem pos = cardViewItemArrayList.get(position);
                Teacher teacher = dbTeacher.getTeacher(temp, pos.getText1());
                Fragment mFragment = new EditTeacherFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("USER_ID", temp);
                bundle.putInt("TEACHER_ID", teacher.getTeacherID());
                mFragment.setArguments(bundle);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mFragment).commit();
            }

            @Override
            public void onDeleteClick(int position) {
                CardViewItem pos = cardViewItemArrayList.get(position);
                Teacher teacher = dbTeacher.getTeacher(temp, pos.getText1());
                subjectList = new ArrayList<>();
                subjectList = dbSubject.getSubjectWithTeacher(temp, teacher.getTeacherID());
                if (subjectList.size() > 0) {
                    Toast.makeText(getContext(),"Teacher is currently assigned to a class",
                            Toast.LENGTH_LONG).show();
                }else {
                    subjectList.size();
                    Toast.makeText(getContext(), teacher.getTeacherName() +
                            " deleted.", Toast.LENGTH_SHORT).show();
                    dbTeacher.deleteTeacher(teacher);
                    Fragment mFragment = new TeachersFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("USER_ID", temp);
                    mFragment.setArguments(bundle);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            mFragment).commit();
                }
            }
        });
    }

    private void buildButtons() {
        teacherFloatingActionButton.setOnClickListener(v -> {
            Toast.makeText(getContext(),"Add Teacher", Toast.LENGTH_SHORT).show();
            Fragment mFragment = new AddTeacherFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", temp);
            mFragment.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mFragment).commit();            });
    }

}