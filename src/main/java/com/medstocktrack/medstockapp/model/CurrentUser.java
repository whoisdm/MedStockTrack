package com.medstocktrack.medstockapp.model;

import com.medstocktrack.medstockapp.managers.DataBaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrentUser implements HashableUser{
    private String username;
    private String role;
    private static final CurrentUser instance = new CurrentUser();
    private CurrentUser(){}

    @Override
    public int isExisting(String username, String password) {
        var dataSource = DataBaseManager.getDataSource();
        String hash = HashableUser.hash(password);
        try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM medstocktrack.users WHERE user_login = ? AND user_password = ?;");){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hash);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                instance.setUsername(resultSet.getString("user_login"));
                instance.setRole(resultSet.getString("user_role"));
                resultSet.close();
                return 1;
            } else {
                resultSet.close();
                return -1;
            }
        } catch (SQLException e) {
            return -1;
        }
    }

    public static CurrentUser getCurrentUser() {
        return instance;
    }
    public String getUsername() {
        return username;
    }
    private void setUsername(String username) {
        this.username = username;
    }
    public String getRole() {
        return role;
    }
    private void setRole(String role) {
        this.role = role;
    }
}

