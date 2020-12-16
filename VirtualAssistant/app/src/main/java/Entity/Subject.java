package Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity (tableName = "subject_table")
public class Subject implements Serializable {

    @PrimaryKey (autoGenerate = true)
    private int subjectID;
    @ForeignKey(entity = User.class, parentColumns = "userID", childColumns = "userID")
    private int userID;
    @ForeignKey(entity = Teacher.class, parentColumns = "teacherID", childColumns = "teacherID")
    private int teacherID;
    private String subjectName;
    private String subjectTime;
    private int icon;

    public Subject(int userID, int teacherID, String subjectName, String subjectTime, int icon){
        this.userID = userID;
        this.teacherID = teacherID;
        this.subjectName = subjectName;
        this.subjectTime = subjectTime;
        this.icon = icon;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    @NonNull
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(@NonNull String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectTime() {
        return subjectTime;
    }

    public void setSubjectTime(String subjectTime) {
        this.subjectTime = subjectTime;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}