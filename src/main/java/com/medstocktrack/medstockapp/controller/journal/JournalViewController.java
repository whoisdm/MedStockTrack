package com.medstocktrack.medstockapp.controller.journal;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import com.medstocktrack.medstockapp.model.Action;
import com.medstocktrack.medstockapp.model.JournalManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JournalViewController implements Initializable {

    @FXML
    private TableColumn<Action, String> actionDate;

    @FXML
    private TableColumn<Action, String> actionMed;

    @FXML
    private TableColumn<Action, Integer> actionSize;

    @FXML
    private TableColumn<Action, String> actionType;

    @FXML
    private TableColumn<Action, String> actionUser;

    @FXML
    private DatePicker endDate;

    @FXML
    private Button exitButton;

    @FXML
    private Button makeButton;

    @FXML
    private DatePicker startDate;

    @FXML
    private TableView<Action> table;

    @FXML
    private ChoiceBox<String> typeField;

    @FXML
    private TextField userField;

    @FXML
    private TextField limitField;

    @FXML
    private Label msg;

    private JournalManager journalManager;

    @FXML
    void exit(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("mainmenu.fxml", "MedStockTrack - Головне меню", exitButton);
    }

    @FXML
    void makeJournal(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("journal/journalMake.fxml", "MedStockTrack - Створення звітів", makeButton);
    }

    @FXML
    void searchJournal(ActionEvent event) {
        msg.setVisible(false);
        String type = typeField.getValue().equals("-") ? "" : typeField.getValue();

        String limit = null;
        if (limitField.getText().trim().matches("^[1-9]\\d*$")){
            limit = limitField.getText().trim();
        } else if (!limitField.getText().trim().isEmpty()){
            msg.setVisible(true);
            msg.setText("Обмеження має бути цілим числом!");
            return;
        }

        ObservableList<Action> actions = journalManager.getActionList(startDate.getValue(), endDate.getValue(), type, userField.getText().trim(), limit);
        table.setItems(actions);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actionUser.setCellValueFactory(new PropertyValueFactory<Action, String>("actionUser"));
        actionType.setCellValueFactory(new PropertyValueFactory<Action, String>("actionType"));
        actionDate.setCellValueFactory(new PropertyValueFactory<Action, String>("actionDate"));
        actionMed.setCellValueFactory(new PropertyValueFactory<Action, String>("actionMedicine"));
        actionSize.setCellValueFactory(new PropertyValueFactory<Action, Integer>("actionQuantity"));
        journalManager = new JournalManager();
    }
}
