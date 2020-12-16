package DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Entity.AsgmtIncomplete;

@Dao
public interface AsgmtIncDAO {
    @Query("SELECT * FROM asgmt_incomplete_table WHERE userID = :userID ORDER BY asgmtDueDate, asgmtDueTime")
    List<AsgmtIncomplete> getAllAsgmtInc(int userID);

    @Query("SELECT * FROM asgmt_incomplete_table WHERE userID = :userID AND subjID = :subjectID")
    List<AsgmtIncomplete> getAsgmtIncWithSubject(int userID, int subjectID);

    @Query("SELECT * FROM asgmt_incomplete_table WHERE userID = :userID AND asgmtName = :aName")
    AsgmtIncomplete getAsgmtInc(int userID, String aName);

    @Query("SELECT * FROM asgmt_incomplete_table WHERE userID = :userID AND asgmtName = :aName AND asgmtID = :asgmtID")
    AsgmtIncomplete getAsgmtInc(int userID, int asgmtID, String aName);

    @Query("SELECT asgmtCompPercent FROM asgmt_incomplete_table WHERE userID = :userID AND asgmtID = :aID")
    int getAsgmtIncCompPercent(int userID, int aID);

    @Query("SELECT * FROM asgmt_incomplete_table WHERE userID = :userID AND asgmtDueDate = :assignmentDate")
    List<AsgmtIncomplete> getAsgmtIncDueToday(int userID, String assignmentDate);

    @Query("SELECT asgmtIcon FROM asgmt_incomplete_table WHERE asgmtID = :assignmentID")
    int getAsgmtIncIcon(int assignmentID);

    @Query("SELECT asgmtName FROM asgmt_incomplete_table WHERE userID = :userID AND asgmtID = :assignmentID")
    String getAsgmtIncName(int userID, int assignmentID);

    @Query("SELECT asgmtDueDate FROM asgmt_incomplete_table WHERE userID = :userID AND asgmtID = :assignmentID")
    String getAsgmtIncDate(int userID, int assignmentID);

    @Query("SELECT asgmtDueTime FROM asgmt_incomplete_table WHERE userID = :userID AND asgmtID = :assignmentID")
    String getAsgmtIncTime(int userID, int assignmentID);

    @Query("SELECT subjID FROM asgmt_incomplete_table WHERE userID = :userID AND asgmtID = :assignmentID")
    int getAsgmtIncSubjectID(int userID, int assignmentID);

    @Query("SELECT asgmtNotes FROM asgmt_incomplete_table WHERE userID = :userID AND asgmtID = :assignmentID")
    String getAsgmtIncNotes(int userID, int assignmentID);

    @Query("UPDATE asgmt_incomplete_table SET subjID = :subjectID, userID = :userID, asgmtName" +
            " = :assignmentName, asgmtDueDate = :assignmentDate, asgmtDueTime = " +
            ":assignmentTime, asgmtName = :assignmentName, asgmtNotes = " +
            ":assignmentNotes, asgmtCompPercent = :asgmtCompPercent WHERE asgmtID = :assignmentID")
    void updateAsgmtInc(int assignmentID, int subjectID, int userID, String assignmentName,
                          String assignmentDate, String assignmentTime, String assignmentNotes,
                        int asgmtCompPercent);

    @Insert
    void insertAsgmtInc(AsgmtIncomplete assignmentInsert);

    @Delete
    void deleteAsgmtInc(AsgmtIncomplete assignmentDelete);


}
