package DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Entity.Teacher;

@Dao
public interface TeacherDAO {

    @Query("SELECT * FROM teacher_table WHERE userID = :userID ORDER BY teacherName")
    List<Teacher> getAllTeachers(int userID);

    @Query("SELECT * FROM teacher_table WHERE userID = :userID AND teacherName = :teacherName")
    Teacher getTeacher(int userID, String teacherName);

    @Query("SELECT teacherName FROM teacher_table WHERE teacherID = :teacherID")
    String getTeacherName(int teacherID);

    @Query("SELECT teacherEmail FROM teacher_table WHERE teacherID = :teacherID")
    String getTeacherEmail(int teacherID);

    @Query("UPDATE teacher_table SET teacherName = :teacherName, teacherEmail = :teacherEmail" +
            " WHERE teacherID = :teacherID")
    void updateTeacher (int teacherID, String teacherName, String teacherEmail);


    @Insert
    void insertTeacher(Teacher teacherInsert);

    @Delete
    void deleteTeacher (Teacher teacherDelete);

}
