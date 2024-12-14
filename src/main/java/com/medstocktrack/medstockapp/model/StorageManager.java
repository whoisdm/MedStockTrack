package com.medstocktrack.medstockapp.model;

import com.mysql.cj.jdbc.MysqlDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StorageManager {
    private final MysqlDataSource dataSource;

    public StorageManager() {
        dataSource = DataBaseManager.getDataSource();
    }

    public ObservableList<Storage> getStorageList(String medReg, String medName){
        ObservableList<Storage> storages = FXCollections.observableArrayList();
        try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT medicine.med_name , storages.storage_amount FROM medicine " +
                     "INNER JOIN storages ON medicine.med_id = storages.storage_med " +
                     "WHERE medicine.med_id LIKE ? AND medicine.med_name LIKE ?;");){
            preparedStatement.setString(1, "%"+ medReg +"%");
            preparedStatement.setString(2, "%"+ medName +"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Storage storage = new Storage(resultSet.getString("med_name"), resultSet.getInt("storage_amount"));
                storages.add(storage);
            }
            resultSet.close();
            return storages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int addMed(String medReg, int medNumber){
        try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword())){
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM storages WHERE storages.storage_med = ?;")){
                preparedStatement.setString(1, medReg);
                ResultSet resultSet = preparedStatement.executeQuery();
                connection.setAutoCommit(false);
                if (resultSet.next()){
                    try (PreparedStatement addStatement = connection.prepareStatement("UPDATE storages SET storage_amount = ? WHERE storage_med = ?;")){
                        addStatement.setInt(1, resultSet.getInt("storage_amount")+medNumber);
                        addStatement.setString(2, medReg);
                        addStatement.executeUpdate();
                    }
                } else {
                    try (PreparedStatement addStatement = connection.prepareStatement("INSERT INTO storages (storage_med , storage_amount) VAKUES (?,?);")){
                        addStatement.setInt(2, medNumber);
                        addStatement.setString(1, medReg);
                        addStatement.executeUpdate();
                    }
                }
                try(PreparedStatement logStatement = connection.prepareStatement(("INSERT INTO actions (action_user, action_type, action_size, action_date, action_med) " +
                        "VALUES (?, 'ЗАВЕЗЕННЯ', ?, NOW(), ?);"))){
                    logStatement.setString(1, CurrentUser.getCurrentUser().getUsername());
                    logStatement.setInt(2, medNumber);
                    logStatement.setString(3, medReg);
                    logStatement.executeUpdate();
                }
                connection.commit();
                resultSet.close();
            }
        } catch (SQLException e) {
            return -1;
        }
        return 1;
    }

    public int removeMed(String medReg, int medNumber, boolean isDestroy){
        try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword())){
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM storages WHERE storages.storage_med = ?;")){
                preparedStatement.setString(1, medReg);
                ResultSet resultSet = preparedStatement.executeQuery();
                connection.setAutoCommit(false);
                boolean isResult = resultSet.next();
                if (isResult && resultSet.getInt("storage_amount") > medNumber){
                    try (PreparedStatement removeStatement = connection.prepareStatement("UPDATE storages SET storage_amount = ? WHERE storage_med = ?;");) {
                        removeStatement.setInt(1, resultSet.getInt("storage_amount")-medNumber);
                        removeStatement.setString(2, medReg);
                        removeStatement.executeUpdate();
                    }
                } else if (isResult && resultSet.getInt("storage_amount") == medNumber) {
                    try (PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM storages  WHERE storage_med = ?;");) {
                        removeStatement.setString(1, medReg);
                        removeStatement.executeUpdate();
                    }
                } else if (isResult && resultSet.getInt("storage_amount") < medNumber){
                    return 0;
                } else return -1;
                try(PreparedStatement logStatement = connection.prepareStatement(("INSERT INTO actions (action_user, action_type, action_size, action_date, action_med) " +
                        "VALUES (?, ?, ?, NOW(), ?);"))){
                    logStatement.setString(1, CurrentUser.getCurrentUser().getUsername());
                    String commentary = isDestroy ? "СПИСАННЯ" : "ВИВЕЗЕННЯ";
                    logStatement.setString(2, commentary);
                    logStatement.setInt(3, medNumber);
                    logStatement.setString(4, medReg);
                    logStatement.executeUpdate();
                }
                connection.commit();
                resultSet.close();
            }
        } catch (SQLException e) {
            return -1;
        }
        return 1;
    }

}
