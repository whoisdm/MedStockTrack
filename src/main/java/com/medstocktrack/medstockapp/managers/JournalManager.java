package com.medstocktrack.medstockapp.managers;

import com.medstocktrack.medstockapp.model.Action;
import com.medstocktrack.medstockapp.model.ReportData;
import com.mysql.cj.jdbc.MysqlDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JournalManager {
    private final MysqlDataSource dataSource;

    private final static String[] actionTypes = new String[]{"ЗАВЕЗЕННЯ", "ВИВЕЗЕННЯ", "СПИСАННЯ"};

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
                String date = formatDate(resultSet.getString("action_date"));
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

    private String formatDate(String date){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(date, inputFormatter);
        return localDateTime.format(outputFormatter);
    }

    public int makeReport(LocalDate startDate, LocalDate endDate){
        if (startDate == null || endDate == null) {
            return -1;
        }
        if (startDate.isAfter(endDate)) {
            return -1;
        }

        ArrayList<ArrayList<ReportData>> reportData = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            reportData.add(new ArrayList<>());
            try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword());
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT actions.action_med, medicine.med_name, sum(actions.action_size) AS quantity, medicine.med_price " +
                        "FROM actions INNER JOIN medicine ON actions.action_med = medicine.med_id " +
                        "WHERE actions.action_type LIKE ? AND actions.action_date BETWEEN ? AND ? GROUP BY actions.action_med;");){
                preparedStatement.setString(1, "%" + actionTypes[i] + "%");
                preparedStatement.setString(2, startDate.toString());
                preparedStatement.setString(3, endDate.toString());
                fillDataArray(reportData, i, preparedStatement);
            } catch (SQLException e) {
                return -1;
            }
        }

        return formReport(reportData);
    }

    public int makeReport(String year, String quarter, String month){
        ArrayList<ArrayList<ReportData>> reportData = new ArrayList<>();
        boolean quarterFlag = false;
        boolean monthFlag = false;

        String sql = "SELECT actions.action_med, medicine.med_name, sum(actions.action_size) as quantity, medicine.med_price " +
                "FROM actions INNER JOIN medicine ON actions.action_med = medicine.med_id " +
                "WHERE actions.action_type LIKE ? AND YEAR(actions.action_date) = ? ";
        if (quarter == null && month == null) {
            sql = sql + "GROUP BY actions.action_med;";
        } else if (quarter == null) {
            sql = sql + "AND MONTH(actions.action_date) = ? GROUP BY actions.action_med;";
            monthFlag = true;
        } else if (month == null) {
            sql = sql + "AND QUARTER(actions.action_date) = ? GROUP BY actions.action_med;";
            quarterFlag = true;
        }

        for (int i = 0; i < 3; i++){
            reportData.add(new ArrayList<>());
            try (Connection connection = dataSource.getConnection(dataSource.getUser(), dataSource.getPassword());
                PreparedStatement preparedStatement = connection.prepareStatement(sql);){
                preparedStatement.setString(1, "%" + actionTypes[i] + "%");
                preparedStatement.setInt(2, Integer.parseInt(year));
                if (quarterFlag) {
                    int dotIndex = quarter.indexOf(".");
                    String quarterNumber = (dotIndex != -1) ? quarter.substring(0, dotIndex).trim() : quarter;
                    preparedStatement.setInt(3, Integer.parseInt(quarterNumber));
                }
                if (monthFlag) {
                    int dotIndex = month.indexOf(".");
                    String monthNumber = (dotIndex != -1) ? month.substring(0, dotIndex).trim() : month;
                    preparedStatement.setInt(3, Integer.parseInt(monthNumber));
                }
                fillDataArray(reportData, i, preparedStatement);
            } catch (SQLException e) {
                return -1;
            }
        }

        return formReport(reportData);
    }

    private void fillDataArray(ArrayList<ArrayList<ReportData>> reportData, int i, PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int quantity = resultSet.getInt("quantity");
            double price = resultSet.getDouble("med_price") * quantity;
            ReportData data = new ReportData(resultSet.getString("action_med"), resultSet.getString("med_name"), quantity, price);
            reportData.get(i).add(data);
        }
        resultSet.close();
    }

    private int formReport(ArrayList<ArrayList<ReportData>> reportData){
        try (XSSFWorkbook workbook = new XSSFWorkbook()){
            for (int i = 0; i < 3; i++) {
                XSSFSheet sheet = workbook.createSheet(actionTypes[i]);
                createSheet(sheet, reportData.get(i));
            }

            File directory = new File("reports");
            boolean isExist;
            if (!directory.exists()) {
                isExist = directory.mkdirs();
            } else {
                isExist = directory.exists();
            }
            if (!isExist) return -1;

            String filePath = "reports/report-%s%s.xlsx";
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            int index = 0;
            File file;
            while (true) {
                file = new File(String.format(filePath, today.format(formatter), index == 0 ? "" : "_" + index));
                if (file.exists() && file.isFile()){
                    index++;
                } else {
                    break;
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                return -1;
            }
        } catch (IOException e) {
            return -1;
        }
        return 1;
    }

    private void createSheet(XSSFSheet sheet, ArrayList<ReportData> data){
        String[] headers = {"РП ліків", "Назва ліків", "Обсяг", "Вартість"};

        XSSFWorkbook workbook = sheet.getWorkbook();
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int totalQuantity = 0;
        double totalPrice = 0;
        int rows;
        for (rows = 1; rows <= data.size(); rows++){
            Row row = sheet.createRow(rows);

            row.createCell(0).setCellValue(data.get(rows-1).getId());
            row.createCell(1).setCellValue(data.get(rows-1).getName());
            row.createCell(2).setCellValue(data.get(rows-1).getQuantity());
            row.createCell(3).setCellValue(data.get(rows-1).getPrice());

            totalQuantity += data.get(rows-1).getQuantity();
            totalPrice += data.get(rows-1).getPrice();
        }

        int lastRow = rows+1;
        Row mergedRow = sheet.createRow(lastRow);
        mergedRow.createCell(0).setCellValue("Загалом:");
        mergedRow.createCell(2).setCellValue(totalQuantity);
        mergedRow.createCell(3).setCellValue(totalPrice);
        sheet.addMergedRegion(new CellRangeAddress(lastRow, lastRow, 0, 1));

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
