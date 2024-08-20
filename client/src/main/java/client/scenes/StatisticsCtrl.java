package client.scenes;

import java.net.URL;
import java.util.*;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXToggleButton;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class StatisticsCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Label statisticsTitle;
    @FXML
    public Label warningSentence;
    @FXML
    public AnchorPane biggestPane;

    public ChoiceBox<Participant> whoChoiceBox;

    private Event event;
    private List<PieChart.Data> slices;
    private List<Tag> tags;
    private List<Expense> expenses;
    private ObservableList<PieChart.Data> pieCharData;
    public ResourceBundle bundle;
    public boolean isHighContrast;
    @FXML
    public Text totalCostOfEventHeader;

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Label totalCostLabel;

    @FXML
    public ImageView backArrow;
    @FXML
    public Rectangle backRectangle;
    @FXML
    public AnchorPane backToStartScreenButton;
    @FXML
    public Label eventTitle;
    @FXML
    public JFXToggleButton contrastToggle;
    @FXML
    private PieChart pieChart;

    /**
     * Constructs a new StatisticsCtrl instance with the specified ServerUtils and MainCtrl.
     *
     * @param server   the ServerUtils instance used for server communication
     * @param mainCtrl the MainCtrl instance used for controlling the main application flow
     */
    @Inject
    public StatisticsCtrl(ServerUtils server, MainCtrl mainCtrl) throws InterruptedException {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.tags = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.slices = new ArrayList<>();
        this.pieCharData = FXCollections.observableArrayList();
    }

    /**
     * Initializes the Statistics View and adds some default data to the pie-chart
     * (are later to be implemented as tags)
     *
     * @param url            The location used to resolve relative paths for the root object, or
     *                       {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or {@code null} if
     *                       the root object was not localized.
     */
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (event != null) {
            eventTitle.setText(this.event.name);
        }
        eventTitle.setStyle("-fx-font-family: 'Open Sans Bold';-fx-font-size: 30");

        EventOverviewCtrl.configureBackButton(backToStartScreenButton, backArrow, backRectangle);
        whoChoiceBox.setConverter(new StringConverter<Participant>() {
            @Override
            public String toString(Participant item) {
                if (item == null) {
                    return "Everyone";
                } else {
                    return item.name;
                }
            }

            @Override
            public Participant fromString(String string) {
                return event.participants
                        .stream()
                        .filter(x -> x.name.equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        whoChoiceBox.setOnAction(event -> {
            refresh(false);
        });

        contrastToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
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
        eventTitle.setTextFill(Color.WHITE);
        statisticsTitle.setTextFill(Color.WHITE);
        totalCostOfEventHeader.setFill(Color.WHITE);
        totalCostLabel.setStyle("-fx-background-color: #75e9fc; -fx-background-radius: 15");
        contrastToggle.setTextFill(Color.WHITE);
        contrastToggle.setStyle("background-color: black");
    }

    private void resetColors() {
        biggestPane.setStyle("-fx-background-color: white");
        eventTitle.setTextFill(Color.BLACK);
        statisticsTitle.setTextFill(Color.BLACK);
        totalCostOfEventHeader.setFill(Color.BLACK);
        totalCostLabel.setStyle("-fx-background-color:  #fff3de; -fx-background-radius: 15");
        contrastToggle.setTextFill(Color.BLACK);
        contrastToggle.setStyle("background-color: white");
    }

    /**
     * Refreshes the statistics page
     */
    void refresh(boolean refreshParticipantChoice) {
        changeLanguage(this.bundle.getLocale().getLanguage());
        eventTitle.setText(event.name);
        this.tags = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.slices = new ArrayList<>();

        configurePieChart();

        var selectedParticipant = whoChoiceBox.getValue();

        // Sets the total cost text
        double totalCost = event.expenses.stream()
                .filter(x ->
                    selectedParticipant == null || x.debtors.contains(selectedParticipant)
                )
                .mapToDouble(expense -> expense.amount /
                        (selectedParticipant == null ? 1 : expense.debtors.size()))
                .sum();
        totalCostLabel.setText(String.format("%.2f", totalCost));

        if (refreshParticipantChoice) {
            whoChoiceBox.getItems().clear();
            whoChoiceBox.getItems().setAll(event.participants);
        }
    }

    private void configurePieChart() {
        // Fill the distinct tags and create empty data slices
        for (Expense expense : event.expenses) {
            if (!tags.contains(expense.type)) {
                tags.add(expense.type);
                slices.add(new PieChart.Data(expense.type.getName(), 0));
            }
        }

        var selectedParticipant = whoChoiceBox.getValue();

        // Fill the slices with the appropriate sizes
        for (Tag tag : tags) {
            for (Expense expense : event.expenses) {
                if (selectedParticipant != null &&
                        !expense.debtors.contains(selectedParticipant)) {
                    continue;
                }

                if (expense.type.equals(tag)) {
                    slices.get(tags.indexOf(tag))
                            .setPieValue(
                                    slices.get(tags.indexOf(tag)).getPieValue()
                                            + expense.amount);
                }
            }
        }

        pieCharData = FXCollections.observableArrayList(slices);
        pieChart.setData(pieCharData);
        pieChart.requestLayout();
        pieChart.applyCss();

        setPieChartColors();
    }

    private void setPieChartColors() {
        for (PieChart.Data data : pieCharData) {
            String colorClass = "";
            for (String cls : data.getNode().getStyleClass()) {
                if (cls.startsWith("default-color")) {
                    colorClass = cls;
                    break;
                }
            }

            var optionalTag = tags
                    .stream()
                    .filter(x -> x
                            .getName()
                            .equals(data
                                    .getName()))
                    .findFirst();

            if (optionalTag.isEmpty())
                continue;

            var tag = optionalTag.get();

            for (Node n : pieChart.lookupAll("." + colorClass)) {
                n.setStyle("-fx-pie-color: " + tag.getColor());
            }
        }
    }



    /**
     * Goes back to the event overview
     */
    public void back() {
        // Clear the pie chart
        pieCharData = FXCollections.observableArrayList();
        pieChart.setData(pieCharData);
        mainCtrl.showOverview(event, this.bundle, this.isHighContrast);
    }

    /**
     * Change language of the view.
     *
     * @param languageCode the language to change to.
     */
    public void changeLanguage(String languageCode) {
        bundle = ResourceBundle.getBundle("languages.messages", Locale.of(languageCode));
        statisticsTitle.setText(bundle.getString("statisticsTitle"));
        totalCostOfEventHeader.setText(bundle.getString("totalCostOfEvent"));
        contrastToggle.setText(bundle.getString("highContrast"));
        warningSentence.setText(bundle.getString("noExpensesToViewStatistics"));
    }

    /**
     * Actions when user presses a key in Statistics Page
     * @param e the key event
     */
    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
            back();
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
                refresh(true);
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
