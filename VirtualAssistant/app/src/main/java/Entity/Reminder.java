package Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "reminder_table")
public class Reminder implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int reminderID;
    @ForeignKey(entity = User.class, parentColumns = "userID", childColumns = "userID")
    private int userID;
    //@ForeignKey(entity = AsgmtIncomplete.class, parentColumns = "asgmtID", childColumns = "asgmtID")
    private int asgmtID;
    private String date;
    private int generatedNumber;

    public Reminder(int userID, int asgmtID, int generatedNumber, String date) {
        this.userID = userID;
        this.asgmtID = asgmtID;
        this.generatedNumber = generatedNumber;
        this.date = date;
    }

    public int getReminderID() {
        return reminderID;
    }

    public void setReminderID(int reminderID) {
        this.reminderID = reminderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getAsgmtID() {
        return asgmtID;
    }

    public void setAsgmtID(int asgmtID) {
        this.asgmtID = asgmtID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGeneratedNumber() {
        return generatedNumber;
    }

    public void setGeneratedNumber(int generatedNumber) {
        this.generatedNumber = generatedNumber;
    }
}
