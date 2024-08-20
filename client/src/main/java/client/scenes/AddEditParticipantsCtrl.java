package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXToggleButton;
import commons.Event;
import commons.Participant;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


public class AddEditParticipantsCtrl implements Initializable {
    private final ServerUtils server;
    public final MainCtrl mainCtrl;
    public Participant thisParticipant;

    private Event event;
    public ResourceBundle bundle;
    public boolean isHighContrast;

    @FXML
    public AnchorPane biggestPane;
    @FXML
    public ImageView friendsImage;
    @FXML
    public ImageView backArrow;
    @FXML
    public AnchorPane backToStartScreenButton;
    @FXML
    public Rectangle backRectangle;
    @FXML
    public JFXToggleButton contrastToggle;
    @FXML
    public MFXGenericDialog warningDialog;
    @FXML
    public Button closeDialogButton;
    @FXML
    public Label dataSaveLabel;
    @FXML
    public Button closeDialogButton1;
    @FXML
    public MFXGenericDialog confirmDialog;
    @FXML
    public Label dataErrorLabel;
    @FXML
    public Button cancelSaveDataButton;
    @FXML
    private Label addEditParticipantHeader;
    @FXML
    private MFXButton abortButton;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label ibanLabel;
    @FXML
    private Label bicLabel;
    @FXML
    public TextField bicField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField ibanField;
    @FXML
    public TextField nameField;
    @FXML
    private MFXButton okButton;
    @FXML
    private Label invalidBIC;
    @FXML
    private Label invalidEmail;
    @FXML
    private Label invalidIBAN;
    @FXML
    private Label invalidName;

    /**
     * Constructs a new AddEditParticipantsCtrl instance with the
     * specified ServerUtils and MainCtrl.
     *
     * @param server   the ServerUtils instance used for server communication
     * @param mainCtrl the MainCtrl instance used for controlling the main application flow
     */
    @Inject
    public AddEditParticipantsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the UI
     *
     * @param location  url location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invalidName.setStyle("-fx-font-family: 'Century Gothic'; -fx-text-fill: red;");
        invalidEmail.setStyle("-fx-font-family: 'Century Gothic'; -fx-text-fill: red;");
        invalidIBAN.setStyle("-fx-font-family: 'Century Gothic'; -fx-text-fill: red;");
        invalidBIC.setStyle("-fx-font-family: 'Century Gothic'; -fx-text-fill: red;");

        EventOverviewCtrl.configureBackButton(backToStartScreenButton, backArrow, backRectangle);

        friendsImage.setImage(new Image("/friends.png"));
        nameField.textProperty().addListener((obs, oldVal, newVal)
                -> okButton.setDisable(newVal.isEmpty()));

        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                invalidName.setText("");
            }
        });

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                invalidEmail.setText("");
            }
        });

        ibanField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty() && bicField.getText().isEmpty()) {
                invalidIBAN.setText("");
                invalidBIC.setText("");
            }
        });

        bicField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty() && ibanField.getText().isEmpty()) {
                invalidBIC.setText("");
                invalidIBAN.setText("");
            }
        });

        warningDialog.setStyle("-fx-font-family: 'Century Gothic';" +
                "-fx-font-size: 13");
        warningDialog.setVisible(false);
        confirmDialog.setVisible(false);

        setElementsOnKeyPressed();

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.web("#089ce7"));
        StartScreenCtrl.setButtonsOnFocus(okButton, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(abortButton, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(closeDialogButton, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(closeDialogButton1, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(cancelSaveDataButton, dropShadow);

        contrastToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                isHighContrast = true;
                changeToHighContrast();
            } else {
                isHighContrast = false;
                resetColors();
            }
        });
    }

    private void changeToHighContrast() {
        biggestPane.setStyle("-fx-background-color: black");
        addEditParticipantHeader.setTextFill(Color.WHITE);
        nameLabel.setTextFill(Color.WHITE);
        emailLabel.setTextFill(Color.WHITE);
        ibanLabel.setTextFill(Color.WHITE);
        bicLabel.setTextFill(Color.WHITE);

        okButton.setStyle("-fx-background-color: #75e9fc; " +
                "   -fx-background-radius: 20; -fx-text-fill: black");
        abortButton.setStyle("-fx-border-color: white; -fx-background-color: black; " +
                "-fx-text-fill: white; -fx-background-radius: 20; -fx-border-radius: 20");
        contrastToggle.setStyle("-fx-background-color: black");
        contrastToggle.setTextFill(Color.WHITE);

        closeDialogButton1.setStyle("-fx-background-color: #75e9fc; -fx-background-radius: 13");
        closeDialogButton1.setTextFill(Color.BLACK);
        cancelSaveDataButton.setStyle("-fx-background-color: #f5c52a; -fx-background-radius: 13");
        closeDialogButton.setStyle("-fx-background-color: #f5c52a; -fx-background-radius: 13");
    }

    private void resetColors() {
        biggestPane.setStyle("-fx-background-color: white");
        addEditParticipantHeader.setTextFill(Color.BLACK);
        nameLabel.setTextFill(Color.BLACK);
        emailLabel.setTextFill(Color.BLACK);
        ibanLabel.setTextFill(Color.BLACK);
        bicLabel.setTextFill(Color.BLACK);

        okButton.setStyle("-fx-background-color:  #455a64; " +
                "-fx-background-radius: 20; -fx-text-fill: white");
        abortButton.setStyle("-fx-border-color:  #455a64; -fx-background-color:  #FFFFFF; " +
                "-fx-text-fill: black; -fx-background-radius: 20; -fx-border-radius: 20");
        contrastToggle.setStyle("-fx-background-color: white");
        contrastToggle.setTextFill(Color.BLACK);

        closeDialogButton1.setStyle("-fx-background-color:  #a9e4de; -fx-background-radius: 13");
        closeDialogButton1.setTextFill(Color.WHITE);
        cancelSaveDataButton.setStyle("-fx-background-color:  #f49595; -fx-background-radius: 13");
        closeDialogButton.setStyle("-fx-background-color:  #f49595; -fx-background-radius: 13");
    }

    private void setElementsOnKeyPressed() {
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.TAB) {
                emailField.requestFocus();
            }
        });
        emailField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.TAB) {
                ibanField.requestFocus();
            } else if (e.getCode() == KeyCode.UP) {
                nameField.requestFocus();
            }
        });
        ibanField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.TAB) {
                bicField.requestFocus();
            } else if (e.getCode() == KeyCode.UP) {
                emailField.requestFocus();
            }
        });
        setBicFieldOnKeyPressed();
        okButton.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.TAB) {
                abortButton.requestFocus();
            }
        });
    }

    private void setBicFieldOnKeyPressed() {
        bicField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.TAB || e.getCode() == KeyCode.DOWN) {
                if (!okButton.isDisabled()) {
                    okButton.requestFocus();
                } else {
                    nameField.requestFocus();
                }

            } else if (e.getCode() == KeyCode.UP) {
                ibanField.requestFocus();
            }
        });
    }

    /**
     * Refreshes the view with details of a participant (for editing participant).
     */
    public void refresh() {
        warningDialog.setVisible(false);
        confirmDialog.setVisible(false);
        if (thisParticipant != null) {
            nameField.setText(thisParticipant.name);
            if (thisParticipant.email != null) {
                emailField.setText(thisParticipant.email);
            }
            if (thisParticipant.bic != null) {
                bicField.setText(thisParticipant.bic);
            } else {
                invalidBIC.setText("");
            }
            if (thisParticipant.iban != null) {
                ibanField.setText(thisParticipant.iban);
            } else {
                invalidIBAN.setText("");
            }
        } else {
            clearFields();
            invalidBIC.setText("");
            invalidName.setText("");
            invalidEmail.setText("");
            invalidIBAN.setText("");
        }
        changeLanguage(this.bundle.getLocale().getDisplayLanguage());
        if (isHighContrast) {
            contrastToggle.setSelected(true);
            changeToHighContrast();
        } else {
            contrastToggle.setSelected(false);
            resetColors();
        }
    }

    /**
     * Cancels adding a new participant.
     */
    public void cancelAddParticipant() {
        mainCtrl.showOverview(this.event, this.bundle, this.isHighContrast);
    }

    /**
     * Verify if the entered data is correct and save it
     * or ask for another input if necessary
     * Added a timer for the displayed InputError message
     */
    public void addParticipant() {
        if (thisParticipant == null) {
            // Case when IBAN or BIC field is not empty.
            if (!bicField.getText().isEmpty() || !ibanField.getText().isEmpty()) {
                // If email is not empty, validate all fields.
                if (!emailField.getText().isEmpty()) {
                    validateAllDetails();
                    server.sentConfirmation(emailField.getText(),event);
                } else {
                    // If email is empty, validate name, BIC and IBAN
                    validateNameAndBankWhileEmailEmpty();
                }
            } else {
                // If BIC and IBAN is left empty, only validate name (and email).
                if (!emailField.getText().isEmpty()) {
                    validateNameAndEmailWhileBankEmpty();
                } else {
                    validateNameWhileBankEmpty();
                }
            }

            server.updateEvent(event);
        } else {
            if (!bicField.getText().isEmpty() || !ibanField.getText().isEmpty()) {
                // If email is not empty, validate all fields.
                validateAllFieldsForEditingParticipant();
            } else {
                // If BIC and IBAN is left empty, only validate name.
                validateNameAndEmailIfBankEmpty();
            }
        }
    }

    private void validateNameAndEmailIfBankEmpty() {
        if (!emailField.getText().isEmpty()) {
            if (isValidEmail() && isValidName()) {
                showConfirmation();
            } else {
                showErrorWarningNameEmail();
            }
        } else {
            if (isValidName()) {
                showConfirmation();
            } else {
                wrongName(isValidName());
                showAlert();
            }
        }
    }

    private void validateAllFieldsForEditingParticipant() {
        if (!emailField.getText().isEmpty()) {
            if (isValidName() && isValidIBAN() && isValidBIC() && isValidEmail()) {
                showConfirmation();
            } else {
                showErrorWarningAll();
            }
        } else {
            // If email is empty, validate name, BIC and IBAN
            if (isValidName() && isValidIBAN() && isValidBIC()) {
                showConfirmation();
            } else {
                wrongName(isValidName());
                wrongIban(isValidIBAN());
                wrongBic(isValidBIC());
                showAlert();
            }
        }
    }

    private void validateAllDetails() {
        if (isValidName() && isValidIBAN() && isValidBIC() && isValidEmail()) {
            showConfirmation();
        } else {
            showErrorWarningAll();
        }
    }


    private void validateNameAndBankWhileEmailEmpty() {
        if (isValidName() && isValidIBAN() && isValidBIC()) {
            showConfirmation();
        } else {
            wrongName(isValidName());
            wrongIban(isValidIBAN());
            wrongBic(isValidBIC());

            showAlert();
        }
    }


    private void validateNameWhileBankEmpty() {
        if (isValidName()) {
            showConfirmation();
        } else {
            wrongName(isValidName());
            showAlert();
        }
    }


    private void validateNameAndEmailWhileBankEmpty() {
        if (isValidEmail() && isValidName()) {
            showConfirmation();
        } else {
            showErrorWarningNameEmail();
        }
    }

    private void showConfirmation() {
        closeDialogButton1.setFocusTraversable(true);
        cancelSaveDataButton.setFocusTraversable(true);

        MFXFontIcon icon = new MFXFontIcon("fas-circle-check",18);
        confirmDialog.setHeaderText(bundle.getString("dataSaveConfirmation"));
        confirmDialog.setHeaderIcon(icon);
        dataSaveLabel.setText(bundle.getString("dataSavedSuccessfully"));
        confirmDialog.setVisible(true);
        disableButtonWhileShowingDialog(true);
        closeDialogButton1.requestFocus();
        closeDialogButton1.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                saveParticipantsData();
            } else if (e.getCode() == KeyCode.TAB || e.getCode() == KeyCode.LEFT) {
                cancelSaveDataButton.requestFocus();
            }
        });
    }

    /**
     * Add/Update participant data to the database.
     */
    public void saveParticipantsData() {
        confirmDialog.setVisible(false);
        disableButtonWhileShowingDialog(false);
        if (thisParticipant == null) {
            var newParticipant = server.addParticipant(new
                    Participant(nameField.getText(),
                    ibanField.getText(), bicField.getText(), emailField.getText()));
            this.event.participants.add(newParticipant);
            server.updateEvent(this.event);
            if(!emailField.getText().isEmpty())
                server.sentConfirmation(emailField.getText(), event);
        } else {
            thisParticipant.name = nameField.getText();
            thisParticipant.email = emailField.getText();
            thisParticipant.bic = bicField.getText();
            thisParticipant.iban = ibanField.getText();
            server.updateParticipant(thisParticipant, event.id);
            if(!emailField.getText().isEmpty())
                server.sentConfirmationUpdate(emailField.getText(), thisParticipant);
        }
        clearFields();
        mainCtrl.showOverview(this.event, this.bundle, this.isHighContrast);
    }

    /**
     * Close confirmation dialog.
     */
    public void closeConfirmDialog() {
        confirmDialog.setVisible(false);
        disableButtonWhileShowingDialog(false);
    }

    private void disableButtonWhileShowingDialog(boolean b) {
        backToStartScreenButton.setDisable(b);
        okButton.setDisable(b);
        abortButton.setDisable(b);
        nameField.setDisable(b);
        ibanField.setDisable(b);
        emailField.setDisable(b);
        bicField.setDisable(b);
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        ibanField.clear();
        bicField.clear();
    }

    /**
     * shows errors for all attributes
     */
    private void showErrorWarningAll() {
        wrongName(isValidName());
        wrongEmail(isValidEmail());
        wrongIban(isValidIBAN());
        wrongBic(isValidBIC());

        showAlert();
    }

    private void showAlert() {
        closeDialogButton.setFocusTraversable(true);
        MFXFontIcon icon = new MFXFontIcon("fas-circle-exclamation",18);
        warningDialog.setHeaderIcon(icon);
        warningDialog.setHeaderText(bundle.getString("invalidData"));
        dataErrorLabel.setText(bundle.getString("pleaseChangeData"));
        warningDialog.setVisible(true);
        closeDialogButton.requestFocus();
        closeDialogButton.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                closeWarningDialog();
            }
        });

        disableButtonWhileShowingDialog(true);

    }

    /**
     * Close alert dialog.
     */
    public void closeWarningDialog() {
        warningDialog.setVisible(false);
        disableButtonWhileShowingDialog(false);
    }

    /**
     * Changes the language of the application based on the provided language code.
     * Updates the text properties of various UI elements to reflect the new language.
     *
     * @param languageCode The language code representing the desired language
     */
    public void changeLanguage(String languageCode) {
        bundle = ResourceBundle.getBundle("languages.messages", Locale.of(languageCode));

        addEditParticipantHeader.setText(bundle.getString("addEditParticipantHeader"));
        nameLabel.setText(bundle.getString("nameLabel"));
        emailLabel.setText(bundle.getString("emailLabel"));
        ibanLabel.setText(bundle.getString("ibanLabel"));
        bicLabel.setText(bundle.getString("bicLabel"));
        abortButton.setText(bundle.getString("cancelButtonCommon"));
        contrastToggle.setText(bundle.getString("highContrast"));
    }

    /**
     * shows errors only for Name and Email
     */
    private void showErrorWarningNameEmail() {
        wrongName(isValidName());
        wrongEmail(isValidEmail());
        showAlert();
    }

    /**
     * Display message if the email is in wrong format
     */
    private void wrongEmail(boolean valid) {
        if (!valid)
            invalidEmail.setText(bundle.getString("invalidEmail"));
        else
            invalidEmail.setText("");
    }

    /**
     * Display message if the IBAN is in wrong format
     */
    private void wrongIban(boolean valid) {
        if (!valid)
            invalidIBAN.setText(bundle.getString("invalidIBAN"));
        else
            invalidIBAN.setText("");
    }

    /*
    Display message if the BIC is in wrong format
     */
    private void wrongBic(boolean valid) {
        if (!valid)
            invalidBIC.setText(bundle.getString("invalidBIC"));
        else
            invalidBIC.setText("");
    }

    /*
    check for invalid (empty) name
     */
    private void wrongName(boolean valid) {
        if (!valid)
            invalidName.setText(bundle.getString("invalidName"));
        else
            invalidName.setText("");
    }

    /*
    Check if the email is valid
     */
    private boolean isValidEmail() {
        String emailFormat = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return emailField.getText().matches(emailFormat);
    }

    /*
    Check if the IBAN is valid
     */
    private boolean isValidIBAN() {
        String ibanFormat = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{4}[0-9]{7}([A-Z0-9]?){0,16}$";
        return ibanField.getText().matches(ibanFormat);
    }

    /*
    Check if the BIC is valid
     */
    private boolean isValidBIC() {
        String bicFormat = "^[A-Za-z]{4}[A-Za-z]{2}[A-Za-z0-9]{2}([A-Za-z0-9]{3})?$";
        return bicField.getText().matches(bicFormat);
    }

    private boolean isValidName() {
        String nameFormat = "^[A-Za-z\\s'-]+$";
        return nameField.getText().matches(nameFormat);
    }

    /**
     * Key press functionality
     *
     * @param e the key event
     */
    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
            abort();
        }
    }

    /**
     * cancelAction method overload with different name to avoid ambiguity
     */
    public void abort() {
        mainCtrl.showOverview(this.event, this.bundle, this.isHighContrast);
    }

    /**
     * Sets the event of this instance
     * @param event the event to set it as
     */
    public void setEvent(Event event) {
        server.closeWebSocketListener();

        this.event = event;

        try {
            this.server.openWebSocketEventListener(Long.toString(event.id), x -> this.event = x);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Getter for the current event
     * @return event ot be returned
     */
    public Event getEvent() {
        return event;
    }
}

