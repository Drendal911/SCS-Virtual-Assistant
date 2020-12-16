package Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import DAO.AsgmtCompDAO;
import DAO.AsgmtIncDAO;
import DAO.ReminderDAO;
import DAO.SubjectDAO;
import DAO.TeacherDAO;
import DAO.UserDAO;
import Entity.AsgmtComplete;
import Entity.AsgmtIncomplete;
import Entity.Reminder;
import Entity.Subject;
import Entity.Teacher;
import Entity.User;

@Database(entities = {User.class, Subject.class, Teacher.class, AsgmtIncomplete.class,
        AsgmtComplete.class, Reminder.class}, version = 1, exportSchema = false)
public abstract class VADatabase extends RoomDatabase {

    public abstract UserDAO getUserDAO();
    public abstract SubjectDAO getSubjectDAO();
    public abstract TeacherDAO getTeacherDAO();
    public abstract AsgmtCompDAO getAsgmtCompDAO();
    public abstract AsgmtIncDAO getAsgmtIncDAO();
    public abstract ReminderDAO getReminderDAO();

}
