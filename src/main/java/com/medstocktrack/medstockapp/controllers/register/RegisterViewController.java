package com.medstocktrack.medstockapp.controllers.register;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import com.medstocktrack.medstockapp.TableCopyUtil;
import com.medstocktrack.medstockapp.model.CurrentUser;
import com.medstocktrack.medstockapp.model.Medicine;
import com.medstocktrack.medstockapp.managers.RegisterManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterViewController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button exitButton;

    @FXML
    private TableColumn<Medicine, String> medActive;

    @FXML
    private TableColumn<Medicine, String> medForm;

    @FXML
    private TableColumn<Medicine, String> medID;

    @FXML
    private TableColumn<Medicine, String> medName;

    @FXML
    private TableColumn<Medicine, Double> medPrice;

    @FXML
    private TableColumn<Medicine, String> medSize;

    @FXML
    private TableColumn<Medicine, String> medType;

    @FXML
    private TextArea msg;

    @FXML
    private Button removeButton;

    @FXML
    private TableView<Medicine> table;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField activeField;


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
    void removeRegister(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("register/registerRemove.fxml", "MedStockTrack - Видалення ліків з реєстру", removeButton);
    }

    @FXML
    void searchRegister(ActionEvent event) {
        ObservableList<Medicine> medicines = registerManager.getMedicineList(idField.getText().trim(),
                nameField.getText().trim(),
                typeField.getText().trim(),
                activeField.getText().trim());
        table.setItems(medicines);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String currentUser = CurrentUser.getCurrentUser().getRole();
        if (currentUser.trim().equals("worker")) {
            addButton.setVisible(false);
            removeButton.setVisible(false);
        }

        medActive.setCellValueFactory(new PropertyValueFactory<>("medicineActive"));
        medForm.setCellValueFactory(new PropertyValueFactory<>("medicineForm"));
        medID.setCellValueFactory(new PropertyValueFactory<>("medicineID"));
        medName.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        medPrice.setCellValueFactory(new PropertyValueFactory<>("medicinePrice"));
        medSize.setCellValueFactory(new PropertyValueFactory<>("medicineSize"));
        medType.setCellValueFactory(new PropertyValueFactory<>("medicineType"));
        registerManager = new RegisterManager();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                msg.setVisible(true);
                msg.setText(String.format("Виробник: %s, %s.\nПротипоказання: %s %s",
                        newSelection.getProducer().getProducerName(),
                        newSelection.getProducer().getProducerCountry(),
                        newSelection.getMedicineContr(),
                        newSelection.isMedicinePrescription() == 1 ? "Потребує рецепту." : "Не потребує рецепту."));
            } else {
                msg.setText("");
                msg.setVisible(false);
            }
        });

        TableCopyUtil.enableCellCopying(table);
    }
}
