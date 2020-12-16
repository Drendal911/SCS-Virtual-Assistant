package Entity;

import androidx.room.Entity;

@Entity (tableName = "asgmt_incomplete_table")
public class AsgmtIncomplete extends Asgmt{
    private String asgmtDueDate;
    private String asgmtDueTime;
    private int asgmtCompPercent;

    public AsgmtIncomplete(int subjID, int userID, String asgmtName, String asgmtNotes,
                           int asgmtIcon, String asgmtDueDate, String asgmtDueTime,
                           int asgmtCompPercent) {
        super(subjID, userID, asgmtName, asgmtNotes, asgmtIcon);
        this.asgmtDueDate = asgmtDueDate;
        this.asgmtDueTime = asgmtDueTime;
        this.asgmtCompPercent = asgmtCompPercent;
    }



    public String getAsgmtDueDate() {
        return asgmtDueDate;
    }

    public void setAsgmtDueDate(String asgmtDueDate) {
        this.asgmtDueDate = asgmtDueDate;
    }

    public String getAsgmtDueTime() {
        return asgmtDueTime;
    }

    public void setAsgmtDueTime(String asgmtDueTime) {
        this.asgmtDueTime = asgmtDueTime;
    }

    public int getAsgmtCompPercent() {
        return asgmtCompPercent;
    }

    public void setAsgmtCompPercent(int asgmtCompPercent) {
        this.asgmtCompPercent = asgmtCompPercent;
    }
}
