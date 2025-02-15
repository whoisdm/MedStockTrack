package com.medstocktrack.medstockapp.controllers.register;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import com.medstocktrack.medstockapp.managers.RegisterManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterRemoveController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField medId;

    @FXML
    private Label msg;

    @FXML
    private Button viewButton;

    private RegisterManager registerManager;

    @FXML
    void addRegister(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("register/registerAdd.fxml", "MedStockTrack - Додавання ліків до реєстру", addButton);
    }

    @FXML
    void exit(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("mainmenu.fxml", "MedStockTrack - Головне меню", exitButton);
    }

    @FXML
    void removeRegister(ActionEvent event) {
        if (!medId.getText().trim().matches("^UA/\\d{4,5}/\\d{2}/\\d{2}$")){
            msg.setText("Реєстраційне посвідчення некоректної форми!");
            msg.setVisible(true);
            return;
        }
        int result = registerManager.removeRegister(medId.getText().trim());
        if (result == 1){
            msg.setText("Препарат успішно видалено з реєстру!");
            msg.setVisible(true);
        } else {
            msg.setText("Сталася помилка, перевірте дані та спробуйте знову!");
            msg.setVisible(true);
        }
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
