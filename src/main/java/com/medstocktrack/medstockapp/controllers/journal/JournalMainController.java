package com.medstocktrack.medstockapp.controllers.journal;

import com.medstocktrack.medstockapp.SceneSwitcherUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class JournalMainController {

    @FXML
    private Button makeButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button viewButton;

    @FXML
    void exit(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("mainmenu.fxml", "MedStockTrack - Головне меню", exitButton);
    }

    @FXML
    void makeJournal(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("journal/journalMake.fxml", "MedStockTrack - Створення звітів", makeButton);
    }

    @FXML
    void viewJournal(ActionEvent event) throws IOException {
        SceneSwitcherUtil.switchScene("journal/journalView.fxml", "MedStockTrack - Перегляд журналу складу", viewButton);
    }

}
