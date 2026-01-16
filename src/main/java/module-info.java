module dev.reportit.reportit {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens dev.reportit.reportit to javafx.fxml;
    exports dev.reportit.reportit;
}