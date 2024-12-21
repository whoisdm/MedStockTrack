package com.medstocktrack.medstockapp.controller.admin;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import com.medstocktrack.medstockapp.model.NewUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

public class UserAddController {

    @FXML
    private Button editButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField loginField;

    @FXML
    private Label msg;

    @FXML
    private TextField passwordConf;

    @FXML
    private TextField passwordField;

    @FXML
    private ChoiceBox<String> roleChoice;

    @FXML
    void addUser(ActionEvent event) {
        if (loginField.getText().trim().isEmpty() || passwordConf.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
            msg.setText("Заповніть усі поля форми!");
            msg.setVisible(true);
            return;
        }
        if (Objects.equals(roleChoice.getValue(), "-")){
            msg.setText("Оберіть роль користувача!");
            msg.setVisible(true);
            return;
        }
        if (!passwordConf.getText().trim().equals(passwordField.getText().trim())){
            msg.setText("Паролі мають співпадати!");
            msg.setVisible(true);
            return;
        }
        NewUser newUser = new NewUser();
        int isExists = newUser.isExisting(loginField.getText().trim(), passwordField.getText().trim());
        if (isExists != 1){
            msg.setText("Користувач вже існує!");
            msg.setVisible(true);
            return;
        }
        newUser.setRole(roleChoice.getValue());
        int result = newUser.addUser();
        if (result == 1){
            msg.setText("Користувач успішно зареєстрований!");
            msg.setVisible(true);
        } else {
            msg.setText("Помилка! Перевірте дані та спробуйте ще!");
            msg.setVisible(true);
        }
    }

    @FXML
    void editUser(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("admin/userEdit.fxml", "MedStockTrack - Редагування існуючого користувача", editButton);
    }

    @FXML
    void exit(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("mainmenu.fxml", "MedStockTrack - Головне меню", exitButton);
    }

}
