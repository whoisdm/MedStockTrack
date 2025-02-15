package com.medstocktrack.medstockapp.controllers.storage;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import com.medstocktrack.medstockapp.model.Storage;
import com.medstocktrack.medstockapp.managers.StorageManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StorageViewController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button destroyButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField medName;

    @FXML
    private TextField medReg;

    @FXML
    private TableColumn<Storage, String> name;

    @FXML
    private TableColumn<Storage, Integer> number;

    @FXML
    private Button removeButton;

    @FXML
    private TableView<Storage> table;

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
    void removeStorage(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("storage/storageRemove.fxml","MedStockTrack - Реєстрація вивезення", removeButton);
    }

    @FXML
    void searchStorage(ActionEvent event) {
        ObservableList<Storage> storages = storageManager.getStorageList(medReg.getText().trim() , medName.getText().trim());
        table.setItems(storages);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<>("medName"));
        number.setCellValueFactory(new PropertyValueFactory<>("medNumber"));
        storageManager = new StorageManager();
    }
}
