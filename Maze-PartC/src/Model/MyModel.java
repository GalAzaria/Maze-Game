package Model;

import Client.Client;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;


import java.net.URL;
import java.nio.file.Files;
import java.util.Random;
import java.util.ResourceBundle;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.scene.input.KeyCode;
import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import Server.IServerStrategy;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Path;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyModel extends Observable implements IModel{

    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private Maze maze;
    private Position currPos;
    private Solution sol;

    public MyModel(){
        threadPool = Executors.newFixedThreadPool(5);

    }
    public Maze getObjMaze(){
        return maze;
    }

    public void setChar(int x, int y){
        Position curr = new Position(x,y);
        this.currPos = curr;
    }

    @Override
    public void generateMaze(int width, int height) {

            CommunicateWithServer_MazeGenerating(width,height);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers();

    }
    public static void Connect(IServerStrategy server){
        if(server instanceof ServerStrategyGenerateMaze) {
            Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
            mazeGeneratingServer.start();
        }
        else if(server instanceof ServerStrategySolveSearchProblem){
            Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
            solveSearchProblemServer.start();
        }
    }

    private void CommunicateWithServer_MazeGenerating(int row,int col) {
        IServerStrategy server =new ServerStrategyGenerateMaze();
        Connect(server);


        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {


                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[col*row +12]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        currPos = maze.getStartPosition();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveCharacter(KeyEvent keyEvent) {
            int characterRowCurrentPosition = currPos.getRowIndex();
            int characterColumnCurrentPosition = currPos.getColumnIndex();
            int characterRowNewPosition = characterRowCurrentPosition;
            int characterColumnNewPosition = characterColumnCurrentPosition;
            int [][]mazeData = maze.getMaze();


            if (keyEvent.getCode() == KeyCode.W) {
                if(0 <= characterRowCurrentPosition - 1) {

                    if (mazeData[characterRowCurrentPosition - 1][characterColumnCurrentPosition] != 1) {
                        characterRowNewPosition = characterRowCurrentPosition - 1;
                        characterColumnNewPosition = characterColumnCurrentPosition;
                    }
                }
            }
            else if (keyEvent.getCode() == KeyCode.X) {
                if(mazeData.length >= characterRowCurrentPosition+1) {

                    if (mazeData[characterRowCurrentPosition + 1][characterColumnCurrentPosition] != 1) {
                        characterRowNewPosition = characterRowCurrentPosition + 1;
                        characterColumnNewPosition = characterColumnCurrentPosition;
                    }
                }
            }
            else if (keyEvent.getCode() == KeyCode.D) {
                if(mazeData[0].length >characterColumnCurrentPosition+1 ) {

                    if (mazeData[characterRowCurrentPosition][characterColumnCurrentPosition + 1] != 1) {
                        characterRowNewPosition = characterRowCurrentPosition;
                        characterColumnNewPosition = characterColumnCurrentPosition + 1;
                    }
                }
            }
            else if (keyEvent.getCode() == KeyCode.A) {
                if(0 <= characterColumnCurrentPosition-1 ) {

                    if (mazeData[characterRowCurrentPosition][characterColumnCurrentPosition - 1] != 1) {

                        characterRowNewPosition = characterRowCurrentPosition;
                        characterColumnNewPosition = characterColumnCurrentPosition - 1;
                    }
                }
            }
            else if (keyEvent.getCode() == KeyCode.HOME){

                characterRowNewPosition = 0;
                characterColumnNewPosition = 0;
            }
            else if (keyEvent.getCode()== KeyCode.Z){
                if(mazeData.length >= characterRowCurrentPosition +1 && 0 <= characterColumnCurrentPosition-1 ) {

                    if (mazeData[characterRowCurrentPosition +1][characterColumnCurrentPosition - 1] != 1) {
                        characterRowNewPosition = characterRowCurrentPosition + 1;
                        characterColumnNewPosition = characterColumnCurrentPosition - 1;
                    }
                }
            }
            else if (keyEvent.getCode()== KeyCode.C){
                if(mazeData.length >= characterRowCurrentPosition +1 && mazeData[0].length >=characterColumnCurrentPosition +1 ) {

                    if (mazeData[characterRowCurrentPosition +1][characterColumnCurrentPosition + 1] != 1) {
                        characterRowNewPosition = characterRowCurrentPosition + 1;
                        characterColumnNewPosition = characterColumnCurrentPosition + 1;
                    }
                }
            }
            else if (keyEvent.getCode()== KeyCode.E){
                if(0 <= characterRowCurrentPosition -1 && mazeData[0].length >=characterColumnCurrentPosition +1 ) {

                    if (mazeData[characterRowCurrentPosition -1][characterColumnCurrentPosition + 1] != 1) {
                        characterRowNewPosition = characterRowCurrentPosition - 1;
                        characterColumnNewPosition = characterColumnCurrentPosition + 1;
                    }
                }
            }
            else if (keyEvent.getCode()== KeyCode.Q){
                if( 0 <= characterRowCurrentPosition -1 && 0 <characterColumnCurrentPosition-1 ) {

                    if (mazeData[characterRowCurrentPosition-1][characterColumnCurrentPosition - 1] != 1) {
                        characterRowNewPosition = characterRowCurrentPosition - 1;
                        characterColumnNewPosition = characterColumnCurrentPosition - 1;
                    }
                }
            }
            //Updates the MazeDisplayer

            //Updates the labels
            Position newpos =new Position(characterRowNewPosition,characterColumnNewPosition);
            currPos = newpos;
            setChanged();
            notifyObservers();
            keyEvent.consume();

    }

    @Override
    public int[][] getMaze() {
        return maze.getMaze();
    }

    @Override
    public int getCharacterPositionRow() {
        return currPos.getRowIndex();
    }

    @Override
    public int getCharacterPositionColumn() {
        return currPos.getColumnIndex();
    }

    private void CommunicateWithServer_SolveSearchProblem() {
        IServerStrategy server =new ServerStrategySolveSearchProblem();
        Connect(server);

        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        EmptyMazeGenerator mg = new EmptyMazeGenerator();


                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        sol = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public Solution GetSolution(){

            CommunicateWithServer_SolveSearchProblem();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers();
           // if(sol == null)

        return sol;
    }

    public void LoadMaze(File lFile){
        if(lFile != null) {
            try {

                byte[] finalload = Files.readAllBytes(lFile.toPath());
                byte[] mazebytes = new byte[finalload.length - 2];
                for (int i = 0; i < mazebytes.length; i++) {
                    mazebytes[i] = finalload[i];
                }
                Maze Lmaze = new Maze(mazebytes);
                this.maze = Lmaze;
                int xPos = (int)finalload[mazebytes.length];
                int yPos = (int)finalload[mazebytes.length+1];
                Position newPos = new Position(xPos,yPos);
                this.currPos = newPos;

            } catch (IOException e) {
            }
        }
        setChanged();
        notifyObservers();
    }

    public void SaveMaze(File sFile){
        int loc = 0;
        byte [] mazebyte = maze.toByteArray();
        byte [] finalSave = new byte[maze.toByteArray().length + 2];
        for(int i=0;i<mazebyte.length;i++){
            finalSave[i]=mazebyte[i];
            loc = i;
        }
        finalSave[loc+1]= (byte)currPos.getRowIndex();
        finalSave[loc+2]= (byte)currPos.getColumnIndex();

        try{
            sFile.createNewFile();
            FileOutputStream file = new FileOutputStream(sFile);
            file.write(finalSave);
        }
        catch (IOException e){}

    }
}
