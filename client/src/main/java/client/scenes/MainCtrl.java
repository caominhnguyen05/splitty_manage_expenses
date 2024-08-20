/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.data_structures.Deleteable;
import commons.Event;
import commons.Participant;
import commons.Expense;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ResourceBundle;

public class MainCtrl {
    private Stage primaryStage;
    private OpenDebtsCtrl openDebtsCtrl;
    private Scene invitation;
    private InvitationViewCtrl invitationCtrl;
    private Scene adminLogin;
    private AdminLoginCtrl adminLoginCtrl;
    private Scene adminView;
    private AdminViewCtrl adminViewCtrl;
    private Scene debts;
    private EventOverviewCtrl overviewScreenCtrl;
    private Scene overviewScreen;

    private StatisticsCtrl statisticsCtrl;
    private Scene statisticsScreen;
    private AddEditExpenseCtrl addEditExpenseCtrl;
    private Scene addEdit;
    private StartScreenCtrl startScreenCtrl;
    public Scene startScreen;

    private AddEditParticipantsCtrl addEditParticipants;
    private Scene addOrEdit;

    private ResourceBundle bundle;

    /**
     * Initialize all the views and control pages.
     *
     * @param primaryStage   the primary stage
     * @param invitation     add invitation page
     * @param debts          open debts page
     * @param addEdit        add/edit expense page
     * @param overviewScreen overview screen page
     * @param startScreen    start screen page
     * @param addOrEdit      add/edit participant page
     * @param statistics     statistics page
     * @param adminLogin     admin login page
     * @param adminView      admin overview page
     */
    public void initialize(Stage primaryStage,
                           Pair<InvitationViewCtrl, Parent> invitation,
                           Pair<OpenDebtsCtrl, Parent> debts,
                           Pair<AddEditExpenseCtrl, Parent> addEdit,
                           Pair<EventOverviewCtrl, Parent> overviewScreen,
                           Pair<StartScreenCtrl, Parent> startScreen,
                           Pair<AddEditParticipantsCtrl, Parent> addOrEdit,
                           Pair<StatisticsCtrl, Parent> statistics,
                           Pair<AdminLoginCtrl, Parent> adminLogin,
                           Pair<AdminViewCtrl, Parent> adminView) {

        this.primaryStage = primaryStage;

        this.startScreenCtrl = startScreen.getKey();
        this.startScreen = new Scene(startScreen.getValue());

        // Create debts screen view scene
        this.openDebtsCtrl = debts.getKey();
        this.debts = new Scene(debts.getValue());

        // Create statistics screen view scene
        this.statisticsCtrl = statistics.getKey();
        this.statisticsScreen = new Scene(statistics.getValue());

        // Create invitation screen view scene
        this.invitationCtrl = invitation.getKey();
        this.invitation = new Scene(invitation.getValue());

        // Create overview screen view scene.
        this.overviewScreenCtrl = overviewScreen.getKey();
        this.overviewScreen = new Scene(overviewScreen.getValue());

        // Create add/edit view scene.
        this.addEditExpenseCtrl = addEdit.getKey();
        this.addEdit = new Scene(addEdit.getValue());

        // Create add/edit view scene.
        this.addEditParticipants = addOrEdit.getKey();
        this.addOrEdit = new Scene(addOrEdit.getValue());

        // Create admin-login scene.
        this.adminLoginCtrl = adminLogin.getKey();
        this.adminLogin = new Scene(adminLogin.getValue());

        // Create admin-view scene.
        this.adminViewCtrl = adminView.getKey();
        this.adminView = new Scene(adminView.getValue());

        bundle = ResourceBundle.getBundle("languages.messages");

        showStartScreen(this.bundle, false);
        primaryStage.show();
    }

    /**
     * Shows start screen window.
     *
     * @param bundle The ResourceBundle containing the language to be applied.
     * @param isHighContrast Whether the view should be in high contrast mode.
     */
    public void showStartScreen(ResourceBundle bundle, boolean isHighContrast) {
        startScreenCtrl.bundle = bundle;
        startScreenCtrl.isHighContrast = isHighContrast;
        startScreenCtrl.refresh();
        primaryStage.setTitle("Splitty: Home");
        primaryStage.setScene(startScreen);
        startScreen.setOnKeyPressed(e -> startScreenCtrl.keyPressed(e));
    }

    /**
     * Shows open debts window.
     *
     * @param event  event that the open debts window should refer to.
     * @param bundle The ResourceBundle containing the language to be
     *               applied to the open debts screen.
     * @param isHighContrast Whether the view should be in high contrast mode.
     */
    public void showOpenDebts(Event event, ResourceBundle bundle, boolean isHighContrast) {
        openDebtsCtrl.setEvent(event);
        openDebtsCtrl.bundle = bundle;
        openDebtsCtrl.isHighContrast = isHighContrast;
        openDebtsCtrl.refresh();
        primaryStage.setTitle("Splitty: Open Debts");
        primaryStage.setScene(debts);
        debts.setOnKeyPressed(e -> openDebtsCtrl.keyPressed(e));
    }

    /**
     * Shows statistics window.
     *
     * @param event event that the open debts window should refer to.
     * @param bundle bundle which contains the language to set the page to.
     * @param isHighContrast Whether the view should be in high contrast mode.
     */
    public void showStatistics(Event event, ResourceBundle bundle, boolean isHighContrast) {
        statisticsCtrl.setEvent(event);
        statisticsCtrl.bundle = bundle;
        statisticsCtrl.isHighContrast = isHighContrast;
        statisticsCtrl.refresh(true);
        primaryStage.setTitle("Splitty: Statistics");
        primaryStage.setScene(statisticsScreen);
        statisticsScreen.setOnKeyPressed(e -> statisticsCtrl.keyPressed(e));
    }

    /**
     * Refreshes the debts, this method is needed when the debts should be refreshed also
     * when you are not in the open debts view.
     * @param event the event the use
     * @param bundle The ResourceBundle containing the language to set to.
     * @param isHighContrast Whether the view should be in high contrast mode.
     */
    public void refreshDebts(Event event, ResourceBundle bundle, boolean isHighContrast) {
        openDebtsCtrl.setEvent(event);
        openDebtsCtrl.bundle = bundle;
        openDebtsCtrl.isHighContrast = isHighContrast;
        openDebtsCtrl.refresh();
        openDebtsCtrl.savedForCurrency = null;
    }

    /**
     * Shows overview screen window.
     *
     * @param event  event that the overview window should refer to.
     * @param bundle The ResourceBundle containing the language to be
     *               applied to the overview screen.
     * @param isHighContrast Whether the view should be in high contrast mode.
     */
    public void showOverview(Event event, ResourceBundle bundle, boolean isHighContrast) {
        openDebtsCtrl.setEvent(event);
        Deleteable del = openDebtsCtrl.getDeleteable();
        overviewScreenCtrl.setDeletable(del);

        overviewScreenCtrl.setEvent(event);
        overviewScreenCtrl.bundle = bundle;
        overviewScreenCtrl.isHighContrast = isHighContrast;
        overviewScreenCtrl.refresh();
        overviewScreenCtrl.addExpenseButton.setDisable(event.participants.isEmpty());
        overviewScreenCtrl.addExpenseImageGroup.setDisable(event.participants.isEmpty());
        primaryStage.setTitle("Splitty: Overview");
        primaryStage.setScene(overviewScreen);
        overviewScreen.setOnKeyPressed(e -> overviewScreenCtrl.keyPressed(e));
    }

    /**
     * Show add/edit expense overview
     *
     * @param event             The event with the expense to add/edit
     * @param expenseToBeEdited the expense to be edited.
     * @param bundle            The ResourceBundle containing the language to be
     *                          applied to the Add/Edit expense view.
     * @param isHighContrast Whether the view should be in high contrast mode.
     */
    public void showAddEdit(Event event, Expense expenseToBeEdited,
                            ResourceBundle bundle, boolean isHighContrast) {
        addEditExpenseCtrl.setEvent(event);
        addEditExpenseCtrl.expense = expenseToBeEdited;
        addEditExpenseCtrl.bundle = bundle;
        addEditExpenseCtrl.isHighContrast = isHighContrast;
        addEditExpenseCtrl.refresh();
        primaryStage.setTitle("Splitty: Modify Expense");
        primaryStage.setScene(addEdit);
        addEdit.setOnKeyPressed(e -> addEditExpenseCtrl.keyPressed(e));
    }

    /**
     * Shows add/edit participants window
     *
     * @param event       event that the add/edit participants window should refer to.
     * @param participant the participant to edit (if not null)
     * @param bundle      The ResourceBundle containing the language to be
     *                    applied to the add/edit participant view.
     * @param isHighContrast Whether the view should be in high contrast mode.
     */
    public void showAddEditParticipants(Event event, Participant participant,
                                        ResourceBundle bundle, boolean isHighContrast) {
        addEditParticipants.setEvent(event);
        addEditParticipants.thisParticipant = participant;
        addEditParticipants.bundle = bundle;
        addEditParticipants.isHighContrast = isHighContrast;
        addEditParticipants.refresh();
        primaryStage.setTitle("Splitty: Modify Participants");
        primaryStage.setScene(addOrEdit);
        addOrEdit.setOnKeyPressed(e -> addEditParticipants.keyPressed(e));
    }

    /**
     * Shows invitation participants window
     *
     * @param event  event that the invite page should use to invite people
     * @param bundle The ResourceBundle containing the language to be
     *               applied to the invitation view.
     * @param isHighContrast Whether the view should be in high contrast mode.
     */
    public void showInvite(Event event, ResourceBundle bundle, boolean isHighContrast) {
        invitationCtrl.setEvent(event);
        invitationCtrl.bundle = bundle;
        invitationCtrl.isHighContrast = isHighContrast;
        invitationCtrl.refresh();
        primaryStage.setTitle("Splitty: Invitation");
        primaryStage.setScene(invitation);
        invitation.setOnKeyPressed(e -> invitationCtrl.keyPressed(e));
    }

    /**
     * Shows the admin login window.
     * @param bundle The ResourceBundle containing the language to set the view to.
     */
    public void showAdminLogin(ResourceBundle bundle) {
        adminLoginCtrl.bundle = bundle;
        adminLoginCtrl.refresh();
        primaryStage.setTitle("Splitty: Admin Login");
        primaryStage.setScene(adminLogin);
        adminLogin.setOnKeyPressed(e -> adminLoginCtrl.keyPressed(e));
    }

    /**
     * Shows the admin-view window.
     * @param bundle The ResourceBundle containing the language to set the view to.
     */
    public void showAdminView(ResourceBundle bundle) {
        adminViewCtrl.bundle = bundle;
        primaryStage.setTitle("Splitty: Admin View");
        adminViewCtrl.refresh();
        primaryStage.setScene(adminView);
        adminView.setOnKeyPressed(e -> adminViewCtrl.keyPressed(e));
    }
}

