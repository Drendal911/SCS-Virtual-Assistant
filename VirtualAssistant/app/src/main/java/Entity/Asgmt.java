package Entity;

import androidx.room.PrimaryKey;

public abstract class Asgmt {

    @PrimaryKey(autoGenerate = true)
    private int asgmtID;
    private int subjID;
    private int userID;
    private String asgmtName;
    private String asgmtNotes;
    private int asgmtIcon;

    public Asgmt(int subjID, int userID, String asgmtName, String asgmtNotes,
                 int asgmtIcon) {
        this.asgmtID = asgmtID;
        this.subjID = subjID;
        this.userID = userID;
        this.asgmtName = asgmtName;
        this.asgmtNotes = asgmtNotes;
        this.asgmtIcon = asgmtIcon;
    }

    public int getAsgmtID() {
        return asgmtID;
    }

    public void setAsgmtID(int asgmtID) {
        this.asgmtID = asgmtID;
    }

    public int getSubjID() {
        return subjID;
    }

    public void setSubjID(int subjID) {
        this.subjID = subjID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getAsgmtName() {
        return asgmtName;
    }

    public void setAsgmtName(String asgmtName) {
        this.asgmtName = asgmtName;
    }

    public String getAsgmtNotes() {
        return asgmtNotes;
    }

    public void setAsgmtNotes(String asgmtNotes) {
        this.asgmtNotes = asgmtNotes;
    }

    public int getAsgmtIcon() {
        return asgmtIcon;
    }

    public void setAsgmtIcon(int asgmtIcon) {
        this.asgmtIcon = asgmtIcon;
    }
}
