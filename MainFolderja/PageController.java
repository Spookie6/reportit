import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PageController {
    // Database Setup
    private static final String DB_URL = "";
    private static final String DB_USER = "";
    private static final String DB_PASS = "";

    private double xOffset = 0;
    private double yOffset = 0;

    private static int activeThreadId = -1;
    private static String activeThreadTitle = "Chat";

    @FXML private StackPane moodContainer;
    @FXML private Label lblMoodValue;
    @FXML private Label lblWelcome;
    @FXML private Circle profileCircle;
    @FXML private StackPane rootStackPane;

    @FXML private TextField tfTitle;
    @FXML private TextField tfLocation;
    @FXML private TextArea taDetails;
    @FXML private VBox featuredContainer;
    @FXML private VBox nearYouContainer;
    @FXML private TextField tfSearchArea;

    @FXML private VBox threadContainer;
    @FXML private VBox chatContainer;
    @FXML private Label lblChatTitle;
    @FXML private TextField tfChatInput;

    // --- NEW: Added for the popup ---
    @FXML private TextField tfNewThreadTitle;

    public void initialize() {
        String username = getUsernameFromDatabase();
        if (lblWelcome != null) {
            lblWelcome.setText("Welcome, " + username);
        }
        loadProfilePicture();

        if (featuredContainer != null) loadFeaturedIncidents();
        if (nearYouContainer != null) loadIncidentsNearYou();

        if (threadContainer != null) {
            loadThreads();
        }

        if (chatContainer != null) {
            if (lblChatTitle != null) lblChatTitle.setText(activeThreadTitle);
            loadChatMessages();
        }
    }

    private void loadProfilePicture() {
        if (profileCircle == null) return;
        String imagePath = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png";

        /* try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT profile_img_url FROM users WHERE id = 1"; // Assuming user ID 1
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String dbUrl = rs.getString("profile_img_url");
                if (dbUrl != null && !dbUrl.isEmpty()) imagePath = dbUrl;
            }
        } catch (Exception e) { e.printStackTrace(); }
        */

        try {
            Image img = new Image(imagePath, false);
            if (img.isError()) profileCircle.setFill(Color.LIGHTGRAY);
            else profileCircle.setFill(new ImagePattern(img));
        } catch (Exception e) { profileCircle.setFill(Color.LIGHTGRAY); }
    }

    private String getUsernameFromDatabase() {
        String username = "Guest";
        /* try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT username FROM users WHERE id = 1"; // Assuming user ID 1
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
            }
        } catch (Exception e) { e.printStackTrace(); }
        */
        return username;
    }

    @FXML
    void onMoodClicked(MouseEvent event) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(createMenuItem("Good", "mood-good"), createMenuItem("Decent", "mood-decent"), createMenuItem("Bad", "mood-bad"));
        menu.show(moodContainer, Side.BOTTOM, 0, 0);
    }

    private MenuItem createMenuItem(String text, String style) {
        MenuItem item = new MenuItem(text);
        item.setOnAction(e -> updateMood(" " + text, style));
        return item;
    }

    private void updateMood(String text, String styleClass) {
        lblMoodValue.setText(text);
        lblMoodValue.getStyleClass().removeAll("mood-good", "mood-decent", "mood-bad");
        lblMoodValue.getStyleClass().add(styleClass);
        saveMoodToDatabase(text.trim());
    }

    private void saveMoodToDatabase(String mood) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(">>> SAVING MOOD: " + mood + " at " + now);
        /*
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO user_moods (user_id, mood, created_at) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, 1); // User ID
            stmt.setString(2, mood);
            stmt.setString(3, now.toString());
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        */
    }

    @FXML
    void onSubmitIncident(ActionEvent event) {
        String title = tfTitle.getText();
        String location = tfLocation.getText();
        String details = taDetails.getText();

        if (title == null || title.trim().isEmpty() || location == null || location.trim().isEmpty() || details == null || details.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in ALL fields.");
            alert.showAndWait();
            return;
        }

        saveIncidentToDatabase(title, location, details);
        onClose(event);
    }

    private void saveIncidentToDatabase(String title, String location, String details) {
        System.out.println(">>> SAVING INCIDENT: " + title);
        /* try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO incidents (title, location, details, created_at) VALUES (?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, location);
            stmt.setString(3, details);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        */
    }

    public static class Incident {
        String title, location, details, time;
        public Incident(String t, String l, String d, String tm) { title=t; location=l; details=d; time=tm; }
    }

    private void loadFeaturedIncidents() {
        List<Incident> incidents = new ArrayList<>();
        /* try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT title, location, details, created_at FROM incidents ORDER BY created_at DESC LIMIT 10";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                incidents.add(new Incident(
                    rs.getString("title"),
                    rs.getString("location"),
                    rs.getString("details"),
                    rs.getString("created_at")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        */

        if (incidents.isEmpty()) {
            incidents.add(new Incident("Suspicious Activity", "Downtown", "Saw someone looking into cars.", "2 hours ago"));
            incidents.add(new Incident("Lost Dog", "Central Park", "Golden Retriever, answers to 'Buddy'.", "5 hours ago"));
        }
        populateList(featuredContainer, incidents);
    }

    @FXML void onSearchNearYou(ActionEvent event) { loadIncidentsNearYou(); }

    private void loadIncidentsNearYou() {
        if (nearYouContainer == null) return;
        nearYouContainer.getChildren().clear();
        List<Incident> incidents = new ArrayList<>();

        String searchText = (tfSearchArea != null) ? tfSearchArea.getText() : "";
        /* try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM incidents WHERE location LIKE ? LIMIT 10";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                incidents.add(new Incident(
                    rs.getString("title"),
                    rs.getString("location"),
                    rs.getString("details"),
                    rs.getString("created_at")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        */

        if (incidents.isEmpty()) {
            incidents.add(new Incident("Noise Complaint", "0.5km away", "Loud music.", "30 min ago"));
            incidents.add(new Incident("Bike Theft", "1.2km away", "Blue mountain bike.", "Today"));
        }
        populateList(nearYouContainer, incidents);
    }

    private void populateList(VBox container, List<Incident> incidents) {
        for (Incident inc : incidents) {
            StackPane card = new StackPane();
            card.getStyleClass().add("listItem");
            card.setCursor(javafx.scene.Cursor.HAND);
            VBox vBox = new VBox(2);
            vBox.setAlignment(Pos.CENTER_LEFT);
            vBox.getChildren().addAll(new Label(inc.title), new Label(inc.time + " • " + inc.location));
            card.getChildren().add(vBox);
            card.setOnMouseClicked(e -> showIncidentDetails(inc));
            container.getChildren().add(card);
        }
    }

    private void showIncidentDetails(Incident inc) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Incident Details");
        alert.setHeaderText(inc.title);
        alert.setContentText("Location: " + inc.location + "\nTime: " + inc.time + "\n\n" + inc.details);
        alert.showAndWait();
    }

    @FXML void onEmergencyClicked(MouseEvent event) { openWebpage("https://www.politie.nl/onderwerpen/contact-met-112.html"); }
    @FXML void onNonEmergencyClicked(MouseEvent event) { openWebpage("https://www.politie.nl/informatie/wanneer-bel-ik-0900-8844.html"); }

    private void openWebpage(String url) {
        try { java.awt.Desktop.getDesktop().browse(new java.net.URI(url)); } catch (Exception e) {}
    }

    public static class ThreadItem {
        int id;
        String title, preview, time;
        public ThreadItem(int id, String t, String p, String time) { this.id = id; this.title = t; this.preview = p; this.time = time; }
    }

    private void loadThreads() {
        threadContainer.getChildren().clear();
        List<ThreadItem> threads = new ArrayList<>();

        /* try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT id, title, last_message, created_at FROM threads ORDER BY created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                threads.add(new ThreadItem(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("last_message"),
                    rs.getString("created_at")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        */

        if (threads.isEmpty()) {
            threads.add(new ThreadItem(1, "Harassment Report", "I need advice...", "9:41 AM"));
            threads.add(new ThreadItem(2, "Support Group", "Is anyone free?", "Yesterday"));
        }

        for (ThreadItem t : threads) {
            HBox item = createThreadItem(t);
            threadContainer.getChildren().add(item);
        }
    }

    private HBox createThreadItem(ThreadItem t) {
        HBox hbox = new HBox(15);
        hbox.getStyleClass().add("thread-item");
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setCursor(javafx.scene.Cursor.HAND);
        hbox.setOnMouseClicked(e -> onThreadClicked(t));

        VBox contentBox = new VBox(3);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        Label lblTitle = new Label(t.title);
        lblTitle.getStyleClass().add("thread-name");
        lblTitle.setStyle("-fx-font-weight: bold;");
        Label lblPreview = new Label(t.preview);
        lblPreview.getStyleClass().add("thread-preview");
        contentBox.getChildren().addAll(lblTitle, lblPreview);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label lblTime = new Label(t.time);
        lblTime.getStyleClass().add("date-label");

        hbox.getChildren().addAll(contentBox, spacer, lblTime);
        return hbox;
    }

    private void onThreadClicked(ThreadItem t) {
        activeThreadId = t.id;
        activeThreadTitle = t.title;
        switchScene(null, "/javaFx2/chatS/Chat/ChatUI.fxml");
    }


    @FXML
    void onNewThreadClicked(ActionEvent event) {
        // Opens the popup
        openOverlay("/javaFx2/chatS/Thread/NewThreadDialog.fxml");
    }

    @FXML
    void onCreateThread(ActionEvent event) {
        String title = tfNewThreadTitle.getText();

        if (title == null || title.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please enter a title");
            alert.showAndWait();
            return;
        }

        System.out.println(">>> CREATING THREAD: " + title);

        /* try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO threads (title, last_message, created_at) VALUES (?, 'New chat', NOW())";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        */

        if (threadContainer != null) {
            loadThreads();
        }

        onClose(event);
    }

    public static class ChatMessage {
        boolean isMe;
        String content, time;
        public ChatMessage(boolean me, String c, String t) { isMe = me; content = c; time = t; }
    }

    private void loadChatMessages() {
        chatContainer.getChildren().clear();
        List<ChatMessage> messages = new ArrayList<>();

        /* try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT is_me, content, created_at FROM messages WHERE thread_id = ? ORDER BY created_at ASC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, activeThreadId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new ChatMessage(
                    rs.getBoolean("is_me"),
                    rs.getString("content"),
                    rs.getString("created_at")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        */

        if (messages.isEmpty()) {
            messages.add(new ChatMessage(false, "Hello, how can I help?", "9:41 AM"));
            messages.add(new ChatMessage(true, "I saw something earlier.", "9:42 AM"));
        }

        for (ChatMessage m : messages) {
            addMessageToChat(m);
        }
    }

    private void addMessageToChat(ChatMessage m) {
        HBox row = new HBox();
        row.setAlignment(m.isMe ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        Label bubble = new Label(m.content);
        bubble.setWrapText(true);
        bubble.getStyleClass().add(m.isMe ? "bubble-right" : "bubble-left");
        row.getChildren().add(bubble);
        chatContainer.getChildren().add(row);
    }

    @FXML
    void onSendMessage(ActionEvent event) {
        String text = tfChatInput.getText();
        if (text == null || text.trim().isEmpty()) return;
        addMessageToChat(new ChatMessage(true, text, "Now"));
        tfChatInput.clear();
        saveMessageToDatabase(text);
    }

    private void saveMessageToDatabase(String content) {
        /* try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO messages (thread_id, is_me, content, created_at) VALUES (?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, activeThreadId);
            stmt.setBoolean(2, true); // True because 'I' sent it
            stmt.setString(3, content);
            stmt.executeUpdate();

            String updateThread = "UPDATE threads SET last_message = ? WHERE id = ?";
            PreparedStatement threadStmt = conn.prepareStatement(updateThread);
            threadStmt.setString(1, content);
            threadStmt.setInt(2, activeThreadId);
            threadStmt.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
        */
    }

    @FXML
    void onBackFromChat(ActionEvent event) {
        switchScene(event, "/javaFx2/chatS/Thread/Thread.fxml");
    }

    @FXML void onAddIncidentCardClicked(MouseEvent event) { openOverlay("/javaFx2/reportS/AddIncidentDialog.fxml"); }
    @FXML void onFeaturedCardClicked(MouseEvent event) { openOverlay("/javaFx2/reportS/FeaturedIncidentsDialog.fxml"); }
    @FXML void onNearYouCardClicked(MouseEvent event) { openOverlay("/javaFx2/reportS/IncidentsNearYouDialog.fxml"); }
    @FXML void onGetHelpCardClicked(MouseEvent event) { openOverlay("/javaFx2/reportS/GetHelpDialog.fxml"); }

    private void openOverlay(String fxmlPath) {
        if (rootStackPane == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent overlay = loader.load();
            rootStackPane.getChildren().add(overlay);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    void onClose(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Node overlay = source;
            while (overlay != null) {
                if (overlay.getStyleClass().contains("dialogOverlay")) break;
                overlay = overlay.getParent();
            }
            if (overlay != null && overlay.getParent() instanceof javafx.scene.layout.Pane) {
                ((javafx.scene.layout.Pane) overlay.getParent()).getChildren().remove(overlay);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void onWindowPressed(MouseEvent event) { xOffset = event.getSceneX(); yOffset = event.getSceneY(); }
    @FXML private void onWindowDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset); stage.setY(event.getScreenY() - yOffset);
    }

    @FXML void onHomeClicked(MouseEvent event) { switchScene(event, "/javaFx2/homeS/HomeScreen.fxml"); }
    @FXML void onAddClicked(MouseEvent event) { switchScene(event, "/javaFx2/reportS/ReportScreen.fxml"); }
    @FXML void onThreadsClicked(MouseEvent event) { switchScene(event, "/javaFx2/chatS/Thread/Thread.fxml"); }

    private void switchScene(Event event, String fxmlPath) {
        try {
            Stage stage;
            if (event != null) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } else {
                if (threadContainer != null && threadContainer.getScene() != null) {
                    stage = (Stage) threadContainer.getScene().getWindow();
                } else if (chatContainer != null && chatContainer.getScene() != null) {
                    stage = (Stage) chatContainer.getScene().getWindow();
                } else {
                    return;
                }
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
}