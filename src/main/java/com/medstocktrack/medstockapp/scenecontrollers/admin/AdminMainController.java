package com.medstocktrack.medstockapp.scenecontrollers.admin;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AdminMainController {

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button exitButton;

    @FXML
    void addUser(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("admin/userAdd.fxml", "MedStockTrack - Створення нового користувача", addButton);
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
