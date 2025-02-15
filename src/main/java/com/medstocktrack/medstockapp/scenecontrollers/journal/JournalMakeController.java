package com.medstocktrack.medstockapp.scenecontrollers.journal;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import com.medstocktrack.medstockapp.managers.JournalManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.Objects;
import java.util.ResourceBundle;

public class JournalMakeController implements Initializable {

    @FXML
    private Button exitButton;

    @FXML
    private ChoiceBox<String> monthChoice;

    @FXML
    private Label msg;

    @FXML
    private ChoiceBox<String> quarterChoice;

    @FXML
    private Button viewButton;

    @FXML
    private TextField yearField;

    @FXML
    private RadioButton isCustom;

    private JournalManager journalManager;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;


    @FXML
    void exit(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("mainmenu.fxml", "MedStockTrack - Головне меню", exitButton);
    }

    @FXML
    void makeReport(ActionEvent event) {
        int result = 0;

        if (isCustom.isSelected()) {
            result = journalManager.makeReport(startDate.getValue(), endDate.getValue());
        } else {
            if (!isValidYear(yearField.getText().trim())){
                msg.setText("Перевірте коректність обраного року!");
                msg.setVisible(true);
                return;
            }

            String month = Objects.equals(monthChoice.getValue(), "-") ? null : monthChoice.getValue();
            String quarter = Objects.equals(quarterChoice.getValue(), "-") ? null : quarterChoice.getValue();
            if (month != null && quarter != null) {
                msg.setText("Не можна одночасно обрати місяць та квартал!");
                msg.setVisible(true);
                return;
            }

            result = journalManager.makeReport(yearField.getText().trim(), quarter, month);
        }

        if (result == 1){
            msg.setText("Звіт був успішно сформований!");
        } else {
            msg.setText("Помилка! Перевірте дані та спробуйте ще!");
        }
        msg.setVisible(true);
    }

    @FXML
    void viewJournal(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("journal/journalView.fxml", "MedStockTrack - Перегляд журналу складу", viewButton);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        journalManager = new JournalManager();
    }

    public static boolean isValidYear(String str) {
        try {
            int year = Integer.parseInt(str);
            int currentYear = Year.now().getValue();
            return year > 0 && year <= currentYear;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
