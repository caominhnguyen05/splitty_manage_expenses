package client.scenes;

import client.utils.ServerUtils;
import com.jfoenix.controls.*;
import commons.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.scene.effect.DropShadow;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;
import static javafx.scene.input.KeyCode.*;

public class AddEditExpenseCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public AnchorPane biggestPane;

    private Event event;
    public Expense expense;
    public ResourceBundle bundle;
    public boolean isHighContrast;
    @FXML
    public Label expenseTypeLabel;
    @FXML
    public Label howToSplitLabel;
    @FXML
    public Label whenLabel;
    @FXML
    public Label howMuchLabel;
    @FXML
    public Label whatForLabel;
    @FXML
    public Label whoPaidLabel;
    @FXML
    public Label addEditExpenseTitle;
    @FXML
    public AnchorPane backToStartScreenButton;
    @FXML
    public ImageView backArrow;
    @FXML
    public Rectangle backRectangle;
    @FXML
    public JFXToggleButton contrastToggle;

    @FXML
    private JFXComboBox<Participant> whoPaidChoiceBox;

    @FXML
    private TextField whatForTextField;

    @FXML
    private TextField howMuchTextField;

    @FXML
    private JFXComboBox<String> currencyChoiceBox;

    @FXML
    private MFXDatePicker whenDatePicker;

    @FXML
    private MFXButton abortButton;

    @FXML
    private MFXButton addButton;

    @FXML
    private ToggleGroup splitting;

    @FXML
    private JFXComboBox<String> expenseTypeChoiceBox;

    @FXML
    private MFXScrollPane selectUsersScrollPane;

    @FXML
    private JFXRadioButton equallyBetweenRadio;

    @FXML
    private JFXRadioButton onlySomePeopleRadio;

    @FXML
    private MFXButton editTagsButton;

    @FXML
    private MFXGenericDialog editTagsDialog;

    @FXML
    private JFXListView<Tag> listViewTagsToEdit;

    private List<JFXCheckBox> usernameCheckBoxes;

    /**
     * Constructor for add/edit expense control class.
     *
     * @param server   the server
     * @param mainCtrl the main control object
     */
    @Inject
    public AddEditExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        usernameCheckBoxes = new ArrayList<>();
    }

    /**
     * Initializes UI with default data.
     */
    public void initialize() {
        bundle = ResourceBundle.getBundle("languages.messages");

        // Setting up listener that checks if the value entered is a number.
        // Updates addButton after each change.
        howMuchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty() || (NumberUtils.isParsable(newValue)
                    && Double.parseDouble(newValue) > 0)) {
                howMuchTextField.setStyle("-fx-text-fill: black;");
            } else {
                howMuchTextField.setStyle("-fx-text-fill: red;");
            }

            updateAddButton();
        });

        // Setting up listener that updates addButton after each input change.
        whatForTextField.textProperty().addListener((observable, oldValue, newValue)
                -> updateAddButton());

        // Setting currencies available in the picker and selecting the default one
        currencyChoiceBox.getItems().addAll("EUR", "USD", "CHF");
        currencyChoiceBox.setValue("EUR");

        // Setting current date as date selected in the datePicker.
        whenDatePicker.setValue(LocalDate.now());

        // Setting up listener that enables and disables username selection
        // according to the selected splitting type.
        splitting.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            selectUsersScrollPane.setDisable(newValue == equallyBetweenRadio);
            updateAddButton();
        });

        EventOverviewCtrl.configureBackButton(backToStartScreenButton, backArrow, backRectangle);
        initializeKeyboardNavigations();

        contrastToggle.selectedProperty().addListener((obs, oldVal, newVal)-> {
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
        addEditExpenseTitle.setTextFill(Color.WHITE);
        whoPaidLabel.setTextFill(Color.WHITE);
        whoPaidChoiceBox.setStyle("-fx-background-color: #f5c52a");
        whatForLabel.setTextFill(Color.WHITE);
        howMuchLabel.setTextFill(Color.WHITE);
        currencyChoiceBox.setStyle("-fx-background-color: #f5c52a");
        whenLabel.setTextFill(Color.WHITE);
        whenDatePicker.setStyle("-fx-background-color:  #f5c52a");
        howToSplitLabel.setTextFill(Color.WHITE);
        equallyBetweenRadio.setTextFill(Color.WHITE);
        onlySomePeopleRadio.setTextFill(Color.WHITE);
        expenseTypeLabel.setTextFill(Color.WHITE);
        expenseTypeChoiceBox.setStyle("-fx-background-color: #f5c52a");

        addButton.setStyle("-fx-background-color: #75e9fc; -fx-background-radius: 20;" +
                " -fx-border-radius: 20");
        addButton.setTextFill(Color.BLACK);
        abortButton.setStyle("-fx-background-color: white; -fx-background-radius: 20;" +
                " -fx-border-radius: 20; -fx-border-color: #75e9fc");
        contrastToggle.setStyle("-fx-background-color: black");
        contrastToggle.setTextFill(Color.WHITE);
        editTagsButton.setStyle("-fx-background-color: #75e9fc; -fx-background-radius: 20;" +
                "-fx-border-radius: 20");
        editTagsButton.setTextFill(Color.BLACK);
    }

    private void resetColors() {
        biggestPane.setStyle("-fx-background-color: white");
        addEditExpenseTitle.setTextFill(Color.BLACK);
        whoPaidLabel.setTextFill(Color.BLACK);
        whoPaidChoiceBox.setStyle("-fx-background-color: white");
        whatForLabel.setTextFill(Color.BLACK);
        howMuchLabel.setTextFill(Color.BLACK);
        currencyChoiceBox.setStyle(null);
        whenLabel.setTextFill(Color.BLACK);
        howToSplitLabel.setTextFill(Color.BLACK);
        equallyBetweenRadio.setTextFill(Color.BLACK);
        onlySomePeopleRadio.setTextFill(Color.BLACK);
        expenseTypeLabel.setTextFill(Color.BLACK);
        expenseTypeChoiceBox.setStyle(null);

        addButton.setStyle("-fx-background-color:  #ff725e; -fx-background-radius: 20;" +
                " -fx-border-radius: 20");
        abortButton.setStyle("-fx-background-color: white; -fx-background-radius: 20;" +
                " -fx-border-radius: 20;; -fx-border-color:  #ff725e");
        contrastToggle.setStyle("-fx-background-color: white");
        contrastToggle.setTextFill(Color.BLACK);
        editTagsButton.setStyle("-fx-background-color:  #ff725e; " +
                "-fx-background-radius: 20;-fx-border-radius: 20");
        editTagsButton.setTextFill(Color.WHITE);
    }

    private void initializeKeyboardNavigations() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.web("#089ce7"));
        StartScreenCtrl.setButtonsOnFocus(addButton, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(abortButton, dropShadow);
        whoPaidChoiceBox.setOnKeyPressed(e -> {
            if ( e.getCode() == TAB) {
                whatForTextField.requestFocus();
            }
        });
        whatForTextField.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> whoPaidChoiceBox.requestFocus();
                case DOWN, TAB -> howMuchTextField.requestFocus();
            }
        });
        howMuchTextField.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> whatForTextField.requestFocus();
                case DOWN -> {
                    whenDatePicker.requestFocus();
                    whenDatePicker.requestFocus();
                }
                case TAB -> currencyChoiceBox.requestFocus();
            }
        });
        currencyChoiceBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                currencyChoiceBox.show();
            } else if (e.getCode() == RIGHT) {
                whenDatePicker.requestFocus();
                whenDatePicker.show();
            } else if (e.getCode() == LEFT) {
                howMuchTextField.requestFocus();
                System.out.println("here");
            }
        });
        whenDatePicker.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case DOWN, RIGHT, TAB -> equallyBetweenRadio.requestFocus();
                case UP -> howMuchTextField.requestFocus();
            }
        });

        editTagsDialog.setVisible(false);
        initTagListView();
    }

    void refresh() {
        whoPaidChoiceBox.requestFocus();
        changeLanguage(this.bundle.getLocale().getDisplayLanguage());
        // Setting whoPaid available choices. Sets the default value.
        whoPaidChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Participant participant) {
                return participant.name;
            }

            @Override
            public Participant fromString(String string) {
                // This method is not needed for ChoiceBox
                return null;
            }
        });

        updateTagChoices();

        // Creating box that will display user checkboxes vertically in the scrollPane.
        VBox userNameSelectionBox = new VBox();
        userNameSelectionBox.setSpacing(10);
        userNameSelectionBox.setPadding(new Insets(10));

        event.participants.stream().map(participant -> {
            JFXCheckBox participantCheckBox = new JFXCheckBox();
            participantCheckBox.setUserData(participant.id);
            participantCheckBox.textProperty().setValue(participant.name);
            return participantCheckBox;
        }).forEach(checkBox -> {
            usernameCheckBoxes.add(checkBox);
            userNameSelectionBox.getChildren().add(checkBox);

            checkBox.selectedProperty().addListener((observable, oldValue, newValue)
                    -> updateAddButton());
        });

        // Set the box with username checkboxes as the body of the scrollPane.
        selectUsersScrollPane.setContent(userNameSelectionBox);
        selectUsersScrollPane.setDisable(true);

        //Check if an Expense is selected to be edited and autofill
        //the corresponding fields with its data
        if (expense != null) {
            whoPaidChoiceBox.getItems().clear();
            whoPaidChoiceBox.getItems().addAll((event.participants));

            whenDatePicker.setValue(expense.date);
            whoPaidChoiceBox.setValue(expense.creditor);
            expenseTypeChoiceBox.setValue(expense.type.getName());
            expenseTypeChoiceBox.setUserData(expense.type.getId());
            whatForTextField.setText(expense.title);
            howMuchTextField.setText(String.valueOf(expense.amount));
            currencyChoiceBox.setValue(expense.currency);
            //tick the boxes for current debtors
            for (CheckBox checkBox : usernameCheckBoxes) {
                Participant attribute = server.getParticipant((long) checkBox.getUserData());
                if (expense.debtors.contains(attribute)) {
                    checkBox.setSelected(true);
                }
            }
        }
        //if the expense is null, then an Expense is set to be added
        else {
            whoPaidChoiceBox.getItems().addAll(event.participants);
            if (event.participants != null && !event.participants.isEmpty()) {
                whoPaidChoiceBox.setValue(event.participants.getFirst());
            }
        }

        //Only add a new placeholder tag if the event does not contain an old placeholder anymore
        if (event != null && event.tags.stream().noneMatch(tag -> tag.getName().isEmpty())) {
            addPlaceholderTag();
            server.updateEvent(event);
        }

        editTagsDialog.setVisible(false);
        if (isHighContrast) {
            contrastToggle.setSelected(true);
            changeToHighContrast();
        } else {
            contrastToggle.setSelected(false);
            resetColors();
        }
    }

    /**
     * If whatFor text field is empty or howMuch is empty or the value of howMuch
     * is not a number, or the only some people option is selected with no people selected,
     * disable the add button.
     */
    private void updateAddButton() {
        var whatFor = whatForTextField.textProperty().get();
        var howMuch = howMuchTextField.textProperty().get();

        addButton.setDisable(whatFor.isEmpty() ||
                howMuch.isEmpty() ||
                !(NumberUtils.isParsable(howMuch) && Double.parseDouble(howMuch) > 0) ||
                (splitting.getSelectedToggle() == onlySomePeopleRadio
                        && usernameCheckBoxes.stream()
                        .noneMatch(CheckBox::isSelected)
                ));
    }

    /**
     * Changes the language of the application based on the provided language code.
     * Updates the text properties of various UI elements to reflect the new language.
     *
     * @param languageCode The language code representing the desired language
     */
    public void changeLanguage(String languageCode) {
        bundle = ResourceBundle.getBundle("languages.messages", Locale.of(languageCode));
        addEditExpenseTitle.setText(bundle.getString("addEditExpenseTitle"));
        whoPaidLabel.setText(bundle.getString("whoPaidLabel"));
        whatForLabel.setText(bundle.getString("whatForLabel"));
        howMuchLabel.setText(bundle.getString("howMuchLabel"));
        whenLabel.setText(bundle.getString("whenLabel"));
        howToSplitLabel.setText(bundle.getString("howToSplitLabel"));
        equallyBetweenRadio.setText(bundle.getString("equallyBetweenRadio"));
        onlySomePeopleRadio.setText(bundle.getString("onlySomePeopleRadio"));
        expenseTypeLabel.setText(bundle.getString("expenseTypeLabel"));
        abortButton.setText(bundle.getString("cancelButtonCommon"));
        addButton.setText(bundle.getString("addButtonCommon"));
        contrastToggle.setText(bundle.getString("highContrast"));
    }

    /**
     * Closes the window and cancels the expense creation.
     */
    public void abort() {
        clearFields();
        mainCtrl.showOverview(event, this.bundle, this.isHighContrast);
    }

    /**
     * Adds the expense with current input.
     */
    public void add() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("warning"));
        alert.setHeaderText(bundle.getString("addThisExpense"));
        alert.setContentText(bundle.getString("pressOk"));

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        if (result == ButtonType.OK) {
            String whatFor = whatForTextField.getText();
            Participant c = whoPaidChoiceBox.getValue();
            double howMuch = Double.parseDouble(howMuchTextField.getText());
            String currency = currencyChoiceBox.getValue();
            Tag tag = server.getTag((long) expenseTypeChoiceBox.getUserData());
            LocalDate date = whenDatePicker.getValue();

            // For consistency, we store every amount in EUR, but we don't change the
            // currency symbol, so we know which currency was used at first
            // this means every amount should be converted if the currency is not EUR
            howMuch *= server.getConversionRate(currency, "EUR", date);

            Expense newExpense = new Expense(whatFor, howMuch, currency, tag, date);

            chooseIncludedUsers(newExpense);

            newExpense.creditor = c;

            if (expense != null) {
                //update the existing expense if applicable
                expense.creditor = c;
                expense.title = whatFor;
                expense.amount = howMuch;
                expense.currency = currency;
                expense.type = tag;
                expense.date = date;
                expense.debtors.clear();
                expense.debtors.addAll(newExpense.debtors);

                server.updateExpense(expense,event.id);
                server.updateEvent(event);
            } else {

                Expense fromServer = server.addExpense(newExpense);

                event.expenses.add(fromServer);
                server.updateEvent(event);
            }
            clearFields();
            mainCtrl.showOverview(event, this.bundle, this.isHighContrast);
        } else {
            alert.hide();
        }
    }

    private void chooseIncludedUsers(Expense newExpense) {
        if (equallyBetweenRadio.isSelected()) {
            // Add all users
            for (CheckBox checkBox : usernameCheckBoxes) {
                Participant p = server.getParticipant((long) checkBox.getUserData());
                if(!newExpense.debtors.contains(p))
                    newExpense.debtors.add(p);
            }
        } else{
            // Add only selected users
            for (CheckBox checkBox : usernameCheckBoxes) {
                if (checkBox.isSelected()) {
                    Participant p = server.getParticipant((long) checkBox.getUserData());
                    if(!newExpense.debtors.contains(p))
                        newExpense.debtors.add(p);
                } else{
                    Participant p = server.getParticipant((long) checkBox.getUserData());
                    newExpense.debtors.remove(p);
                }
            }
        }
    }

    /**
     * Clears and sets default values for some fields.
     */
    private void clearFields() {
        whoPaidChoiceBox.getItems().clear();

        if (!event.participants.isEmpty())
            whoPaidChoiceBox.setValue(event.participants.getFirst());

        whatForTextField.clear();
        howMuchTextField.clear();
        currencyChoiceBox.setValue("EUR");
        equallyBetweenRadio.setSelected(true);
        for (CheckBox checkBox : usernameCheckBoxes) checkBox.setSelected(false);
        whenDatePicker.setValue(LocalDate.now());
    }

    /**
     * Use keyboard shortcuts for adding an expense
     * and for going back to Event Page
     *
     * @param e the key event
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                if (!addButton.isDisable())
                    add();
                break;
            case ESCAPE:
                abort();
                break;
            default:
                break;
        }
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
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Sets the expense of this instance
     * @param expense the expense to set it as
     */
    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    /**
     * Method which shows the tags dialog, this is the dialog where you can manage the event's tags
     */
    public void showTagsDialog() {
        editTagsDialog.setVisible(true);
        editTagsDialog.toFront();
        listViewTagsToEdit.setItems(FXCollections
                .observableArrayList(event.tags));
    }

    /**
     * Method which closes the tags dialog
     */
    public void closeEditTags() {
        System.out.println("Saving tags:");
        for (Tag tag : event.tags) {
            System.out.println(tag.toString());
            server.updateTag(tag);

        }
        editTagsDialog.setVisible(false);
        updateTagChoices();
    }

    /**
     * Tags ListView initializer.
     * This method sets the font-family and sets up a cell-factory for the listview.
     */
    private void initTagListView() {
        listViewTagsToEdit.setStyle("-fx-font-family: 'Century Gothic'");
        listViewTagsToEdit.setCellFactory(param -> createTagListCell());
    }

    /**
     * Creates ListCell for the current Tag object
     * @return returns the generated ListCell
     */
    private ListCell<Tag> createTagListCell() {
        return new ListCell<>() {
            // All Nodes in a Tag list-item:
            private final Button deleteButton = new Button("x");
            private final TextField textField = new TextField();
            private final Region spacer = new Region();
            private final ColorPicker colorPicker = new ColorPicker();
            private final HBox hbox = new HBox(textField, spacer, colorPicker, deleteButton);

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
            }

            // Initialization of event handlers
            {
                //Delete button event handler:
                deleteButton.setOnAction(actionEvent -> {
                    Tag tag = getItem();
                    if (tag != null && canDelete(tag.getName())) {
                        editTagsDialog.setHeaderText("Edit Tags");
                        listViewTagsToEdit.getItems().remove(tag);
                        event.tags.remove(tag);
                        server.updateEvent(event);
                        server.deleteTag(tag.getId());
                    } else {
                        editTagsDialog.setHeaderText("Tag is being used, can not delete!");
                    }
                });

                //Color picker event handler:
                colorPicker.setOnAction(actionEvent -> {
                    Tag tag = getItem();
                    setTagColor(tag,
                            colorPicker.getValue());
                });

                //TextField event handler:
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    // Use the header-text of the dialog as an error-label, to print errors
                    //Can not name the tag to an empty string or required tag
                    // (the latter because it would disable the elements
                    // immediately and not allow for update anymore)
                    if (newValue.isEmpty()) {
                        editTagsDialog.setHeaderText("Tag name can't be empty!");
                    }
                    //Can not name the tag to an already existing tag-name
                    else if (requiredTags.contains(newValue)){
                        editTagsDialog.setHeaderText("Tag name is already used!");
                    }
                    else { //New value is valid thus update the name values of the Object
                        Tag tag = getItem();
                        if (tag != null) {
                            tag.setName(newValue);
                        }
                        editTagsDialog.setHeaderText("Edit Tags"); //Reset 'error-label'
                    }
                });
            }

            // Override updateItem method
            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                if (empty || tag == null) {
                    setText(null);
                    setGraphic(null);
                } else { // Tag != null
                    //Set the fx:id's of the list-view nodes
                    deleteButton.idProperty().set("deleteTag_" + tag.id);
                    textField.idProperty().set("testFieldTag_" + tag.id);
                    colorPicker.idProperty().set("colorPickerTag_" + tag.id);

                    //Set the text-field and color picker to the tag's values
                    textField.setText(tag.getName());
                    colorPicker.setValue(Color.web(tag.getColor()));
                    if (requiredTags.contains(tag.getName())) {
                        textField.setDisable(true);
                        deleteButton.setDisable(true);
                    }
                    setGraphic(hbox);
                }
            }
        };
    }

    private static final List<String> requiredTags = Arrays.asList(
            "Entrance Fees", "Food", "Travel");

    /**
     * Method to check if a tag can be deleted
     * @param name String name of the tag
     * @return returns boolean value representing if the tag can be deleted
     */
    private boolean canDelete(String name) {
        // Can not delete if this tag is one of the required tags
        if (requiredTags.contains(name)) return false;

        for (Expense expense : event.expenses) {
            // Can not delete if an expense in the event uses this tag
            if (expense.type.getName().equals(name)) return false;
        }
        return true;
    }

    private static void setTagColor(Tag tag, Color selectedColor) {
        if (tag != null) {
            String hex = String.format("#%02X%02X%02X",
                    (int) (selectedColor.getRed() * 255),
                    (int) (selectedColor.getGreen() * 255),
                    (int) (selectedColor.getBlue() * 255));

            tag.setColor(hex);
        }
    }

    /**
     * Add an empty tag to the event, so that the user can customise this new tag.
     */
    private void addPlaceholderTag() {
        Tag tag = new Tag("", "#fff");
        tag = server.addTag(tag);
        event.tags.add(tag);
        this.event = server.updateEvent(event);
    }

    private void updateTagChoices() {
        // Setting expenseType available choices. Sets the default value.
        List<String> tagNames = new ArrayList<>();
        for (Tag tag : event.tags) {
            if (!tag.getName().isEmpty()) {
                tagNames.add(tag.getName());
            }
        }
        expenseTypeChoiceBox.setItems(FXCollections
                .observableArrayList(tagNames));
        if (expense != null) {
            expenseTypeChoiceBox.setValue(expense.type.getName());
            expenseTypeChoiceBox.setUserData(expense.type.getId());
        }
        else {
            expenseTypeChoiceBox.setValue(event.tags.getFirst().getName());
            expenseTypeChoiceBox.setUserData(event.tags.getFirst().getId());
        }
    }

    /**
     * On-Action method of the expenseType-ChoiceBox
     * This method finds the id of the selected Tag and sets
     *  the user-data attribute of the choice-box to this id.
     * <p>
     * This user-data is used by the Add method, which adds the expense
     * with the Tag (retrieved by this userdata id) to the database
     *
     * @param actionEvent action event
     */
    public void changeTagSelection(ActionEvent actionEvent) {
        System.out.println("Change Tag Choice!!");
        System.out.println("Value: " + expenseTypeChoiceBox.getValue());

        //TODO: need to update the userdata field to the id of the new tag
        //Find the id of the tag with the same name
        String name = expenseTypeChoiceBox.getValue();

        // Find the Tag with the given name and get its ID.
        // If not found: default to the first tag in the events tags list
        long id = event.tags.stream()
                .filter(obj -> obj.getName().equals(name))
                .mapToLong(obj -> obj.id)
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("defaulted to first id");
                    return event.tags.isEmpty() ? 0 : event.tags.getFirst().getId();
                });

        if (id != -1) {
            System.out.println("ID of " + name + " is: " + id);
        } else {
            throw new RuntimeException("wtf where is the tag");
        }

        expenseTypeChoiceBox.setUserData(id);

        System.out.println("userdata: " + expenseTypeChoiceBox.getUserData());
        System.out.println("Fetched Tag (of userdata): " +
                server.getTag((long) expenseTypeChoiceBox.getUserData()));
    }
}
