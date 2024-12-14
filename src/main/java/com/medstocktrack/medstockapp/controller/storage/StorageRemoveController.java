package com.medstocktrack.medstockapp.controller.storage;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import com.medstocktrack.medstockapp.model.StorageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StorageRemoveController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button destroyButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField medNum;

    @FXML
    private TextField medReg;

    @FXML
    private Label msg;

    @FXML
    private Button viewButton;

    private StorageManager storageManager;

    @FXML
    void addStorage(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("storage/storageAdd.fxml","MedStockTrack - Реєстрація завезення", addButton);
    }

    @FXML
    void destroyStorage(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("storage/storageDestroy.fxml","MedStockTrack - Реєстрація списання", destroyButton);
    }

    @FXML
    void exit(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("mainmenu.fxml", "MedStockTrack - Головне меню", exitButton);
    }

    @FXML
    void removeStorage(ActionEvent event) {
        if (medNum.getText().trim().matches("^[1-9]\\d*$") && medReg.getText().trim().matches("^UA/\\d{4,5}/\\d{2}/\\d{2}$")){
            int result = storageManager.removeMed(medReg.getText().trim(), Integer.parseInt(medNum.getText().trim()), false);
            if (result > 0){
                msg.setText("Вивезення успішно зареєстровано!");
            } else if (result == 0){
                msg.setText("Сталася помилка, на складі недостатньо ліків!");
            } else {
                msg.setText("Сталася помилка, можливо введено неіснуюче РП!");
            }
            msg.setVisible(true);
        } else {
            msg.setText("Некоректне РП або кількість вивезення!");
            msg.setVisible(true);
        }
    }

    @FXML
    void viewStorage(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("storage/storageView.fxml","MedStockTrack - Перегляд складу", viewButton);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        storageManager = new StorageManager();
    }
}
