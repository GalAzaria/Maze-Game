package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.TextField;
import javax.xml.soap.Text;
//import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Aviadjo on 3/9/2017.
 */
public class MazeDisplayer extends Canvas {

    //region Properties
    private int[][] maze;
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;
    private int characterPositionRow = 0;
    private int characterPositionColumn = 0;

    public MazeDisplayer(){}

    public void SetMaze(Maze Meizu){
        this.maze = Meizu.getMaze();
        Position startPos = Meizu.getStartPosition();
        this.startRow = startPos.getRowIndex();
        this.startCol = startPos.getColumnIndex();

        Position endPos = Meizu.getGoalPosition();
        this.endRow = endPos.getRowIndex();
        this.endCol = endPos.getColumnIndex();

        DrawMaze();
        DrawPos(startRow,startCol);
    }
    public void SetLoadMaze(Maze m){
        this.maze = m.getMaze();
        Position startPos = m.getStartPosition();
        this.startRow = startPos.getRowIndex();
        this.startCol = startPos.getColumnIndex();

        Position endPos = m.getGoalPosition();
        this.endRow = endPos.getRowIndex();
        this.endCol = endPos.getColumnIndex();

        DrawMaze();

    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }


    public void setCharacterPosition(int row, int column) {
        DrawPos(row,column);
        characterPositionRow = row;
        characterPositionColumn = column;
    }

    public void DrawMaze() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;
            GraphicsContext GC2d = getGraphicsContext2D();
            GC2d.setFill(Color.ORANGE);
            GC2d.clearRect(0,0,getWidth(),getHeight());
            for(int i = 0; i<maze.length;i++){
                for(int j=0 ; j<maze[0].length;j++){
                    if(maze[i][j]==1) {
                        GC2d.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                    }
                }
            }
            GC2d.setFill(Color.GREEN);
            GC2d.fillRect(characterPositionColumn * cellWidth , characterPositionRow*cellHeight,cellWidth,cellHeight);
            GC2d.setFill(Color.RED);
            GC2d.fillRect(endCol * cellWidth , endRow*cellHeight,cellWidth,cellHeight);


        }
    }
    public void DeletePos(){
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / maze.length;
        double cellWidth = canvasWidth / maze[0].length;
        GraphicsContext GC2d = getGraphicsContext2D();
        GC2d.clearRect((this.characterPositionColumn) * cellWidth , (this.characterPositionRow)*cellHeight,cellWidth,cellHeight);

    }


    public void DrawPos(int r,int c) {
        if (maze != null) {
            DeletePos();
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;
            GraphicsContext GC2d = getGraphicsContext2D();
            GC2d.setFill(Color.GREEN);
            GC2d.fillRect(c * cellWidth , r*cellHeight,cellWidth,cellHeight);
            if(r==endRow&&c==endCol) {
                try {
                    Stage stage = new Stage();
                    stage.setTitle("About");
                    Button win = new Button();
                    win.setMinWidth(100);
                    win.setText("MAIN MENU");
                    Label message = new Label("!!! YOU WIN !!! ");
                    message.setAlignment(Pos.TOP_CENTER);
                    message.setFont(Font.font("Press Start 2P", FontWeight.BOLD, 20));
                    message.setTextFill(Color.ORANGERED);
                    VBox root = new VBox() ;//fxmlLoader.load(getClass().getResource("View/About.fxml").openStream());
                    win.setOnAction((event) -> {
                        MainView play = MainView.getInstance();
                        try {
                            play.StartMainScreen();
                        }
                        catch (Exception e){};
                        stage.close();
                    });
                    root.getChildren().addAll(message,win);
                    root.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(root, 400, 100);
                    scene.getStylesheets().add("View/WinWindow.CSS");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                    stage.setResizable(false);
                    stage.setOnCloseRequest(evt -> {
                        evt.consume();
                    });
                    stage.show();
                }
                catch (Exception e) {
                }
            }
            }
        }

    public void DrawSol(Solution sol){
        ArrayList<AState> solpath = sol.getSolutionPath();
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / maze.length;
        double cellWidth = canvasWidth / maze[0].length;
        GraphicsContext GC2d = getGraphicsContext2D();
        GC2d.setFill(Color.RED);
        for(int i = 0; i<solpath.size(); i ++){
            MazeState tmp = (MazeState)solpath.get(i);
            Position tmpPos = tmp.getCurrPosition();

            GC2d.fillRect(tmpPos.getColumnIndex() * cellWidth , (tmpPos.getRowIndex())*cellHeight,cellWidth,cellHeight);

        }


    }

}
