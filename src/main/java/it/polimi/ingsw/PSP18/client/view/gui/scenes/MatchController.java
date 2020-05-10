package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.networking.messages.toserver.*;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.model.*;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.model.Worker;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fxyz3d.importers.Importer3D;
import org.fxyz3d.importers.Model3D;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MatchController extends Controller {
    @FXML
    private Pane color1;
    @FXML
    private Pane color2;
    @FXML
    private Pane color3;
    @FXML
    private ImageView border3;
    @FXML
    private Label hintLabel;
    @FXML
    private ImageView button1;
    @FXML
    private ImageView button2;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Pane leftBar;
    @FXML
    private ImageView divinity1;
    @FXML
    private Label nick1;
    @FXML
    private ImageView divinity2;
    @FXML
    private Label nick2;
    @FXML
    private ImageView divinity3;
    @FXML
    private Label nick3;
    @FXML
    private GridPane lowBar;
    @FXML
    private SubScene matchScene;
    private Group matchSceneGroup = new Group();

    private int cameraDistance = 30;
    private int cameraXAngle = 60;
    private Translate pivot = new Translate(0,0,cameraDistance);
    private Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
    private double previousX;

    private static final double DELTA = 2.5, DELTAZ1 = 1.1, DELTAZ2 = 2.2, DELTAZ3 = 2.9;
    private HashMap<String, Group> workList= new HashMap<>();
    private Cell[][] mapCells;
    private boolean followMessage = false, standardMove = true, matchStarted = false;
    private Worker newWorker1, newWorker2, oldWorker1, oldWorker2;

    private Worker myWorker1, myWorker2;

    private ArrayList<Direction> directionList1, directionList2;

    private static final int WIDTH= 1280;
    private static final int HEIGHT= 720;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "Match";

        //import models
        Group map = loadModel(getClass().getResource("/3DGraphics/mappa.obj"));
        Group cliff = loadModel(getClass().getResource("/3DGraphics/cliff.obj"));
        Group sea = loadModel(getClass().getResource("/3DGraphics/lava.obj"));
        Group walls = loadModel(getClass().getResource("/3DGraphics/mura.obj"));

        //setup Scene and camera
        matchSceneGroup.getChildren().add(sea);
        matchSceneGroup.getChildren().add(cliff);
        matchSceneGroup.getChildren().add(map);
        matchSceneGroup.getChildren().add(walls);
        PointLight pointLight = new PointLight();
        pointLight.getTransforms().addAll(new Translate(0,-50,0));
        matchSceneGroup.getChildren().add(pointLight);

        Camera camera= new PerspectiveCamera(true);

        matchScene.setFill(Color.RED);
        matchScene.setCamera(camera);
        matchScene.setRoot(matchSceneGroup);

        camera.setNearClip(1);
        camera.setFarClip(1000);

        camera.translateXProperty().set(0);
        camera.translateYProperty().set(0);

        camera.translateZProperty().set(0);

        camera.translateZProperty().set(-cameraDistance);

        camera.getTransforms().addAll (
                pivot,
                yRotate,
                new Rotate(-cameraXAngle, Rotate.X_AXIS),
                new Translate(0, 0, -cameraDistance)
        );


        matchScene.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
            switch (event.getCode()){
                case RIGHT:
                    camera.getTransforms().addAll (
                            pivot,
                            new Rotate(cameraXAngle, Rotate.X_AXIS),
                            new Rotate(-10, Rotate.Y_AXIS),
                            new Rotate(-cameraXAngle, Rotate.X_AXIS),
                            new Translate(0, 0, -cameraDistance)
                    );
                    break;
                case LEFT:
                    camera.getTransforms().addAll (
                            pivot,
                            new Rotate(cameraXAngle, Rotate.X_AXIS),
                            new Rotate(10, Rotate.Y_AXIS),
                            new Rotate(-cameraXAngle, Rotate.X_AXIS),
                            new Translate(0, 0, -cameraDistance)
                    );
                    break;
            }
        });

        matchScene.setOnMousePressed(e -> {
            matchScene.requestFocus();
            e.consume();
        });

        matchScene.setOnMouseDragged(event -> {
            if(previousX - event.getSceneX() < 0) {
                camera.getTransforms().addAll (
                        pivot,
                        new Rotate(cameraXAngle, Rotate.X_AXIS),
                        new Rotate((event.getSceneX())/500, Rotate.Y_AXIS),
                        new Rotate(-cameraXAngle, Rotate.X_AXIS),
                        new Translate(0, 0, -cameraDistance)
                );
            } else {
                camera.getTransforms().addAll (
                        pivot,
                        new Rotate(cameraXAngle, Rotate.X_AXIS),
                        new Rotate((-event.getSceneX())/500, Rotate.Y_AXIS),
                        new Rotate(-cameraXAngle, Rotate.X_AXIS),
                        new Translate(0, 0, -cameraDistance)
                );
            }
            previousX = event.getSceneX();
        });

        // Press button on label press
        label1.setOnMousePressed(e -> {
            Event.fireEvent(button1, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
        });

        // Press button on label press
        label2.setOnMousePressed(e -> {
            Event.fireEvent(button1, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null));
        });
    }

    public Group loadModel(URL url) {
        Model3D model = null;
        try {
            model = Importer3D.loadAsPoly(url);
            return model.getRoot();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void buildUpdate(int oldX, int oldY){
        Group block;
        Boolean dome = mapCells[oldX][oldY].getDome();
        final Timeline timeline = new Timeline();

        switch( mapCells[oldX][oldY].getBuilding()) {
            case 0:
                block = loadModel(getClass().getResource("/3DGraphics/Dome.obj"));
                block.setTranslateY(-10);
                block.setTranslateX(indexToCoordinateX(oldX));
                block.setTranslateZ(indexToCoordinateY(oldY));
                Platform.runLater(() -> matchSceneGroup.getChildren().add(block));

                timeline.setCycleCount(1);
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                        new KeyValue (block.translateYProperty(), 0)));
                timeline.play();

                break;

            case 1:
                if(!dome) {
                    block = loadModel(getClass().getResource("/3DGraphics/BuildingBlock01.obj"));
                }
                else {
                    block = loadModel(getClass().getResource("/3DGraphics/Dome.obj"));
                }
                block.setTranslateY(-10);
                block.setTranslateX(indexToCoordinateX(oldX));
                block.setTranslateZ(indexToCoordinateY(oldY));
                Platform.runLater(() -> matchSceneGroup.getChildren().add(block));
                timeline.setCycleCount(1);
                if(dome){
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                            new KeyValue (block.translateYProperty(), -DELTAZ1)));
                }
                else{
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                            new KeyValue (block.translateYProperty(), 0)));
                }
                timeline.play();
                break;

            case 2:
                if(!dome) {
                    block = loadModel(getClass().getResource("/3DGraphics/BuildingBlock02.obj"));
                }
                else{
                    block = loadModel(getClass().getResource("/3DGraphics/Dome.obj"));
                }
                block.setTranslateY(-10);
                block.setTranslateX(indexToCoordinateX(oldX));
                block.setTranslateZ(indexToCoordinateY(oldY));
                Platform.runLater(() -> matchSceneGroup.getChildren().add(block));
                timeline.setCycleCount(1);
                if(dome){
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                            new KeyValue (block.translateYProperty(), -DELTAZ2)));
                }
                else{
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                            new KeyValue (block.translateYProperty(), 0)));
                }
                timeline.play();
                break;

            case 3:
                if(!dome) {
                    block = loadModel(getClass().getResource("/3DGraphics/BuildingBlock03.obj"));
                }
                else{
                    block = loadModel(getClass().getResource("/3DGraphics/Dome.obj"));
                }

                block.setTranslateY(-10);
                block.setTranslateX(indexToCoordinateX(oldX));
                block.setTranslateZ(indexToCoordinateY(oldY));
                Platform.runLater(() -> matchSceneGroup.getChildren().add(block));

                timeline.setCycleCount(1);
                if(dome){
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                            new KeyValue (block.translateYProperty(), -DELTAZ3)));
                }
                else{
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                            new KeyValue (block.translateYProperty(), 0)));
                }
                timeline.play();
                break;
        }
    }
    public void standardMoveUpdate(Worker oldWork, Worker newWork){
        final Timeline timeline = new Timeline();
        Group workerSelected;

        switch (oldWork.getPlayerColor()){

            case BLUE:
                switch(oldWork.getID()){
                    case 0:
                        workerSelected = workList.get("WB1");

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerSelected.translateXProperty(), indexToCoordinateX(newWork.getX()))));
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerSelected.translateZProperty(), indexToCoordinateY(newWork.getY()))));
                        timeline.play();
                        break;
                    case 1:

                        workerSelected = workList.get("WB2");

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerSelected.translateXProperty(), indexToCoordinateX(newWork.getX()))));
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerSelected.translateZProperty(), indexToCoordinateY(newWork.getY()))));
                        timeline.play();
                        break;
                }
                break;

            case RED:
                switch(oldWork.getID()){
                    case 0:

                        workerSelected = workList.get("WR1");

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerSelected.translateXProperty(), indexToCoordinateX(newWork.getX()))));
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerSelected.translateZProperty(), indexToCoordinateY(newWork.getY()))));
                        timeline.play();
                        break;

                    case 1:
                        workerSelected = workList.get("WR2");

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerSelected.translateXProperty(), indexToCoordinateX(newWork.getX()))));
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerSelected.translateZProperty(), indexToCoordinateY(newWork.getY()))));
                        timeline.play();
                        break;
                }
                break;

            case GREEN:
                switch(oldWork.getID()){
                    case 0:

                        workerSelected = workList.get("WW1");

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerSelected.translateXProperty(), indexToCoordinateX(newWork.getX()))));
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerSelected.translateZProperty(), indexToCoordinateY(newWork.getY()))));
                        timeline.play();
                        break;
                    case 1:

                        workerSelected = workList.get("WW2");

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerSelected.translateXProperty(), indexToCoordinateX(newWork.getX()))));
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerSelected.translateZProperty(), indexToCoordinateY(newWork.getY()))));
                        timeline.play();
                        break;
                }
                break;
        }
    }
    public void apolloMoveUpdate(Worker newWork1, Worker oldWork2, Worker oldWork1,Worker newWorker2){

    }
    public void minotaurMoveUpdate(Worker newWork1, Worker oldWork2, Worker oldWork1,Worker newWorker2){

    }
    public void placeWorkerUpdate(Worker worker){
        Group workerBlue1;
        Group workerBlue2;
        Group workerRed1;
        Group workerRed2;
        Group workerWhite1;
        Group workerWhite2;




        final Timeline timeline = new Timeline();


        switch (worker.getPlayerColor()){

            case BLUE:
                switch(worker.getID()){
                    case 0:
                        workerBlue1 = loadModel(getClass().getResource("/3DGraphics/workerBlue.obj"));
                        workList.put("WB1", workerBlue1);
                        workerBlue1.setTranslateY(-10);
                        workerBlue1.setTranslateX(indexToCoordinateX(worker.getX()));
                        workerBlue1.setTranslateZ(indexToCoordinateY(worker.getY()));
                        Platform.runLater(() -> matchSceneGroup.getChildren().add(workerBlue1));

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerBlue1.translateYProperty(), 0)));
                        timeline.play();
                        break;
                    case 1:
                        workerBlue2 = loadModel(getClass().getResource("/3DGraphics/workerBlue.obj"));
                        workList.put("WB2", workerBlue2);
                        workerBlue2.setTranslateY(-10);
                        workerBlue2.setTranslateX(indexToCoordinateX(worker.getX()));
                        workerBlue2.setTranslateZ(indexToCoordinateY(worker.getY()));
                        Platform.runLater(() -> matchSceneGroup.getChildren().add(workerBlue2));

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerBlue2.translateYProperty(), 0)));
                        timeline.play();
                        break;
                }
                break;

            case RED:
                switch(worker.getID()){
                    case 0:
                        workerRed1 = loadModel(getClass().getResource("/3DGraphics/workerRed.obj"));
                        workList.put("WR1", workerRed1);
                        workerRed1.setTranslateY(-10);
                        workerRed1.setTranslateX(indexToCoordinateX(worker.getX()));
                        workerRed1.setTranslateZ(indexToCoordinateY(worker.getY()));

                        Platform.runLater(() -> matchSceneGroup.getChildren().add(workerRed1));

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerRed1.translateYProperty(), 0)));
                        timeline.play();
                        break;
                    case 1:
                        workerRed2 = loadModel(getClass().getResource("/3DGraphics/workerRed.obj"));
                        workList.put("WR2", workerRed2);

                        workerRed2.setTranslateY(-10);
                        workerRed2.setTranslateX(indexToCoordinateX(worker.getX()));
                        workerRed2.setTranslateZ(indexToCoordinateY(worker.getY()));
                        Platform.runLater(() -> matchSceneGroup.getChildren().add(workerRed2));

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerRed2.translateYProperty(), 0)));
                        timeline.play();
                        break;
                }
                break;

            case GREEN:
                switch(worker.getID()){
                    case 0:
                        workerWhite1 = loadModel(getClass().getResource("/3DGraphics/workerWhite.obj"));
                        workList.put("WW1", workerWhite1);

                        workerWhite1.setTranslateY(-10);
                        workerWhite1.setTranslateX(indexToCoordinateX(worker.getX()));
                        workerWhite1.setTranslateZ(indexToCoordinateY(worker.getY()));
                        Platform.runLater(() -> matchSceneGroup.getChildren().add(workerWhite1));

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerWhite1.translateYProperty(), 0)));
                        timeline.play();
                        break;
                    case 1:
                        workerWhite2 = loadModel(getClass().getResource("/3DGraphics/workerWhite.obj"));
                        workList.put("WW2", workerWhite2);
                        workerWhite2.setTranslateY(-10);
                        workerWhite2.setTranslateX(indexToCoordinateX(worker.getX()));
                        workerWhite2.setTranslateZ(indexToCoordinateY(worker.getY()));
                        Platform.runLater(() -> matchSceneGroup.getChildren().add(workerWhite2));

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
                                new KeyValue (workerWhite2.translateYProperty(), 0)));
                        timeline.play();
                        break;
                }
                break;
        }
    }

    /***
     * Method used to determine if the current move is one between:
     * placeWorker : placing the worker in the initial phase of the game
     * standardMove : a standard move
     * apolloMoveUpdate : a move where the two workers involved are swapped
     * minotaurMoveUpdate : a mov where one worker pushes the other one
     * We then proceed to save all the workers needed for the methods above in parameters
     * @param gameMapUpdate Contains contains the new map, the last direction, the last x and y coordinate and a boolean that signals if the move is a build or a move
     */
    public void mapUpdate(GameMapUpdate gameMapUpdate){
        Cell[][] oldMap = mapCells;
        mapCells = gameMapUpdate.getGameMap();
        if(!matchStarted){
            newWorker1 = mapCells[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
            if(newWorker1 != null) {
                placeWorkerUpdate(newWorker1);
            }
            return;
        }
        if(gameMapUpdate.isLastActionIsBuild()){
                buildUpdate(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY());
        } else {
            if(oldMap[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker() == null) {
                if (!followMessage) {
                    //Standard 1
                    newWorker1 = mapCells[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                    newWorker1.setPosition(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY());
                    followMessage = true;
                    standardMove = true;
                } else {
                    //Minotaur 3
                    newWorker2 = mapCells[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                    newWorker2.setPosition(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY());
                    minotaurMoveUpdate(newWorker1, oldWorker2, oldWorker1, newWorker2);
                    followMessage = false;
                }
            } else {
                if(!followMessage){
                    //Apollo and Minotaur 1
                    newWorker1 = mapCells[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                    newWorker1.setPosition(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY());
                    oldWorker2 = oldMap[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                    oldWorker2.setPosition(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY());
                    followMessage = true;
                    standardMove = false;
                } else {
                    if(mapCells[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker() == null){
                        if(standardMove) {
                            //StandardMove second half
                            oldWorker1 = oldMap[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                            oldWorker1.setPosition(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY());
                            standardMoveUpdate(oldWorker1, newWorker1);
                            followMessage = false;
                        } else {
                            //Minotaur 2
                            oldWorker1 = oldMap[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                            oldWorker1.setPosition(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY());
                            followMessage = true;
                        }

                    } else {
                        //Apollo 2
                        newWorker2 = mapCells[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                        newWorker2.setPosition(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY());
                        oldWorker1 = oldMap[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                        oldWorker1.setPosition(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY());
                        followMessage = false;
                        apolloMoveUpdate(newWorker1, oldWorker2, oldWorker1, newWorker2);
                    }
                }
            }
        }
    }

    public void setMatchStarted(boolean matchStarted) {
        this.matchStarted = matchStarted;
    }

    public void showBuildList(BuildList buildList) {
        for (Direction dir : buildList.getBuildlist()) {
            double newX = indexToCoordinateX(DirectionManagement.getX(buildList.getWorker().getX(), dir));
            double newY = indexToCoordinateY(DirectionManagement.getY(buildList.getWorker().getY(), dir));
            //TODO: Color the cells
        }
    }

    public void standardBuildList(BuildList buildList) {
        showBuildList(buildList);
        Platform.runLater(() -> hintLabel.setText("Choose where to build!"));
        matchScene.setOnMousePressed(e -> {
            matchScene.requestFocus();
            int[] newIndexes = coordinateToIndex(e.getPickResult());
            if(newIndexes[0] == -1 || newIndexes[1] == -1) {
                // Click is out of bound
                return;
            }
            for(Direction direction : buildList.getBuildlist()) {
                int newX = DirectionManagement.getX(buildList.getWorker().getX(), direction);
                int newY = DirectionManagement.getY(buildList.getWorker().getY(), direction);

                if(newIndexes[0] == newX && newIndexes[1] == newY) { // Found the valid direction
                    socket.sendMessage(new BuildReceiver(direction));
                    matchScene.setOnMousePressed(e2 -> {
                        matchScene.requestFocus();
                        e2.consume();
                    });
                }
            }
            e.consume();
        });
    }

    // Show both move lists
    public void showMoveList(MoveList moveList) {
        myWorker1 = moveList.getWorker1();
        myWorker2 = moveList.getWorker2();
        Platform.runLater(() -> hintLabel.setText("Click on the worker you want to move with"));

        Platform.runLater(()->
                hintLabel.setText("Choose where to Move!")
        );

        matchScene.setOnMousePressed(e -> {
            matchScene.requestFocus();
            int[] indexes = coordinateToIndex(e.getPickResult());
            if(indexes[0] == -1 || indexes[1] == -1) {
                // Click is out of bound
                return;
            }
            Platform.runLater(() -> hintLabel.setText("Click on the green cell you want to move to"));
            if(indexes[0] == myWorker1.getX() && indexes[1] == myWorker1.getY()) {
                showSingleMoveList(moveList, indexes[0], indexes[1]);
                matchScene.setOnMousePressed(e2 -> {
                    matchScene.requestFocus();
                    int[] newIndexes = coordinateToIndex(e2.getPickResult());
                    if(newIndexes[0] == -1 || newIndexes[1] == -1) {
                        // Click is out of bound
                        return;
                    }
                    for(Direction direction : moveList.getMoveList1()) {
                        int newX = DirectionManagement.getX(moveList.getWorker1().getX(), direction);
                        int newY = DirectionManagement.getY(moveList.getWorker1().getY(), direction);

                        if(newIndexes[0] == newX && newIndexes[1] == newY) { // Found the valid direction
                            socket.sendMessage(new MoveReceiver(direction, 0));
                            matchScene.setOnMousePressed(e3 -> {
                                matchScene.requestFocus();
                                e3.consume();
                            });
                        }
                    }
                    e2.consume();
                });
            } else if(indexes[0] == myWorker2.getX() && indexes[1] == myWorker2.getY()) {
                showSingleMoveList(moveList, indexes[0], indexes[1]);
                matchScene.setOnMousePressed(e2 -> {
                    matchScene.requestFocus();
                    int[] newIndexes = coordinateToIndex(e2.getPickResult());
                    if(newIndexes[0] == -1 || newIndexes[1] == -1) {
                        // Click is out of bound
                        return;
                    }
                    for(Direction direction : moveList.getMoveList2()) {
                        int newX = DirectionManagement.getX(moveList.getWorker2().getX(), direction);
                        int newY = DirectionManagement.getY(moveList.getWorker2().getY(), direction);

                        if(newIndexes[0] == newX && newIndexes[1] == newY) { // Found the valid direction
                            socket.sendMessage(new MoveReceiver(direction, 1));
                            matchScene.setOnMousePressed(e3 -> {
                                matchScene.requestFocus();
                                e3.consume();
                            });
                        }
                    }
                    e2.consume();
                });
            }
            e.consume();
        });
    }

    // Show only one move list after the click
    private void showSingleMoveList(MoveList moveList, int x, int y) {
        if(moveList.getWorker1().getX() == x && moveList.getWorker1().getY() == y) {
            for (Direction dir : moveList.getMoveList1()) {
                double newX = indexToCoordinateX(DirectionManagement.getX(moveList.getWorker1().getX(), dir));
                double newY = indexToCoordinateY(DirectionManagement.getY(moveList.getWorker1().getY(), dir));
                //TODO: Color the cells
            }
        } else if(moveList.getWorker2().getX() == x && moveList.getWorker2().getY() == y) {
            for (Direction dir : moveList.getMoveList2()) {
                double newX = indexToCoordinateX(DirectionManagement.getX(moveList.getWorker2().getX(), dir));
                double newY = indexToCoordinateY(DirectionManagement.getY(moveList.getWorker2().getY(), dir));
                //TODO: Color the cells
            }
        } else {
            System.err.println("Click is not in a cell occupied by a worker");
        }
    }

    public void atlasBuild(AtlasBuildList buildList) {
        Platform.runLater(() -> label1.setText("Dome"));
        showBuildList(buildList);
        Platform.runLater(() -> hintLabel.setText("Choose where to build!"));
        button1.setOnMousePressed(e -> {
            if(button1.getImage().equals(new Image("/2DGraphics/GreenButton.png"))){
                Image image = new Image("/2DGraphics/RedButton.png");
                button1.setImage(image);
            } else {
                Image image1 = new Image("/2DGraphics/GreenButton.png");
                button1.setImage(image1);
            }
            e.consume();
        });

        matchScene.setOnMousePressed(e1 -> {
            matchScene.requestFocus();
            int[] newIndexes = coordinateToIndex(e1.getPickResult());
            if(newIndexes[0] == -1 || newIndexes[1] == -1) {
                // Click is out of bound
                return;
            }
            for(Direction direction : buildList.getBuildlist()) {
                int newX = DirectionManagement.getX(buildList.getWorker().getX(), direction);
                int newY = DirectionManagement.getY(buildList.getWorker().getY(), direction);

                if(newIndexes[0] == newX && newIndexes[1] == newY && button1.getImage().equals(new Image("/2DGraphics/GreenButton.png"))) { // Found the valid direction
                    socket.sendMessage(new AtlasBuildReceiver(direction, false));
                    matchScene.setOnMousePressed(e2 -> {
                        matchScene.requestFocus();
                        e2.consume();
                    });
                } else if(newIndexes[0] == newX && newIndexes[1] == newY && button1.getImage().equals(new Image("/2DGraphics/RedButton.png"))){
                    socket.sendMessage(new AtlasBuildReceiver(direction, true));
                    matchScene.setOnMousePressed(e2 -> {
                        matchScene.requestFocus();
                        e2.consume();
                    });
                }
            }
            e1.consume();
        });
    }

    public void showEndTurn() {
        Platform.runLater(() -> hintLabel.setText("End your turn!"));
        Image image = new Image("/2DGraphics/GreenButton.png");
        Platform.runLater(() -> button2.setImage(image));
        button2.setOnMousePressed(e -> {
            Image image1 = new Image("/2DGraphics/RedButton.png");
            Platform.runLater(() -> button2.setImage(image1));
            socket.sendMessage(new EndTurnReceiver());
            matchScene.setOnMousePressed(e2 -> {
                matchScene.requestFocus();
                e2.consume();
            });
            e.consume();
        });
    }

    public void placeWorkerInit() {
        Platform.runLater(() -> hintLabel.setText("Select a free position for the first worker"));
        matchScene.setOnMousePressed(e -> {
            matchScene.requestFocus();
            int[] indexes1 = coordinateToIndex(e.getPickResult());
            if(indexes1[0] == -1 || indexes1[1] == -1) {
                // Click is out of bound
                Platform.runLater(() -> hintLabel.setText("Select a free position for the first worker"));
                return;
            }
            Platform.runLater(() -> hintLabel.setText("Select a free position for the second worker"));
            matchScene.setOnMousePressed(e2 -> {
                matchScene.requestFocus();
                int[] indexes2 = coordinateToIndex(e2.getPickResult());
                if(indexes2[0] == -1 || indexes2[1] == -1 || (indexes2[0] == indexes1[0] && indexes2[1] == indexes1[1])) {
                    // Click is out of bound or is the same position of the first worker
                    Platform.runLater(() -> hintLabel.setText("Select a free position for the second worker"));
                    return;
                }
                socket.sendMessage(new WorkerReceiver(indexes1[0], indexes1[1], indexes2[0], indexes2[1]));
                matchScene.setOnMousePressed(e3 -> {
                    matchScene.requestFocus();
                    e3.consume();
                });
                e2.consume();
            });
            e.consume();
        });
    }

    public void prometheusBuildShow(PrometheusBuildList prometheusBuildList) {
        for (Direction dir : prometheusBuildList.getBuildlist1()) {
            double newX = indexToCoordinateX(DirectionManagement.getX(prometheusBuildList.getWorker1().getX(), dir));
            double newY = indexToCoordinateY(DirectionManagement.getY(prometheusBuildList.getWorker1().getY(), dir));
            //TODO: Color the cells
        }

        for (Direction dir : prometheusBuildList.getBuildlist2()) {
            double newX = indexToCoordinateX(DirectionManagement.getX(prometheusBuildList.getWorker2().getX(), dir));
            double newY = indexToCoordinateY(DirectionManagement.getY(prometheusBuildList.getWorker2().getY(), dir));
            //TODO: Color the cells
        }

        // TODO: show message that if he doesn't move up he can build both times
    }

    public void singleMoveUpdate(SingleMoveList singleMoveList) {
        MoveList moveList = new MoveList(singleMoveList.getMoveList(), null, singleMoveList.getWorker(), null);
        showSingleMoveList(moveList, singleMoveList.getWorker().getX(), singleMoveList.getWorker().getY());

        if(singleMoveList.isOptional()) {
            // TODO: show toggle button that the move is optional to skip if not just wait for the move
        } else {
            // TODO: wait for the move
        }
    }

    public void updatePlayers(ArrayList<PlayerData> players) {
        if (players.size() == 2) {
            for (PlayerData playerData : players) {
                if (playerData.getPlayOrder() == 0) {
                    if (!playerData.getLost()) {
                        Platform.runLater(() -> {
                            nick1.setText(playerData.getPlayerID());
                            Image image = new Image("/2DGraphics/" + playerData.getDivinity() + ".png");
                            divinity1.setImage(image);
                        });
                    } else {
                        Platform.runLater(() -> {
                            ColorAdjust blackout = new ColorAdjust();
                            blackout.setBrightness(-0.7);

                            divinity1.setEffect(blackout);
                            divinity1.setCache(true);
                            divinity1.setCacheHint(CacheHint.SPEED);
                        });
                    }
                    color1.setStyle("-fx-background-color: Red;");
                }
                if (playerData.getPlayOrder() == 1) {
                    if (!playerData.getLost()) {
                        Platform.runLater(() -> {
                            nick2.setText(playerData.getPlayerID());
                            Image image1 = new Image("/2DGraphics/" + playerData.getDivinity() + ".png");
                            divinity2.setImage(image1);
                        });
                    } else {
                        Platform.runLater(() -> {
                            ColorAdjust blackout = new ColorAdjust();
                            blackout.setBrightness(-0.7);

                            divinity2.setEffect(blackout);
                            divinity2.setCache(true);
                            divinity2.setCacheHint(CacheHint.SPEED);
                        });
                    }
                    color2.setStyle("-fx-background-color: Blue;");
                }
                Platform.runLater(() -> {
                    nick3.setVisible(false);
                    divinity3.setVisible(false);
                    border3.setVisible(false);
                    color3.setVisible(false);
                });
            }
        } else if (players.size() == 3) {
            for (PlayerData playerData : players) {
                if (playerData.getPlayOrder() == 0) {
                    if (!playerData.getLost()) {
                        Platform.runLater(() -> {
                            nick1.setText(playerData.getPlayerID());
                            Image image = new Image("/2DGraphics/" + playerData.getDivinity() + ".png");
                            divinity1.setImage(image);
                        });
                    } else {
                        Platform.runLater(() -> {
                            ColorAdjust blackout = new ColorAdjust();
                            blackout.setBrightness(-0.7);

                            divinity1.setEffect(blackout);
                            divinity1.setCache(true);
                            divinity1.setCacheHint(CacheHint.SPEED);
                        });
                    }
                    color1.setStyle("-fx-background-color: Red;");
                }
                if (playerData.getPlayOrder() == 1) {
                    if (!playerData.getLost()) {
                        Platform.runLater(() -> {
                            nick2.setText(playerData.getPlayerID());
                            Image image1 = new Image("/2DGraphics/" + playerData.getDivinity() + ".png");
                            divinity2.setImage(image1);
                        });
                    } else {
                        Platform.runLater(() -> {
                            ColorAdjust blackout = new ColorAdjust();
                            blackout.setBrightness(-0.7);

                            divinity2.setEffect(blackout);
                            divinity2.setCache(true);
                            divinity2.setCacheHint(CacheHint.SPEED);
                        });
                    }
                    color2.setStyle("-fx-background-color: Blue;");
                }
                if (playerData.getPlayOrder() == 2) {
                    if (!playerData.getLost()) {
                        Platform.runLater(() -> {
                            nick3.setText(playerData.getPlayerID());
                            Image image2 = new Image("/2DGraphics/" + playerData.getDivinity() + ".png");
                            divinity3.setImage(image2);
                        });
                    } else {
                        Platform.runLater(() -> {
                            ColorAdjust blackout = new ColorAdjust();
                            blackout.setBrightness(-0.7);

                            divinity3.setEffect(blackout);
                            divinity3.setCache(true);
                            divinity3.setCacheHint(CacheHint.SPEED);
                        });
                    }
                    color3.setStyle("-fx-background-color: White;");
                }
            }
        }
    }

    public void optionalBuildUpdate(BuildListFlag buildList) {
        showBuildList(buildList);
        Platform.runLater(()->
                label1.setText("Skip Build")
                );
        Platform.runLater(()->
                hintLabel.setText("Choose where to build!")
        );
        button1.setOnMousePressed(e -> {
            socket.sendMessage(new BuildReceiver(null));
            matchScene.setOnMousePressed(e2 -> {
                matchScene.requestFocus();
                e2.consume();
            });
            e.consume();
        });

        matchScene.setOnMousePressed(e -> {
            matchScene.requestFocus();
            int[] newIndexes = coordinateToIndex(e.getPickResult());
            if(newIndexes[0] == -1 || newIndexes[1] == -1) {
                // Click is out of bound
                return;
            }
            for(Direction direction : buildList.getBuildlist()) {
                int newX = DirectionManagement.getX(buildList.getWorker().getX(), direction);
                int newY = DirectionManagement.getY(buildList.getWorker().getY(), direction);

                if(newIndexes[0] == newX && newIndexes[1] == newY) { // Found the valid direction
                    socket.sendMessage(new BuildReceiver(direction));
                    matchScene.setOnMousePressed(e2 -> {
                        matchScene.requestFocus();
                        e2.consume();
                    });
                }
            }
            e.consume();
        });

    }

    public static double indexToCoordinateX(int index){
        return (index-2)*DELTA;
    }

    public static double indexToCoordinateY(int index){
        return (index-2)*DELTA*(-1);
    }

    public static int[] coordinateToIndex(PickResult pick) {
        int[] index = new int[3];
        double x = pick.getIntersectedNode().localToScene(0,0,0).getX() + pick.getIntersectedPoint().getX();
        double y = -1 * (pick.getIntersectedNode().localToScene(0,0,0).getY() + pick.getIntersectedPoint().getY());
        double z = pick.getIntersectedNode().localToScene(0,0,0).getZ() + pick.getIntersectedPoint().getZ();
        index[0] = (int)((x+(DELTA*2.5)) / DELTA);
        if(index[0]>4 || index[0]<0) {
            index[0] = -1;
        }
        index[1] = (int)(((-1*z)+(DELTA*2.5)) / DELTA);
        if(index[1]>4 || index[1]<0) {
            index[1] = -1;
        }
        if(y>=0 && y<DELTAZ1) {
            index[2] = 0;
        } else if(y>=DELTAZ1 && y<DELTAZ2) {
            index[2] = 1;
        } else if(y>=DELTAZ2 && y<DELTAZ3) {
            index[2] = 2;
        } else if(y>=DELTAZ3) {
            index[2] = 3;
        }
        return index;
    }
}
