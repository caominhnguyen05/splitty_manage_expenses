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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.InvitationViewCtrl;

import client.scenes.StartScreenCtrl;

import com.google.inject.Injector;

import client.scenes.MainCtrl;

import client.scenes.OpenDebtsCtrl;
import client.scenes.AddEditExpenseCtrl;
import client.scenes.EventOverviewCtrl;

import javafx.application.Application;
import javafx.stage.Stage;

import client.scenes.*;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     * The entry point for the JavaFX application.
     *
     * @param args command-line arguments (not used in this method)
     * @throws URISyntaxException if a URI syntax error occurs
     * @throws IOException        if an I/O error occurs
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     * Initializes and starts the JavaFX application by loading various FXML files
     * and initializing the main controller with the necessary components.
     *
     * @param primaryStage the primary stage for the JavaFX application
     * @throws IOException if an I/O error occurs while loading FXML files
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        var invitation = FXML.load(InvitationViewCtrl.class,
                "client", "scenes", "InvitationView.fxml");

        var startScreen = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");

        var debts = FXML.load(OpenDebtsCtrl.class, "client", "scenes", "OpenDebts.fxml");
        var addEdit = FXML.load(AddEditExpenseCtrl.class,
                "client", "scenes", "AddEditExpenseView.fxml");
        var overviewScreen = FXML.load(EventOverviewCtrl.class,
                "client", "scenes", "EventOverview.fxml");
        var addParticipants = FXML.load(AddEditParticipantsCtrl.class,
                "client", "scenes", "AddEditParticipants.fxml");
        var adminLogin = FXML.load(AdminLoginCtrl.class,
                "client", "scenes", "AdminLogin.fxml");
        var adminView = FXML.load(AdminViewCtrl.class,
                "client", "scenes", "AdminView.fxml");
        var statistics = FXML.load(StatisticsCtrl.class,
                "client", "scenes", "Statistics.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        mainCtrl.initialize(primaryStage, invitation, debts,
                addEdit, overviewScreen, startScreen, addParticipants, statistics,
                adminLogin, adminView);

        primaryStage.setOnCloseRequest(e -> startScreen.getKey().stop());
    }
}