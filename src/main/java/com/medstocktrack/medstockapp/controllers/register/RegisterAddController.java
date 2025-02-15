package com.medstocktrack.medstockapp.controllers.register;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import com.medstocktrack.medstockapp.model.Medicine;
import com.medstocktrack.medstockapp.managers.RegisterManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterAddController implements Initializable {

    @FXML
    private Button exitButton;

    @FXML
    private TextField medActive;

    @FXML
    private TextField medContr;

    @FXML
    private TextField medCountry;

    @FXML
    private TextField medForm;

    @FXML
    private TextField medId;

    @FXML
    private TextField medName;

    @FXML
    private RadioButton medPrescription;

    @FXML
    private TextField medPrice;

    @FXML
    private TextField medProducer;

    @FXML
    private TextField medSize;

    @FXML
    private TextField medType;

    @FXML
    private Label msg;

    @FXML
    private Button removeButton;

    @FXML
    private Button viewButton;

    private RegisterManager registerManager;

    @FXML
    void addRegister(ActionEvent event) {
        if (!medId.getText().trim().matches("^UA/\\d{4,5}/\\d{2}/\\d{2}$")){
            msg.setText("Реєстраційне посвідчення некоректної форми!");
            msg.setVisible(true);
            return;
        }
        if (!medPrice.getText().trim().matches("^(?!0+(\\.0{1,2})?$)(\\d+|\\d+\\.\\d{1,2})$")){
            msg.setText("Ціна некоректної форми!");
            msg.setVisible(true);
            return;
        }
        if (medActive.getText().isEmpty() || medContr.getText().isEmpty() || medCountry.getText().isEmpty()
                || medForm.getText().isEmpty() || medName.getText().isEmpty()
                || medProducer.getText().isEmpty() || medSize.getText().isEmpty() || medType.getText().isEmpty()) {
            msg.setText("Заповніть усі поля!");
            msg.setVisible(true);
            return;
        }
        int prescription = medPrescription.isSelected() ? 1 : 0;
        Medicine medicine = new Medicine(medId.getText().trim(), medName.getText().trim().toUpperCase(), medType.getText().trim(),
                medActive.getText().trim(), medSize.getText().trim(), medContr.getText().trim(), medForm.getText().trim(),
                Double.valueOf(medPrice.getText().trim()), prescription, medProducer.getText().trim(), medCountry.getText().trim());

        int result = registerManager.addRegister(medicine);
        if (result == 1){
            msg.setText("Ліки успішно зареєстровані!");
            msg.setVisible(true);
        } else {
            msg.setText("Сталася помилка, перевірте дані та спробуйте знову!");
            msg.setVisible(true);
        }
    }

    @FXML
    void exit(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("mainmenu.fxml", "MedStockTrack - Головне меню", exitButton);
    }

    @FXML
    void removeRegister(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("register/registerRemove.fxml", "MedStockTrack - Видалення ліків з реєстру", removeButton);
    }

    @FXML
    void viewRegister(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("register/registerView.fxml", "MedStockTrack - Перегляд реєстру ліків", viewButton);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerManager = new RegisterManager();
    }
}
