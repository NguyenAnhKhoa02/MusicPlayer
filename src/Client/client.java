package Client;

import Business.Java.controllerSceneHome;
import Business.Java.manageFile;
import Data.musicFile;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class client extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private void setImage(Stage stage) {
        Image image = new Image("file:../MusicPlayer/src/Icon/iconApp.png");
        stage.getIcons().add(image);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setMaximized(true);

        String css = this.getClass().getResource("sceneHome.css")
                .toExternalForm();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sceneHome.fxml"));
        Parent root = loader.load();
        controllerSceneHome con = loader.getController();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css);
        setImage(stage);

        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent arg0) {
                con.setCloseButton();
            }
        });
    }

}
