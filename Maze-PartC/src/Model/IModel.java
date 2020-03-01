package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;

public interface IModel {
        void generateMaze(int width, int height);
        void moveCharacter(KeyEvent movement);
        int[][] getMaze();
        Maze getObjMaze();
        Solution GetSolution();
        int getCharacterPositionRow();
        int getCharacterPositionColumn();
        void LoadMaze(File lFile);
        void SaveMaze(File sFile);
        void setChar(int x, int y);


        }
