package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Mockito.verify;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class AdminViewCtrlTest extends ApplicationTest {

    @Mock
    private AdminViewCtrl sut;

    @Mock
    private ServerUtils server;

    @Mock
    private MainCtrl main;

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

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/AdminView.fxml"));
        loader.setControllerFactory(param -> new AdminViewCtrl(server, main));
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

    @Test
    void cancelTest1() {
        clickOn("#adminView", MouseButton.PRIMARY);
        verify(main).showStartScreen(ResourceBundle.getBundle("languages.messages", Locale.of("en_US")), false);
    }

    @Test
    void cancelTest2() {
        clickOn("#backToStartScreenButton", MouseButton.PRIMARY);
        verify(main).showStartScreen(ResourceBundle.getBundle("languages.messages", Locale.of("en_US")), false);
    }

}