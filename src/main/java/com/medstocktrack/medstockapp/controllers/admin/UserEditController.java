package com.medstocktrack.medstockapp.controllers.admin;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import com.medstocktrack.medstockapp.model.NewUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

public class UserEditController {

    @FXML
    private Button addButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField loginField;

    @FXML
    private Label msg;

    @FXML
    private PasswordField passwordConf;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<String> roleChoice;

    @FXML
    void addUser(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("admin/userAdd.fxml", "MedStockTrack - Створення нового користувача", addButton);
    }

    @FXML
    void editUser(ActionEvent event) {
        if (loginField.getText().trim().isEmpty()){
            msg.setText("Введіть логін існуючого користувача!");
            msg.setVisible(true);
            return;
        }
        if (!passwordField.getText().trim().equals(passwordConf.getText().trim())){
            msg.setText("Паролі мають співпадати!");
            msg.setVisible(true);
            return;
        }

        String newPassword = !passwordField.getText().trim().isEmpty() ? passwordField.getText().trim() : null;
        String newRole = Objects.equals(roleChoice.getValue(), "-") ? null : roleChoice.getValue();

        NewUser newUser = new NewUser(loginField.getText().trim(), newPassword, newRole);
        int result = newUser.editUser(loginField.getText().trim());
        if (result == 1){
            msg.setText("Інформація про користувача успішно оновлена!");
            msg.setVisible(true);
        } else {
            msg.setText("Помилка! Перевірте дані та спробуйте ще!");
            msg.setVisible(true);
        }
    }

    @FXML
    void exit(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("mainmenu.fxml", "MedStockTrack - Головне меню", exitButton);
    }

}
