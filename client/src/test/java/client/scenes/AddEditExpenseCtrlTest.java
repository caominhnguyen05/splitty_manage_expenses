package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.util.WaitForAsyncUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.lenient;


@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class AddEditExpenseCtrlTest extends org.testfx.framework.junit5.ApplicationTest {
    @Mock
    private AddEditExpenseCtrl controller;
    @Mock
    private ServerUtils server;
    @Mock
    private MainCtrl main;

    /*
    tests: submit button disables when:
        what for is empty
        how much is empty
        split equally and nobody selected

    Also test:
    choose new tag type (e.g. entrance fee --> food)
    click on agenda --> agenda window opens
    cancel --> back to event overview
    submit --> confirm dialog opens
     */

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("START!!");

        //Get the FXML file:
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/AddEditExpenseView.fxml"));
        //Link the controller to the FXML file
        loader.setControllerFactory(param -> new AddEditExpenseCtrl(server, main));

        Parent root = loader.load();

        System.out.println("Root Node: " + root);
        System.out.println("Controller: " + loader.getController());

        controller = loader.getController();
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();

        //Create an Event which will be used for testing
        // 3 Participants, 1 Expense
        Event e = new Event("test event");
        Participant p1 = new Participant("a", "", "", "");
        Participant p2 = new Participant("b", "", "", "");
        Participant p3 = new Participant("c", "", "", "");
        p1.id = 1;
        p2.id = 2;
        p3.id = 3;

        Tag entranceTag = new Tag("Entrance Fees", "#FFF");
        entranceTag.id = 1;
        Tag foodTag = new Tag("Food", "#FFF");
        foodTag.id = 2;
        Tag travelTag = new Tag("Travel", "#FFF");
        travelTag.id = 3;
        e.tags = List.of(entranceTag, foodTag, travelTag);

        Expense ex = new Expense("Bowling", 100, "EUR", foodTag, LocalDate.now());
        ex.id = 1;
        ex.creditor = p1;
        ex.debtors.add(p1);
        ex.debtors.add(p2);
        e.participants.add(p3);
        e.participants.add(p2);
        e.participants.add(p1);
        e.expenses.add(ex);

        // Mocking server behaviour:
        //This mocks the adding to server, and just returns the same tag as it was passed
        lenient().when(server.addTag(Mockito.any(Tag.class))).thenAnswer(i -> {
            System.out.println("ADD TAG AT SERVER");
            return i.getArguments()[0];
        });
        //This mocks the updating of an event in the database, and just returns the same tag as it was passed
        lenient().when(server.updateEvent(Mockito.any(Event.class))).thenAnswer(i -> {
            System.out.println("UPDATE EVENT AT SERVER");
            return i.getArguments()[0];
        });
        //This mocks the getting of a tag with a certain ID
        lenient().when(server.getTag(Mockito.anyLong())).thenAnswer(i -> {
            System.out.println("GET TAG BY ID AT SERVER");
            Tag t =  new Tag("TEST TAG", "#FFF");
            t.id = (long) i.getArguments()[0];
            return t;
        });

        //Link the event and its first expense to the Controller class:
//        controller.setEvent(e);
//        controller.setExpense(e.expenses.get(0));
//        controller.refresh();
    }

    //Before starting the tests, do necessary setup
    @BeforeAll
    public static void setupSpec() throws Exception {
        System.out.println("SETUPSYNC!!");

        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");

        WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);
    }

    /*
    Testing Tags.
        Tag items have been given id's:
            deleteButton.idProperty().set("deleteTag_" + tag.id);
            textField.idProperty().set("testFieldTag_" + tag.id);
            colorPicker.idProperty().set("colorPickerTag_" + tag.id);

        "Edit Tags" button: #editTagsButton

        To test:
        tag dialog pops up, with 3 default tags, and
            an empty one, cannot delete defaults or change name, can change color though
            dialog actually closes when click on close or minimize
            should now also be able to select that tag for an expense
            that tag should contain the same name as inputted in the tags dialog
     */

    @Test
    void openTagsDialogTest() {
//        System.out.println("DIALOGTEST!!");

//        Button button = lookup("#editTagsButton").query();
//        MFXGenericDialog dialog = lookup("#editTagsDialog").query();
//
//        clickOn(button);
//
//        // Check if the dialog is visible
//        verifyThat(dialog, isVisible());
    }
}