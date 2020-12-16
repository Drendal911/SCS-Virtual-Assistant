package DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Entity.Reminder;

@Dao
public interface ReminderDAO {
    @Query("SELECT * FROM reminder_table WHERE userID = :userID")
    List<Reminder> getAllReminders(int userID);

    @Query("DELETE FROM reminder_table WHERE userID = :userID AND asgmtID = :asgmtID")
    void deleteReminders (int userID, int asgmtID);

   @Delete
    void deleteReminder (Reminder reminderDelete);

    @Insert
    void insertReminder(Reminder reminderInsert);
}
