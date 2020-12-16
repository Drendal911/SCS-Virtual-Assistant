package DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Entity.AsgmtComplete;
import Entity.AsgmtIncomplete;

@Dao
public interface AsgmtCompDAO {
    @Query("SELECT * FROM asgmt_complete_table WHERE userID = :userID ORDER BY asgmtName")
    List<AsgmtComplete> getAllAsgmtComp(int userID);

    @Query("SELECT * FROM asgmt_complete_table WHERE userID = :userID AND subjID = :subjectID")
    List<AsgmtComplete> getAsgmtCompWithSubject(int userID, int subjectID);

    @Query("SELECT * FROM asgmt_complete_table WHERE userID = :userID AND asgmtName = :aName")
    AsgmtComplete getAsgmtComp(int userID, String aName);

    @Query("SELECT asgmtIcon FROM asgmt_complete_table WHERE asgmtID = :assignmentID")
    int getAsgmtCompIcon(int assignmentID);

    @Query("SELECT asgmtName FROM asgmt_complete_table WHERE userID = :userID AND asgmtID = :assignmentID")
    String getAsgmtCompName(int userID, int assignmentID);

    @Query("SELECT asgmtCompleteDate FROM asgmt_complete_table WHERE userID = :userID AND asgmtID = :assignmentID")
    String getAsgmtCompDate(int userID, int assignmentID);

    @Insert
    void insertAsgmtComp(AsgmtComplete assignmentInsert);

    @Delete
    void deleteAsgmtComp(AsgmtComplete assignmentDelete);
}