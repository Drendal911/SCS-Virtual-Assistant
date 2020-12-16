package DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import Entity.User;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM user_table WHERE userName = :userName AND password = :password")
    User getUser (String userName, String password);

    @Query("SELECT userID FROM user_table WHERE userName = :userName")
    int getUserID (String userName);

    @Query("SELECT * FROM user_table WHERE userName = :userName")
    User checkUser (String userName);

    @Insert
    void insertUser (User user);
}
