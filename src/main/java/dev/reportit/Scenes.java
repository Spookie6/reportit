package dev.reportit;

public enum Scenes {
    CHATUI("chat/Chat/ChatUi.fxml"),
    THREAD("chat/Thread/Thread.fxml"),
    MIDDLE("home/MiddleScreen.fxml"),
    ADDINCIDENTDIALOG("report/AddIncidentDialog.fxml"),
    GETHELPDIALOG("report/GetHelpDialog.fxml"),
    INCIDENTSNEARYOUDIALOG("report/IncidentsNearYou.fxml"),
    REPORTSCREEN("report/ReportScreen.fxml"),
    REGISTER("login/RegisterScreen.fxml"),
    LOGIN("login/LoginScreen.fxml");

    private final String fxml;

    Scenes(String fxml) {
        this.fxml = fxml;
    }

    public String getFxml() {
        return this.fxml;
    }
}
