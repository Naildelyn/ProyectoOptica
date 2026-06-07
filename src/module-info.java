module visionmaster {
    requires javafx.controls;
    requires javafx.fxml;
    opens visionmaster to javafx.fxml;
    opens visionmaster.ui to javafx.fxml;
    exports visionmaster;
    exports visionmaster.model;
    exports visionmaster.enums;
    exports visionmaster.interfaces;
    exports visionmaster.ui;
}
