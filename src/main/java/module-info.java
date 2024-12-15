module com.medstocktrack.medstockapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;
    requires java.naming;


    opens com.medstocktrack.medstockapp to javafx.fxml;
    opens com.medstocktrack.medstockapp.controller to javafx.fxml;
    opens com.medstocktrack.medstockapp.controller.storage to javafx.fxml;
    opens com.medstocktrack.medstockapp.controller.register to javafx.fxml;
    opens com.medstocktrack.medstockapp.controller.journal to javafx.fxml;

    opens com.medstocktrack.medstockapp.model to javafx.base;

    exports com.medstocktrack.medstockapp;
}