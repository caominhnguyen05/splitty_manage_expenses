package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})

public class StartScreenCtrlTest extends ApplicationTest {


//    @Mock
//    private StartScreenCtrl startScreenCtrl;
//
//    @Mock
//    private ServerUtils server;
//
//    @Mock
//    private MainCtrl main;
//
//
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/StartScreen.fxml"));
//        loader.setControllerFactory(param -> new StartScreenCtrl(server, main));
//        Parent root = loader.load();
//        startScreenCtrl = loader.getController();
//        Scene scene = new Scene(root, 800, 600);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    @Override
//    public void stop() throws Exception {
//        FxToolkit.hideStage();
//    }
//
//    @BeforeEach
//    public void doEnglish() {
//        clickOn("#currentFlag");
//        clickOn("English");
//    }
//
//    @BeforeAll
//    public static void setupSpec() throws Exception {
//        System.setProperty("testfx.robot", "glass");
//        System.setProperty("testfx.headless", "true");
//        System.setProperty("glass.platform", "Monocle");
//        System.setProperty("monocle.platform", "Headless");
//        System.setProperty("prism.order", "sw");
//        System.setProperty("prism.text", "t2k");
//        System.setProperty("java.awt.headless", "true");
//    }
//
//
//    @Test
//    public void testCreateEventButtonEmptyInput() {
//
//        clickOn("#createEventButton", MouseButton.PRIMARY);
//
//        WaitForAsyncUtils.waitForFxEvents();
//
//        try {
//            Node okButton = lookup("OK").query();
//            Node popup = lookup("Empty field.").query();
//            assertNotNull(okButton);
//            assertNotNull(popup);
//            clickOn("OK");
//        } catch (Exception e) {
//            fail("Alert dialog did not show up");
//        }
//
//    }
//
//    @Test
//    public void testCreateEventButtonValidInput() {
//
//        Event mockEvent = new Event("New Event");
//        when(server.addEvent(any(Event.class))).thenReturn(mockEvent);
//
//        clickOn("#createEventBox");
//        write("New Event");
//
//        clickOn("#createEventButton", MouseButton.PRIMARY);
//
//        WaitForAsyncUtils.waitForFxEvents();
//
//        try {
//            Node okButton = lookup("OK").query();
//            Node popup = lookup("Event created successfully!").query();
//            assertNotNull(okButton);
//            assertNotNull(popup);
//            clickOn("OK");
//        } catch (Exception e) {
//            fail("Alert dialog did not show up");
//        }
//
//    }
//
//    @Test
//    public void testJoinEventButtonEmptyInput() {
//
//        clickOn("#joinEventButton", MouseButton.PRIMARY);
//
//        WaitForAsyncUtils.waitForFxEvents();
//
//        try {
//            Node okButton = lookup("OK").query();
//            Node popup = lookup("Empty field.").query();
//            assertNotNull(okButton);
//            assertNotNull(popup);
//            clickOn("OK");
//        } catch (Exception e) {
//            fail("Alert dialog did not show up");
//        }
//    }
//
//    @Test
//    public void testJoinEventButtonInvalidInput() {
//
//        clickOn("#joinEventBox");
//        write("Invalid code");
//
//        clickOn("#joinEventButton", MouseButton.PRIMARY);
//
//        WaitForAsyncUtils.waitForFxEvents();
//
//        try {
//            Node okButton = lookup("OK").query();
//            Node popup = lookup("The code is invalid.").query();
//            assertNotNull(okButton);
//            assertNotNull(popup);
//            clickOn("OK");
//        } catch (Exception e) {
//            fail("Alert dialog did not show up");
//        }
//
//    }
//
//    @Test
//    public void testLanguageSelection() {
//
//        clickOn("#currentFlag");
//        clickOn("Dutch");
//
//        Label label = lookup("#mainTitleStartScreen").queryAs(Label.class);
//        String actualValue = label.getText();
//
//      //  assertEquals("Splitty: Beheer groepsuitgaven", actualValue);
//
//        clickOn("#currentFlag");
//        clickOn("Deutsch");
//
//        label = lookup("#mainTitleStartScreen").queryAs(Label.class);
//        actualValue = label.getText();
//
////        assertEquals("Splitty: Gruppenausgaben verwalten", actualValue);
//
//        clickOn("#currentFlag");
//        clickOn("English");
//
//        label = lookup("#mainTitleStartScreen").queryAs(Label.class);
//        actualValue = label.getText();
//
//        //assertEquals("Splitty: Manage Group expenses", actualValue);
//
//    }
//
//    @Test
//    public void testClickOnEvent() {
//
//        Event mockEvent = new Event("Test Event 1");
//        when(server.addEvent(any(Event.class))).thenReturn(mockEvent);
//        when(server.getEvent(anyLong())).thenReturn(mockEvent);
//
//        clickOn("#createEventBox");
//        write("Test Event 1");
//        clickOn("#createEventButton", MouseButton.PRIMARY);
//        clickOn("OK");
//
//        try {
//            clickOn("Test Event 1");
//        } catch (Exception e) {
//            fail("Can't find or click on Event!");
//        }
//
//    }
//
//    @Test
//    public void testDeleteEvent() {
//
//        Event mockEvent = new Event("Test Delete Event 1");
//        when(server.addEvent(any(Event.class))).thenReturn(mockEvent);
//
//        clickOn("#createEventBox");
//        write("Test Delete Event 1");
//        clickOn("#createEventButton", MouseButton.PRIMARY);
//        clickOn("OK");
//        clickOn("x");
//
//        try {
//            Node okButton = lookup("OK").query();
//            Node popup = lookup("Do you want to delete this event?").query();
//            assertNotNull(okButton);
//            assertNotNull(popup);
//            clickOn("OK");
//        } catch (Exception e) {
//            fail("Alert dialog did not show up");
//        }
//    }

}
