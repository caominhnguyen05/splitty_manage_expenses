package client.scenes;

import client.data_structures.Debt;
import client.data_structures.Deleteable;
import client.data_structures.Pair;
import client.utils.PreferenceUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXToggleButton;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.LEFT;

public class OpenDebtsCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    public boolean isHighContrast;
    @FXML
    public Button paymentLogButton;
    @FXML
    public Button closePaymentLogButton;
    @FXML
    public JFXToggleButton contrastToggle;
    @FXML
    public AnchorPane biggestPane;
    @FXML
    public Pane currencyImagePane;
    @FXML
    public Label paymentDialogLabel;

    private PreferenceUtils preferences;

    public Event event;

    public ResourceBundle bundle;
    @FXML
    private Label openDebtsTitle;
    @FXML
    private Button seeDebtsButton;

    @FXML
    private Label availableParticipants;
    @FXML
    private ListView<HBox> logListView;

    @FXML
    public ImageView backArrow;
    @FXML
    public Rectangle backRectangle;
    @FXML
    public AnchorPane backToStartScreenButton;

    public List<Debt> debts;

    private Participant currentlyShownDebtor;

    @FXML
    private Accordion accordion;
    @FXML
    private Accordion accordion1;
    @FXML
    private AnchorPane paymentDialog;
    @FXML
    private TextField input;
    @FXML
    private Label creditorsList;
    @FXML
    private Label errorMSG;
    @FXML
    private Label sumDebts;
    @FXML
    private Label sumCredits;
    @FXML
    private Label totalSum;
    @FXML
    private ListView<String> listViewCurrencies;
    @FXML
    public ImageView currencyPicture;
    private List<Participant> participants;
    private List<String> participantNames;
    private List<String> participantNamesLowercase;
    private List<Expense> expenses;

    public Participant savedForCurrency;

    private final Image euroFlag = new Image("/Euro_picture.png");
    private final Image dollarFlag = new Image("/Dollar_picture.png");
    private final Image francFlag = new Image("/Franc_picture.png");

    /**
     * Constructs a new OpenDebtsCtrl instance with the specified ServerUtils and MainCtrl.
     *
     * @param server   the ServerUtils instance used for server communication
     * @param mainCtrl the MainCtrl instance used for controlling the main application flow
     */
    @Inject
    public OpenDebtsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.participants = new ArrayList<>();
        this.participantNames = new ArrayList<>();
        this.participantNamesLowercase = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.debts = new ArrayList<>();
        preferences = new PreferenceUtils();
    }

    /**
     * Initializes the page
     */
    public void initialize() {
        paymentDialog.setVisible(false);
        totalSum.setStyle("-fx-font-family: 'Century Gothic';-fx-font-size: 15;" +
                "-fx-background-color: #FFE5D9;-fx-background-radius: 13");
        totalSum.setPrefHeight(35);
        EventOverviewCtrl.configureBackButton(backToStartScreenButton, backArrow, backRectangle);
        accordion.setStyle("-fx-font-family: 'Century Gothic'");
        accordion1.setStyle("-fx-font-family: 'Century Gothic'");

        paymentLogButton.setFocusTraversable(true);
        seeDebtsButton.setFocusTraversable(true);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.web("#089ce7"));
        StartScreenCtrl.setButtonsOnFocus(seeDebtsButton, dropShadow);
        StartScreenCtrl.setButtonsOnFocus(paymentLogButton, dropShadow);

        input.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onNewDebtor();
            } else if (e.getCode() == KeyCode.TAB) {
                seeDebtsButton.requestFocus();
            }
        });
        seeDebtsButton.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> onNewDebtor();
                case TAB, RIGHT -> paymentLogButton.requestFocus();
                case LEFT -> input.requestFocus();
            }
        });

        paymentLogButton.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                showPaymentLog();
                paymentDialog.requestFocus();
            }
        });
        paymentDialog.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W && e.isControlDown()) {
                closePaymentLog();
            }
        });

        currencyPicture.setImage(euroFlag);
        preferences.saveCurrency("EUR");

        initializeCurrencyListView();
        listViewCurrencies.setVisible(false);

        contrastToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                isHighContrast = true;
                changeHighContrast();
            } else {
                isHighContrast = false;
                resetColors();
            }
        });
    }

    private void changeHighContrast() {
        biggestPane.setStyle("-fx-background-color: black");
        openDebtsTitle.setTextFill(Color.WHITE);
        availableParticipants.setTextFill(Color.WHITE);
        creditorsList.setTextFill(Color.WHITE);
        sumDebts.setTextFill(Color.WHITE);
        sumCredits.setTextFill(Color.WHITE);
        errorMSG.setTextFill(Color.WHITE);

        seeDebtsButton.setStyle("-fx-background-color: #75e9fc; -fx-background-radius: 13");
        seeDebtsButton.setTextFill(Color.BLACK);
        paymentLogButton.setStyle("-fx-background-color: #75e9fc; -fx-background-radius: 13");
        paymentLogButton.setTextFill(Color.BLACK);
        contrastToggle.setTextFill(Color.WHITE);
        contrastToggle.setStyle("-fx-background-color: black");
        totalSum.setStyle("-fx-font-family: 'Century Gothic';-fx-font-size: 15;" +
                "-fx-background-color: #f5c52a;-fx-background-radius: 13");
        currencyImagePane.setStyle("-fx-background-color: white");
    }

    private void resetColors() {
        biggestPane.setStyle("-fx-background-color: white");
        openDebtsTitle.setTextFill(Color.BLACK);
        availableParticipants.setTextFill(Color.BLACK);
        creditorsList.setTextFill(Color.BLACK);
        sumDebts.setTextFill(Color.BLACK);
        sumCredits.setTextFill(Color.BLACK);
        errorMSG.setTextFill(Color.BLACK);

        seeDebtsButton.setStyle("-fx-background-color:  #000080; -fx-background-radius: 13");
        seeDebtsButton.setTextFill(Color.WHITE);
        paymentLogButton.setStyle("-fx-background-color:  #000080; -fx-background-radius: 13");
        paymentLogButton.setTextFill(Color.WHITE);
        contrastToggle.setTextFill(Color.BLACK);
        contrastToggle.setStyle("-fx-background-color: white");
        totalSum.setStyle("-fx-font-family: 'Century Gothic';-fx-font-size: 15;" +
                "-fx-background-color: #FFE5D9;-fx-background-radius: 13");
    }

    /**
     * Refreshes the view by filling in the participant lists and displaying the names
     */
    void refresh() {
        changeLanguage(this.bundle.getLocale().getLanguage());

        // Fill the participants and participantNames lists (normal & lowercase)
        this.participants = new ArrayList<>();
        this.participantNames = new ArrayList<>();
        this.participantNamesLowercase = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.debts = new ArrayList<>();

        participants.addAll(event.participants);

        accordion.getPanes().clear();
        accordion1.getPanes().clear();
        sumDebts.setText("");
        sumCredits.setText("");
        totalSum.setText("");
        totalSum.setVisible(false);

        for (Participant participant : participants) {
            participantNames.add(participant.name);
        }

        participantNamesLowercase = participantNames.stream()
                .map(String::toLowerCase)
                .toList();

        // Fill the list with debts
        getDebts();

        // Set the text to the list of participants
        StringBuilder builder = new StringBuilder();

        // Display every participant
        for (String p : participantNames) {
            builder.append(p);
            if (participantNames.indexOf(p) < participantNames.size() - 1) {
                builder.append(", ");
            }

        }
        creditorsList.setText(builder.toString());
        if (isHighContrast) {
            contrastToggle.setSelected(true);
            changeHighContrast();
        } else {
            contrastToggle.setSelected(false);
            resetColors();
        }

        listViewCurrencies.setVisible(false);
    }

    /**
     * Changes the language of the application based on the provided language code.
     * Updates the text properties of various UI elements to reflect the new language.
     *
     * @param languageCode The language code representing the desired language
     */
    public void changeLanguage(String languageCode) {
        bundle = ResourceBundle.getBundle("languages.messages", Locale.of(languageCode));

        // Main things
        openDebtsTitle.setText(bundle.getString("openDebtsTitle"));
        availableParticipants.setText(bundle.getString("availableParticipants"));
        seeDebtsButton.setText(bundle.getString("seeDebtsButton"));
        input.setPromptText(bundle.getString("inputCreditorName"));
        contrastToggle.setText(bundle.getString("highContrast"));
    
        // Payment log stuff
        paymentDialogLabel.setText(bundle.getString("paymentLog"));
        paymentLogButton.setText(bundle.getString("paymentLog"));
        closePaymentLogButton.setText(bundle.getString("closePaymentLog"));
    }

    /**
     * Fill the debts list with all the "expenses" in the system
     */
    public void getDebts() {
        // Mapping expenses to debts
        expenses.addAll(event.expenses);
        for (Expense expense : expenses) {
            String currentCreditor = expense.creditor.name;
            for (Participant debtor : expense.debtors) {
                debts.add(new Debt(currentCreditor, debtor.name,
                        expense.amount / expense.debtors.size()));
            }
        }
        // Combine the expenses so only one debt is owed from A to B
        simplifyDebts();

        filterPayments();
    }

    /**
     * Sets all the "deleteOrNot" fields for all participants to true or false,
     * whether the participant has debts associated to him or not.
     *
     * @return returns the Deleteable object that is constructed
     */
    public Deleteable getDeleteable() {
        Deleteable deleteable = new Deleteable(event.participants);
        for (Participant participant : event.participants) {
            boolean delete = true;
            for (Debt debt : debts) {
                if (debt.getDebtor().equalsIgnoreCase(participant.name)
                        || debt.getCreditor().equalsIgnoreCase(participant.name)) {
                    if (debt.getAmount() != 0) {
                        delete = false;
                        break;
                    }
                }
            }
            deleteable.setDelete(participant.name, delete);
        }
        return deleteable;
    }

    /**
     * calculates the balance for every participant
     * by substracting the money he owes from the money
     * he needs to receive
     *
     * @param participant the participant to calculate balance for
     * @return balance of participant
     */
    public double calculateBalance(Participant participant){
        double positiveBalance=0; //money he has to receive
        double negativeBalance=0; //money he owes

        List<Expense> currentExpenses = new ArrayList<>();
        currentExpenses.addAll(event.expenses);
        for (Expense e : currentExpenses) {
            if (e.creditor.equals(participant)) {
                if (!e.debtors.contains(participant))
                    positiveBalance += e.amount;
                else
                    positiveBalance += e.amount - e.amount / e.debtors.size();
                //do not include the money that he owes to himself
                //if the expense is split equally between ALL participants
            } else if (e.debtors.contains(participant))
                negativeBalance += e.amount / e.debtors.size();
        }
        return positiveBalance - negativeBalance;
    }

    /**
     * Combines Debts to get N-1 debts
     * for a group of N participants
     */
    private void simplifyDebts() {
        List<Pair<Participant, Double>> participantBalanceList = new ArrayList<>();
        Queue<Pair<Participant, Double>> creditors = new LinkedList<>();
        Queue<Pair<Participant, Double>> debtors = new LinkedList<>();
        for (Participant p : event.participants)
            participantBalanceList.add(new Pair<>(p, calculateBalance(p)));
        for (Pair<Participant, Double> p : participantBalanceList) {
            if (p.getBalance() > 0) // getValue()=balance
                creditors.add(p);
            else if (p.getBalance() < 0)
                debtors.add(p);
        }
        debts.clear();

        Pair<Participant, Double> creditor;
        Pair<Participant, Double> debtor;
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            creditor = creditors.peek();
            debtor = debtors.peek();
            if (Math.abs(debtor.getBalance()) < creditor.getBalance()) {
                debtors.poll();
                creditor.setBalance(creditor.getBalance() -
                        Math.abs(debtor.getBalance()));
                debts.add(new Debt(creditor.getParticipant().name,
                        debtor.getParticipant().name, Math.abs(debtor.getBalance())));
            } else if (Math.abs(debtor.getBalance()) == creditor.getBalance()) {
                debtors.poll();
                creditors.poll();
                debts.add(new Debt(creditor.getParticipant().name,
                        debtor.getParticipant().name, Math.abs(debtor.getBalance())));
            } else {
                creditors.poll();
                debtor.setBalance(debtor.getBalance() + creditor.getBalance());
                debts.add(new Debt(creditor.getParticipant().name,
                        debtor.getParticipant().name, Math.abs(creditor.getBalance())));
            }
        }
    }

    /**
     * Filters the debts lists with debts that have been paid
     */
    private void filterPayments() {
        List<Debt> debtsToRemove = new ArrayList<>();
        for (Payment payment : event.payments) {
            for (Debt debt : debts) {
                if (payment.payer.equals(debt.getDebtor())) {
                    if (payment.receiver.equals(debt.getCreditor())) {
                        debtsToRemove.add(debt);
                    }
                }
            }
        }

        for (Debt debt : debtsToRemove) {
            debts.remove(debt);
        }
    }

    /**
     * Fill the FXML Accordion with the debts that the participant still needs to pay
     *
     * @param participant the debtor whose debts to show
     * @return return the total debt sum of this participant
     */
    private double fillNodesDebtor(Participant participant) {

        currentlyShownDebtor = participant;

        String currency = preferences.getSavedCurrency();

        double sumAmount = 0;
        for (Debt debt : this.debts) {
            // Only display the debts that the current debtor owes
            if (!debt.getDebtor().equalsIgnoreCase(participant.name)) {
                continue;
            }
            sumAmount += debt.getAmount();

            TitledPane newDebt = new TitledPane();
            // Round the double value to 2 decimals (%,.2)

            newDebt.setText(String.format("%s owes %s %,.2f %s",
                    debt.getDebtor(), debt.getCreditor(),
                    debt.getAmount() * server.getConversionRate("EUR", currency, LocalDate.now())
                , currency));

            GridPane head = getHead(debt);
            //dispaly creditors details  instead of debtors ones
            GridPane details = getDetails(debt, getParticipantByName(debt.getCreditor()));

            GridPane wrapper = new GridPane();
            wrapper.add(head, 0, 0);
            wrapper.add(details, 0, 1);

            newDebt.setContent(wrapper);

            accordion.getPanes().add(newDebt);
        }

        String formatted = String.format
                ("%.2f", sumAmount * server.getConversionRate
                        ("EUR", currency, LocalDate.now()));
        sumDebts.setText(bundle.getString("youStillNeedToPay") + " " + formatted + " " + currency);

        if (sumAmount == 0) sumDebts.setText("");

        return sumAmount;
    }

    /**
     * Gets the head GridPane for a debt
     *
     * @param debt the debt to get the head of
     * @return the GridPane head
     */
    private GridPane getHead(Debt debt) {
        GridPane head = new GridPane();
        Button mailButton = new Button(bundle.getString("mail"));
        Button bankButton = new Button(bundle.getString("bank"));
        Button markReceivedButton = new Button(bundle.getString("markReceived"));

        //set a different color for the buttons if the specified information is/is not available
        if(getParticipantByName(debt.getDebtor()).email.isEmpty() ||
            getParticipantByName(debt.getCreditor()).email.isEmpty())
            mailButton.setStyle("-fx-background-color: #c9c9c9;-fx-background-radius: 10");
        else {
            mailButton.setStyle("-fx-background-color: #afeeac;-fx-background-radius: 10");
            mailButton.setOnAction(
                arg0 -> {
                    Button source = (Button) arg0.getSource();
                    source.setDisable(true);
                    String email = getParticipantByName(debt.getDebtor()).email;
                    String emailSent = bundle.getString("emailSentTo");
                    try {
                        server.sendReminder(email, getParticipantByName(debt.getCreditor()));
                        source.setText(emailSent);
                    } catch (RuntimeException e) {
                        source.setText("Failed");
                    }
                }
            );
        }
        if(getParticipantByName(debt.getCreditor()).iban.isEmpty() ||
                    getParticipantByName(debt.getCreditor()).bic.isEmpty())
            bankButton.setStyle("-fx-background-color: #c9c9c9;-fx-background-radius: 10");
        else
            bankButton.setStyle("-fx-background-color: #afeeac;-fx-background-radius: 10");

        markReceivedButton.setStyle("-fx-background-color: #afeeac;-fx-background-radius: 10");
        head.add(mailButton, 0, 0);
        head.add(bankButton, 1, 0);
        head.add(markReceivedButton, 2, 0);
        head.setHgap(10);
        head.setAlignment(Pos.CENTER);

        markReceivedButton.setOnAction(event1 -> removeDebt(debt));
        return head;
    }

    /**
     * Fill the FXML Accordion with the credits that the participant still needs to receive
     *
     * @param participant the creditor whose credits to show
     * @return double value
     */
    private double fillNodesCreditor(Participant participant) {
        double sumAmount = 0;
        String currency = preferences.getSavedCurrency();
        for (Debt debt : this.debts) {
            // Only display the debts that the current debtor owes
            if (!debt.getCreditor().equalsIgnoreCase(participant.name)) {
                continue;
            }
            sumAmount += debt.getAmount();

            TitledPane newDebt = new TitledPane();
            // Round the double value to 2 decimals (%,.2)
            newDebt.setText(String.format("%s owes %s %,.2f %s",
                      debt.getDebtor(), debt.getCreditor(),
                    debt.getAmount() * server.getConversionRate("EUR", currency, LocalDate.now())
                    , currency));

            GridPane head = getHead(debt);
            GridPane details = getDetails(debt, participant);

            GridPane wrapper = new GridPane();
            wrapper.add(head, 0, 0);
            wrapper.add(details, 0, 1);

            newDebt.setContent(wrapper);

            accordion1.getPanes().add(newDebt);
        }

        String formatted = String.format
                ("%.2f", sumAmount * server.getConversionRate
                        ("EUR", currency, LocalDate.now()));
        sumCredits.setText(bundle.getString
                ("youStillNeedToReceive") + " " + formatted + " " + currency);

        if (sumAmount == 0) sumCredits.setText("");

        return sumAmount;
    }

    /**
     * Gets the details GridPane for a debt
     * Uses the bank details from the creditor
     * and the email address of the debtor
     * @param debt the debt to get the details of
     * @return the GridPane details
     */
    private GridPane getDetails(Debt debt, Participant participant) {
        GridPane details = new GridPane();
        details.add(new Label(bundle.getString("bankInformationAvailable")), 0, 0);
        String accString = bundle.getString("accountHolder") + " %s";
        details.add(new Label(String.format(accString, debt.getCreditor())), 0, 1);
        if (participant.iban != null) {
            details.add(new Label(String.format("IBAN: %s", participant.iban)), 0, 2);
        }
        if (participant.bic != null) {
            details.add(new Label(String.format("BIC: %s", participant.bic)), 0, 3);
        }

        return details;
    }

    /**
     * Gets called when the user presses the "See Debts" button
     * Will validate the creditor-name input and show their debts if the input is valid
     */
    public void onNewDebtor() {
        String userInput = input.getText();
        input.setText("");

        showNewDebtor(userInput);
    }

    /**
     * Sets the payment dialog as visible and disables the buttons
     */
    public void showPaymentLog() {
        ObservableList<HBox> items = FXCollections.observableArrayList();

        for (Payment payment : this.event.payments) {
            Label textLabel = new Label(payment.toString());
            Region spacer = new Region();
            Button undoButton = new Button("Undo");
            HBox hbox = new HBox(textLabel, spacer, undoButton);

            HBox.setHgrow(spacer, Priority.ALWAYS);
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(10);

            undoButton.setOnAction(event -> undoPayment(payment));

            items.add(hbox);
        }
        logListView.setItems(items);

        paymentDialog.setVisible(true);
        disableButtons(true);
    }

    /**
     * Sets the payment dialog as invisible
     */
    public void closePaymentLog() {
        paymentDialog.setVisible(false);
        disableButtons(false);
    }

    /**
     * Disables other buttons while a dialog is being opened.
     *
     * @param b whether to disable buttons or not
     */
    private void disableButtons(boolean b) {
        seeDebtsButton.setDisable(b);
        backToStartScreenButton.setDisable(b);
        input.setDisable(b);
        openDebtsTitle.setDisable(b);
        paymentLogButton.setDisable(b);
    }

    /**
     * Removes the provided debt for the debt list and adds it to payments as a payment
     */
    private void removeDebt(Debt debt) {
        String currency = preferences.getSavedCurrency();
        Payment payment = new Payment(debt.getDebtor(), debt.getCreditor(),
                debt.getAmount() * server.getConversionRate
                        ("EUR", currency, LocalDate.now()), currency);

        // Add the debt to remove as a payment to the payment log
        Payment paymentToAdd = server.addPayment(payment);
        this.event.payments.add(paymentToAdd);
        server.updateEvent(event);
        setEvent(event);

        debts.remove(debt);

        for (String part : participantNamesLowercase) {
            if (part.equals(payment.payer.toLowerCase())) {
                showNewDebtor(part);
            }
        }

    }

    /**
     * Removes a payment from the database and adds it back as a debt
     *
     * @param payment the payment to remove (the one clicked with "undo")
     */
    private void undoPayment(Payment payment) {
        String currency = preferences.getSavedCurrency();
        debts.add(new Debt(payment.receiver, payment.payer,
                payment.amount * server.getConversionRate(currency, "EUR", LocalDate.now())));

        event.payments.remove(payment);
        server.removePayment(payment);
        server.updateEvent(event);
        setEvent(event);

        closePaymentLog();

        for (String part : participantNamesLowercase) {
            if (part.equals(payment.payer.toLowerCase())) {
                showNewDebtor(part);
            }
        }
    }

    private void showNewDebtor(String name) {
        if (participantNamesLowercase.contains(name.toLowerCase())) {
            errorMSG.setText("");
            accordion.getPanes().removeAll(accordion.getPanes());
            accordion1.getPanes().removeAll(accordion1.getPanes());
            int index = participantNamesLowercase.indexOf(name.toLowerCase());
            savedForCurrency = participants.get(index);
            double debt = fillNodesDebtor(participants.get(index));
            double credit = fillNodesCreditor(participants.get(index));
            showTotalSum(debt, credit);
        } else {
            errorMSG.setText(bundle.getString("debtorNotKnownToSystem"));
        }
    }

    private void showTotalSum(double debt, double credit) {

        double difference = debt - credit;
        String currency = preferences.getSavedCurrency();
        String formatted = String.format("%.2f",
                Math.abs(difference) * server.getConversionRate
                        ("EUR", currency, LocalDate.now()));
        String s = "";
        if (difference > 0) {
            s = "-";
        } else if (difference < 0) {
            s = "+";
        }

        totalSum.setText(bundle.getString("yourBalance") + " " + s + formatted + " " + currency);
        totalSum.setVisible(true);
        if (creditorsList.getText() == null ||
                creditorsList.getText().isBlank()) totalSum.setText("");
    }
    private Participant getParticipantByName(String name){
        for(Participant p : participants)
            if(p.name.equals(name))
                return p;
        return null;
    }

    /**
     * Routing, back to "home"
     */
    public void back() {
        mainCtrl.showOverview(event, this.bundle, this.isHighContrast);
    }

    /**
     * Routes back to "event" overview page.
     */
    public void backToOverview() {
        mainCtrl.showOverview(event, this.bundle, this.isHighContrast);
    }

    /**
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
                if (paymentDialog.isVisible()) {
                    showPaymentLog();
                }

                if (currentlyShownDebtor != null) {
                    showNewDebtor(currentlyShownDebtor.name);
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Actions when user presses a key in open debts page.
     * @param e the key event
     */
    public void keyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            back();
        } else if (e.getCode() == KeyCode.P && e.isControlDown()) {
            showPaymentLog();
        } else if (e.getCode() == K && e.isControlDown()) {
            showCurrenciesDialog();
            listViewCurrencies.requestFocus();
        }
    }

    public void showCurrenciesDialog() { listViewCurrencies.setVisible(true); }

    private void initializeCurrencyListView() {
        ObservableList<String> items = FXCollections.observableArrayList("EUR", "USD", "CHF");
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
                    if(getItem().equals("CHF")) {
                        currencyPicture.setImage(francFlag);
                        preferences.saveCurrency("CHF");
                        changeCurrency();
                    } else if(getItem().equals("USD")) {
                        currencyPicture.setImage(dollarFlag);
                        preferences.saveCurrency("USD");
                        changeCurrency();
                    } else if(getItem().equals("EUR")) {
                        currencyPicture.setImage(euroFlag);
                        preferences.saveCurrency("EUR");
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
                    }
                    lastClickedIndex.set(listViewCurrencies.getSelectionModel().getSelectedIndex());
                    listViewCurrencies.refresh();
                }
            } else if (e.getCode() == RIGHT || e.getCode() == TAB) {
                contrastToggle.requestFocus();
                listViewCurrencies.setVisible(false);
            } else if (e.getCode() == LEFT) {
                listViewCurrencies.setVisible(false);
                paymentLogButton.requestFocus();
                listViewCurrencies.setVisible(false);
            }
        });
    }

    /**
     * refreshes the debts and the balances so the currency changes are applied
     */
    public void changeCurrency() {
        if(savedForCurrency == null) return;
        errorMSG.setText("");
        accordion.getPanes().removeAll(accordion.getPanes());
        accordion1.getPanes().removeAll(accordion1.getPanes());
        double debt = fillNodesDebtor(savedForCurrency);
        double credit = fillNodesCreditor(savedForCurrency);
        showTotalSum(debt, credit);
    }
}
