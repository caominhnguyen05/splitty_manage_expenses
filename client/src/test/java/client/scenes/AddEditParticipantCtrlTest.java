package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import static org.mockito.Mockito.verify;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class AddEditParticipantCtrlTest extends ApplicationTest {
    @Mock
    private AddEditParticipantsCtrl sut;

    @Mock
    private ServerUtils server;

    @Mock
    private MainCtrl main;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/AddEditParticipants.fxml"));
        loader.setControllerFactory(param -> new AddEditParticipantsCtrl(server, main));
        Parent root = loader.load();
        sut = loader.getController();
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
        sut.setEvent(new Event());
        sut.bundle = ResourceBundle.getBundle("languages.messages");
        sut.refresh();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    @BeforeAll
    public static void setupSpec() throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
    }

    @BeforeEach
    public void doEnglish() {
//        sut.bundle = ResourceBundle.getBundle("languages.messages",
//                Locale.of("en_US"));
    }

    @Test
    public void addValidFields() {
        clickOn("#nameField");
        write("Jack");

        clickOn("#ibanField");
        write("NL91ABNA0417164300");

        clickOn("#emailField");
        write("m@gmail.com");

        clickOn("#bicField");
        write("BARCGB22XXX");
        clickOn("#okButton", MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();

        try {
            Node popup = lookup("Data Save Confirmation").query();
            clickOn("#closeDialogButton1", MouseButton.PRIMARY);
            assertNotNull(popup);
        } catch (Exception e) {
            fail("Confirm dialog did not show up.");
        }
    }

    @Test
    void addOnlyValidName() {
        clickOn("#nameField");
        write("Jack");
        clickOn("#okButton");
        Node pop = lookup("Data Save Confirmation").query();
        assertNotNull(pop);
    }

    @Test
    public void addInvalidData() {
        clickOn("#nameField");
        write("44");

        clickOn("#ibanField");
        write("afb");

        clickOn("#bicField");
        write("m");

        clickOn("#emailField");
        write("m");
        clickOn("#okButton", MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();

        try {
            Node popup = lookup("Invalid data entered!").query();
            assertNotNull(popup);
        } catch (Exception e) {
            fail("Alert dialog did not show up.");
        }
    }

    @Test
    public void validNameEmailWithBankEmpty() {
        clickOn("#nameField");
        write("a");
        clickOn("#emailField");
        write("a@gmail.com");

        clickOn("#okButton", MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();

        try {
            Node popup = lookup("Data Save Confirmation").query();
            assertNotNull(popup);
        } catch (Exception e) {
            fail("Confirm dialog did not show up.");
        }
    }

    @Test
    public void validNameBankEmailEmpty() {
        clickOn("#nameField");
        write("Jack");
        clickOn("#ibanField");
        write("NL91ABNA0417164300");
        clickOn("#bicField");
        write("BARCGB22XXX");
        clickOn("#okButton", MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();

        try {
            Node popup = lookup("Data Save Confirmation").query();
            assertNotNull(popup);
        } catch (Exception e) {
            fail("Confirm dialog did not show up.");
        }
    }

    @Test
    public void emailEmptyWrongDetails() {
        clickOn("#nameField");
        write("1");
        clickOn("#ibanField");
        write("bb");
        clickOn("#bicField");
        write("bb");
        clickOn("#okButton", MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();

        try {
            Node popup = lookup("Invalid data entered!").query();
            assertNotNull(popup);
        } catch (Exception e) {
            fail("Alert dialog did not show up.");
        }
    }

    @Test
    public void wrongEmailName() {
        clickOn("#nameField");
        write("1");
        clickOn("#emailField");
        write("bb");
        clickOn("#okButton", MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();

        try {
            Node popup = lookup("Invalid data entered!").query();
            assertNotNull(popup);
        } catch (Exception e) {
            fail("Alert dialog did not show up.");
        }
    }

    @Test
    public void updateParticipant() {
        sut.thisParticipant = new Participant("Jack", "", "BARCGB22XXX", "j@gmail.com");
        sut.refresh();
        clickOn("#nameField");
        write("Jack");

        clickOn("#ibanField");
        write("NL91ABNA0417164300");

        clickOn("#emailField");
        write("m@gmail.com");

        clickOn("#bicField");
        write("BARCGB22XXX");
        clickOn("#okButton", MouseButton.PRIMARY);
        WaitForAsyncUtils.waitForFxEvents();

        try {
            Node popup = lookup("Data Save Confirmation").query();
            clickOn("#closeDialogButton1", MouseButton.PRIMARY);
            assertNotNull(popup);
        } catch (Exception e) {
            fail("Confirm dialog did not show up.");
        }
    }

    @Test
    public void editParticipantNoBank() {
        sut.thisParticipant = new Participant("Jack", null, null, "j@gmail.com");
        sut.refresh();
        clickOn("#nameField");
        write("Jack");
        clickOn("#okButton", MouseButton.PRIMARY);
        Node pop = lookup("Data Save Confirmation").query();
        assertNotNull(pop);
    }

    @Test
    public void validateNameAndEmailIfBankEmptyForUpdate() {
        sut.thisParticipant = new Participant("Jack", "", "", "j@gmail.com");
        sut.refresh();
        clickOn("#nameField");
        write("Jack");

        clickOn("#emailField");
        write("m@gmail.com");
        clickOn("#okButton", MouseButton.PRIMARY);
        Node pop = lookup("Data Save Confirmation").query();
        assertNotNull(pop);
    }

    @Test
    void validateForEdit1() {
        sut.thisParticipant = new Participant("Jack", null, null, "j@gmail.com");
        clickOn("#nameField");
        write("1");
        clickOn("#okButton");
        Node pop = lookup("Invalid data entered!").query();
        assertNotNull(pop);
    }

    @Test
    void validNameAndEmailInUpdating() {
        sut.thisParticipant = new Participant("Jack", "", "", "j@gmail.com");
        clickOn("#nameField");
        write("Jack");
        clickOn("#emailField");
        write("m@gmail.com");
        Node pop = lookup("Data Save Confirmation").query();
        assertNotNull(pop);
    }

    @Test
    void emailEmptyWrongOthersInEditing() {
        sut.thisParticipant = new Participant("Jack", "", "", "j@gmail.com");
        clickOn("#nameField");
        write("1");
        clickOn("#ibanField");
        write("bb");
        clickOn("#bicField");
        write("bb");
        clickOn("#okButton", MouseButton.PRIMARY);
        Node pop = lookup("Invalid data entered!").query();
        assertNotNull(pop);
    }

    @Test
    void onlyValidNameInEditing() {
        sut.thisParticipant = new Participant("Ja", null, null, "");
        clickOn("#nameField");
        write("Jack");
        clickOn("#okButton", MouseButton.PRIMARY);
    }

    @Test
    void emailEmptyOtherValidInEditing() {
        sut.thisParticipant = new Participant("Jack", "", "", "");
        sut.refresh();
        clickOn("#nameField");
        write("Jack");
        clickOn("#emailField");
        write("");
        clickOn("#ibanField");
        write("NL91ABNA0417164300");
        clickOn("#bicField");
        write("BARCGB22XXX");
        clickOn("#okButton", MouseButton.PRIMARY);
        Node pop = lookup("Data Save Confirmation").query();
        assertNotNull(pop);
    }

//    @Test
//    void goBack() {
//        sut.abort();
//        verify(main).showOverview(sut.getEvent(), ResourceBundle.getBundle("languages.messages",
//                Locale.of("en_US")));
//    }

    @Test
    void addOnlyInvalidName() {
        clickOn("#nameField");
        write("1");
        clickOn("#okButton", MouseButton.PRIMARY);
        Node pop = lookup("Invalid data entered!").query();
        assertNotNull(pop);
    }
}
