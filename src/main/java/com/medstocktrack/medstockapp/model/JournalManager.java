package com.medstocktrack.medstockapp.model;

import com.mysql.cj.jdbc.MysqlDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JournalManager {
    private final MysqlDataSource dataSource;

    public JournalManager() {
        dataSource = DataBaseManager.getDataSource();
    }

    public ObservableList<Action> getActionList(LocalDate startDate, LocalDate endDate, String type, String user, String limit) {
        ObservableList<Action> actions = FXCollections.observableArrayList();
        String sql;
        boolean isDates;
        if (startDate == null || endDate == null) {
            sql = "SELECT * FROM actions WHERE actions.action_type LIKE ? AND actions.action_user LIKE ? ORDER BY actions.action_date DESC";
            isDates = false;
        } else {
            sql = "SELECT * FROM actions WHERE actions.action_type LIKE ? AND actions.action_user LIKE ? AND actions.action_date BETWEEN ? AND ? ORDER BY actions.action_date DESC";
            isDates = true;
        }

        if (limit != null) {
            sql = sql + " LIMIT ?;";
        } else {
            sql = sql + ";";
        }

        try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, "%" + type + "%");
            preparedStatement.setString(2, "%" + user + "%");
            if (isDates) {
                preparedStatement.setString(3, startDate.toString());
                preparedStatement.setString(4, endDate.toString());
            }
            if (limit != null && isDates) {
                preparedStatement.setInt(5, Integer.parseInt(limit));
            } else if (limit != null) {
                preparedStatement.setInt(3, Integer.parseInt(limit));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                LocalDateTime localDateTime = LocalDateTime.parse(resultSet.getString("action_date"), inputFormatter);
                String date = localDateTime.format(outputFormatter);

                Action action = new Action(resultSet.getString("action_user"), resultSet.getString("action_type"),
                        date, resultSet.getString("action_med"), resultSet.getInt("action_size"));
                actions.add(action);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return actions;
    }
}
