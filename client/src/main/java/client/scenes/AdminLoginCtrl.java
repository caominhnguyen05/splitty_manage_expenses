package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Locale;
import java.util.ResourceBundle;

public class AdminLoginCtrl {
    @FXML
    public Label adminLoginTitle;
    @FXML
    public Label serverPasswordLabel;
    @FXML
    public Button submitButton;
    @FXML
    public Button cancelButton;
    @FXML
    public ImageView backArrow;
    @FXML
    public Rectangle backRectangle;
    @FXML
    public AnchorPane backToStartScreenButton;
    @FXML
    private TextField input;

    @FXML
    private Label error;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    public ResourceBundle bundle;

    /**
     * Constructor for this class, initializes this.server and this.mainCtrl
     *
     * @param server   reference to the Commons.ServerUtils class, for api communication
     * @param mainCtrl reference to the mainCtrl class, for navigation through views
     */
    @Inject
    public AdminLoginCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the Admin Login view.
     */
    @FXML
    public void initialize() {
        EventOverviewCtrl.configureBackButton(backToStartScreenButton, backArrow, backRectangle);
        input.setStyle("-fx-font-family: 'Century Gothic';-fx-font-size: 15;" +
                "-fx-background-radius: 15");
        submitButton.setOnMouseEntered(e ->
                submitButton.setStyle("-fx-background-radius: 12;-fx-background-color:  #99c9bb"));
        submitButton.setOnMouseExited(e ->
                submitButton.setStyle("-fx-background-radius: 12;-fx-background-color:   #C9E4DE"));
        cancelButton.setOnMouseEntered(e ->
                cancelButton.setStyle("-fx-background-radius: 12;-fx-background-color:  #f1d1bd"));
        cancelButton.setOnMouseExited(e ->
                cancelButton.setStyle("-fx-background-radius: 12;-fx-background-color:  #f5e2d2"));
        input.textProperty().addListener((obs, oldVal, newVal)
                -> submitButton.setDisable(newVal.isEmpty()));
        setKeyboardNavigations();
    }

    private void setKeyboardNavigations() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.web("#089ce7"));

        input.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> submit();
                case TAB -> cancelButton.requestFocus();
            }
        });
        cancelButton.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> toStart();
                case RIGHT, TAB -> submitButton.requestFocus();
            }
        });
        StartScreenCtrl.setButtonsOnFocus(cancelButton, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(submitButton, dropShadow);
    }

    /**
     * Refreshes the admin login view.
     */
    public void refresh() {
        input.requestFocus();
        input.clear();
        error.setVisible(false);
        changeLanguage(this.bundle.getLocale().getLanguage());
    }

    /**
     * Gets called when the user presses the submit button
     * Get the inputted password and checks with the server if it is valid
     */
    public void submit() {
        String password = input.getText();

        //Send request to server to validate
        if (server.authenticate(password)) {
            mainCtrl.showAdminView(this.bundle);
            input.clear();
        } else {
            error.setVisible(true);
            error.setText(bundle.getString("wrongPassword"));
        }
    }

    /**
     * Routing method. Navigates back to the Start screen
     */
    public void toStart() {
        mainCtrl.showStartScreen(this.bundle, false);
    }

    private void changeLanguage(String languageCode) {
        bundle = ResourceBundle.getBundle("languages.messages", Locale.of(languageCode));
        adminLoginTitle.setText(bundle.getString("adminLoginTitle"));
        serverPasswordLabel.setText(bundle.getString("serverPasswordLabel"));
        input.setPromptText(bundle.getString("password"));
        submitButton.setText(bundle.getString("submitButtonCommon"));
        cancelButton.setText(bundle.getString("cancelButtonCommon"));
        error.setText(bundle.getString("wrongPassword"));
    }

    /**
     * Actions when user presses a key in Admin Login view
     * @param e the Key event
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                submit();
                break;
            case ESCAPE:
                toStart();
                break;
            default:
                break;
        }
    }
}
