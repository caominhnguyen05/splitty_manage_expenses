package client.scenes;

import client.utils.PreferenceUtils;
import client.utils.ServerUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import commons.Event;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static javafx.scene.input.KeyCode.*;

public class StartScreenCtrl {

    private final PreferenceUtils preferences;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    public ResourceBundle bundle;
    public boolean isHighContrast;
    @FXML
    public ImageView spendingImage;
    @FXML
    public ImageView groupImage;
    @FXML
    public JFXToggleButton contrastToggle;
    @FXML
    public Button adminLoginButton;
    @FXML
    public MFXGenericDialog confirmDialog;
    @FXML
    public Label dataSaveLabel;
    @FXML
    public JFXButton closeDialogButton1;
    @FXML
    public AnchorPane biggestPane;
    @FXML
    public Pane smallerPane;

    @FXML
    private Label createNewEventLabel;
    @FXML
    private Label mainTitleStartScreen;
    @FXML
    private Label joinEventLabel;
    @FXML
    private Label recentlyViewedEventsLabel;
    @FXML
    private TextField createEventBox;
    @FXML
    public JFXButton createEventButton;
    @FXML
    private TextField joinEventBox;
    @FXML
    private JFXButton joinEventButton;
    @FXML
    private ListView<String> listViewLanguages;
    @FXML
    public ImageView currentFlag;
    @FXML
    private ListView<Long> recentEventsList;
    private final Image templateFlag = new Image("/empty_flag.jpg");
    private static final Image englishFlag = new Image("/english_flag.jpg");
    private static final Image dutchFlag = new Image("/dutch_flag.png");
    private static final Image germanFlag = new Image("german_flag.jpg");
    private boolean isContrastMode = false;
    /**
     * Constructs a new StartScreenCtrl instance with the specified ServerUtils and MainCtrl.
     *
     * @param server   the ServerUtils instance used for server communication
     * @param mainCtrl the MainCtrl instance used for controlling the main application flow
     */
    @Inject
    public StartScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;

        this.preferences = new PreferenceUtils();
    }

    /**
     * Initialize the start screen view.
     */
    @FXML
    public void initialize() {
        initializeButtons(true);

        confirmDialog.setVisible(false);
        groupImage.setImage(new Image("/group.png"));
        bundle = ResourceBundle.getBundle("languages.messages");

        createEventBox.setPromptText(bundle.getString("createEventBox"));
        joinEventBox.setPromptText(bundle.getString("joinEventBox"));

        if (preferences.getSavedLanguage() != null) {
            System.out.println("Saved language: " + preferences.getSavedLanguage());
            if (preferences.getSavedLanguage().contains("en")) {
                bundle = ResourceBundle.getBundle("languages.messages",
                        Locale.of("en_US"));
            } else if (preferences.getSavedLanguage().contains("nl")) {
                bundle = ResourceBundle.getBundle("languages.messages",
                        Locale.of("nl_NL"));
            } else {
                bundle = ResourceBundle.getBundle("languages.messages",
                        Locale.of("de_DE"));
            }
        } else {
            bundle = ResourceBundle.getBundle("languages.messages");
        }

        initializeRecentlyViewedEventsList();

        // Initializes the choice box with flags and languages.
        initializeLanguagesListView();
        listViewLanguages.setVisible(false);

        if (preferences.getSavedLanguage() == null || preferences.getSavedLanguage().isEmpty()) {
            changeLanguage("en_US");
        } else {
            changeLanguage(preferences.getSavedLanguage());
        }
        initializeKeyboardNavigations();

        contrastToggle.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                isHighContrast = true;
                changeToHighContrast();
            } else {
                isHighContrast = false;
                resetToNormalColors();
            }
        });
    }

    private void changeToHighContrast() {
        biggestPane.setStyle("-fx-background-color: black");
        smallerPane.setStyle("-fx-background-color: black");
        mainTitleStartScreen.setTextFill(Color.WHITE);
        recentlyViewedEventsLabel.setStyle("-fx-text-fill: white");
        createNewEventLabel.setTextFill(Color.WHITE);
        joinEventLabel.setTextFill(Color.WHITE);
        createEventButton.setStyle("-fx-background-color: #75e9fc; -fx-background-radius: 15");
        joinEventButton.setStyle("-fx-background-color: #75e9fc; -fx-background-radius: 15");
        adminLoginButton.setStyle("-fx-background-color: #75e9fc; -fx-background-radius: 15");
        contrastToggle.setStyle("-fx-background-color: black");
        contrastToggle.setTextFill(Color.WHITE);

        closeDialogButton1.setStyle("-fx-background-color: #f5c52a; -fx-background-radius: 15");
        initializeButtons(false);
    }

    private void resetToNormalColors() {
        biggestPane.setStyle("-fx-background-color: white");
        smallerPane.setStyle("-fx-background-color: white");
        mainTitleStartScreen.setTextFill(Color.BLACK);
        recentlyViewedEventsLabel.setStyle("-fx-text-fill: black");
        createNewEventLabel.setTextFill(Color.BLACK);
        joinEventLabel.setTextFill(Color.BLACK);
        createEventButton.setStyle("-fx-background-color:  #CFF8E6; -fx-background-radius: 15");
        joinEventButton.setStyle("-fx-background-color:  #FFE4CF; -fx-background-radius: 15");
        adminLoginButton.setStyle("-fx-background-color:  #CFF8E6; -fx-background-radius: 15");
        contrastToggle.setStyle("-fx-background-color: white");
        contrastToggle.setTextFill(Color.BLACK);

        closeDialogButton1.setStyle("-fx-background-color:  #a9e4de; -fx-background-radius: 13");
        closeDialogButton1.setTextFill(Color.WHITE);
        initializeButtons(true);
    }

    private void initializeKeyboardNavigations() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.web("#089ce7"));

        setMainElementsOnKeyPressed();
        recentEventsList.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT -> createEventButton.requestFocus();
                case RIGHT, TAB -> currentFlag.requestFocus();
            }
        });
        currentFlag.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> {
                    showLanguagesDialog();
                    listViewLanguages.requestFocus();
                }
                case UP -> {
                    joinEventButton.requestFocus();
                    System.out.println("Here");
                }
            }
        });
        adminLoginButton.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> showAdmin();
                case LEFT -> currentFlag.requestFocus();
                case TAB, RIGHT -> createEventBox.requestFocus();
            }
        });

        setButtonsOnFocus(createEventButton, dropShadow);
        setButtonsOnFocus(joinEventButton, dropShadow);
        setButtonsOnFocus(adminLoginButton, dropShadow);
        setButtonsOnFocus(currentFlag, dropShadow);

        recentEventsList.setOnKeyPressed(e -> {
            if (e.getCode() == ENTER) {
                if (recentEventsList.getSelectionModel().getSelectedItem() == null) return;

                try {
                    var selectedEvent = server.getEvent(recentEventsList
                            .getSelectionModel().getSelectedItem());
                    preferences.putOnTop(selectedEvent.id);
                    server.closeHttpLongPolling();
                    showOverview(selectedEvent, this.bundle);
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(bundle.getString("eventSingleWordCommon"));
                    alert.setHeaderText(bundle.getString("eventDoesNotExist"));
                    alert.setContentText(bundle.getString("someoneDeletedThisEvent"));
                    alert.showAndWait();
                }
            } else if (e.getCode() == D) {
                if (recentEventsList.getSelectionModel().getSelectedItem() == null) return;
                deleteEventHelper(recentEventsList.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void setMainElementsOnKeyPressed() {
        createEventBox.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> createEventHandler();
                case DOWN -> joinEventBox.requestFocus();
                case TAB, RIGHT -> createEventButton.requestFocus();
            }
        });
        createEventButton.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> createEventHandler();
                case TAB -> joinEventBox.requestFocus();
                case RIGHT -> recentEventsList.requestFocus();
                case LEFT -> createEventBox.requestFocus();
                default -> {
                }
            }
        });
        joinEventBox.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> joinEventHandler();
                case UP -> createEventBox.requestFocus();
                case LEFT -> recentEventsList.requestFocus();
                case RIGHT, TAB -> joinEventButton.requestFocus();
                case DOWN -> currentFlag.requestFocus();
            }
        });
        joinEventButton.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> joinEventHandler();
                case TAB, RIGHT -> recentEventsList.requestFocus();
                case LEFT -> joinEventBox.requestFocus();
                case DOWN -> currentFlag.requestFocus();
            }
        });
    }

    /**
     * Add a blue dropshadow effect to an element when it is in focus.
     * @param button the element to apply the effect to
     * @param dropShadow the drop shadow effect
     */
    public static void setButtonsOnFocus(Node button, DropShadow dropShadow) {
        button.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                button.setEffect(dropShadow);
            } else {
                button.setEffect(null);
            }
        });
    }

    private void initializeRecentlyViewedEventsList() {
        recentEventsList.setCellFactory(param -> new ListCell<>() {
            private final Button deleteButton = new Button("x");
            private final Label textLabel = new Label();
            private final Region spacer = new Region();
            private final HBox hbox = new HBox(textLabel, spacer, deleteButton);

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.setAlignment(Pos.CENTER);
                deleteButton.setStyle("-fx-background-radius: 8; -fx-background-color: #FF6961; " +
                        "-fx-text-fill: white");
                textLabel.setStyle("-fx-font-family: 'Century Gothic'");
            }

            /**
             * update item
             * @param item The new item for the cell.
             * @param empty whether this cell represents data from the list. If it
             *        is empty, then it does not represent any domain data, but is a cell
             *        being used to render an "empty" row.
             */
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        var referencedEvent = server.getEvent(item);

                        textLabel.setText(referencedEvent.name);
                    } catch (Exception e) {
                        //TODO: if its null, remove this element in the listview
                        System.out.println(e);
                    }

                    deleteButton.setOnAction(event -> deleteEventHelper(item));
                    server.registerForUpdates(item, event -> textLabel.setText(event.name));
                    setGraphic(hbox);
                    setPrefHeight(33);
                }
            }
        });

        recentEventsList.setOnMouseClicked(event -> {
            if (recentEventsList.getSelectionModel().getSelectedItem() == null) return;

            try {
                var selectedEvent = server.getEvent(recentEventsList
                        .getSelectionModel().getSelectedItem());
                preferences.putOnTop(selectedEvent.id);
                server.closeHttpLongPolling();
                showOverview(selectedEvent, this.bundle);
            } catch (Exception e) {
                //TODO: make pop-up better and add a "[DELETED]" identifier to list-element
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("eventSingleWordCommon"));
                alert.setHeaderText(bundle.getString("eventDoesNotExist"));
                alert.setContentText(bundle.getString("someoneDeletedThisEvent"));
                alert.showAndWait();
            }
        });
        recentEventsList.setItems(preferences.savedEventIDs);
    }

    private void initializeButtons(boolean enable) {
        if (enable) {
            createEventButton.setOnMouseEntered(e ->
                    createEventButton.setStyle("-fx-background-color:  #a9dec8;" +
                            "-fx-background-radius: 15"));

            createEventButton.setOnMouseExited(e ->
                    createEventButton.setStyle("-fx-background-color:  #cff8e6;" +
                            "-fx-background-radius: 15"));

            joinEventButton.setOnMouseEntered(e ->
                    joinEventButton.setStyle("-fx-background-radius: 15;" +
                            "-fx-background-color:  #eccdb2"));

            joinEventButton.setOnMouseExited(e ->
                    joinEventButton.setStyle("-fx-background-radius: 15;" +
                            "-fx-background-color:  #ffe4cf"));
            adminLoginButton.setOnMouseEntered(e ->
                    adminLoginButton.setStyle("-fx-background-color:  #a9dec8;" +
                            "-fx-background-radius: 15"));
            adminLoginButton.setOnMouseExited(e ->
                    adminLoginButton.setStyle("-fx-background-color:  #cff8e6;" +
                            "-fx-background-radius: 15"));
        } else {
            createEventButton.setOnMouseEntered(null);
            createEventButton.setOnMouseExited(null);
            joinEventButton.setOnMouseEntered(null);
            joinEventButton.setOnMouseExited(null);
            adminLoginButton.setOnMouseEntered(null);
            adminLoginButton.setOnMouseExited(null);
        }

    }

    /**
     * Show the dialog for the user to choose a language.
     */
    public void showLanguagesDialog() {
        listViewLanguages.setVisible(true);
    }

    private void initializeLanguagesListView() {
        //TODO: make sure template download works
        ObservableList<String> items = FXCollections.observableArrayList("English", "Dutch",
                "Deutsch", "Download Template");
        listViewLanguages.setItems(items);
        listViewLanguages.setStyle("-fx-font-family: 'Century Gothic'");

        listViewLanguages.setOnMouseExited(event -> listViewLanguages.setVisible(false));
        AtomicInteger lastClickedIndex = new AtomicInteger(-1);
        listViewLanguages.setCellFactory(param -> new ListCell<>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (name.equals("English")) {
                        imageView.setImage(englishFlag);
                    } else if (name.startsWith("Dutch")) {
                        imageView.setImage(dutchFlag);
                    } else if (name.startsWith("Deutsch")) {
                        imageView.setImage(germanFlag);
                    } else {
                        imageView.setImage(templateFlag);
                    }
                    imageView.setFitWidth(30);
                    imageView.setFitHeight(20);
                    setText(name);
                    setGraphic(imageView);
                    setPrefHeight(32);

                    // Set text style to bold, and add the word "Active" when
                    // the user chooses a language.
                    if (getIndex() == lastClickedIndex.get() || isSelected()) {
                        if (getIndex() != listViewLanguages.getItems().size() - 1) {
                            setStyle("-fx-font-weight: bold; -fx-font-family: 'Century Gothic'");
                            setText(name + " (Active)");
                        }
                    } else {
                        setStyle("-fx-font-weight: normal; -fx-font-family: 'Century Gothic'");
                        setText(name);
                    }
                }
            }

            {
                // Change the current flag when the user chooses a language.
                setOnMouseClicked(event -> {
                    if (getItem().equals("English")) {
                        currentFlag.setImage(englishFlag);
                        changeLanguage("en_US");
                        preferences.saveLanguage("en_US");
                    } else if (getItem().equals("Dutch")) {
                        currentFlag.setImage(dutchFlag);
                        changeLanguage("nl_NL");
                        preferences.saveLanguage("nl_NL");
                    } else if (getItem().equals("Deutsch")) {
                        currentFlag.setImage(germanFlag);
                        changeLanguage("de_DE");
                        preferences.saveLanguage("de_DE");
                    } else {
                        EventOverviewCtrl.saveLanguageTemplateFile();
                    }
                    lastClickedIndex.set(getIndex());
                    getListView().refresh();
                });
            }
        });
        setListViewLanguagesOnKeyPressed(lastClickedIndex);
    }

    private void setListViewLanguagesOnKeyPressed(AtomicInteger lastClickedIndex) {
        listViewLanguages.setOnKeyPressed(e -> {
            if (e.getCode() == ENTER) {
                String selectedItem = listViewLanguages.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    switch (selectedItem) {
                        case "English" -> {
                            currentFlag.setImage(englishFlag);
                            changeLanguage("en_US");
                            preferences.saveLanguage("en_US");
                        }
                        case "Dutch" -> {
                            currentFlag.setImage(dutchFlag);
                            changeLanguage("nl_NL");
                            preferences.saveLanguage("nl_NL");
                        }
                        case "Deutsch" -> {
                            currentFlag.setImage(germanFlag);
                            changeLanguage("de_DE");
                            preferences.saveLanguage("de_DE");
                        }
                        default -> EventOverviewCtrl.saveLanguageTemplateFile();
                    }
                    lastClickedIndex.set(listViewLanguages.getSelectionModel().getSelectedIndex());
                    listViewLanguages.refresh();
                }
            } else if (e.getCode() == RIGHT || e.getCode() == TAB) {
                listViewLanguages.setVisible(false);
                adminLoginButton.requestFocus();
            } else if (e.getCode() == LEFT) {
                listViewLanguages.setVisible(false);
                recentEventsList.requestFocus();
            }
        });
    }

    /**
     * Changes the language of the application based on the provided language code.
     * Updates the text properties of various UI elements to reflect the new language.
     *
     * @param languageCode The language code representing the desired language.
     */
    public void changeLanguage(String languageCode) {
        bundle = ResourceBundle.getBundle("languages.messages", Locale.of(languageCode));
        mainTitleStartScreen.setText(bundle.getString("mainTitleStartScreen"));
        createNewEventLabel.setText(bundle.getString("createNewEventLabel"));
        createEventButton.setText(bundle.getString("createEventButton"));
        joinEventLabel.setText(bundle.getString("joinEventLabel"));
        joinEventButton.setText(bundle.getString("joinEventButton"));
        recentlyViewedEventsLabel.setText(bundle.getString("recentlyViewedEventsLabel"));
        createEventBox.setPromptText(bundle.getString("createEventBox"));
        joinEventBox.setPromptText(bundle.getString("joinEventBox"));
        contrastToggle.setText(bundle.getString("highContrast"));
        adminLoginButton.setText(bundle.getString("adminLoginTitle"));
    }

    @FXML
    void createEventHandler() {
        String input = createEventBox.getText();
        Alert alert;
        if (input.isBlank()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("emptyField"));
            alert.setHeaderText(bundle.getString("emptyField"));
            alert.setContentText(bundle.getString("provideNameForEventWarningCreate"));
            alert.showAndWait();
            return;
        }

        var newEvent = server.addEvent(new commons.Event(input));
        preferences.savedEventIDs.addFirst(newEvent.id);

        createEventBox.clear();
        showConfirmation();
    }

    @FXML
    void joinEventHandler() {
        String input = joinEventBox.getText();
        joinEvent(input);
    }

    private void joinEvent(String input) {
        Alert al;
        if (input.isBlank()) {
            al = new Alert(Alert.AlertType.ERROR);
            al.setTitle(bundle.getString("emptyField"));
            al.setHeaderText(bundle.getString("emptyField"));
            al.setContentText(bundle.getString("provideNameForEventWarningJoin"));
            al.showAndWait();
            return;
        }

        //TODO: this warning could be more specific
        al = new Alert(Alert.AlertType.INFORMATION);
        al.setTitle(bundle.getString("joinEventConfirmation"));
        al.setHeaderText(bundle.getString("joinEventConfirmation"));

        try {
            Long id = Long.parseLong(input);

            if (!preferences.savedEventIDs.contains(id)) {
                var joinedEvent = server.getEvent(id);
                preferences.savedEventIDs.addFirst(joinedEvent.id);

                al.setContentText(bundle.getString("youJoinedThisEvent"));
            } else {
                al.setContentText(bundle.getString("youAlreadyInThisEvent"));
            }
        } catch (Exception e) {
            al.setContentText(bundle.getString("theCodeIsInvalid"));
        }

        al.showAndWait();
        joinEventBox.clear();
    }

    /**
     * Confirmation dialog before removing an event form the list
     *
     * @param item the ID of the event to be removed
     */
    private void deleteEventHelper(Long item) {
        //TODO: refactor delete message
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("warning"));
        alert.setHeaderText(bundle.getString("deleteThisEvent"));
        alert.setContentText(bundle.getString("pressOk"));

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        // Check which button was clicked
        if (result == ButtonType.OK)
            preferences.savedEventIDs.remove(item);
        else
            alert.hide();
    }

    /**
     * Returns back to overview page.
     *
     * @param event  event that should be shown
     * @param bundle The ResourceBundle containing the language
     *               to be applied to the event overview screen.
     */
    public void showOverview(Event event, ResourceBundle bundle) {
        mainCtrl.showOverview(event, bundle, this.isHighContrast);
    }

    /**
     * Refreshes the start screen.
     */
    public void refresh() {
        preferences.savedEventIDs
                .removeIf(x -> {
                    try {
                        var event = server.getEvent(x);

                        if (event == null)
                            return true;
                    } catch (Exception e) {
                        return true;
                    }

                    return false;
                });

        recentEventsList.refresh();
        createEventBox.requestFocus();
        initializeLanguagesListView();
        String languageCode = preferences.getSavedLanguage();
        configureFlags(languageCode, listViewLanguages, currentFlag);
        changeLanguage(languageCode);
        if (isHighContrast) {
            contrastToggle.setSelected(true);
            changeToHighContrast();
        } else {
            contrastToggle.setSelected(false);
            resetToNormalColors();
        }
    }

    /**
     * Change the flags based on the language set on the previous page or user session.
     *
     * @param languageCode      the language code
     * @param listViewLanguages the ListView to change between languages
     * @param currentFlag       the current flag image
     */
    public static void configureFlags(String languageCode, ListView<String> listViewLanguages,
                                      ImageView currentFlag) {
        if (languageCode.contains("nl")) {
            listViewLanguages.getSelectionModel().select(1);
            currentFlag.setImage(dutchFlag);

        } else if (languageCode.contains("en")) {
            listViewLanguages.getSelectionModel().select(0);
            currentFlag.setImage(englishFlag);
        } else {
            listViewLanguages.getSelectionModel().select(2);
            currentFlag.setImage(germanFlag);
        }
    }

    /**
     * Routing method. Navigates to the admin view
     */
    public void showAdmin() {
        mainCtrl.showAdminLogin(this.bundle);
    }

    /**
     * Close create event confirm dialog.
     */
    public void closeConfirmDialog() {
        confirmDialog.setVisible(false);
        createEventButton.setDisable(false);
        recentEventsList.setDisable(false);
        adminLoginButton.setDisable(false);
        createEventBox.setDisable(false);
        joinEventBox.setDisable(false);
    }

    /**
     * Show confirmation after creating an event.
     */
    public void showConfirmation() {
        //TODO: use this warning-dialog style for all warnings
        confirmDialog.setHeaderIcon(new MFXFontIcon("fas-circle-info", 18));
        confirmDialog.setHeaderText(bundle.getString("createEventConfirmation"));
        dataSaveLabel.setText(bundle.getString("eventCreatedSuccessfully"));
        confirmDialog.setVisible(true);
        createEventButton.setDisable(true);
        recentEventsList.setDisable(true);
        adminLoginButton.setDisable(true);
        createEventBox.setDisable(true);
        joinEventBox.setDisable(true);
        closeDialogButton1.requestFocus();
        closeDialogButton1.setOnKeyPressed(e -> {
            if (e.getCode() == ENTER) {
                closeConfirmDialog();
            }
        });
    }

    /**
     * Closes all http long polling and web socket communication
     */
    public void stop() {
        server.closeHttpLongPolling();
        server.closeWebSocketListener();
    }

    /**
     * Actions when the user presses a key in Start Screen view
     * @param e The key event
     */
    public void keyPressed(KeyEvent e) {
        if (e.getCode() == L && e.isControlDown()) {
            showLanguagesDialog();
            listViewLanguages.requestFocus();
        } else if (e.getCode() == M && e.isControlDown()) {
            adminLoginButton.requestFocus();
            showAdmin();
        }
    }
}

