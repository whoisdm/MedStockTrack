package com.medstocktrack.medstockapp;

import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;

public class TableCopyUtil {
    public static <T> void enableCellCopying(TableView<T> tableView) {
        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.C && event.isControlDown()) {
                TablePosition<?, ?> pos = tableView.getFocusModel().getFocusedCell();
                if (pos != null) {
                    Object cellData = tableView.getColumns()
                            .get(pos.getColumn())
                            .getCellData(pos.getRow());

                    if (cellData != null) {
                        ClipboardContent content = new ClipboardContent();
                        content.putString(cellData.toString());
                        Clipboard.getSystemClipboard().setContent(content);
                    }
                }
            }
        });
    }
}
