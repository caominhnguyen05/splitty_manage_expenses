package client.scenes;

import client.data_structures.Deleteable;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.hibernate.sql.Delete;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})

public class EventOverviewScreenCtrlTest extends ApplicationTest {


//    @Mock
//    private EventOverviewCtrl eventOverviewCtrl;
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
//
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/EventOverview.fxml"));
//        loader.setControllerFactory(param -> {
//            try {
//                return new EventOverviewCtrl(server, main);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        Parent root = loader.load();
//        eventOverviewCtrl = loader.getController();
//        Scene scene = new Scene(root, 800, 600);
//        stage.setScene(scene);
//        stage.show();
//
//        Event e = new Event("test");
//
//        Participant p1 = new Participant("a", "", "", "");
//        Participant p2 = new Participant("b", "", "", "");
//        Participant p3 = new Participant("c", "", "", "");
//        p1.id = 1;
//        p2.id = 2;
//        p3.id = 3;
//        Expense ex = new Expense("Bowling", 123, "EUR", new Tag("food", "#FFFFFF"), LocalDate.now());
//        ex.id = 1;
//        ex.creditor = p1;
//        ex.debtors.add(p1);
//        ex.debtors.add(p2);
//        e.participants.add(p3);
//        e.participants.add(p2);
//        e.participants.add(p1);
//        e.expenses.add(ex);
//
//        eventOverviewCtrl.setEvent(e);
//
//        Deleteable d = new Deleteable(
//                new ArrayList<>(e.participants)
//        );
//        d.setDelete(p3.name, true);
//
//        eventOverviewCtrl.setDeleteable(d);
//        eventOverviewCtrl.refresh();
//    }
//
//    @Override
//    public void stop() throws Exception {
//        FxToolkit.hideStage();
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
//    @Test
//    public void testEmptyEventRefresh() {
//        eventOverviewCtrl.setEvent(new Event("Empty"));
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                eventOverviewCtrl.refresh();
//            }
//        });
//    }
//
//
//    @Test
//    public void testCreateEventButtonEmptyInput() {
//        clickOn("#eventTitle", MouseButton.PRIMARY);
//        clickOn("#eventNameTextField1", MouseButton.PRIMARY);
//        write("name");
//        clickOn("#modifyEventNameButton1", MouseButton.PRIMARY);
//
//        Label titleLabel = lookup("#eventTitle").query();
//
//        assertEquals("testname", titleLabel.textProperty().get());
//    }
//
//    @Test
//    public void testIncludingExpenseList() {
//        clickOn("#includingTab", MouseButton.PRIMARY);
//
//        Node titleLabel = lookup("#expenseIncludingPerson").query();
//
//        assertNotNull(titleLabel);
//    }
//
//    @Test
//    public void testFromExpenseList() {
//        clickOn("#fromTab", MouseButton.PRIMARY);
//
//        Node titleLabel = lookup("#expenseFromPerson").query();
//
//        assertNotNull(titleLabel);
//    }
//
//    @Test
//    public void testParticipantExpenseChangeWithRefresh() {
//        clickOn("#participantsChoiceBox", MouseButton.PRIMARY);
//
//        clickOn("a", MouseButton.PRIMARY);
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                eventOverviewCtrl.refresh();
//            }
//        });
//
//        Label tab = lookup("#fromTab").query();
//
//        assertNotEquals("from a", tab.textProperty().get());
//    }
//
//    @Test
//    public void testAllExpenseList() {
//        clickOn("#allTab", MouseButton.PRIMARY);
//
//        Node expenseHistory = lookup("#expenseHistory").query();
//
//        assertNotNull(expenseHistory);
//    }
//
//    @Test
//    public void testParticipantDelete() {
//
//
//            clickOn("#editParticipantIcon", MouseButton.PRIMARY);
//
//            clickOn("#deleteParticipant_3", MouseButton.PRIMARY);
//
//            Label listOfParticipantsText = lookup("#listOfParticipantsText").query();
//
//            assertFalse(listOfParticipantsText.textProperty().get().contains("c"));
//
//    }
//
//    @Test
//    public void testLanguageChange() {
//        clickOn("#currentFlag");
//
//        WaitForAsyncUtils.waitForFxEvents();
//
//        clickOn("Dutch");
//
//        WaitForAsyncUtils.waitForFxEvents();
//
//        Label participantsHeader = lookup("#participantsHeader").query();
//
////        assertEquals("Deelnemers", participantsHeader.textProperty().get());
//    }
//
//    @Test
//    public void testExpenseDelete() {
//        clickOn("#deleteExpense_1", MouseButton.PRIMARY);
//        clickOn("OK", MouseButton.PRIMARY);
//
//        ListView<Expense> expenseHistory = lookup("#expenseHistory").query();
//
//        assertTrue(expenseHistory.getItems().isEmpty());
//    }
}
