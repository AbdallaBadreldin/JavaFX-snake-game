/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakebouns;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Bossm
 */
public class SnakeBouns extends Application {

    private final int UP = 1;
    private final int DOWN = 2;
    private final int RIGHT = 3;
    private final int LEFT = 4;
    private int nextDirection = UP;
    private int direction = UP;

    private int headX = 5;
    private int headY = 5;

    private int tailX = 5;
    private int tailY = 5;

    private int foodX;
    private int foodY;

    private int snakeLengthAdder = 6;

    private List<Integer> snakeTailDirections = new Vector<Integer>();
    private List<ImageView> snakeBodyImages = new Vector<>();

    private FileInputStream imageStream;

    private Image snakeBody;
    private Image faceDown;
    private Image faceLeft;
    private Image faceRight;
    private Image faceUp;

    private Image tailDown;
    private Image tailLeft;
    private Image tailRight;
    private Image tailUp;

    private Image foodImage;
    private ImageView food;
    private GridPane root;
    private final int numColsAndRows = 16;

    private int keyBoardTimeOut = 0;
    private int gameSpeed = 350;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int score = 0;

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            imageStream = new FileInputStream("./src/snake/snakeBody.jpg");
            snakeBody = new Image(imageStream);

            imageStream = new FileInputStream("./src/snake/snakeFace_down.jpg");
            faceDown = new Image(imageStream);

            imageStream = new FileInputStream("./src/snake/snakeFace_left.jpg");
            faceLeft = new Image(imageStream);

            imageStream = new FileInputStream("./src/snake/snakeFace_right.jpg");
            faceRight = new Image(imageStream);

            imageStream = new FileInputStream("./src/snake/snakeFace_up.jpg");
            faceUp = new Image(imageStream);

            imageStream = new FileInputStream("./src/snake/snakeTail_down.jpg");
            tailDown = new Image(imageStream);

            imageStream = new FileInputStream("./src/snake/snakeTail_left.jpg");
            tailLeft = new Image(imageStream);

            imageStream = new FileInputStream("./src/snake/snakeTail_right.jpg");
            tailRight = new Image(imageStream);

            imageStream = new FileInputStream("./src/snake/snakeTail_up.jpg");
            tailUp = new Image(imageStream);

            imageStream = new FileInputStream("./src/snake/food.jpg");
            foodImage = new Image(imageStream);

            imageStream.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SnakeBouns.class.getName()).log(Level.SEVERE, null, ex);
        }

        root = new GridPane();
        root.setGridLinesVisible(false);
//root.setAlignment(Pos.CENTER);
//center element:
//GridPane.setHalignment(node, HPos.CENTER);
//GridPane.setValignment(node, VPos.CENTER);
        throwFood();

        for (int i = 0; i < numColsAndRows; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numColsAndRows);
            root.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numColsAndRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numColsAndRows);
            root.getRowConstraints().add(rowConst);
        }
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        root.getScene().getAccelerators().put(new KeyCodeCombination(
                KeyCode.W, KeyCombination.CONTROL_ANY), new Runnable() {
            @Override
            public void run() {

                try {
                    changeFlagToUp();
                    Thread.sleep(keyBoardTimeOut);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SnakeBouns.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        root.getScene().getAccelerators().put(new KeyCodeCombination(
                KeyCode.S, KeyCombination.CONTROL_ANY), new Runnable() {
            @Override
            public void run() {

                try {
                    changeFlagToDown();

                    Thread.sleep(keyBoardTimeOut);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SnakeBouns.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        root.getScene().getAccelerators().put(new KeyCodeCombination(
                KeyCode.D, KeyCombination.CONTROL_ANY), new Runnable() {
            @Override
            public void run() {

                try {
                    changeFlagToRight();

                    Thread.sleep(keyBoardTimeOut);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SnakeBouns.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        root.getScene().getAccelerators().put(new KeyCodeCombination(
                KeyCode.A, KeyCombination.CONTROL_ANY), new Runnable() {
            @Override
            public void run() {

                try {
                    changeFlagToLeft();

                    Thread.sleep(keyBoardTimeOut);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SnakeBouns.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(gameSpeed);
                        Platform.runLater(() -> moveSnake());
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SnakeBouns.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        ).start();
        //the platform problem
        root.getScene().getAccelerators().put(new KeyCodeCombination(
                KeyCode.P, KeyCombination.CONTROL_ANY), new Runnable() {
            @Override
            public void run() {

                try {
                    stop();

                } catch (Exception ex) {
                    Logger.getLogger(SnakeBouns.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void moveSnake() {

        moveHeadCoordinates();
        if (checkCollision()) {
            endOfGameRoutine();
        }

        if (isOutBorder()) {
            endOfGameRoutine();
        }
        
        drawHead();
        replaceSnakeBody();
                
        if (snakeLengthAdder <= 0) {
            removeTail();
        } else {
            snakeLengthAdder--;
        }

        if (isFoodEaten()) {
            snakeLengthAdder++;
            removeFood();
            throwFood();
        }

    }

    public void removeFood() {
        root.getChildren().remove(food);
    }

    public void moveHeadCoordinates() {
        if (direction == DOWN) {
            headY++;
        }

        if (direction == UP) {
            headY--;
        }

        if (direction == RIGHT) {
            headX++;
        }

        if (direction == LEFT) {
            headX--;
        }

    }

    public void drawHead() {

        if (nextDirection == DOWN) {

            snakeBodyImages.add(new ImageView(faceDown));
            root.add(snakeBodyImages.get(snakeBodyImages.size() - 1), headX, headY);
            //root.getChildren().contains(new ImageView(faceDown)).add(new ImageView(foodImage));
            direction = nextDirection;
        } else if (nextDirection == UP) {

            snakeBodyImages.add(new ImageView(faceUp));
            root.add(snakeBodyImages.get(snakeBodyImages.size() - 1), headX, headY);
            direction = nextDirection;
        } else if (nextDirection == RIGHT) {

            snakeBodyImages.add(new ImageView(faceRight));
            root.add(snakeBodyImages.get(snakeBodyImages.size() - 1), headX, headY);
            direction = nextDirection;
        } else if (nextDirection == LEFT) {

            snakeBodyImages.add(new ImageView(faceLeft));
            root.add(snakeBodyImages.get(snakeBodyImages.size() - 1), headX, headY);
            direction = nextDirection;
        }
        
    }
  ImageView tempImageView;
    public void replaceSnakeBody(){
      int row;
              int col;
/* boolean flag=true;
              if (snakeBodyImages.size() >0&& flag==true) {
flag=false;
            root.getRowIndex(snakeBodyImages.get(snakeBodyImages.size()-1));
            
              row=  root.getRowIndex(snakeBodyImages.get(0));
               col =  root.getColumnIndex(snakeBodyImages.get(0));
            
                if(snakeBodyImages.get(0).getImage().equals(faceDown)){
              tempImageView = new ImageView(tailDown);
              }
                else   if(snakeBodyImages.get(0).getImage().equals(faceUp)){
              tempImageView = new ImageView(tailUp);
              }
                
                else  if(snakeBodyImages.get(0).getImage().equals(faceRight)){
              tempImageView = new ImageView(tailRight);
              }
                else   if(snakeBodyImages.get(0).getImage().equals(faceLeft)){
              tempImageView = new ImageView(tailLeft);
              }
              
              root.getChildren().remove(snakeBodyImages.get(0));
              root.add(tempImageView, col, row);
               snakeBodyImages.remove(0);
               snakeBodyImages.add(0, tempImageView);
            
  */      
            for (int i = 1; i < snakeBodyImages.size()-1 ; i++) {
               row=  root.getRowIndex(snakeBodyImages.get(i));
               col =  root.getColumnIndex(snakeBodyImages.get(i));
              root.getChildren().remove(snakeBodyImages.get(i));
              tempImageView = new ImageView(snakeBody);
              root.add(tempImageView, col, row);
               snakeBodyImages.remove(i);
               snakeBodyImages.add(i, tempImageView);
            }
        }
    
    public void removeTail() {

        root.getChildren().remove(snakeBodyImages.get(0));
        snakeBodyImages.remove(0);
    }

    public void changeFlagToUp() {
        if (direction != DOWN) {
            nextDirection = UP;
        }
    }

    public void changeFlagToDown() {
        if (direction != UP) {
            nextDirection = DOWN;
        }

    }

    public void changeFlagToRight() {
        if (direction != LEFT) {
            nextDirection = RIGHT;
        }

    }

    public void changeFlagToLeft() {
        if (direction != RIGHT) {
            nextDirection = LEFT;
        }
    }

    public boolean isOutBorder() {
        if (headX < 0) {
            return true;
        } else if (headY < 0) {
            return true;
        } else if (headX >= numColsAndRows) {
            return true;
        } else if (headY >= numColsAndRows) {
            return true;
        } else {
            return false;
        }

    }

    public void throwFood() {
        foodX = generateRandomNumber();
        foodY = generateRandomNumber();

        if (getNodeFromGridPane(root, foodX, foodY) != null) {
            // System.out.println("food thrown again");//still under test so try and try and try with debug
            //here we will call lost and score function and stop this loop
            throwFood();
        } else {
            food = new ImageView(foodImage);
            root.add(food, foodX, foodY);
        }
    }

    public boolean isFoodEaten() {
        if (headX == foodX && headY == foodY) {
            return true;
        }
        return false;
    }

    public boolean checkCollision() {
        for (int i = 0; i < snakeBodyImages.size(); i++) {
            if (snakeBodyImages.contains(getNodeFromGridPane(root, headX, headY))) {
                return true;

            }

        }
        return false;
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public int generateRandomNumber() {

        Random rand = new Random();
// nextInt as provided by Random is exclusive of the top value so you need to add 1 
        int num = rand.nextInt((numColsAndRows - 1) + 1);
//            /***it shoud be between 1 and 16 only
        System.out.println(num);
        return num;

    }

    public void endOfGameRoutine() {
        System.out.println("lost the game");
    }

    @Override
    public void stop() throws Exception {
        super.stop(); //To change body of generated methods, choose Tools | Templates.
        System.exit(0);
    }

}
