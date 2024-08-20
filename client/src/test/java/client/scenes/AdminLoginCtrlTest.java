package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class AdminLoginCtrlTest extends ApplicationTest {

    @Mock
    private AdminLoginCtrl sut;

    @Mock
    private ServerUtils server;

    @Mock
    private MainCtrl main;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/AdminLogin.fxml"));
        loader.setControllerFactory(param -> new AdminLoginCtrl(server, main));
        Parent root = loader.load();
        sut = loader.getController();
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
        sut.bundle = ResourceBundle.getBundle("languages.messages");
        sut.refresh();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    @BeforeEach
    public void setup() {
        sut.bundle = ResourceBundle.getBundle("languages.messages", Locale.of("en_US"));
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

    @Test
    public void submitWrongPasswordTest() {
        clickOn("#input");
        write("1");

        clickOn("#submitButton", MouseButton.PRIMARY);

        WaitForAsyncUtils.waitForFxEvents();

        Node wrongPass = lookup("*Wrong password").query();

        assertNotNull(wrongPass);
    }

//    @Test
//    public void otherLanguageTest() {
//        sut.bundle = ResourceBundle.getBundle("languages.messages", Locale.of("de_DE"));
//
//        clickOn("#input");
//        write("1");
//
//        clickOn("#submitButton", MouseButton.PRIMARY);
//
//        WaitForAsyncUtils.waitForFxEvents();
//
//        Node wrongPass = lookup("*Falsches Passwort").query();
//
//        assertNotNull(wrongPass);
//    }

    @Test
    void cancelTest() {
        clickOn("#cancelButton");
        verify(main).showStartScreen(ResourceBundle.getBundle("languages.messages", Locale.of("en_US")), false);
    }

}