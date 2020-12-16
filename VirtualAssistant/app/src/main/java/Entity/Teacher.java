package Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "teacher_table")
public class Teacher implements Serializable {
    @PrimaryKey (autoGenerate = true)
    private int teacherID;
    @ForeignKey(entity = User.class, parentColumns = "userID", childColumns = "userID")
    private int userID;
    private String teacherName;
    private String teacherEmail;
    private int teacherIcon;

    public Teacher(int userID, String teacherName, String teacherEmail, int teacherIcon) {
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
        this.teacherIcon = teacherIcon;
        this.userID = userID;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public int getTeacherIcon() {
        return teacherIcon;
    }

    public void setTeacherIcon(int teacherIcon) {
        this.teacherIcon = teacherIcon;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString(){
        return teacherName;
    }
}
