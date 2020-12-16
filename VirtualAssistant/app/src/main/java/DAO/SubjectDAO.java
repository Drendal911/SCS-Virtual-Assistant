package DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Entity.Subject;

@Dao
public interface SubjectDAO {

    @Query("SELECT * FROM subject_table WHERE userID = :userID ORDER BY subjectName")
    List<Subject> getAllSubjects(int userID);

    @Query("SELECT * FROM subject_table WHERE userID = :userID AND teacherID = :teacherID " +
            "ORDER BY subjectName DESC")
    List<Subject> getSubjectWithTeacher(int userID, int teacherID);

    @Query("SELECT * FROM subject_table WHERE userID = :userID AND subjectName = :subjectName")
    Subject getSubject(int userID, String subjectName);

    @Query("SELECT subjectName FROM subject_table WHERE userID = :userID AND subjectID = :subjectID")
    String getSubjectName(int userID, int subjectID);

    @Query("SELECT subjectTime FROM subject_table WHERE userID = :userID AND subjectID = :subjectID")
    String getSubjectTime(int userID, int subjectID);

    @Query("SELECT teacherID FROM subject_table WHERE userID = :userID AND subjectID = :subjectID")
    int getSubjectTeacherID(int userID, int subjectID);

    @Query("SELECT icon FROM subject_table WHERE userID = :userID AND subjectID = :subjectID")
    int getIcon(int userID, int subjectID);

    @Query("UPDATE subject_table SET teacherID = :teacherID, subjectName = :subjectName, " +
            "subjectTime = :subjectTime WHERE subjectID = :subjectID")
    void updateSubject (int subjectID, int teacherID, String subjectName, String subjectTime);

    @Insert
    void insertSubject(Subject subjectInsert);

    @Delete
    void deleteSubject (Subject subjectDelete);

}