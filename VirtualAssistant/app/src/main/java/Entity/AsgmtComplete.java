package Entity;

import androidx.room.Entity;

@Entity (tableName = "asgmt_complete_table")
public class AsgmtComplete extends Asgmt {
    private String asgmtCompleteDate;


    public AsgmtComplete(int subjID, int userID, String asgmtName, String asgmtNotes,
                         int asgmtIcon, String asgmtCompleteDate) {
        super(subjID, userID, asgmtName, asgmtNotes, asgmtIcon);
        this.asgmtCompleteDate = asgmtCompleteDate;
    }

    public String getAsgmtCompleteDate() {
        return asgmtCompleteDate;
    }

    public void setAsgmtCompleteDate(String asgmtCompleteDate) {
        this.asgmtCompleteDate = asgmtCompleteDate;
    }
}
