package com.medstocktrack.medstockapp.controller.storage;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class StorageMainController {

    @FXML
    private Button addButton;

    @FXML
    private Button destroyButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button viewButton;

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
    void removeStorage(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("storage/storageRemove.fxml","MedStockTrack - Реєстрація вивезення", removeButton);
    }

    @FXML
    void viewStorage(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("storage/storageView.fxml","MedStockTrack - Перегляд складу", viewButton);
    }

}
