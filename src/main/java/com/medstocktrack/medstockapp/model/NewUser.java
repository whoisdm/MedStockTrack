package com.medstocktrack.medstockapp.model;

import com.medstocktrack.medstockapp.managers.DataBaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewUser implements HashableUser{
    private String username;
    private String password;
    private String role;

    public NewUser() {
    }

    public NewUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.setRole(role);
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

            String pass = HashableUser.hash(this.password);
            if (pass == null) return -1;

            preparedStatement.setString(2, pass);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
        return 1;
    }

    public int editUser() {
        if (this.password == null && this.role == null) {
            return -1;
        }
        String sql = "UPDATE medstocktrack.users  ";
        if (this.password != null && this.role != null) {
            sql += "SET user_password = ?, user_role = ? WHERE (user_login = ?);";
        } else if (this.password != null) {
            sql += "SET user_password = ? WHERE (user_login = ?);";
        } else {
            sql += "SET user_role = ? WHERE (user_login = ?);";
        }

        var dataSource = DataBaseManager.getDataSource();
        int count = -1;
        try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            if (this.password != null && this.role != null) {
                preparedStatement.setString(1, HashableUser.hash(this.password));
                preparedStatement.setString(2, this.role);
                preparedStatement.setString(3, this.username);
            } else if (this.password != null) {
                preparedStatement.setString(1, HashableUser.hash(this.password));
                preparedStatement.setString(2, this.username);
            } else {
                preparedStatement.setString(1, this.role);
                preparedStatement.setString(2, this.username);
            }
            count = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            return -1;
        }
        return count > 0 ? 1 : -1;
    }

    public void setRole(String role) {
        if (role == null) {
            this.role = null;
            return;
        }
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
