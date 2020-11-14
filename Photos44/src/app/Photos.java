package app;

/**
 * Main class that runs the application
 * @author Jason Chou and Ty Goldin
 * Group#44
 *
 */


import javafx.application.Application;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Photos extends Application {

    /**
     * Launches application
     * @param args String array of arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Loads the necessary FXML files
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Photos.class.getResource("/view/loginPage.fxml"));

        BorderPane mainBorderPane = (BorderPane)loader.load();

        loginPageController listController = loader.getController();
        listController.start();

        primaryStage.setScene(new Scene(mainBorderPane));
        primaryStage.setTitle("Photo Library");
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
            	
                Platform.exit();
                System.exit(0);
            }
        });
    }

}
