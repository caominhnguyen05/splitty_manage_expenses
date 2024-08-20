package client.scenes;

import client.data_structures.Debt;
import client.data_structures.Deleteable;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Payment;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class OpenDebtsCtrlTest extends ApplicationTest {
//    @Mock
//    private OpenDebtsCtrl openDebtsCtrl;
//
//    @Mock
//    private ServerUtils server;
//
//    @Mock
//    private MainCtrl main;
//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/OpenDebts.fxml"));
//        loader.setControllerFactory(param -> new OpenDebtsCtrl(server, main));
//        Parent root = loader.load();
//        openDebtsCtrl = loader.getController();
//        Scene scene = new Scene(root, 800, 600);
//        stage.setScene(scene);
//        stage.show();
//    }
//    @Override
//    public void stop() throws Exception {
//        FxToolkit.hideStage();
//    }
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
//    @BeforeEach
//    public void setup(){
//        Event event = new Event("Party");
//
//        Participant p1 = new Participant("p1", "1234","p1@email.com","123ABC");
//        Participant p2 = new Participant("p2", "12345","p2@email.com","123ABC");
//        Participant p3 = new Participant("p3", "123456","p3@email.com","123ABC");
//        p1.id=1;
//        p2.id=2;
//        p3.id=3;
//        event.participants.addAll(Arrays.asList(p1,p2,p3));
//
//        Expense e1 = new Expense("e1", 20, "EUR", null, LocalDate.now());
//        Expense e2 = new Expense("e2", 40, "EUR", null, LocalDate.now());
//        Expense e3 = new Expense("e3", 45, "EUR", null, LocalDate.now());
//        e1.id=1;
//        e2.id=2;
//        e3.id=3;
//        event.expenses.addAll(Arrays.asList(e1,e2,e3));
//
//        e1.creditor = p1;
//        e1.debtors.add(p2);
//        e2.creditor = p2;
//        e2.debtors.addAll(Arrays.asList(p1,p3));
//        e3.creditor = p1;
//        e3.debtors.addAll(Arrays.asList(p1,p2,p3));
//
//        openDebtsCtrl.setEvent(event);
//
//        openDebtsCtrl.bundle = ResourceBundle.getBundle("languages.messages", Locale.of("en_US"));
//    }
//    @Test
//    public void calculateBalanceTest(){
//        assertEquals(30, openDebtsCtrl.caluculateBalance(openDebtsCtrl.event.participants.get(0)));
//        assertEquals(5, openDebtsCtrl.caluculateBalance(openDebtsCtrl.event.participants.get(1)));
//        assertEquals(-35, openDebtsCtrl.caluculateBalance(openDebtsCtrl.event.participants.get(2)));
//    }
//    @Test
//    public void calculateDebtsTest(){
//        openDebtsCtrl.getDebts();
//        assertEquals(2, openDebtsCtrl.debts.size());
//        if(openDebtsCtrl.debts.get(0).getCreditor().equals("p1")) {
//            assertEquals("p3", openDebtsCtrl.debts.get(0).getDebtor());
//            assertEquals(30, openDebtsCtrl.debts.get(0).getAmount());
//
//            assertEquals("p2", openDebtsCtrl.debts.get(1).getCreditor());
//            assertEquals(5, openDebtsCtrl.debts.get(1).getAmount());
//            assertEquals("p3", openDebtsCtrl.debts.get(1).getDebtor());
//        }else{
//            assertEquals("p2", openDebtsCtrl.debts.get(0).getCreditor());
//            assertEquals(5, openDebtsCtrl.debts.get(0).getAmount());
//            assertEquals("p3", openDebtsCtrl.debts.get(0).getDebtor());
//
//            assertEquals("p3", openDebtsCtrl.debts.get(1).getDebtor());
//            assertEquals(30, openDebtsCtrl.debts.get(1).getAmount());
//            assertEquals("p1",openDebtsCtrl.debts.get(1).getCreditor());
//        }
//    }
//    @Test
//    public void refreshTest(){
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                openDebtsCtrl.refresh();
//
//                Label participants = lookup("#creditorsList").query();
//                assertEquals("p1, p2, p3", participants.textProperty().get());
//
//            }
//        });
//    }
//    @Test
//    public void deletableTest(){
//        openDebtsCtrl.getDebts();
//        for(Debt debt : openDebtsCtrl.debts)
//            if(debt.getCreditor().equals("p1"))
//                debt.setAmount(0);
//        Deleteable deleteable = openDebtsCtrl.getDeleteable();
//        for(Participant p : deleteable.getParticipantList()) {
//            if (p.name.equals("p1"))
//                assertTrue(deleteable.getDeleteOrNotBooleans().get(
//                    deleteable.getParticipantList().indexOf(p)
//                ));
//            else
//                assertFalse(deleteable.getDeleteOrNotBooleans().get(
//                    deleteable.getParticipantList().indexOf(p)
//                ));
//        }
//    }
//    @Test
//    public void showDebtorBalanceTest(){
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                openDebtsCtrl.refresh();
//                openDebtsCtrl.getDebts();
//
//                clickOn("#input", MouseButton.PRIMARY);
//                write("p3");
//
//                WaitForAsyncUtils.waitForFxEvents();
//                openDebtsCtrl.onNewDebtor(new ActionEvent());
//
//                Label totalSum = lookup("#totalSum").query();
//                Label error = lookup("#errorMSG").query();
//
//                assertTrue(error.textProperty().get().isEmpty());
//                assertEquals("Your Balance: -35.00 Euros", totalSum.textProperty().get());
//            }
//        });
//    }
//    @Test
//    public void invalidParticipantTest(){
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                openDebtsCtrl.refresh();
//                openDebtsCtrl.getDebts();
//
//                clickOn("#input", MouseButton.PRIMARY);
//                write("p4");
//
//                WaitForAsyncUtils.waitForFxEvents();
//                openDebtsCtrl.onNewDebtor(new ActionEvent());
//
//                Label error = lookup("#errorMSG").query();
//
//                assertEquals("This debtor is not known in the system.",error.textProperty().get());
//            }
//        });
//    }
//    @Test
//    public void showCreditorBalanceTest(){
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                openDebtsCtrl.refresh();
//                openDebtsCtrl.getDebts();
//
//                clickOn("#input", MouseButton.PRIMARY);
//                write("p2");
//
//                WaitForAsyncUtils.waitForFxEvents();
//                openDebtsCtrl.onNewDebtor(new ActionEvent());
//
//                Label totalSum = lookup("#totalSum").query();
//                Label error = lookup("#errorMSG").query();
//
//                assertTrue(error.textProperty().get().isEmpty());
//                assertEquals("Your Balance: +5.00 Euros", totalSum.textProperty().get());
//            }
//        });
//    }
//    @Test
//    public void paymentLogTest(){
//        Payment payment = new Payment("p3","p2",5.0,"EUR");
//        openDebtsCtrl.event.payments.add(payment);
//
//        openDebtsCtrl.showPaymentLog();
//
//        Node node = lookup("#logListView").query();
//        assertNotNull(node);
//    }


}
