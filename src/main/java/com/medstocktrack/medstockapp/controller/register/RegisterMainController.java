package com.medstocktrack.medstockapp.controller.register;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import com.medstocktrack.medstockapp.model.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterMainController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button viewButton;

    @FXML
    void addRegister(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("register/registerAdd.fxml", "MedStockTrack - Додавання ліків до реєстру", addButton);
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
        String currentUser = CurrentUser.getCurrentUser().getRole();
        if (currentUser.trim().equals("worker")) {
            addButton.setVisible(false);
            removeButton.setVisible(false);
        }
    }
}

