/*
TODO: write tests for datastructures
TODO: write tests for frontend with TestFx
    (and Robot for locating elements on the ui and interacting with them)
    and Mockito (for mocking serverUtils, dependency injection, mockito.when, mockito.verify)
 */

package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

// JSON dump imports
import java.io.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.*;

public class AdminViewCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Button restoreEventFromJSON;
    @FXML
    public Label orderEventsBy;
    @FXML
    public Label adminView;
    @FXML
    public ImageView backArrow;
    @FXML
    public Rectangle backRectangle;
    @FXML
    public AnchorPane backToStartScreenButton;
    @FXML
    private Accordion eventsAccordion;
    @FXML
    private ComboBox<String> orderCombo;
    @FXML
    private Button orderButton;
    @FXML
    private Label jsonError;
    private boolean orderDirection = true;
    public ResourceBundle bundle;

    /**
     * Constructor for this class, initializes this.server and this.mainCtrl
     * @param server reference to the Commons.ServerUtils class, for api communication
     * @param mainCtrl reference to the mainCtrl class, for navigation through views
     */
    @Inject
    public AdminViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initialize method
     * Gets called once per session when the view is opened for the first time
     * Sets up the combo-box for selecting the order attribute + calls this.refresh()
     */
    public void initialize() {
        EventOverviewCtrl.configureBackButton(backToStartScreenButton, backArrow, backRectangle);
        orderCombo.getItems().addAll("Title", "Creation Date", "Last Activity");
        orderCombo.getSelectionModel().selectFirst();
        orderCombo.setStyle("-fx-font-family: 'Century Gothic'");
        // Ad combo box onchange listener --> refresh page
        orderCombo.valueProperty().addListener((observable, oldValue, newValue) -> this.refresh());
        bundle = ResourceBundle.getBundle("languages.messages");
        this.refresh();
    }

    /**
     * Fetches the events from the database, orders them and populates the page
     */
    public void refresh() {
        //Clear the page
        eventsAccordion.getPanes().remove(0,eventsAccordion.getPanes().size());
        eventsAccordion.setStyle("-fx-font-family: 'Open Sans Regular'");
        List<Event> events = server.getAllEvents();
        //Order the events based on the current order attribute and current order direction
        events = orderEvents(orderCombo.getValue(), this.orderDirection, events);

        for (Event event : events) {
            TitledPane tp = generateTitledPane(event);
            eventsAccordion.getPanes().add(tp);
        }
        changeLanguage(this.bundle.getLocale().getLanguage());
    }

    /**
     * Orders the provided events list based on the provided orderAttribute and orderDirection
     * @param orderAttribute attribute on which the Events should be sorted
     * @param orderDirection direction of the ordering
     * @param events list of Events to be sorted
     * @return returns the ordered list of Events
     */
    private List<Event> orderEvents(String orderAttribute,
                                    boolean orderDirection,
                                    List<Event> events) {
        List<Event> result = new java.util.ArrayList<>(List.copyOf(events));

        if (orderDirection) {
            switch (orderAttribute){
                case "Title":
                    result.sort(Comparator.comparing((Event event) -> event.name));
                    jsonError.setText("");
                    break;
                case "Creation Date":
                    result.sort(Comparator.comparing((Event event) -> event.creationDate));
                    jsonError.setText("");
                    break;
                case "Last Activity":
                    result.sort(Comparator.comparing(this::lastActivity));
                    jsonError.setText("");
                    break;
                default:
                    jsonError.setText("ORDER-ATTRIBUTE ERROR");
            }
        }
        else {
            switch (orderAttribute){
                case "Title":
                    result.sort(Comparator.comparing((Event event) -> event.name).reversed());
                    jsonError.setText("");
                    break;
                case "Creation Date":
                    result.sort(Comparator.comparing(
                            (Event event) -> event.creationDate).reversed());
                    jsonError.setText("");
                    break;
                case "Last Activity":
                    result.sort(Comparator.comparing(this::lastActivity).reversed());
                    jsonError.setText("");
                    break;
                default:
                    jsonError.setText("ORDER-ATTRIBUTE ERROR");
            }
        }

        return result;
    }

    /**
     * Find the last activity Date associated to a provided event
     * Looks at the Event creationDate, dates of the payments and dates of the expenses
     * @param event Event of which the lastActivity is searched for
     * @return returns the lastActivity (= Date) found
     */
    private Date lastActivity(Event event) {
        // Create a list of all dates associated to the event
        List<Date> dates = new ArrayList<>();
        dates.add(event.creationDate);
        for (Payment payment : event.payments) {
            dates.add(payment.date);
        }
        for (Expense expense : event.expenses) {
            dates.add(expense.creationDate);
        }

        // Find the most recent date in the list
        Date mostRecentDate = dates.getFirst();
        for (Date date : dates) {
            if (date.after(mostRecentDate)) {
                mostRecentDate = date;
            }
        }

        return mostRecentDate;
    }

    /**
     * Generates a TitledPane (JavaFX) (based on a template created in SceneBuilder)
     *      using all data of a provided Event
     * @param event Event of which the TitledPane will be generated
     * @return returns the generated TitledPane
     */
    //TODO: add creation-date and available tags
    //TODO: this method should be shorter
    private TitledPane generateTitledPane(Event event) {
        // Creating a new titledPane, which contains all the event data
        TitledPane tp = new TitledPane();
        tp.setText(event.name);
        tp.setAnimated(false);

        // Creating the pane that is the content of the titled pane
        Pane contentPane = new Pane();
        contentPane.setStyle("-fx-pref-height: 438; -fx-pref-width: 530; -fx-min-height: 438;");

        // Adding the eventName to the content pane
        Label eventName = new Label(event.name);
        eventName.setFont(Font.font("System Bold", 16));
        setLayout(eventName, 14.0, 9.0);
        contentPane.getChildren().add(eventName);

        // Adding the inviteCode (= event.id) to the content pane
        Label inviteCode = new Label(bundle.getString("inviteCodeHeader") + " " + event.id);
        setLayout(inviteCode, 250.0, 13.0);
        contentPane.getChildren().add(inviteCode);

        // Adding the download button to the content pane
        Button download = new Button(bundle.getString("download"));
        setLayout(download, 410.0, 18.0);
        download.setStyle("-fx-background-radius: 12;-fx-background-color: #bde0fe");
        download.setPrefHeight(14);
        download.setOnAction(actionEvent -> download(event.id));
        contentPane.getChildren().add(download);

        // Adding the delete button to the content pane
        Button delete = new Button(bundle.getString("delete"));
        delete.setPrefHeight(14);
        delete.setStyle("-fx-background-color: #FFADAD;-fx-background-radius: 12");
        setLayout(delete, 507.0, 18.0);
        delete.setOnAction(actionEvent -> delete(event.id));
        contentPane.getChildren().add(delete);

        // Adding the participant label to the content pane
        Label participants = new Label(bundle.getString("participantsHeader") + ": "
                + event.participantsString());
        setLayout(participants, 28.0, 45.0);
        contentPane.getChildren().add(participants);

        // Adding the expenses indicator to the content pane
        Label expIndicator = new Label(bundle.getString("expensesHeader") + ":");
        setLayout(expIndicator, 17.0, 70.0);
        contentPane.getChildren().add(expIndicator);

        // Creating the scrollPane and accordion containing the event's expenses
        ScrollPane expenses = new ScrollPane();
        setLayout(expenses, 28.0, 92.0);
        expenses.setStyle("-fx-pref-height: 155.0; -fx-pref-width: 533.0;");

        Accordion expenseAccordion = new Accordion();
        expenseAccordion.setStyle("-fx-pref-height: 151.0; -fx-pref-width: 518.0;");

        // Adding every expense to the scrollPane
        for (Expense exp : event.expenses) {
            TitledPane titledPane = new TitledPane();
            titledPane.setText(exp.title + " - " + exp.currency + exp.amount + ", by "
                    + exp.creditor.name + " - " + exp.type.getName());
            titledPane.setStyle("-fx-pref-height: 118.0;" +
                                "-fx-pref-width: 517.0;" +
                                "-fx-animated: false;" +
                                "-fx-text-fill: " + exp.type.getColor() + ";");
            AnchorPane ap = new AnchorPane();
            ap.setStyle("-fx-pref-height: 38.0; -fx-pref-width: 530.0;");
            int counter = 0;
            for (Participant debtor : exp.debtors) {
                Label deb = new Label(debtor.name);
                deb.setLayoutY(5 + counter*20);
                ap.getChildren().add(deb);
                counter++;
            }

            titledPane.setContent(ap);
            expenseAccordion.getPanes().add(titledPane);
        }

        expenses.setContent(expenseAccordion);
        contentPane.getChildren().add(expenses);

        // Adding the payments indicator to the content pane
        Label paymentsIndicator = new Label(bundle.getString("payments") + ":");
        setLayout(paymentsIndicator, 16.0, 255.0);
        contentPane.getChildren().add(paymentsIndicator);

        // Creating the payments scrollPane
        ScrollPane payments = new ScrollPane();
        payments.setStyle("-fx-pref-height: 148.0; -fx-pref-width: 533.0;");
        setLayout(payments, 25.0, 276.0);

        // Adding every payment as a label into the scrollPane
        Pane paymentsPane = generatePaymentsPane(event);
        payments.setContent(paymentsPane);
        contentPane.getChildren().add(payments);
        // Setting the content of the titledPane (= event data) and returning it
        tp.setContent(contentPane);
        return tp;
    }

    /**
     * Helper method for generateTitledPane
     * Generates the pane containing all the payments connected to the provided event
     * This pane contains a label for every individual payment
     * @param event Event from which the payments are used
     * @return returns the generated pane
     */
    private Pane generatePaymentsPane(Event event) {
        // Adding every payment as a label into the scrollPane
        Pane paymentsPane = new Pane();
        //Using a counter to keep the labels at the same distance from each other
        int counter = 0;

        paymentsPane.setStyle("-fx-pref-height: 145.0; -fx-pref-width: 518.0;");
        for (Payment pay : event.payments) {
            String labelTitle = pay.currency + pay.amount
                    + ", from " + pay.payer + " to " + pay.receiver + " at " + pay.date;

            Label payLabel = new Label(labelTitle);
            setLayout(payLabel, 14.0, 15 + counter*35);
            paymentsPane.getChildren().add(payLabel);
            counter++;
        }
        return paymentsPane;
    }

    /**
     * Helper method for generateTitledPane
     * Sets the layout attributes of a provided javafx.scene.Node element
     * @param x x coördinate (relative to parent)
     * @param y y coördinate (relative to parent)
     */
    private void setLayout(Node element, double x, double y) {
        element.setLayoutX(x);
        element.setLayoutY(y);
    }

    /**
     * This method gets called when the user presses on the "Download" button of an Event
     * Generated a JSON object of the Event (event is fetched using the ID) using jackson
     * Opens a FileChooser (JavaFX) in the default download directory
     * @param id id of the Event which needs to be downloaded
     */
    private void download(long id) {
        Event event = server.getEvent(id);

        ObjectMapper objectMapper = new ObjectMapper();
        // Register JavaTimeModule to handle LocalDate
        objectMapper.registerModule(new JavaTimeModule());

        String eventJSON;
        try {
            eventJSON = objectMapper.writeValueAsString(event);
            System.out.println(eventJSON);

            // Downloading the JSON File:
            // Create a FileChooser
            FileChooser fileChooser = new FileChooser();

            // Set extension filter (download as .json)
            ExtensionFilter extFilter = new ExtensionFilter("JSON files (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);

            // Set initial directory to the user's downloads folder
            String userHome = System.getProperty("user.home");
            File downloadsDirectory = new File(userHome + File.separator + "Downloads");
            fileChooser.setInitialDirectory(downloadsDirectory);

            // Pre-name the file to the event's name
            fileChooser.setInitialFileName(event.name + ".json");

            // Show save file dialog
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try (FileWriter writer = new FileWriter(file)) {
                    // Write the JSON string to the file
                    writer.write(eventJSON);
                } catch (IOException ex) {
                    System.err.println("Error saving JSON to file: " + ex.getMessage());
                }
            }

        } catch (JsonProcessingException e) {
            System.err.println("Error converting object to JSON: " + e.getMessage());
            jsonError.setText(bundle.getString("conversionToJSONError"));
        }
    }

    /**
     * This method gets called when the user presses on the "Delete" button of an Event
     * Removes the ID from the database and refresh the page
     * @param id id of the Event which needs to be deleted
     */
    private void delete(long id) {
        server.removeEvent(id);
        this.refresh();
    }

    /**
     * Routing method. Navigates back to the Start screen
     */
    public void toStart() {
        mainCtrl.showStartScreen(this.bundle, false);
    }

    /**
     * Gets called when the user presses the "Restore Event" button.
     * Opens a FileChooser (JavaFX) and tries to parse the uploaded JSON-file to
     *      an Event using jackson
     * If the ID of the uploaded Event is already in the database, it is not added or overridden
     * @param actionEvent actionEvent
     */
    public void restoreEvent(ActionEvent actionEvent) {
        jsonError.setText("");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Event Backup");
        // Only accept .json files
        fileChooser.getExtensionFilters().add(new ExtensionFilter("JSON files (*.json)", "*.json"));

        // Set initial directory to the user's downloads folder
        String userHome = System.getProperty("user.home");
        File downloadsDirectory = new File(userHome + File.separator + "Downloads");
        fileChooser.setInitialDirectory(downloadsDirectory);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile == null) return; // No file selected (window closed)

        try {
//            // Read contents of the selected file as JSON
            ObjectMapper objectMapper = new ObjectMapper();
            // Register JavaTimeModule to handle LocalDate
            objectMapper.registerModule(new JavaTimeModule());

//            String json = objectMapper.readValue(selectedFile, String.class);
//
//            Event backup = objectMapper.readValue(json, Event.class);

            // Read contents of the selected file as String
            StringBuilder stringBuilder = new StringBuilder();
            FileReader fileReader = new FileReader(selectedFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();

            String json = stringBuilder.toString();

            // Parse the JSON string using ObjectMapper
            Event backup = objectMapper.readValue(json, Event.class);

            // Check if the Event is not still in the database
            if (server.getEvent(backup.id) != null) {
                jsonError.setText(bundle.getString("alreadyInTheDatabase"));
                return;
            }

            System.out.println(backup);

            System.out.println("Participants in event: " + backup.participants.size());
            for (Participant participant : backup.participants) {
                System.out.println("Adding backup participant");
                server.addParticipant(participant);
            }

            System.out.println("Expenses in event: " + backup.expenses.size());
            for (Expense expense : backup.expenses) {
                System.out.println("Adding backup expense");
                server.addExpense(expense);
            }

            System.out.println("Payments in event: " + backup.payments.size());
            for (Payment payment : backup.payments) {
                System.out.println("Adding backup payment");
                server.addPayment(payment);
            }

            server.addEvent(backup);


            jsonError.setText(bundle.getString("successfullyRestored"));

            this.refresh();
        }
        catch (IOException ioException) {
            System.err.println("Error in opening the file (not formatted properly?):" +
                    "\n   " + ioException.getMessage());
            jsonError.setText(bundle.getString("couldNotProcessFile"));
        }
    }

    /**
     * Called when button to change the order direction is pressed
     * Changes this.orderDirection, which indicated the order direction (false --> reversed)
     * @param actionEvent actionEvent
     */
    public void orderDirectionChange(ActionEvent actionEvent) {
        this.orderDirection = !this.orderDirection;
        if (orderButton.getText().equals("<--")) orderButton.setText("-->");
        else orderButton.setText("<--");

        this.refresh();
    }

    private void changeLanguage(String languageCode) {
        bundle = ResourceBundle.getBundle("languages.messages", Locale.of(languageCode));
        adminView.setText(bundle.getString("adminView"));
        orderEventsBy.setText(bundle.getString("orderEventsBy"));
        restoreEventFromJSON.setText(bundle.getString("restoreEventFromJSON"));
    }

    /**
     * Actions when user presses a key in Admin View
     * @param e the key event
     */
    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
            toStart();
        }
    }
}