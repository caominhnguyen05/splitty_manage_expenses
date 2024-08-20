package client.scenes;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Locale;
import java.util.ResourceBundle;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXToggleButton;
import commons.Event;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import static javafx.scene.input.KeyCode.ENTER;

public class InvitationViewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    public boolean isHighContrast;
    @FXML
    public AnchorPane biggestPane;
    @FXML
    public HBox inviteCodeBox;
    private Event event;
    public ResourceBundle bundle;

    @FXML
    public Label givePeopleInviteCode;
    @FXML
    public Label invitePeopleByEmail;
    @FXML
    public HBox button;
    @FXML
    public Label sendInvitesLabel;
    @FXML
    public ImageView backArrow;
    @FXML
    public Rectangle backRectangle;
    @FXML
    public AnchorPane backToStartScreenButton;
    @FXML
    public ImageView sendIcon;
    @FXML
    public ImageView mainImage;
    @FXML
    public JFXToggleButton contrastToggle;
    @FXML
    public ImageView copyButton;
    @FXML
    public Label copiedLabel;

    /**
     * Constructs a new InvitationViewController instance
     *
     * @param server   the ServerUtils instance used for server communication
     * @param mainCtrl the MainCtrl instance used for controlling the main application flow
     */
    @Inject
    public InvitationViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    private Label feedbackLabel;

    @FXML
    private Label eventName;

    @FXML
    TextArea emailTextBox;

    @FXML
    Label inviteCodeLabel;

    @FXML
    void initialize() {
        eventName.setStyle("-fx-font-family: 'Century Gothic Bold'");
        mainImage.setImage(new Image("/invite_people.png"));
        sendIcon.setImage(new Image("/send_invite.png"));
        copyButton.setImage(new Image("/copy.png"));
        EventOverviewCtrl.configureBackButton(backToStartScreenButton, backArrow, backRectangle);

        button.setOnMouseClicked(e -> sendInviteButtonClicked());
        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1));
        copyButton.setOnMouseEntered(e -> copyButton.setOpacity(0.5));
        copyButton.setOnMouseExited(e -> copyButton.setOpacity(1));
        emailTextBox.setOnKeyPressed(e -> {
            if (e.getCode() == ENTER && e.isControlDown()) {
                e.consume();
                sendInviteButtonClicked();
            }
        });
        contrastToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                isHighContrast = true;
                changeToHighContrast();
            } else {
                isHighContrast = false;
                resetNormalColors();
            }
        });
    }

    private void changeToHighContrast() {
        biggestPane.setStyle("-fx-background-color: black");
        eventName.setTextFill(Color.WHITE);
        givePeopleInviteCode.setTextFill(Color.WHITE);
        invitePeopleByEmail.setTextFill(Color.WHITE);
        inviteCodeBox.setStyle("-fx-background-color: #75e9fc; -fx-background-radius: 15");

        contrastToggle.setTextFill(Color.WHITE);
        contrastToggle.setStyle("background-color: black");
        backRectangle.setOpacity(1);
    }

    private void resetNormalColors() {
        biggestPane.setStyle("-fx-background-color: white");
        eventName.setTextFill(Color.BLACK);
        givePeopleInviteCode.setTextFill(Color.BLACK);
        invitePeopleByEmail.setTextFill(Color.BLACK);
        inviteCodeBox.setStyle("-fx-background-color:  #fff3de; -fx-background-radius: 15");

        contrastToggle.setTextFill(Color.BLACK);
        contrastToggle.setStyle("background-color: white");
        backRectangle.setOpacity(0.62);
    }

    @FXML
    void sendInviteButtonClicked() {
        String emails = emailTextBox.getText();
        String[] separatedEmails = emails.split("\n");

        // Empty the text box
        emailTextBox.setText("");

        StringBuilder feedback = new StringBuilder();
        for (String email : separatedEmails) {
            // Send a POST request to the API endpoint containing the email of recipient

            if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")) {
                feedback.append(bundle.getString("couldNotSendEmailTo")).append(" ")
                        .append(email).append(". [#1]\n");
                continue;
            }
            try {
                server.sendMail(email, this.event);
                feedback.append(bundle.getString("emailSuccessfullySentTo")).append(": ")
                        .append(email).append("\n");
            } catch (RuntimeException e) {
                feedback.append(bundle.getString("couldNotSendEmailTo")).append(": ")
                        .append(email).append(". [#2]\n");
            }
        }
        feedbackLabel.setText(feedback.toString());
    }

    void refresh() {
        eventName.setText(event.name);
        inviteCodeLabel.setText(Long.toString(event.id));
        feedbackLabel.setText("");
        changeLanguage(this.bundle.getLocale().getDisplayLanguage());
        copiedLabel.setText(bundle.getString("copiedLabel"));
        copiedLabel.setVisible(false);
        if (isHighContrast) {
            contrastToggle.setSelected(true);
            changeToHighContrast();
        } else {
            contrastToggle.setSelected(false);
            resetNormalColors();
        }
    }

    /**
     * Changes the language of the application based on the provided language code.
     * Updates the text properties of various UI elements to reflect the new language.
     *
     * @param languageCode The language code representing the desired language
     */
    public void changeLanguage(String languageCode) {
        this.bundle = ResourceBundle.getBundle("languages.messages", Locale.of(languageCode));
        givePeopleInviteCode.setText(bundle.getString("givePeopleInviteCode"));
        invitePeopleByEmail.setText(bundle.getString("invitePeopleByEmail"));
        sendInvitesLabel.setText(bundle.getString("sendInvitesButton"));
        contrastToggle.setText(bundle.getString("highContrast"));
    }

    /**
     * Event handler that handles the navigation back to event overview
     */
    public void back() {
        mainCtrl.showOverview(event, this.bundle, this.isHighContrast);
    }

    /**
     * Use keyboard shortcuts for sending an invitation
     * and for going back to Event Page
     *
     * @param e the key event
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                sendInviteButtonClicked();
                break;
            case ESCAPE:
                back();
                break;
            default:
                break;
        }
    }

    /**
     * Lets the user copy the invite code.
     */
    public void copyCode() {
        StringSelection selection = new StringSelection(String.valueOf(event.id));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);

        copiedLabel.setVisible(true);
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
        pauseTransition.setOnFinished(event ->
                copiedLabel.setVisible(false));
        pauseTransition.play();
    }

    /**
     * Sets the event of this instance
     * @param event the event to set it as
     */
    public void setEvent(Event event) {
        server.closeWebSocketListener();

        this.event = event;

        try {
            this.server.openWebSocketEventListener(Long.toString(event.id), x -> {
                this.event = x;
                refresh();
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Getter for the current event
     * @return the current event
     */
    public Event getEvent() {
        return this.event;
    }
}
