package com.example.virtualassistant;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import DAO.AsgmtCompDAO;
import DAO.AsgmtIncDAO;
import DAO.ReminderDAO;
import DAO.SubjectDAO;
import Database.VADatabase;
import Entity.AsgmtComplete;
import Entity.AsgmtIncomplete;
import Entity.Reminder;
import Entity.Subject;


public class EditAssignmentFragment extends Fragment {
    private AsgmtIncDAO dbAsgmtInc;
    private AsgmtCompDAO dbAsgmtComp;
    private SubjectDAO dbSubject;
    private ReminderDAO dbReminder;
    private String calDate;
    private String asgmtName;
    private int temp, assignmentID;

    EditText editTextEditAssignmentName2, editTextEditAssignmentSubject2,
            editTextEditAssignmentDueDate2, editTextEditAssignmentDueTime2, editTextTextMultiLine2,
            editTextEditAssignmentStatus2;
    Button buttonAssignmentCancel2, buttonAssignmentSave;
    FloatingActionButton editAssignmentNotificationButton;

    public EditAssignmentFragment() {
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
        return inflater.inflate(R.layout.fragment_edit_assignment, container, false);
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
        if (getArguments() !=null) {
            temp = getArguments().getInt("USER_ID");
            assignmentID = getArguments().getInt("ASSIGNMENT_ID");
            asgmtName = getArguments().getString("ASSIGNMENT_NAME");
            VADatabase database = Room.databaseBuilder(Objects.requireNonNull(getContext()), VADatabase.class, "Database")
                    .allowMainThreadQueries().build();
            dbAsgmtInc = database.getAsgmtIncDAO();
            dbAsgmtComp = database.getAsgmtCompDAO();
            dbSubject = database.getSubjectDAO();
            dbReminder = database.getReminderDAO();
        }
    }

    private void setItemIDs() {
        editTextEditAssignmentName2 = Objects.requireNonNull(getView()).findViewById(R.id.editTextEditAssignmentName2);
        editTextEditAssignmentSubject2 = getView().findViewById(R.id.editTextEditAssignmentSubject2);
        editTextEditAssignmentDueDate2 = getView().findViewById(R.id.editTextEditAssignmentDueDate2);
        editTextEditAssignmentDueTime2 = getView().findViewById(R.id.editTextEditAssignmentDueTime2);
        editTextEditAssignmentStatus2 = getView().findViewById(R.id.editTextEditAssignmentStatus2);
        editTextTextMultiLine2 = getView().findViewById(R.id.editTextTextMultiLine2);
        buttonAssignmentCancel2 = getView().findViewById(R.id.buttonAssignmentCancel2);
        buttonAssignmentSave = getView().findViewById(R.id.buttonAssignmentSave);
        editAssignmentNotificationButton = getView().findViewById
                (R.id.editAssignmentNotificationButton);
        editTextEditAssignmentName2.setText(asgmtName);
        editTextEditAssignmentSubject2.setText(dbSubject.getSubjectName(temp, dbAsgmtInc.
                getAsgmtIncSubjectID(temp, assignmentID)));
        editTextEditAssignmentDueDate2.setText(dbAsgmtInc.getAsgmtIncDate(temp, assignmentID));
        editTextEditAssignmentDueTime2.setText(dbAsgmtInc.getAsgmtIncTime(temp, assignmentID));
        editTextTextMultiLine2.setText(dbAsgmtInc.getAsgmtIncNotes(temp, assignmentID));
        try {
            editTextEditAssignmentStatus2.setText(String.valueOf(dbAsgmtInc.getAsgmtIncCompPercent(temp, assignmentID)));
        }catch (NullPointerException e) {
            editTextEditAssignmentStatus2.setText("");
        }


    }

    private void buildButtons() {
        Calendar cal = Calendar.getInstance();
        editTextEditAssignmentSubject2.setOnClickListener(v -> {
            List<Subject> subjectObjectList = dbSubject.getAllSubjects(temp);
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
            builderSingle.setTitle("Select Class:");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.select_dialog_singlechoice);

            for (Subject s: subjectObjectList){
                arrayAdapter.add(s.getSubjectName());
            }

            builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String chosenSubject = arrayAdapter.getItem(which);
                editTextEditAssignmentSubject2.setText(String.valueOf(chosenSubject));
            });
            builderSingle.show();
        });

        editTextEditAssignmentStatus2.setOnClickListener(v -> {
            List<Integer> statusObjectList = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
            builderSingle.setTitle("Select Completion Percentage:");
            ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.select_dialog_singlechoice);

            for (Integer s: statusObjectList){
                arrayAdapter.add(s);
            }

            builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                Integer chosenStatus = arrayAdapter.getItem(which);
                editTextEditAssignmentStatus2.setText(String.valueOf(chosenStatus));
            });
            builderSingle.show();
        });

        editTextEditAssignmentDueDate2.setOnClickListener(new View.OnClickListener() {
            final Calendar calendar = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (view, year, month, dayOfMonth) -> {
                            calendar.set(year,month,dayOfMonth);
                            editTextEditAssignmentDueDate2.setText(DateFormat.format
                                    ("MMM d, yyyy", calendar));
                        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar
                        .get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        editTextEditAssignmentDueTime2.setOnClickListener(new View.OnClickListener() {
            int hr, min;
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        (view, hourOfDay, minute) -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(0,0,0,hourOfDay,minute);
                            editTextEditAssignmentDueTime2.setText(DateFormat.format
                                    ("hh:mm aa",calendar));
                        },12,0,false);
                timePickerDialog.updateTime(hr,min);
                timePickerDialog.show();
            }
        });

        buttonAssignmentSave.setOnClickListener(v -> {
            String name = editTextEditAssignmentName2.getText().toString();
            String subject = editTextEditAssignmentSubject2.getText().toString();
            String date = editTextEditAssignmentDueDate2.getText().toString();
            String time = editTextEditAssignmentDueTime2.getText().toString();
            String notes = editTextTextMultiLine2.getText().toString();
            int status = Integer.parseInt(editTextEditAssignmentStatus2.getText().toString());

            if (name.isEmpty() || subject.isEmpty() || date.isEmpty() || time.isEmpty() ||
                    Integer.toString(status).isEmpty()) {
                Toast.makeText(getContext(), "Please complete all fields.",
                        Toast.LENGTH_SHORT).show();
            }else {
                try {
                    int requestCode = generateNumber();

                    if (!calDate.isEmpty()){
                        //Create pending intent and alarm manager for an assignment reminder
                        Intent intent = new Intent(getContext(), NotificationBroadcast.class);
                        intent.putExtra("ASSIGNMENT_NAME", asgmtName);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),
                                requestCode, intent, 0);
                        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity())
                                .getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                                pendingIntent);
                        //Insert reminder into the database
                        Reminder reminder =new Reminder(temp, assignmentID,requestCode,calDate);
                        dbReminder.insertReminder(reminder);
                    }
                }catch (NullPointerException e){
                    Toast.makeText(getContext(), "No reminder set.",
                            Toast.LENGTH_SHORT).show();
                }

                int subID = dbSubject.getSubject(temp, subject).getSubjectID();

                //Save assignment to database
                if (Integer.parseInt(editTextEditAssignmentStatus2.getText().toString()) == 100) {
                    //Remove assignment from assignment_incomplete_table
                    AsgmtIncomplete asgmtIncomplete = dbAsgmtInc.getAsgmtInc
                            (temp, assignmentID, asgmtName);
                    dbAsgmtInc.deleteAsgmtInc(asgmtIncomplete);

                    //Add assignment to assignment_complete_table
                    Calendar calendar = Calendar.getInstance();
                    AsgmtComplete asgmtComplete = new AsgmtComplete(subID, temp, name, notes,
                            R.drawable.ic_baseline_assignment_24, DateFormat.format
                            ("MMM d, yyyy", calendar).toString());
                    dbAsgmtComp.insertAsgmtComp(asgmtComplete);

                    Fragment frag = new AssignmentsFragment();
                    Bundle bun = new Bundle();
                    bun.putInt("USER_ID", temp);
                    frag.setArguments(bun);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            frag).commit();
                }else {
                    dbAsgmtInc.updateAsgmtInc(assignmentID, subID, temp, name, date, time,
                            notes, status);

                    Fragment frag = new AssignmentsFragment();
                    Bundle bun = new Bundle();
                    bun.putInt("USER_ID", temp);
                    frag.setArguments(bun);
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            frag).commit();
                }

                Toast.makeText(getContext(),"Assignment updated.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        buttonAssignmentCancel2.setOnClickListener(v -> {
            Fragment frag = new AssignmentsFragment();
            Bundle bun = new Bundle();
            bun.putInt("USER_ID", temp);
            frag.setArguments(bun);
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    frag).commit();
            Toast.makeText(getContext(),"Cancelled.", Toast.LENGTH_SHORT).show();
        });

        editAssignmentNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }

            private void dateDialog() {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (view, year, month, dayOfMonth) -> {
                            cal.set(Calendar.YEAR, year);
                            cal.set(Calendar.MONTH, month);
                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            timeDialog();
                            //timeDialog(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                            //        cal.get(Calendar.DAY_OF_MONTH));
                        },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get
                        (Calendar.DAY_OF_MONTH));
                datePickerDialog.setTitle("SET REMINDER");
                datePickerDialog.show();
            }

            private void timeDialog() {
                int hr = cal.get(Calendar.HOUR);
                int min = cal.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        (view, hourOfDay, minute) -> {
                            Calendar calendar = Calendar.getInstance();
                            cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                            cal.set(Calendar.MINUTE,minute);
                            cal.set(Calendar.SECOND,0);
                            cal.set(Calendar.MILLISECOND, 0);

                            calDate = cal.get(Calendar.YEAR) + " " + cal.get
                                    (Calendar.DAY_OF_YEAR);
                            //Checks reminder date to make sure it isn't in the past
                            if (Integer.parseInt(calDate.substring(0,4)) >= Calendar
                                    .getInstance().get(Calendar.YEAR) && Integer.parseInt
                                    (calDate.substring(5)) >= Calendar.getInstance()
                                    .get(Calendar.DAY_OF_YEAR) && cal.get(Calendar.HOUR_OF_DAY)
                                    >= calendar.get(Calendar.HOUR_OF_DAY) && cal.get
                                    (Calendar.MINUTE) > calendar.get(Calendar.MINUTE)) {
                                Toast.makeText(getContext(), "Reminder Set.",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder
                                        (getContext());
                                builder1.setTitle("Invalid Date");
                                builder1.setMessage("Reminders may not be set for past dates.");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        android.R.string.ok,
                                        (dialog, id) -> {
                                            dialog.cancel();
                                            dateDialog();
                                        });
                                AlertDialog alertDialog = builder1.create();
                                alertDialog.show();
                            }
                        },hr,min,false);
                timePickerDialog.show();
            }
        });
    }

    private int generateNumber() {
        Random random = new Random();
        return random.nextInt(100000);
    }

}



