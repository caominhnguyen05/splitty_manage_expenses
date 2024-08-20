package client.scenes;

import client.data_structures.Deleteable;
import client.utils.PreferenceUtils;
import client.utils.ServerUtils;

import com.google.inject.Inject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;

import commons.Event;
import commons.Participant;
import commons.Expense;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static javafx.scene.input.KeyCode.*;

public class EventOverviewCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    public boolean isHighContrast;
    @FXML
    public ImageView backArrow;
    @FXML
    public ImageView editParticipantIcon;
    @FXML
    public ImageView settleDebtsImage;
    @FXML
    public ImageView inviteImage;
    @FXML
    public ImageView statisticsImage;
    @FXML
    public ImageView addExpenseImage;
    @FXML
    public Rectangle backRectangle;
    @FXML
    public JFXToggleButton contrastToggle;
    @FXML
    public AnchorPane addExpenseImageGroup;
    @FXML
    public ToggleGroup contrast;
    @FXML
    public AnchorPane biggestAnchorPane;
    @FXML
    public AnchorPane leftPane;
    @FXML
    public Pane iconsPane;
    @FXML
    public Pane currencyImagePane;
    private PreferenceUtils preferences;

    private Event event;
    @FXML
    private JFXListView<String> listViewLanguages;
    @FXML
    public ImageView currentFlag;
    @FXML
    private ListView<String> listViewCurrencies;
    @FXML
    public ImageView currencyPicture;
    @FXML
    public Button viewStatisticsButton;
    @FXML
    public JFXComboBox<Participant> participantsChoiceBox;
    @FXML
    private Label inviteCodeHeader;
    @FXML
    private Label participantsHeader;
    @FXML
    private Label expensesHeader;
    @FXML
    private AnchorPane backToStartScreenButton;
    @FXML
    private MFXTextField eventNameTextField1;
    @FXML
    private Label eventTitle;
    @FXML
    private JFXButton modifyEventNameButton1;
    @FXML
    private Label errorDelete;
    @FXML
    private MFXGenericDialog editEventDialog1;
    @FXML
    private Label inviteCode;
    @FXML
    private Label listOfParticipantsText;
    @FXML
    private Label includingTab;
    @FXML
    private Label fromTab;
    @FXML
    private Label allTab;
    @FXML
    private MFXGenericDialog removeParticipantDialog;
    @FXML
    private ListView<Participant> listViewNamesToRemove;
    @FXML
    public Button addExpenseButton;
    @FXML
    private Button settleDebtsButton;
    @FXML
    private ImageView addParticipantIcon;
    @FXML
    private Button sendInvitesButton;
    @FXML
    public ListView<Expense> expenseHistory;
    @FXML
    public ListView<Expense> expenseFromPerson;
    @FXML
    public ListView<Expense> expenseIncludingPerson;
    private final Image templateFlag = new Image("/empty_flag.jpg");
    private final Image englishFlag = new Image("/english_flag.jpg");
    private final Image dutchFlag = new Image("/dutch_flag.png");
    private final Image germanFlag = new Image("german_flag.jpg");
    private final Image currencyDefFlag = new Image("Currency_default_picture.png");
    private final Image euroFlag = new Image("Euro_picture.png");
    private final Image dollarFlag = new Image("Dollar_picture.png");
    private final Image francFlag = new Image("Franc_picture.png");

    private Deleteable deleteObject;

    public ResourceBundle bundle;

    private int currentExpenseTab = 0;

    /**
     * Constructs a new OverviewScreenCtrl instance with the specified ServerUtils and MainCtrl.
     *
     * @param server   the ServerUtils instance used for server communication
     * @param mainCtrl the MainCtrl instance used for controlling the main application flow
     */
    @Inject
    public EventOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) throws InterruptedException {
        this.mainCtrl = mainCtrl;
        this.server = server;
        preferences = new PreferenceUtils();
    }

    /**
     * Sets the field Deletable to the provided object
     *
     * @param del the Deletable object
     */
    public void setDeletable(Deleteable del) {
        this.deleteObject = del;
    }

    /**
     * Initializes the view.
     *
     * @param url            the URL location
     * @param resourceBundle resource bundle containing specific resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventTitle.setStyle("-fx-font-family: 'Roboto Bold'");
        configureBackButton(backToStartScreenButton, backArrow, backRectangle);

        //currencyPicture.setImage(euroFlag);

        initializeMainFunctionalityButtons(true);
        initializeTabButtons(true);

        participantsChoiceBox.setStyle("-fx-font-family: 'Century Gothic'");
        expenseFromPerson.setStyle("-fx-font-family: 'Century Gothic';-fx-vgap: 10");
        expenseIncludingPerson.setStyle("-fx-font-family: 'Century Gothic';-fx-vgap: 10");
        expenseHistory.setStyle("-fx-font-family: 'Century Gothic';-fx-vgap: 10");

        editParticipantIcon.setImage(new Image("/edit_participant.png"));
        addParticipantIcon.setImage(new Image("/add_participant.png"));

        inviteImage.setImage(new Image("/invite.png"));
        addExpenseImage.setImage(new Image("/money_black.png"));
        statisticsImage.setImage(new Image("/statistics.png"));
        settleDebtsImage.setImage(new Image("/icons8-money-90.png"));

        bundle = ResourceBundle.getBundle("languages.messages");

        modifyEventNameButton1.setOnAction(e -> modifyEventName());

        removeParticipantDialog.setVisible(false);

        eventTitle.setOnMouseClicked(event -> showEditEventDialog());
        editEventDialog1.setVisible(false);
        editEventDialog1.setOnClose(event -> cancelModifyEventName());
        eventNameTextField1.setText(eventTitle.getText());

        if (event == null) {
            inviteCode.setText("");
        } else {
            inviteCode.setText(Long.toString(event.id));
        }

        // Initializes the list view with participants' names.
        initializeEditParticipantsListView();

        if (event == null) {
            participantsChoiceBox.setItems(FXCollections
                    .observableArrayList(new ArrayList<>()));
        } else {
            participantsChoiceBox.setItems(FXCollections.observableArrayList(event.participants));
        }

        participantsChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Participant participant) {
                if (participant == null) {
                    return "";
                }
                return participant.name;
            }

            @Override
            public Participant fromString(String string) {
                return null;
            }
        });

        initializeAddEditParticipantIcons();

        // Change the "From..." and "Including..." tabs based on user's choice of participant.
        participantsChoiceBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fromTab.setText(bundle.getString("fromTab") + " " + newVal.name);
                includingTab.setText(bundle.getString("includingTab") + " " + newVal.name);

                refreshExpenseTabs();
            }
        });

        // Initializes the choice box with flags and languages.
        initializeLanguagesListView();
        listViewLanguages.setVisible(false);
        initializeKeyboardNavigations();

        initializeCurrencyListView();
        listViewCurrencies.setVisible(false);

        contrastToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                isHighContrast = true;
                setHighContrastColor();
            } else {
                isHighContrast = false;
                resetColors();
            }
        });
    }

    private void setHighContrastColor() {
        initializeTabButtons(false);
        initializeMainFunctionalityButtons(false);
        biggestAnchorPane.setStyle("-fx-background-color: black");
        eventTitle.setTextFill(Color.WHITE);
        leftPane.setStyle("-fx-background-color: black; -fx-background-radius: 15; " +
                "-fx-text-fill: white");

        participantsHeader.setTextFill(Color.WHITE);
        iconsPane.setStyle("-fx-background-color: white");

        inviteCodeHeader.setTextFill(Color.WHITE);
        inviteCode.setTextFill(Color.WHITE);

        listOfParticipantsText.setTextFill(Color.WHITE);
        expensesHeader.setTextFill(Color.WHITE);
        participantsChoiceBox.setStyle("-fx-text-fill: white; -fx-background-color: #f5c52a");

        sendInvitesButton.setStyle("-fx-background-radius: 15; -fx-background-color: #75e9fc");
        addExpenseButton.setStyle("-fx-background-radius: 15; -fx-background-color: #75e9fc");
        viewStatisticsButton.setStyle("-fx-background-radius: 15; -fx-background-color: #75e9fc");
        settleDebtsButton.setStyle("-fx-background-radius: 15; -fx-background-color: #75e9fc");

        allTab.setStyle("-fx-background-radius: 15; -fx-background-color: #f5c52a");
        fromTab.setStyle("-fx-background-radius: 15; -fx-background-color: #f5c52a");
        includingTab.setStyle("-fx-background-radius: 15; -fx-background-color: #f5c52a");
        contrastToggle.setTextFill(Color.WHITE);
        contrastToggle.setStyle("-fx-background-color: black");
        inviteImage.setVisible(false);
        addExpenseImage.setVisible(false);
        statisticsImage.setVisible(false);
        settleDebtsImage.setVisible(false);

        backRectangle.setOpacity(1);
        currencyImagePane.setStyle("-fx-background-color: white");
    }

    private void resetColors() {
        initializeTabButtons(true);
        initializeMainFunctionalityButtons(true);
        biggestAnchorPane.setStyle("-fx-background-color: white");
        eventTitle.setTextFill(Color.BLACK);
        leftPane.setStyle("-fx-background-color: white; -fx-background-radius: 15;");

        participantsHeader.setTextFill(Color.BLACK);
        iconsPane.setStyle("-fx-background-color: white");

        inviteCodeHeader.setTextFill(Color.BLACK);
        inviteCode.setTextFill(Color.BLACK);

        listOfParticipantsText.setTextFill(Color.BLACK);
        expensesHeader.setTextFill(Color.BLACK);
        participantsChoiceBox.setStyle("-fx-text-fill: black;");

        sendInvitesButton.setStyle("-fx-background-radius: 15; -fx-background-color:  #d6edd9");
        addExpenseButton.setStyle("-fx-background-radius: 15; -fx-background-color:  #e5def0");
        viewStatisticsButton.setStyle("-fx-background-radius: 15; -fx-background-color:  #e5f1fd");
        settleDebtsButton.setStyle("-fx-background-radius: 15; -fx-background-color:  #f6f0d8");

        allTab.setStyle("-fx-background-radius: 15; -fx-background-color:  #e5f1fd");
        fromTab.setStyle("-fx-background-radius: 15; -fx-background-color:  #f6f0d8");
        includingTab.setStyle("-fx-background-radius: 15; -fx-background-color:  #d6edd9");
        contrastToggle.setTextFill(Color.BLACK);
        contrastToggle.setStyle("-fx-background-color: white");

        inviteImage.setVisible(true);
        addExpenseImage.setVisible(true);
        statisticsImage.setVisible(true);
        settleDebtsImage.setVisible(true);
        backRectangle.setOpacity(0.62);
    }

    private void initializeAddEditParticipantIcons() {
        // Change colour on mouse hover over add icon.
        addParticipantIcon.setOnMouseEntered(event
                -> addParticipantIcon.setStyle("-fx-opacity: 0.6"));
        addParticipantIcon.setOnMouseExited(event
                -> addParticipantIcon.setStyle("-fx-opacity: 1"));

        // Change colour on mouse hover over edit participants (pencil) icon.
        editParticipantIcon.setOnMouseEntered(event
                -> editParticipantIcon.setStyle("-fx-opacity: 0.6"));
        editParticipantIcon.setOnMouseExited(event
                -> editParticipantIcon.setStyle("-fx-opacity: 1"));
    }

    private void initializeTabButtons(boolean enable) {
        if (enable) {
            allTab.setOnMouseEntered(e ->
                    allTab.setStyle("-fx-background-color: #bad0e8; -fx-background-radius: 15;"));

            allTab.setOnMouseExited(e ->
                    allTab.setStyle("-fx-background-color: #e5f1fd;-fx-background-radius: 15;"));

            fromTab.setOnMouseEntered(e ->
                    fromTab.setStyle("-fx-background-radius: 15; -fx-background-color: #ded4a6"));

            fromTab.setOnMouseExited(e ->
                    fromTab.setStyle("-fx-background-radius: 15; -fx-background-color:  #f6f0d8"));

            includingTab.setOnMouseEntered(e ->
                    includingTab.setStyle("-fx-background-radius: 15; " +
                            "-fx-background-color: #89bb8c"));

            includingTab.setOnMouseExited(e ->
                    includingTab.setStyle("-fx-background-radius: 15; " +
                            "-fx-background-color: #d6edd9"));
        } else {
            allTab.setOnMouseEntered(null);
            allTab.setOnMouseExited(null);
            fromTab.setOnMouseEntered(null);
            fromTab.setOnMouseExited(null);
            includingTab.setOnMouseEntered(null);
            includingTab.setOnMouseExited(null);
        }
    }

    private void initializeMainFunctionalityButtons(boolean enable) {
        if (enable) {
            sendInvitesButton.setOnMouseEntered(e ->
                    sendInvitesButton.setStyle("-fx-background-color: #7a947c; " +
                            "-fx-background-radius: 15"));

            sendInvitesButton.setOnMouseExited(e ->
                    sendInvitesButton.setStyle("-fx-background-color:  #d6edd9; " +
                            "-fx-background-radius: 15"));

            addExpenseButton.setOnMouseEntered(e ->
                    addExpenseButton.setStyle("-fx-background-color:  #968ba2; " +
                            "-fx-background-radius: 15"));

            addExpenseButton.setOnMouseExited(e ->
                    addExpenseButton.setStyle("-fx-background-color:  #e5def0; " +
                            "-fx-background-radius: 15"));

            settleDebtsButton.setOnMouseEntered(e ->
                    settleDebtsButton.setStyle("-fx-background-color:  #b4ad8c; " +
                            "-fx-background-radius: 15"));

            settleDebtsButton.setOnMouseExited(e ->
                    settleDebtsButton.setStyle("-fx-background-color:  #f6f0d8; " +
                            "-fx-background-radius: 15"));

            viewStatisticsButton.setOnMouseEntered(e ->
                    viewStatisticsButton.setStyle("-fx-background-color:  #99afc5; " +
                            "-fx-background-radius: 15"));

            viewStatisticsButton.setOnMouseExited(e ->
                    viewStatisticsButton.setStyle("-fx-background-color:  #e5f1fd; " +
                            "-fx-background-radius: 15"));
        } else {
            sendInvitesButton.setOnMouseEntered(null);
            sendInvitesButton.setOnMouseExited(null);
            addExpenseButton.setOnMouseEntered(null);
            addExpenseButton.setOnMouseExited(null);
            settleDebtsButton.setOnMouseEntered(null);
            settleDebtsButton.setOnMouseExited(null);
            viewStatisticsButton.setOnMouseEntered(null);
            viewStatisticsButton.setOnMouseExited(null);
        }
    }

    /**
     * Configure the back button
     *
     * @param backToStartScreenButton the Anchor Pane containing the elements
     * @param backArrow               the back arrow
     * @param backRectangle           the round rectangle
     */
    public static void configureBackButton(AnchorPane backToStartScreenButton,
                                           ImageView backArrow, Rectangle backRectangle) {
        backArrow.setImage(new Image("/back.png"));
        backToStartScreenButton.setOnMouseEntered(event -> {
            backArrow.setOpacity(1);
            backRectangle.setOpacity(1);
        });

        backToStartScreenButton.setOnMouseExited(event -> {
            backArrow.setOpacity(0.42);
            backRectangle.setOpacity(0.62);
        });
    }

    private void initializeLanguagesListView() {
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
        setLanguagesDialogOnKeyboardActions(lastClickedIndex);
    }

    private void setLanguagesDialogOnKeyboardActions(AtomicInteger lastClickedIndex) {
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
                currencyPicture.requestFocus();
                listViewLanguages.setVisible(false);
            } else if (e.getCode() == LEFT) {
                listViewLanguages.setVisible(false);
                addParticipantIcon.requestFocus();
                listViewLanguages.setVisible(false);
            }
        });
    }

    private void initializeCurrencyListView() {
        ObservableList<String> items = FXCollections.
                observableArrayList("EUR", "USD", "CHF", "Default");
        listViewCurrencies.setItems(items);
        listViewCurrencies.setStyle("-fx-font-family: 'Century Gothic'");

        listViewCurrencies.setOnMouseExited(event -> listViewCurrencies.setVisible(false));
        AtomicInteger lastClickedIndex = new AtomicInteger(-1);
        listViewCurrencies.setCellFactory(param -> new ListCell<>() {

            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(name);
                    setPrefHeight(32);

                    // Set text style to bold when the user chooses an item.
                    if (getIndex() == lastClickedIndex.get() || isSelected()) {
                        setStyle("-fx-font-weight: bold; -fx-font-family: 'Century Gothic'");
                        setText(name + " (Active)");
                    } else {
                        setStyle("-fx-font-weight: normal; -fx-font-family: 'Century Gothic'");
                        setText(name);
                    }


                }
            }

            {
                setOnMouseClicked(event -> {
                    if (getItem().equals("CHF")) {
                        currencyPicture.setImage(francFlag);
                        preferences.saveCurrency("CHF");
                        changeCurrency();
                    } else if (getItem().equals("USD")) {
                        currencyPicture.setImage(dollarFlag);
                        preferences.saveCurrency("USD");
                        changeCurrency();
                    } else if (getItem().equals("EUR")) {
                        currencyPicture.setImage(euroFlag);
                        preferences.saveCurrency("EUR");
                        changeCurrency();
                    } else if (getItem().equals("Default")) {
                        currencyPicture.setImage(currencyDefFlag);
                        preferences.saveCurrency("Default");
                        changeCurrency();
                    }
                    lastClickedIndex.set(getIndex());
                    getListView().refresh();
                });
            }
        });
        setCurrenciesDialogOnKeyboardActions(lastClickedIndex);
    }

    private void setCurrenciesDialogOnKeyboardActions(AtomicInteger lastClickedIndex) {
        listViewCurrencies.setOnKeyPressed(e -> {
            if (e.getCode() == ENTER) {
                String selectedItem = listViewCurrencies.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    switch (selectedItem) {
                        case "EUR" -> {
                            currencyPicture.setImage(euroFlag);
                            preferences.saveCurrency("EUR");
                            changeCurrency();
                        }
                        case "USD" -> {
                            currencyPicture.setImage(dollarFlag);
                            preferences.saveCurrency("USD");
                            changeCurrency();
                        }
                        case "CHF" -> {
                            currencyPicture.setImage(francFlag);
                            preferences.saveCurrency("CHF");
                            changeCurrency();
                        }
                        case "Default" -> {
                            currencyPicture.setImage(currencyDefFlag);
                            preferences.saveCurrency("Default");
                            changeCurrency();
                        }
                    }
                    lastClickedIndex.set(listViewCurrencies.getSelectionModel().getSelectedIndex());
                    listViewCurrencies.refresh();
                }
            } else if (e.getCode() == RIGHT || e.getCode() == TAB) {
                sendInvitesButton.requestFocus();
                listViewCurrencies.setVisible(false);
            } else if (e.getCode() == LEFT) {
                listViewCurrencies.setVisible(false);
                currentFlag.requestFocus();
                listViewCurrencies.setVisible(false);
            }
        });
    }

    /**
     * Let the user save the language template file to their device.
     */
    public static void saveLanguageTemplateFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Language Template File");

        String userHome = System.getProperty("user.home");
        File downloadsDirectory = new File(userHome + File.separator + "Downloads");
        fileChooser.setInitialDirectory(downloadsDirectory);

        fileChooser.setInitialFileName("language_template.properties");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Properties Files (*.properties)",
                        "*.properties")
        );

        File file = fileChooser.showSaveDialog(new Stage());
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new
                    FileReader("client/src/main/resources/template_language.properties"));
            lines.addAll(reader.lines().toList());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (file != null) {
            try {
                EventOverviewCtrl.saveSystem(file, lines);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Save the language template file.
     *
     * @param file  the template file.
     * @param lines all lines in the file
     * @throws IOException exception caused by dealing with file
     */
    public static void saveSystem(File file, List<String> lines) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.println(line);
            }
        }
    }

    private void initializeEditParticipantsListView() {
        listViewNamesToRemove.setStyle("-fx-font-family: 'Century Gothic'");
        listViewNamesToRemove.setCellFactory(param -> new ListCell<>() {
            private final Button deleteButton = new Button("x");
            private final Label textLabel = new Label();
            private final Button editButton = new Button(bundle.getString("editButtonCommon"));
            private final Region spacer = new Region();
            private final HBox hbox = new HBox(textLabel, spacer, editButton, deleteButton);

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
            }

            {
                setDeleteButtonStyle(deleteButton);
                setEditButtonStyle(editButton);
            }

            // Delete a participant when the "X" button is clicked.
            {
                deleteButton.setOnAction(event -> {
                    Participant thisParticipant = getItem();
                    errorDelete.setVisible(false);
                    if (deleteObject.getDelete(thisParticipant.name)) {
                        deleteParticipantHelper(thisParticipant);
                    } else {
                        errorDelete.setVisible(true);
                    }
                });
                editButton.setOnMouseClicked(event -> editParticipantHelper(getItem()));

            }

            @Override
            public void updateItem(Participant participant, boolean empty) {
                super.updateItem(participant, empty);

                if (empty || participant == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    deleteButton.idProperty().set("deleteParticipant_" + participant.id);
                    textLabel.setText(participant.name);
                    setGraphic(hbox);
                    setPrefHeight(33);
                }
            }
        });

        listViewNamesToRemove.setOnKeyPressed(e -> {
            var item = listViewNamesToRemove.getSelectionModel().getSelectedItem();
            if (e.getCode() == ENTER) {
                editParticipantHelper(item);
            } else if (e.getCode() == D) {
                errorDelete.setVisible(false);
                if (deleteObject.getDelete(item.name)) {
                    deleteParticipantHelper(item);
                } else {
                    errorDelete.setVisible(true);
                }
            } else if (e.getCode() == W && e.isControlDown()) {
                closeRemoveParticipant();
            }
        });
    }

    /**
     * Event handler when user clicks on the add participant (plus) icon
     */
    public void showAddParticipantPage() {
        mainCtrl.showAddEditParticipants(event, null, this.bundle, this.isHighContrast);
    }

    private void editParticipantHelper(Participant participant) {
        mainCtrl.showAddEditParticipants(event, participant, this.bundle, this.isHighContrast);
    }

    /**
     * Show the dialog to remove a participant to the user.
     */
    public void showRemoveParticipantDialog() {

        errorDelete.setVisible(false);
        removeParticipantDialog.setVisible(true);
        listViewNamesToRemove.requestFocus();
        disableButtonsWhileInDialog(true);
    }

    /**
     * Closes the "Remove" participant dialog when the user clicks on "Close" button.
     */
    public void closeRemoveParticipant() {
        removeParticipantDialog.setVisible(false);
        eventTitle.setDisable(false);
        errorDelete.setVisible(false);
        disableButtonsWhileInDialog(false);
        if (event.participants.isEmpty()) addExpenseButton.setDisable(true);
    }

    /**
     * Handles event when the user clicks on the "X" button to delete a participant.
     *
     * @param participant the participant to delete
     */
    private void deleteParticipantHelper(Participant participant) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("warning"));
        alert.setHeaderText(bundle.getString("deleteThisParticipant"));
        alert.setContentText(bundle.getString("pressOk"));

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            listViewNamesToRemove.getItems().remove(participant);
            event.participants.remove(participant);
            server.removeParticipant(participant);

            // Update list of participants' names under the "Participants" header.
            if (event.participants.isEmpty()) {
                listOfParticipantsText.setText(bundle.getString("listOfParticipantsText"));
                fromTab.setText(bundle.getString("fromTab"));
                includingTab.setText(bundle.getString("includingTab"));

            } else {
                updateParticipantsListUnderHeader();
            }

            // Update choice box list of names
            if (participantsChoiceBox.getValue() != null) {
                if (participantsChoiceBox.getValue().equals(participant)) {
                    if (!event.participants.isEmpty()) {
                        participantsChoiceBox.setValue(event.participants.getFirst());
                    }
                }
            }

            server.updateEventWS(event);
            participantsChoiceBox.getItems().remove(participant);
        } else {
            alert.hide();
        }
    }

    /**
     * Update the list of participants' names displayed under the "Participants" header.
     */
    private void updateParticipantsListUnderHeader() {
        if (event.participants != null && !event.participants.isEmpty()) {
            StringBuilder listParticipantsString = new StringBuilder();
            for (int i = 0; i < event.participants.size() - 1; i++) {
                listParticipantsString.append(event.participants.get(i).name).append(", ");
            }
            listParticipantsString.append(event.participants.getLast().name);

            // Set the list of participants under the "Participants" header.
            listOfParticipantsText.setText(listParticipantsString.toString());
        } else {
            listOfParticipantsText.setText("No participants.");
        }
    }

    /**
     * Disables other buttons while a dialog is being opened.
     *
     * @param b whether to disable buttons or not
     */
    private void disableButtonsWhileInDialog(boolean b) {
        backToStartScreenButton.setDisable(b);
        addParticipantIcon.setDisable(b);
        editParticipantIcon.setDisable(b);

        sendInvitesButton.setDisable(b);
        addExpenseButton.setDisable(b);
        settleDebtsButton.setDisable(b);
        viewStatisticsButton.setDisable(b);

        allTab.setDisable(b);
        fromTab.setDisable(b);
        includingTab.setDisable(b);
        participantsChoiceBox.setDisable(b);
        expenseFromPerson.setDisable(b);
        expenseHistory.setDisable(b);
        expenseIncludingPerson.setDisable(b);
    }

    /**
     * Shows the dialog to edit an event's title.
     */
    public void showEditEventDialog() {
        editEventDialog1.setVisible(true);
        eventNameTextField1.setFocusTraversable(true);
        modifyEventNameButton1.setFocusTraversable(true);
        disableButtonsWhileInDialog(true);
        eventNameTextField1.requestFocus();
        eventNameTextField1.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!eventNameTextField1.getText().isEmpty()) {
                    modifyEventName();
                }
            } else if (event.getCode() == KeyCode.TAB) {
                modifyEventNameButton1.requestFocus();
            }
        });
        modifyEventNameButton1.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> modifyEventName();
                case UP, TAB -> eventNameTextField1.requestFocus();
            }
        });
        eventNameTextField1.textProperty().addListener((obs, old, newV)
                -> modifyEventNameButton1.setDisable(newV.isEmpty()));
    }

    /**
     * Handles event when user clicks on "Modify" button to change the event's name.
     */
    public void modifyEventName() {
        eventTitle.setText(eventNameTextField1.getText());
        this.event.name = eventNameTextField1.getText();

        server.updateEvent(event);
        editEventDialog1.setVisible(false);
        disableButtonsWhileInDialog(false);
    }

    /**
     * Handles event when the user clicks on "Cancel" button while editing an event name.
     */
    public void cancelModifyEventName() {
        editEventDialog1.setVisible(false);
        disableButtonsWhileInDialog(false);
    }

    /**
     * Direct to send invitation page.
     */
    public void sendInvites() {
        mainCtrl.showInvite(event, this.bundle, this.isHighContrast);
    }

    /**
     * Direct to add/edit expense page.
     */
    public void addExpense() {
        mainCtrl.showAddEdit(event, null, this.bundle, this.isHighContrast);
    }

    /**
     * Direct to settle debts page.
     */
    public void settleDebts() {
        mainCtrl.showOpenDebts(event, this.bundle, this.isHighContrast);
    }

    /**
     * Changes the language of the application based on the provided language code.
     * Updates the text properties of various UI elements to reflect the new language.
     *
     * @param languageCode The language code representing the desired language
     */
    public void changeLanguage(String languageCode) {
        bundle = ResourceBundle.getBundle("languages.messages", Locale.of(languageCode));

        sendInvitesButton.setText(bundle.getString("sendInvitesButton"));
        addExpenseButton.setText(bundle.getString("addExpenseButton"));
        viewStatisticsButton.setText(bundle.getString("viewStatisticsButton"));
        settleDebtsButton.setText(bundle.getString("settleDebtsButton"));

        participantsHeader.setText(bundle.getString("participantsHeader"));
        if (this.event.participants == null || this.event.participants.isEmpty()) {
            listOfParticipantsText.setText(bundle.getString("listOfParticipantsText"));
        }

        expensesHeader.setText(bundle.getString("expensesHeader"));
        allTab.setText(bundle.getString("allTab"));
        fromTab.setText(bundle.getString("fromTab"));
        includingTab.setText(bundle.getString("includingTab"));
        inviteCodeHeader.setText(bundle.getString("inviteCodeHeader"));

        modifyEventNameButton1.setText(bundle.getString("modifyButtonCommon"));
        editEventDialog1.setHeaderText(bundle.getString("editEventNameLabel"));
        removeParticipantDialog.setHeaderText(bundle.getString("editRemoveParticipantLabel"));

        if (participantsChoiceBox.getValue() != null) {
            fromTab.setText(bundle.getString("fromTab") + " "
                    + participantsChoiceBox.getValue().name);
            includingTab.setText(bundle.getString("includingTab") + " " +
                    participantsChoiceBox.getValue().name);
        }
        contrastToggle.setText(bundle.getString("highContrast"));
        editEventDialog1.setHeaderText(bundle.getString("editEventNameLabel"));

        initializeEditParticipantsListView();
        showExpensesFromAPerson();
        showExpensesIncludingAPerson();
        showAllExpenses();
    }

    /**
     * Puts data of the currently assigned event object
     * to the respective fields in the view.
     */
    public void refresh() {
        mainCtrl.refreshDebts(this.event, this.bundle, this.isHighContrast);
        errorDelete.setVisible(false);
        editEventDialog1.setVisible(false);
        errorDelete.setText(bundle.getString("pleaseSettleDebts"));
        eventTitle.setText(event.name);
        addParticipantIcon.requestFocus();
        eventNameTextField1.setText(event.name);
        inviteCode.setText(Long.toString(event.id));
        listViewCurrencies.setVisible(false);
        listViewLanguages.setVisible(false);

        initializeLanguagesListView();
        initializeCurrencyListView();

        String languageCode = bundle.getLocale().getLanguage();
        changeLanguage(languageCode);
        StartScreenCtrl.configureFlags(languageCode, listViewLanguages, currentFlag);

        refreshCurrency();

        if (event.participants == null || event.participants.isEmpty()) {
            listOfParticipantsText.setText(bundle.getString("listOfParticipantsText"));
            fromTab.setText(bundle.getString("fromTab"));
            includingTab.setText(bundle.getString("includingTab"));
        } else {
            updateParticipantsListUnderHeader();
            if (participantsChoiceBox.getValue() != null) {
                fromTab.setText(bundle.getString("fromTab")
                        + participantsChoiceBox.getValue().name);
                includingTab.setText(bundle.getString("fromTab")
                        + participantsChoiceBox.getValue().name);
            }
        }

        // Update list of participants available to be deleted
        initializeEditParticipantsListView();
        listViewNamesToRemove.setItems(FXCollections
                .observableArrayList(event.participants));

        // Update choice box list of names
        participantsChoiceBox.setItems(FXCollections.observableArrayList(event.participants));

        if (event != null) {
            if (event.participants != null && !event.participants.isEmpty()) {
                participantsChoiceBox.setValue(event.participants.getLast());
            }
        } else {
            participantsChoiceBox.setValue(null);
        }

        refreshExpenseTabs();
        if (isHighContrast) {
            contrastToggle.setSelected(true);
            setHighContrastColor();
        } else {
            contrastToggle.setSelected(false);
            resetColors();
        }
    }

    private void refreshExpenseTabs() {
        if (currentExpenseTab == 0) {
            showAllExpenses();
        } else if (currentExpenseTab == 1) {
            showExpensesIncludingAPerson();
        } else if (currentExpenseTab == 2) {
            showExpensesFromAPerson();
        }
    }

    private void refreshCurrency() {
        String currencyCode = preferences.getSavedCurrency();
        switch (currencyCode) {
            case "EUR" -> {
                listViewCurrencies.getSelectionModel().select(0);
                currencyPicture.setImage(euroFlag);
            }
            case "USD" -> {
                listViewCurrencies.getSelectionModel().select(1);
                currencyPicture.setImage(dollarFlag);
            }
            case "CHF" -> {
                listViewCurrencies.getSelectionModel().select(2);
                currencyPicture.setImage(francFlag);
            }
            default -> {
                listViewCurrencies.getSelectionModel().select(3);
                currencyPicture.setImage(currencyDefFlag);
            }
        }
    }

    /**
     * Display all expenses history for everyone.
     */
    public void showAllExpenses() {
        currentExpenseTab = 0;

        expenseHistory.setVisible(true);
        expenseFromPerson.setVisible(false);
        expenseIncludingPerson.setVisible(false);
        if (this.event != null) {
            showAllExpenseHelper();
            expenseHistory.setItems(FXCollections
                    .observableArrayList(event.expenses));
        }
    }

    /**
     * initialize the expenseHistory ListView
     * and give the proper format to every expense shown
     */
    public void showAllExpenseHelper() {
        expenseHistory.setCellFactory(param -> new ListCell<>() {
            private final Button deleteButton = new Button("x");
            private final Button editButton = new Button(bundle.
                    getString("editButtonCommon"));

            private final Label tagLabel = new Label("HELLO WORLD");
            private final Label textLabel = new Label();
            private final Region spacer = new Region();
            private final HBox hbox = new HBox(textLabel, spacer, tagLabel,
                    editButton, deleteButton);

            {

                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
            }

            {
                setDeleteButtonStyle(deleteButton);
                setEditButtonStyle(editButton);
            }

            {
                deleteButton.setOnAction(event -> {
                    Expense thisExpense = getItem();
                    deleteExpenseHelper(thisExpense);
                });
            }

            {
                editButton.setOnAction(event -> {
                    Expense thisExpense = getItem();
                    editExpenseHelper(thisExpense);
                });
            }

            @Override
            public void updateItem(Expense expense, boolean empty) {
                super.updateItem(expense, empty);
                if (empty || expense == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    deleteButton.setId("deleteExpense_" + expense.id);

                    StringBuilder debtors = new StringBuilder();
                    String currency = preferences.getSavedCurrency();
                    String amountToShow = String.format("%.2f", (expense.amount
                            * server.getConversionRate("EUR", expense.currency, expense.date)));
                    if (!currency.equals("Default")) amountToShow = String.
                            format("%.2f", (expense.amount * server.
                                    getConversionRate("EUR", currency, expense.date)));
                    String currencyToShow = expense.currency;
                    if (!currency.equals("Default")) currencyToShow = currency;
                    for (Participant p : expense.debtors)
                        debtors.append(p.name).append(" ");
                    textLabel.setText(expense.date.getDayOfMonth() + "." +
                            expense.date.getMonthValue() + ".   " +
                            expense.creditor.name + " " + bundle.getString("paidWord") + " " +
                            amountToShow + " " +
                            currencyToShow + " " + bundle.getString("forWord") + " "
                            + expense.title + "\n" +
                            bundle.getString("debtor") + ": " + debtors);
                    setGraphic(hbox);
                    setPrefHeight(40);
                }

                if (getItem() != null) {
                    tagLabel.setText(getItem().type.getName());

                    String hexString = getItem().type.getColor();

                    // Create Color object from hexadecimal String representation
                    Color backgroundColor = Color.web(hexString);
                    String cssColor = String.format("#%02X%02X%02X",
                            (int) (backgroundColor.getRed() * 255),
                            (int) (backgroundColor.getGreen() * 255),
                            (int) (backgroundColor.getBlue() * 255));
                    tagLabel.setStyle("-fx-background-color: " + cssColor + ";");

                }
            }
        });
    }

    /**
     * Display all expenses from the chosen person
     * by filtering the expense list in order to select only the expense
     * that have the given participant as creditor
     */
    public void showExpensesFromAPerson() {
        currentExpenseTab = 2;

        expenseIncludingPerson.setVisible(false);
        expenseHistory.setVisible(false);
        expenseFromPerson.setVisible(true);
        if (participantsChoiceBox.getValue() != null) {
            showExpensesFromAPersonHelper(participantsChoiceBox.getValue());
            expenseFromPerson.setItems(FXCollections
                    .observableArrayList(event.expenses.stream()
                            .filter(expense -> expense.creditor.
                                    equals(participantsChoiceBox.getValue())).toList()));
        }

    }

    /**
     * Show the expenseFromPerson ListView
     * in the proper format regarding the selected Participant
     *
     * @param participant the participant to show expense from.
     */
    public void showExpensesFromAPersonHelper(Participant participant) {
        expenseFromPerson.setCellFactory(param -> new ListCell<>() {
            private final Button deleteButton = new Button("x");
            private final Button editButton = new Button(bundle.getString("editButtonCommon"));
            private final Label textLabel = new Label();
            private final Region spacer = new Region();
            private final HBox hbox = new HBox(textLabel, spacer, editButton, deleteButton);

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
            }

            {
                setDeleteButtonStyle(deleteButton);
                setEditButtonStyle(editButton);
            }

            {
                deleteButton.setOnAction(event -> {
                    Expense thisExpense = getItem();
                    deleteExpenseHelper(thisExpense);
                });
            }

            {
                editButton.setOnAction(event -> {
                    Expense thisExpense = getItem();
                    editExpenseHelper(thisExpense);
                });
            }

            @Override
            public void updateItem(Expense expense, boolean empty) {
                super.updateItem(expense, empty);
                if (empty || expense == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    deleteButton.idProperty().set("deleteExpense_" + expense.id);

                    StringBuilder debtors = new StringBuilder();
                    String currency = preferences.getSavedCurrency();
                    String amountToShow = String.format("%.2f",
                            (expense.amount * server.getConversionRate
                                    ("EUR", expense.currency, expense.date)));
                    if (!currency.equals("Default"))
                        amountToShow = String.format("%.2f",
                                (expense.amount * server.getConversionRate
                                        ("EUR", currency, expense.date)));
                    String currencyToShow = expense.currency;
                    if (!currency.equals("Default")) currencyToShow = currency;
                    for (Participant p : expense.debtors)
                        debtors.append(p.name).append(" ");
                    textLabel.setText(expense.date.getDayOfMonth() + "." +
                            expense.date.getMonthValue() + ".   " +
                            expense.creditor.name + ": " + expense.title +
                            ", " + amountToShow + " " + currencyToShow + "\n" +
                            bundle.getString("debtor") + ": " + debtors);
                    setGraphic(hbox);
                    setPrefHeight(40);
                }
            }
        });
    }

    private static void setDeleteButtonStyle(Button deleteButton) {
        deleteButton.setStyle("-fx-background-radius: 8; -fx-background-color: #FF6961; " +
                "-fx-text-fill: white");
    }

    private static void setEditButtonStyle(Button editButton) {
        editButton.setStyle("-fx-background-radius: 8; -fx-background-color: #a5d6f7;" +
                "-fx-text-fill: white");
    }

    /**
     * Display all expenses
     * including the chosen person by filtering the expense list
     * for the expenses that have the given participant
     * as one of the debtors
     */
    public void showExpensesIncludingAPerson() {
        currentExpenseTab = 1;

        expenseIncludingPerson.setVisible(true);
        expenseFromPerson.setVisible(false);
        expenseHistory.setVisible(false);
        if (participantsChoiceBox.getValue() != null) {
            showExpensesIncludingAPersonHelper(participantsChoiceBox.getValue());
            List<Expense> filteredExpenses = new ArrayList<>();
            for (Expense expense : event.expenses)
                for (Participant participant : expense.debtors)
                    if (participant.equals(participantsChoiceBox.getValue())) {
                        filteredExpenses.add(expense);
                        break;
                    }
            expenseIncludingPerson.setItems(FXCollections
                    .observableArrayList(filteredExpenses));
        }
    }

    /**
     * show the expenseIncludingPerson ListView
     * in the proper format regarding the selected Participant
     *
     * @param participant the participant to show expense including that person
     */
    public void showExpensesIncludingAPersonHelper(Participant participant) {
        expenseIncludingPerson.setCellFactory(param -> new ListCell<>() {
            private final Button deleteButton = new Button("x");
            private final Button editButton = new Button(bundle
                    .getString("editButtonCommon"));
            private final Label textLabel = new Label();
            private final Region spacer = new Region();
            private final HBox hbox = new HBox(textLabel, spacer, editButton, deleteButton);

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
            }

            {
                setDeleteButtonStyle(deleteButton);
                setEditButtonStyle(editButton);
            }

            {
                deleteButton.setOnAction(event -> {
                    Expense thisExpense = getItem();
                    deleteExpenseHelper(thisExpense);
                });
            }

            {
                editButton.setOnAction(event -> {
                    Expense thisExpense = getItem();
                    editExpenseHelper(thisExpense);
                });
            }

            @Override
            public void updateItem(Expense expense, boolean empty) {
                super.updateItem(expense, empty);
                if (empty || expense == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    deleteButton.setId("deleteExpense_" + expense.id);

                    StringBuilder debtors = new StringBuilder();
                    String currency = preferences.getSavedCurrency();
                    String amountToShow = String.format("%.2f",
                            (expense.amount * server.getConversionRate
                                    ("EUR", expense.currency, expense.date)));
                    if (!currency.equals("Default")) amountToShow = String.
                            format("%.2f", (expense.amount * server.getConversionRate
                                    ("EUR", currency, expense.date)));
                    String currencyToShow = expense.currency;
                    if (!currency.equals("Default")) currencyToShow = currency;
                    for (Participant p : expense.debtors)
                        debtors.append(p.name).append(" ");
                    textLabel.setText(expense.date.getDayOfMonth() + "." +
                            expense.date.getMonthValue() + ".   " +
                            bundle.getString("includingTab") + " " + participant.name + ": "
                            + expense.title + ", " + amountToShow + " " + currencyToShow + "\n" +
                            bundle.getString("debtor") + ": " + debtors);
                    setGraphic(hbox);
                    setPrefHeight(40);
                }
            }
        });
    }

    /**
     * deletes the expense the deleteButton was clicked for
     * but firstly asks the user if he wants to permanently delete it
     *
     * @param expense the expense to be deleted.
     */
    private void deleteExpenseHelper(Expense expense) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("warning"));
        alert.setHeaderText(bundle.getString("deleteThisExpense"));
        alert.setContentText(bundle.getString("pressOk"));

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        // Check which button was clicked
        if (result == ButtonType.OK) {
            System.out.println(expense);
            expenseHistory.getItems().remove(expense);
            expenseFromPerson.getItems().remove(expense);
            expenseIncludingPerson.getItems().remove(expense);

            event.expenses.remove(expense);
            server.removeExpense(expense);
            server.updateEvent(event);
        } else
            alert.hide();
    }

    /**
     * opens the Expense Overview in order to edit the selected expense
     *
     * @param expense the expense to be edited
     */
    private void editExpenseHelper(Expense expense) {
        mainCtrl.showAddEdit(event, expense, this.bundle, this.isHighContrast);
    }

    /**
     * Event handler for when the user hovers over the page title
     * It will change the page title style to indicate its clickable
     *
     * @param mouseEvent mouse event that will trigger the action
     */
    public void hoverEnter(MouseEvent mouseEvent) {
        eventTitle.setCursor(Cursor.HAND);
        eventTitle.setUnderline(true);
    }

    /**
     * Event handler for when the user stops hovering over the page title
     */
    public void hoverExit() {
        eventTitle.setCursor(Cursor.DEFAULT);
        eventTitle.setUnderline(false);
    }

    /**
     * Show the choice box for languages (and flags).
     */
    public void showLanguagesDialog() {
        listViewLanguages.setVisible(true);
    }

    /**
     * Show currency dialog.
     */
    public void showCurrenciesDialog() {
        listViewCurrencies.setVisible(true);
    }

    /**
     * Show the statistics view.
     */
    public void showStatisticsPage() {
        mainCtrl.showStatistics(this.event, this.bundle, this.isHighContrast);
    }

    /**
     * Returns to the start screen.
     */
    public void backToStart() {
        mainCtrl.showStartScreen(this.bundle, this.isHighContrast);
    }

    /**
     * Sets the current event and starts listening for it's changes
     * Sets the current event and starts listening for it's changes
     *
     * @param event event to be displayed
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

    private void initializeKeyboardNavigations() {
        editParticipantIcon.setFocusTraversable(true);
        addParticipantIcon.setFocusTraversable(true);
        allTab.setFocusTraversable(true);
        includingTab.setFocusTraversable(true);
        fromTab.setFocusTraversable(true);
        sendInvitesButton.setFocusTraversable(true);
        addExpenseButton.setFocusTraversable(true);
        eventTitle.setFocusTraversable(true);
        participantsChoiceBox.setFocusTraversable(true);
        eventNameTextField1.setFocusTraversable(true);
        modifyEventNameButton1.setFocusTraversable(true);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.web("#089ce7"));

        StartScreenCtrl.setButtonsOnFocus(allTab, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(fromTab, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(includingTab, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(sendInvitesButton, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(addExpenseButton, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(settleDebtsButton, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(viewStatisticsButton, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(currentFlag, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(modifyEventNameButton1, dropShadow);
        setMainButtonsOnKeyboardActions();
        setTabsOnKeyboardActions();

        DropShadow iconShadow = new DropShadow();
        iconShadow.setColor(Color.web("#8CDBFF"));
        StartScreenCtrl.setButtonsOnFocus(editParticipantIcon, iconShadow);
        StartScreenCtrl.setButtonsOnFocus(addParticipantIcon, iconShadow);

        editParticipantIcon.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> showRemoveParticipantDialog();
                case TAB, RIGHT -> addParticipantIcon.requestFocus();
                case UP -> eventTitle.requestFocus();
            }
        });
        addParticipantIcon.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> showAddParticipantPage();
                case TAB -> participantsChoiceBox.requestFocus();
                case RIGHT -> sendInvitesButton.requestFocus();
                case LEFT -> editParticipantIcon.requestFocus();
                case UP -> eventTitle.requestFocus();
            }
        });
        currentFlag.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> {
                    showLanguagesDialog();
                    listViewLanguages.requestFocus();
                }
                case UP -> addParticipantIcon.requestFocus();
                case TAB -> settleDebtsButton.requestFocus();
            }
        });
        participantsChoiceBox.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case TAB -> {
                    allTab.requestFocus();
                    showAllExpenses();
                }
                case RIGHT -> sendInvitesButton.requestFocus();
            }
        });
        eventTitle.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                eventTitle.setCursor(Cursor.HAND);
                eventTitle.setUnderline(true);
            } else {
                eventTitle.setCursor(Cursor.DEFAULT);
                eventTitle.setUnderline(false);
            }
        });
        eventTitle.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> showEditEventDialog();
                case DOWN -> {
                    addParticipantIcon.setFocusTraversable(true);
                    addParticipantIcon.requestFocus();
                }
                case RIGHT -> sendInvitesButton.requestFocus();
            }
        });

        expensesListViewKeyboardShortcuts();
    }

    private void setTabsOnKeyboardActions() {
        allTab.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case RIGHT, TAB -> {
                    includingTab.requestFocus();
                    showExpensesIncludingAPerson();
                }
                case UP, LEFT -> participantsChoiceBox.requestFocus();
            }
        });
        includingTab.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT -> {
                    allTab.requestFocus();
                    showAllExpenses();
                }
                case RIGHT, TAB -> {
                    fromTab.requestFocus();
                    showExpensesFromAPerson();
                }
            }
        });
        fromTab.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT -> {
                    includingTab.requestFocus();
                    showExpensesIncludingAPerson();
                }
                case RIGHT, TAB -> sendInvitesButton.requestFocus();
                case UP -> participantsChoiceBox.requestFocus();
            }
        });
    }

    private void setMainButtonsOnKeyboardActions() {
        sendInvitesButton.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> sendInvites();
                case DOWN, TAB -> addExpenseButton.requestFocus();
                case UP -> eventTitle.requestFocus();
                case LEFT -> addParticipantIcon.requestFocus();
            }
        });
        addExpenseButton.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> addExpense();
                case UP -> sendInvitesButton.requestFocus();
                case DOWN, TAB -> viewStatisticsButton.requestFocus();
                case LEFT -> addParticipantIcon.requestFocus();
            }
        });
        viewStatisticsButton.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> showStatisticsPage();
                case UP -> addExpenseButton.requestFocus();
                case DOWN, TAB, RIGHT -> settleDebtsButton.requestFocus();
                case LEFT -> {
                    fromTab.requestFocus();
                    showExpensesFromAPerson();
                }
            }
        });
        settleDebtsButton.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> settleDebts();
                case TAB, RIGHT -> currentFlag.requestFocus();
                case LEFT -> {
                    fromTab.requestFocus();
                    showExpensesFromAPerson();
                }
            }
        });
    }

    private void expensesListViewKeyboardShortcuts() {
        showCorrespondingExpense(expenseHistory);
        showCorrespondingExpense(expenseIncludingPerson);
        showCorrespondingExpense(expenseFromPerson);
    }

    private void showCorrespondingExpense(ListView<Expense> expenseIncludingPerson) {
        expenseIncludingPerson.setOnKeyPressed(e -> {
            var item = expenseIncludingPerson.getSelectionModel().getSelectedItem();
            if (e.getCode() == KeyCode.ENTER) {
                editExpenseHelper(item);
            } else if (e.getCode() == KeyCode.D) {
                deleteExpenseHelper(item);
            } else if (e.getCode() == KeyCode.TAB) {
                currentFlag.requestFocus();
            }
        });
    }

    /**
     * Actions when the user presses a key/combination of key in the view.
     *
     * @param e The key event.
     */
    public void keyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            backToStart();
        } else {
            keyPressedWithCtrl(e);
        }
    }

    private void keyPressedWithCtrl(KeyEvent e) {
        if (e.getCode() == I && e.isControlDown()) {
            sendInvites();
        } else if (e.getCode() == E && e.isControlDown() && !this.event.participants.isEmpty()) {
            addExpense();
        } else if (e.getCode() == S && e.isControlDown()) {
            showStatisticsPage();
        } else if (e.getCode() == D && e.isControlDown()) {
            settleDebts();
        } else if (e.getCode() == L && e.isControlDown()) {
            showLanguagesDialog();
            listViewLanguages.requestFocus();
        } else if (e.getCode() == P && e.isControlDown()) {
            showAddParticipantPage();
        } else if (e.getCode() == K && e.isControlDown()) {
            showCurrenciesDialog();
            listViewCurrencies.requestFocus();
        }
    }

    /**
     * refreshes the expenses so the currency changes are applied
     */
    public void changeCurrency() {
        showExpensesFromAPerson();
        showExpensesIncludingAPerson();
        showAllExpenses();
    }
}