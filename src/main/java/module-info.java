module com.medstocktrack.medstockapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;
    requires java.naming;
    requires org.apache.poi.ooxml;


    opens com.medstocktrack.medstockapp to javafx.fxml;
    opens com.medstocktrack.medstockapp.controllers to javafx.fxml;
    opens com.medstocktrack.medstockapp.controllers.storage to javafx.fxml;
    opens com.medstocktrack.medstockapp.controllers.register to javafx.fxml;
    opens com.medstocktrack.medstockapp.controllers.journal to javafx.fxml;
    opens com.medstocktrack.medstockapp.controllers.admin to javafx.fxml;

    opens com.medstocktrack.medstockapp.model to javafx.base;
    opens com.medstocktrack.medstockapp.managers to javafx.base;

    exports com.medstocktrack.medstockapp;
}