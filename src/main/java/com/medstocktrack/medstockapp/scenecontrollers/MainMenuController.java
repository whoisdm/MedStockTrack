package com.medstocktrack.medstockapp.scenecontrollers;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import com.medstocktrack.medstockapp.model.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button adminButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button logButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button storageButton;

    @FXML
    void exit(ActionEvent event) {
        SceneSwitcherUtil.closeScene(exitButton);
    }

    @FXML
    void toAdmin(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("admin/adminMain.fxml", "MedStockTrack - Панель адміністратора", registerButton);
    }

    @FXML
    void toLog(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("journal/journalMain.fxml", "MedStockTrack - Робота з журналом складу", registerButton);
    }

    @FXML
    void toRegister(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("register/registerMain.fxml", "MedStockTrack - Робота з реєстром ліків", registerButton);
    }

    @FXML
    void toStorage(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("storage/storageMain.fxml", "MedStockTrack - Робота зі складом", storageButton);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String currentUser = CurrentUser.getCurrentUser().getRole();
        if (currentUser.trim().equals("manager")) {
            logButton.setVisible(true);
        }
        if (currentUser.trim().equals("admin")) {
            logButton.setVisible(true);
            adminButton.setVisible(true);
        }
    }
}
