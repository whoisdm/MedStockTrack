package com.medstocktrack.medstockapp.controllers;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import com.medstocktrack.medstockapp.model.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button authButton;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passField;

    @FXML
    private Label errLabel;

    @FXML
    void login(ActionEvent event) {
        if (loginField.getText().trim().matches("^[a-zA-Z0-9]*$") && passField.getText().trim().matches("^[a-zA-Z0-9]*$")) {
            int result = CurrentUser.getCurrentUser().isExisting(loginField.getText().trim(), passField.getText().trim());
            if (result == 1) {
                try {
                    SceneSwitcherUtil.switchScene("mainmenu.fxml", "MedStockTrack - Головне меню", authButton, false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (result == -1) {
                errLabel.setVisible(true);
            }
        } else {
            errLabel.setVisible(true);
        }
    }

}
