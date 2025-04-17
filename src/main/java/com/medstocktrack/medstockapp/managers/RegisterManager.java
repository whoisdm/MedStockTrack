package com.medstocktrack.medstockapp.managers;

import com.medstocktrack.medstockapp.model.CurrentUser;
import com.medstocktrack.medstockapp.model.Medicine;
import com.mysql.cj.jdbc.MysqlDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class RegisterManager {
    private final MysqlDataSource dataSource;

    public RegisterManager() {
        dataSource = DataBaseManager.getDataSource();
    }

    public ObservableList<Medicine> getMedicineList(String medID, String medName, String medType, String medActive) {
        ObservableList<Medicine> medicines = FXCollections.observableArrayList();
        try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM medicine JOIN producers ON medicine.med_producer = producers.producer_id " +
                     "WHERE medicine.med_id LIKE ? AND medicine.med_name LIKE ? AND medicine.med_type LIKE ? AND medicine.med_active LIKE ?;");){
            preparedStatement.setString(1, "%"+ medID +"%");
            preparedStatement.setString(2, "%"+ medName +"%");
            preparedStatement.setString(3, "%"+ medType +"%");
            preparedStatement.setString(4, "%"+ medActive +"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Medicine medicine = new Medicine(resultSet.getString("med_id"),
                        resultSet.getString("med_name"),
                        resultSet.getString("med_type"),
                        resultSet.getString("med_active"),
                        resultSet.getString("med_size"),
                        resultSet.getString("med_contr"),
                        resultSet.getString("med_form"),
                        resultSet.getDouble("med_price"),
                        resultSet.getInt("med_prescription"),
                        resultSet.getString("producer_name"),
                        resultSet.getString("producer_country"));
                medicines.add(medicine);
            }
            resultSet.close();
            return medicines;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int addRegister (Medicine medicine) {
        try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword())){
            try {
                connection.setAutoCommit(false);
                int producerId = -1;

                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM medicine INNER JOIN producers ON medicine.med_producer = producers.producer_id " +
                        "WHERE producers.producer_name = ? AND producers.producer_country = ?;")){
                    preparedStatement.setString(1, medicine.getProducer().getProducerName());
                    preparedStatement.setString(2, medicine.getProducer().getProducerCountry());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()){
                        producerId = resultSet.getInt("producer_id");
                    } else {
                        try (PreparedStatement newProducer = connection.prepareStatement("INSERT INTO producers (`producer_name`, `producer_country`) VALUES (?, ?);" , Statement.RETURN_GENERATED_KEYS)){
                            newProducer.setString(1, medicine.getProducer().getProducerName());
                            newProducer.setString(2, medicine.getProducer().getProducerCountry());

                            int affectedRows = newProducer.executeUpdate();

                            if (affectedRows == 1){
                                try (ResultSet generatedKeys = newProducer.getGeneratedKeys()) {
                                    if (generatedKeys.next()) producerId = generatedKeys.getInt(1);
                                }
                            }
                        }
                    }
                    resultSet.close();
                }

                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM medicine WHERE medicine.med_id = ?;")){
                    preparedStatement.setString(1, medicine.getMedicineID());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    boolean isResult = resultSet.next();
                    if (isResult){
                        return -1;
                    } else {
                        try (PreparedStatement newMedicine = connection.prepareStatement("INSERT INTO `medstocktrack`.`medicine` (`med_id`, `med_name`, `med_type`, `med_active`, `med_size`, `med_contr`, `med_form`, `med_price`, `med_prescription`, `med_producer`)" +
                                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")){
                            newMedicine.setString(1, medicine.getMedicineID());
                            newMedicine.setString(2, medicine.getMedicineName());
                            newMedicine.setString(3, medicine.getMedicineType());
                            newMedicine.setString(4, medicine.getMedicineActive());
                            newMedicine.setString(5, medicine.getMedicineSize());
                            newMedicine.setString(6, medicine.getMedicineContr());
                            newMedicine.setString(7, medicine.getMedicineForm());
                            newMedicine.setDouble(8, medicine.getMedicinePrice());
                            newMedicine.setInt(9, medicine.isMedicinePrescription());
                            newMedicine.setInt(10, producerId);
                            newMedicine.executeUpdate();
                        }
                    }
                    resultSet.close();
                }

                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO actions (action_user, action_type, action_size, action_date, action_med) VALUES (?, 'РЕЄСТРАЦІЯ', 0, NOW(), ?);")){
                    preparedStatement.setString(1, CurrentUser.getCurrentUser().getUsername());
                    preparedStatement.setString(2, medicine.getMedicineID());
                    preparedStatement.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                return -1;
            }

        } catch (SQLException e) {
            return -1;
        }
        return 1;
    }

    public int removeRegister (String medicineID) {
        try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword())){
            try {
                connection.setAutoCommit(false);
                try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM storages WHERE storage_med = ?;")) {
                    preparedStatement.setString(1, medicineID);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()){
                        int amount = resultSet.getInt("storage_amount");
                        try (PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM storages WHERE storage_med = ?;")){
                            deleteStatement.setString(1, medicineID);
                            deleteStatement.executeUpdate();
                        }
                        try(PreparedStatement logStatement = connection.prepareStatement(("INSERT INTO actions (action_user, action_type, action_size, action_date, action_med) " +
                                "VALUES (?, ?, ?, NOW(), ?);"))){
                            logStatement.setString(1, CurrentUser.getCurrentUser().getUsername());
                            logStatement.setString(2, "СПИСАННЯ");
                            logStatement.setInt(3, amount);
                            logStatement.setString(4, medicineID);
                            logStatement.executeUpdate();
                        }
                    }
                    resultSet.close();
                }

                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM medicine WHERE med_id = ?;")){
                    preparedStatement.setString(1, medicineID);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()){
                        try (PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM medicine WHERE med_id = ?;")){
                            deleteStatement.setString(1, medicineID);
                            deleteStatement.executeUpdate();
                        }
                    } else {
                        resultSet.close();
                        return -1;
                    }
                    resultSet.close();
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO actions (action_user, action_type, action_size, action_date, action_med) VALUES (?, 'ВИДАЛЕННЯ', 0, NOW(), ?);")){
                    preparedStatement.setString(1, CurrentUser.getCurrentUser().getUsername());
                    preparedStatement.setString(2, medicineID);
                    preparedStatement.executeUpdate();
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                return -1;
            }
        } catch (SQLException e) {
            return -1;
        }
        return 1;
    }

}
