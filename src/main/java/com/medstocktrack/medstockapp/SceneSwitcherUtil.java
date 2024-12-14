package com.medstocktrack.medstockapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneSwitcherUtil {

    public static void switchScene(String file , String title, Button button, boolean isSameSize) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneSwitcherUtil.class.getResource(file)));
        Stage stage = (Stage) button.getScene().getWindow();

        if (isSameSize) {
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);
        }

        Scene new_scene = new Scene(root);
        stage.setTitle(title);
        stage.getIcons().add(new Image(Objects.requireNonNull(SceneSwitcherUtil.class.getResource("img/medical_bag.png")).openStream()));
        stage.setScene(new_scene);

        stage.show();
    }

    public static void switchScene(String file , String title, Button button) throws IOException {
        switchScene(file, title, button, true);
    }


    public static void closeScene(Button button){
        Stage current_stage = (Stage) button.getScene().getWindow();
        current_stage.close();
    }
}
