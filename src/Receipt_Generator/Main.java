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
        root.getStylesheets().add(getClass().getResource("/Receipt_Generator/styles/mainUIstyle.css").toString());
        primaryStage.setTitle("Covid-19 Vaccination Center Receipt Generator");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/titleLogo.png")));
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
