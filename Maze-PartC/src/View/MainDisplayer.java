package View;


import ViewModel.ViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.xml.bind.annotation.XmlElementDecl;


public class MainDisplayer {

    private javafx.scene.text.Text title;
    private VBox menuBox;
    private int currentItem=0;
    private MainView play;

    public  void showMenu(Pane root, Scene scene){
        title = new Text("Maze");
        title.setFont(Font.font("Press Start 2P", FontWeight.BOLD, 52));
        title.setFill(Color.WHITE);
        title.setTranslateY(700/-5);

        root.getChildren().add(title);

        root.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double num = (double)newValue;
                title.setTranslateY(num/-5);

            }
        });
        play = MainView.getInstance();

        MenuItem itemExit = new MenuItem("EXIT",false);
        MenuItem startGame = new MenuItem("START GAME",true);
       MenuItem  help = new MenuItem("HELP",false);

        itemExit.setOnActivate(() -> System.exit(0));
        startGame.setOnActivate(() -> play.StartPlay());
        help.setOnActivate(()->play.HelpMenu());


        menuBox = new VBox(10,startGame,help,itemExit);
        menuBox.setAlignment(Pos.CENTER);
        root.getChildren().add(menuBox);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                if (currentItem > 0) {
                    getMenuItem(currentItem).setActive(false);
                    getMenuItem(--currentItem).setActive(true);
                }
            }

            if (event.getCode() == KeyCode.DOWN) {
                if (currentItem < menuBox.getChildren().size() - 1) {
                    getMenuItem(currentItem).setActive(false);
                    getMenuItem(++currentItem).setActive(true);
                }
            }

            if (event.getCode() == KeyCode.ENTER) {
                getMenuItem(currentItem).activate();
            }
        });

    }


    private static class MenuItem extends HBox {

        private Text text;
        private Runnable script;

        public MenuItem(String name,boolean b) {
            super(15);
            setAlignment(Pos.CENTER);

            text = new Text(name);
            text.setFont(Font.font("Press Start 2P", FontWeight.BOLD, 20));

            getChildren().addAll(text);
            setActive(b);

        }

        public void setActive(boolean b) {
            text.setFill(b ? Color.ORANGERED : Color.WHITE);
        }

        public void activate() {
            if (script != null)
                script.run();
        }

        public void setOnActivate(Runnable r) {
            script = r;
        }
    }
    private MenuItem getMenuItem(int index) {
        return (MenuItem)menuBox.getChildren().get(index);
    }

}
