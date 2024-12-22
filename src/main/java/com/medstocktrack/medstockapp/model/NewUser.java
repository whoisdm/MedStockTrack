package com.medstocktrack.medstockapp.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewUser implements User{
    private String username;
    private String password;
    private String role;

    public NewUser() {
    }

    @Override
    public int isExisting(String username, String password) {
        var dataSource = DataBaseManager.getDataSource();
        try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM medstocktrack.users WHERE user_login = ?;");){
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                this.username = username;
                this.password = password;
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

    public int addUser(){
        if (this.username == null || this.password == null || this.role == null) {
            return -1;
        }
        var dataSource = DataBaseManager.getDataSource();
        try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO medstocktrack.users (user_login, user_password, user_role) VALUES (?, ?, ?);")){
            preparedStatement.setString(1, this.username);
            preparedStatement.setString(3, this.role);

            String pass = User.hash(this.password);
            if (pass == null) return -1;

            preparedStatement.setString(2, pass);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
        return 1;
    }

    public void setRole(String role) {
        int dotIndex = role.indexOf(".");
        String roleNumber = (dotIndex != -1) ? role.substring(0, dotIndex).trim() : role;
        switch (roleNumber){
            case "1":
                this.role = "worker";
                break;
            case "2":
                this.role = "manager";
                break;
            case "3":
                this.role = "admin";
                break;
        }
    }
}
