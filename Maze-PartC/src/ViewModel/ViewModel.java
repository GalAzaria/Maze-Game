package ViewModel;

import Model.IModel;
import Model.MyModel;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javax.swing.text.Position;
import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.ResourceBundle;

public class ViewModel extends Observable implements Observer {

    @FXML

    private IModel model;

    public ViewModel(IModel m){
        this.model = m ;
    }


    public void generateMaze(int r,int c) {

        model.generateMaze(r,c);
    }
    public Maze getMaze(){
        return model.getObjMaze();
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public Solution solveMaze() {
        return model.GetSolution();
    }

    public void moveCharacter(KeyEvent keyEvent) {
        model.moveCharacter(keyEvent);
    }



    public int getCharacterPositionRow(){
        return model.getCharacterPositionRow();
    }
    public int getCharacterPositionColumn(){
        return model.getCharacterPositionColumn();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == model){
            setChanged();
            notifyObservers();
        }
    }

    public void viewSaveMaze(File save){
        model.SaveMaze(save);

    }
    public void viewLoadMaze(File load){
        model.LoadMaze(load);
    }

    //endregion
}
