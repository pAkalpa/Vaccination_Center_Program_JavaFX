package Receipt_Generator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainUI.fxml"));
        root.getStylesheets().add(getClass().getResource("/Receipt_Generator/styles/mainUIstyle.css").toString()); // Style Sheet
        primaryStage.setTitle("Covid-19 Vaccination Center Receipt Generator"); // window title
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/titleLogo.png"))); // Window icon
        primaryStage.setResizable(false); // Make Window resize disable
        primaryStage.setAlwaysOnTop(true); // Make Window top
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
