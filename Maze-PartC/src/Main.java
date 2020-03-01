import Model.MyModel;
import View.MainDisplayer;
import View.MyViewController;
import ViewModel.ViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import View.MainView;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import javax.swing.text.View;
import java.awt.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.Executors;


//
//    @Override
//    public void start(Stage primaryStage) throws Exception{
//      Pane root = new StackPane();
//      primaryStage.setTitle("Hello World");
//      Scene scene = new Scene(root, 800, 700);
//      scene.getStylesheets().add("View/MainScreen.CSS");
//      MainDisplayer displayer = new MainDisplayer();
//      displayer.showMenu(root,scene,primaryStage);
//
//      primaryStage.show();
//      primaryStage.setScene(scene);
//
//
//
//
//    }
public class Main extends Application {
    static Media media;
    static MediaPlayer mediaPlayer;
    @Override

    public void start(Stage primaryStage) throws Exception {
        MainView play = MainView.getInstance();
        final URL resource = getClass().getResource("./ACTION.mp3");
        media = new Media(resource.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        play.init(primaryStage);
        play.StartMainScreen();
        mediaPlayer.play();




    }



    public static void main(String[] args) {

        launch(args);
    }
}


