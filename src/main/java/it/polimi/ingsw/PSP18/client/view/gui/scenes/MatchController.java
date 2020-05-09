package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.networking.messages.toserver.MoveReceiver;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;
import it.polimi.ingsw.PSP18.server.model.Cell;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.model.Worker;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.PickResult;
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
import java.util.ResourceBundle;

public class MatchController extends Controller {
    @FXML
    private SubScene matchScene;
    private Group matchSceneGroup = new Group();

    private int cameraDistance = 30;
    private int cameraXAngle = 60;
    private Translate pivot = new Translate(0,0,cameraDistance);
    private Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

    private static final double DELTA = 2.5, DELTAZ1 = 1.1, DELTAZ2 = 2.2, DELTAZ3 = 2.9;

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
        Group sea = loadModel(getClass().getResource("/3DGraphics/Sea.obj"));
        Group walls = loadModel(getClass().getResource("/3DGraphics/mura.obj"));
        Group islands = loadModel(getClass().getResource("/3DGraphics/isole.obj"));

        //setup Scene and camera
        matchSceneGroup.getChildren().add(sea);
        matchSceneGroup.getChildren().add(islands);
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
            PickResult pr = e.getPickResult();
            System.out.println(pr.getIntersectedPoint());
            e.consume();
        });

        /*
        matchScene.setOnMousePressed(e -> {
            GameMap gameMap = new GameMap();
            mapCells = gameMap.getMapCells();
            ArrayList<Direction> directions1 = new ArrayList<>();
            directions1.add(Direction.UP);
            ArrayList<Direction> directions2 = new ArrayList<>();
            directions2.add(Direction.DOWN);
            Worker worker1 = new Worker(0, 0, 0, it.polimi.ingsw.PSP18.server.model.Color.RED);
            Worker worker2 = new Worker(0, 2, 1, it.polimi.ingsw.PSP18.server.model.Color.RED);
            mapCells[0][0].setWorker(worker1);
            mapCells[0][2].setWorker(worker2);
            myWorker1 = worker1;
            myWorker2 = worker2;
            MoveList moveList = new MoveList(directions1, directions2, worker1, worker2);
            matchScene.requestFocus();
            int[] indexes = coordinateToIndex(e.getPickResult());
            if(indexes[0] == -1 || indexes[1] == -1) {
                // Click is out of bound
                return;
            }
            if(mapCells[indexes[0]][indexes[1]].getWorker() == myWorker1) {
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
                        }
                    }
                    e2.consume();
                });
            } else if(mapCells[indexes[0]][indexes[1]].getWorker() == myWorker2) {
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
                        }
                    }
                    e2.consume();
                });
            }
            e.consume();
        });

      */
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

    public void buildUpdate( int oldX, int oldY){
        Group block;
        Boolean dome = mapCells[oldX][oldY].getDome();
        final Timeline timeline = new Timeline();

        switch( mapCells[oldX][oldY].getBuilding()) {
            case 0:
                block = loadModel(getClass().getResource("Dome.obj"));
                block.setTranslateY(10);
                block.setTranslateX(indexToCoordinateX(oldX));
                block.setTranslateZ(indexToCoordinateY(oldY));

                timeline.setCycleCount(1);
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                        new KeyValue (block.translateYProperty(), 0)));
                timeline.play();

                break;

            case 1:
                if(!dome) {
                    block = loadModel(getClass().getResource("BuildingBlock01.obj"));
                }
                else {
                    block = loadModel(getClass().getResource("Dome.obj"));
                }
                block.setTranslateY(10);
                block.setTranslateX(indexToCoordinateX(oldX));
                block.setTranslateZ(indexToCoordinateY(oldY));
                if(dome){
                    timeline.setCycleCount(1);
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                            new KeyValue (block.translateYProperty(), DELTAZ1)));
                    timeline.play();
                }
                else{
                    timeline.setCycleCount(1);
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                            new KeyValue (block.translateYProperty(), 0)));
                    timeline.play();
                }
                break;

            case 2:
                if(!dome) {
                    block = loadModel(getClass().getResource("BuildingBlock02.obj"));
                }
                else{
                    block = loadModel(getClass().getResource("Dome.obj"));
                }
                block.setTranslateY(10);
                block.setTranslateX(indexToCoordinateX(oldX));
                block.setTranslateZ(indexToCoordinateY(oldY));
                if(dome){
                    timeline.setCycleCount(1);
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                            new KeyValue (block.translateYProperty(), DELTAZ2)));
                    timeline.play();
                }
                else{
                    timeline.setCycleCount(1);
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                            new KeyValue (block.translateYProperty(), 0)));
                    timeline.play();
                }
                break;

            case 3:
                if(!dome) {
                    block = loadModel(getClass().getResource("BuildingBlock03.obj"));
                }
                else{
                    block = loadModel(getClass().getResource("Dome.obj"));
                }
                block.setTranslateY(10);
                block.setTranslateX(indexToCoordinateX(oldX));
                block.setTranslateZ(indexToCoordinateY(oldY));
                if(dome){
                    timeline.setCycleCount(1);
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                            new KeyValue (block.translateYProperty(), DELTAZ3)));
                    timeline.play();
                }
                else{
                    timeline.setCycleCount(1);
                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                            new KeyValue (block.translateYProperty(), 0)));
                    timeline.play();
                }
                break;
        }
    }
    public void standardMoveUpdate(Worker oldWork, Worker newWork){
        oldWork.getPlayerColor();
    }
    public void apolloMoveUpdate(Worker newWork1, Worker oldWork2, Worker oldWork1,Worker newWorker2){

    }
    public void minotaurMoveUpdate(Worker newWork1, Worker oldWork2, Worker oldWork1,Worker newWorker2){

    }
    public void placeWorkerUpdate(Worker worker){

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
            placeWorkerUpdate(newWorker1);
            return;
        }
        if(gameMapUpdate.isLastActionIsBuild()){
            if(mapCells[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getDome()) {
                buildUpdate(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY());
            } else {
                buildUpdate(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY());
            }
        } else {
            if(oldMap[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker() == null) {
                if (!followMessage) {
                    //Standard 1
                    newWorker1 = mapCells[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                    followMessage = true;
                    standardMove = true;
                } else {
                    //Minotaur 3
                    newWorker2 = mapCells[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                    minotaurMoveUpdate(newWorker1, oldWorker2, oldWorker1, newWorker2);
                    followMessage = false;
                }
            } else {
                if(!followMessage){
                    //Apollo and Minotaur 1
                    newWorker1 = mapCells[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                    oldWorker2 = oldMap[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                    followMessage = true;
                    standardMove = false;
                } else {
                    if(mapCells[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker() == null){
                        if(standardMove) {
                            //StandardMove second half
                            oldWorker1 = oldMap[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                            standardMoveUpdate(newWorker1, oldWorker1);
                            followMessage = false;
                        } else {
                            //Minotaur 2
                            oldWorker1 = oldMap[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                            followMessage = true;
                        }

                    } else {
                        //Apollo 2
                        newWorker2 = mapCells[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
                        oldWorker1 = oldMap[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker();
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

    public void showBuildList(BuildList buildList){
        for (Direction dir : buildList.getBuildlist()) {
           double newX = indexToCoordinateX(DirectionManagement.getX(buildList.getWorker().getX(), dir));
           double newY = indexToCoordinateY(DirectionManagement.getY(buildList.getWorker().getY(), dir));
           //TODO: Color the cells
        }

        // TODO: show message and start click waiting on worker for the build
    }

    // Show both move lists
    public void showMoveList(MoveList moveList) {
        myWorker1 = moveList.getWorker1();
        myWorker2 = moveList.getWorker2();

        // TODO: show message and start click waiting on worker for the move
        matchScene.setOnMousePressed(e -> {
            matchScene.requestFocus();
            int[] indexes = coordinateToIndex(e.getPickResult());
            if(indexes[0] == -1 || indexes[1] == -1) {
                // Click is out of bound
                return;
            }
            if(mapCells[indexes[0]][indexes[1]].getWorker() == myWorker1) {
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
            } else if(mapCells[indexes[0]][indexes[1]].getWorker() == myWorker2) {
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

    public void atlasShowBuild(AtlasBuildList buildList) {
        showBuildList(buildList);
        // TODO: show dome toggle button
    }

    public void showEndTurn() {
        // TODO: show end turn button
    }

    public void placeWorkerInit() {
        // TODO: show worker placement message and start cell click wait
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

    public static double indexToCoordinateX(int index){
        return DELTA*index;
    }

    public static double indexToCoordinateY(int index){
        return DELTA*index;
    }

    public static int[] coordinateToIndex(PickResult pick) {
        int[] index = new int[3];
        index[0] = (int)((pick.getIntersectedPoint().getX()+(DELTA*2.5)) / DELTA);
        if(index[0]>4 || index[0]<0) {
            index[0] = -1;
        }
        index[1] = (int)(((-1*pick.getIntersectedPoint().getZ())+(DELTA*2.5)) / DELTA);
        if(index[1]>4 || index[1]<0) {
            index[1] = -1;
        }
        if(pick.getIntersectedPoint().getY()>=0 && pick.getIntersectedPoint().getZ()<DELTAZ1) {
            index[2] = 0;
        } else if(pick.getIntersectedPoint().getY()>=DELTAZ1 && pick.getIntersectedPoint().getZ()<DELTAZ2) {
            index[2] = 1;
        } else if(pick.getIntersectedPoint().getY()>=DELTAZ2 && pick.getIntersectedPoint().getZ()<DELTAZ3) {
            index[2] = 2;
        } else if(pick.getIntersectedPoint().getY()>=DELTAZ3) {
            index[2] = 3;
        }
        return index;
    }
}
