module se233.chapter1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    opens se233.chapter1 to javafx.fxml;
    exports se233.chapter1;
}