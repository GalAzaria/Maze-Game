package View;


import Server.Configurations;
import ViewModel.ViewModel;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.Solution;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;



import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.Key;
import java.util.*;

public class MyViewController implements IView, Observer {
    public javafx.scene.control.Button Generate;

    @FXML

    //protected Button Generate;
    private ViewModel viewModel;
    public TextField ld_rowsNum;
    public TextField ld_colsNum;

    public ComboBox<String> choiceBox1;
    private Stage stage;
    //button sol;
    private boolean mazegenerated = false;
    public MazeDisplayer mazeDisplayer;
    public Boolean Sec = false;
    public Boolean first = true;



    public void KeyPressed(KeyEvent key) {
        viewModel.moveCharacter(key);
        key.consume();
    }


    public void setViewModel(ViewModel v) {
        this.viewModel = v;
    }

    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            mazeDisplayer.setCharacterPosition(viewModel.getCharacterPositionRow(), viewModel.getCharacterPositionColumn());
            Generate.setDisable(false);
            this.zoom(mazeDisplayer);
        }
    }

    public void setStage(Stage pri) {
        this.stage = pri;
    }


    public void setResizeEvent(Scene scene) {

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                mazeDisplayer.setWidth((scene.getWidth() * 0.75));
                mazeDisplayer.DrawMaze();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                mazeDisplayer.setHeight((scene.getHeight() * 0.85));
                mazeDisplayer.DrawMaze();
            }
        });
    }

    public void ClickedLoadMaze() {
        if (mazegenerated) {
            FileChooser lFile = new FileChooser();
            lFile.setInitialDirectory(new File(System.getProperty("user.home")));
            lFile.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze ", "*.MGmaze"));
            File load = lFile.showOpenDialog(stage);

            if (load != null && load.canRead()) {
                viewModel.viewLoadMaze(load);
                mazeDisplayer.SetLoadMaze(viewModel.getMaze());
                mazeDisplayer.DrawPos(viewModel.getCharacterPositionRow(), viewModel.getCharacterPositionColumn());
            }


        }
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }


    public void ClickedSaveMaze() {
        if (mazegenerated) {
            FileChooser sFile = new FileChooser();
            sFile.setInitialDirectory(new File(System.getProperty("user.home")));
            sFile.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MGmaze", "*.MGmaze"));
            sFile.setInitialFileName("");
            viewModel.viewSaveMaze(sFile.showSaveDialog(stage));
        } else {
            showAlert("You Have To Generate a New Maze Before.");
        }
    }


    public void ClickedGenerateMaze() {
        try {

            int row = Integer.parseInt(ld_rowsNum.getText());
            int col = Integer.parseInt(ld_colsNum.getText());
            Generate.setDisable(true);
            if(row<1||col<1){
                Generate.setDisable(false);
                showAlert("You Have To Insert a Number Grater Then 0.");
            }
            else {
                mazegenerated = true;
                viewModel.generateMaze(row, col);
                mazeDisplayer.SetMaze(viewModel.getMaze());
                Sec = true;
            }

        } catch (NumberFormatException e) {
            Generate.setDisable(false);
            showAlert("You Have To Insert a Number.");
        }
    }

    public void ClickedSolveMaze() {
        if (mazegenerated) {
            Solution sol = viewModel.solveMaze();
            mazeDisplayer.DrawSol(sol);
        } else {
            showAlert("You Have To Generate a New Maze Before.");
        }


    }


    public void About(javafx.event.ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 400, 75);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
        }
    }


    public void SetProperties() {
        try {
            String fileAddress = "./resources/config.properties";
            FileInputStream input = new FileInputStream(fileAddress);
            Properties properties = new Properties();
            properties.load(input);
            Label A = new Label("Number Of threads :"+   properties.getProperty("poolSize"));
            Label C = new Label("Solve methood :"+  properties.getProperty("solverMethod"));

            Label E = new Label("Generate methood :"+  properties.getProperty("GeneratorMethod"));


            Stage stage = new Stage();
            AnchorPane root = new AnchorPane();// fxmlLoader.load(getClass().getResource("properties.fxml").openStream());
            Scene scene = new Scene(root, 200, 50);
            stage.setScene(scene);
            VBox v = new VBox();
            //Label text = new Label("Chose search algo");
            //  v.getChildren().add(text);
            v.setAlignment(Pos.CENTER);
            v.getChildren().addAll(A,C,E);
            //     apply.setOnAction(e -> getChoice(choiceBox1)getChoice(choiceBox1));
            root.getChildren().add(v);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.setResizable(false);
            stage.show();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }




    public void zoom(MazeDisplayer pane) {
        pane.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        if (event.isControlDown()) {
                            double zoomFactor = 1.05;
                            double deltaY = event.getDeltaY();

                            if (deltaY < 0) {
                                zoomFactor = 0.95;
                            }
                            pane.setScaleX(pane.getScaleX() * zoomFactor);
                            pane.setScaleY(pane.getScaleY() * zoomFactor);
                            event.consume();
                        }
                    }
                });
    }

    public void newGame() {

       MainView mainView= MainView.getInstance();
       mainView.StartPlay();

    }

    public void HelpMenue() {

        MainView mainView= MainView.getInstance();
        mainView.HelpMenu();

    }

    public void Exit() {

       // MainView mainView= MainView.getInstance();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
          stage.close();
        } else {
            // ... user chose CANCEL or closed the dialog

        }

    }
}