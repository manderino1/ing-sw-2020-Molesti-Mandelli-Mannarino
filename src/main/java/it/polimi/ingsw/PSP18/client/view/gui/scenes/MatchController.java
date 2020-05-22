package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.networking.messages.toserver.*;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.model.*;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;
import javafx.animation.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import it.polimi.ingsw.PSP18.client.view.gui.scenes.utils.Importer3D;
import org.fxyz3d.importers.Model3D;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.max;

/***
 * Manages the 3d scene of the gui
 * Is opened when the match starts
 */
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
    private SubScene matchScene;
    private Group matchSceneGroup = new Group();
    private Group planes = new Group();
    private final Timeline planesTimeline = new Timeline();

    private int cameraDistance = 35;
    private int cameraXAngle = 45;
    private Translate pivot = new Translate(0,0,cameraDistance);
    private Rotate yRotate = new Rotate(15, Rotate.Y_AXIS);
    private double previousX;

    private static final double DELTA = 2.5, DELTAZ1 = 1.1, DELTAZ2 = 2.2, DELTAZ3 = 2.9;
    private HashMap<String, Group> workList= new HashMap<>();
    private Cell[][] mapCells;
    private boolean followMessage = false, standardMove = true, matchStarted = false;
    private Worker newWorker1, newWorker2, oldWorker1, oldWorker2;

    private Worker myWorker1, myWorker2;

    /***
     * Initialize the 3d objects required for the graphic
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "Match";

        //import models
        Group map = loadModel(getClass().getResource("/3DGraphics/mappa.obj"));
        Group cliff = loadModel(getClass().getResource("/3DGraphics/Cliff.obj"));
        Group sea = loadModel(getClass().getResource("/3DGraphics/Sea.obj"));
        Group walls = loadModel(getClass().getResource("/3DGraphics/mura.obj"));
        Group islands = loadModel(getClass().getResource("/3DGraphics/isole.obj"));

        //setup Scene and camera
        matchSceneGroup.getChildren().add(sea);
        matchSceneGroup.getChildren().add(cliff);
        matchSceneGroup.getChildren().add(map);
        matchSceneGroup.getChildren().add(walls);
        matchSceneGroup.getChildren().add(islands);

        AmbientLight ambientLight = new AmbientLight();
        matchSceneGroup.getChildren().add(ambientLight);

        Camera camera= new PerspectiveCamera(true);

        matchScene.setFill(Color.AQUA);
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

        planesTimeline.setCycleCount(Timeline.INDEFINITE);
        planesTimeline.setAutoReverse(true);


        matchScene.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
            switch (event.getCode()){
                case UP:
                    if (cameraXAngle<=90) {
                        camera.getTransforms().addAll(
                                pivot,
                                new Rotate(cameraXAngle, Rotate.X_AXIS),
                                new Rotate(-2, Rotate.X_AXIS),
                                new Rotate(-cameraXAngle, Rotate.X_AXIS),
                                new Translate(0, 0, -cameraDistance)
                        );
                        cameraXAngle = cameraXAngle + 2;
                    }
                    break;
                case DOWN:
                    if (cameraXAngle>=30) {
                        camera.getTransforms().addAll(
                                pivot,
                                new Rotate(cameraXAngle, Rotate.X_AXIS),
                                new Rotate(2, Rotate.X_AXIS),
                                new Rotate(-cameraXAngle, Rotate.X_AXIS),
                                new Translate(0, 0, -cameraDistance)
                        );
                    }
                    cameraXAngle=cameraXAngle - 2;
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
    }

    /***
     * Load the required 3d model
     * @param url the 3d model location
     * @return the loaded 3d model
     */
    public Group loadModel(URL url) {
        Model3D model;
        try {
            model = Importer3D.loadAsPoly(url);
            return model.getRoot();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /***
     * Used to update the 3d scene on a new build
     * @param oldX x coordinate of the build location
     * @param oldY y coordinate of the build location
     */
    public void buildUpdate(int oldX, int oldY){
        Group block;
        Boolean dome = mapCells[oldX][oldY].getDome();
        final Timeline timeline = new Timeline();

        switch(mapCells[oldX][oldY].getBuilding()) {
            case 0:
                block = loadModel(getClass().getResource("/3DGraphics/Dome.obj"));
                break;

            case 1:
                if(!dome) {
                    block = loadModel(getClass().getResource("/3DGraphics/BuildingBlock01.obj"));
                }
                else {
                    block = loadModel(getClass().getResource("/3DGraphics/Dome.obj"));
                }
                break;

            case 2:
                if(!dome) {
                    block = loadModel(getClass().getResource("/3DGraphics/BuildingBlock02.obj"));
                }
                else{
                    block = loadModel(getClass().getResource("/3DGraphics/Dome.obj"));
                }
                break;
            case 3:
                if(!dome) {
                    block = loadModel(getClass().getResource("/3DGraphics/BuildingBlock03.obj"));
                }
                else{
                    block = loadModel(getClass().getResource("/3DGraphics/Dome.obj"));
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + mapCells[oldX][oldY].getBuilding());
        }

        block.setTranslateY(-20);
        block.setTranslateX(indexToCoordinateX(oldX));
        block.setTranslateZ(indexToCoordinateY(oldY));
        Platform.runLater(() -> matchSceneGroup.getChildren().add(block));

        timeline.setCycleCount(1);
        if(dome){
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                    new KeyValue (block.translateYProperty(), levelToCoordZ(mapCells[oldX][oldY].getBuilding()),Interpolator.EASE_OUT)));
        }
        else{
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                    new KeyValue (block.translateYProperty(), 0,Interpolator.EASE_OUT)));
        }
        timeline.play();
    }

    /***
     * Update the 3d scene on a standard move
     * @param oldWork the old worker reference
     * @param newWork the new worker reference
     */
    public void standardMoveUpdate(Worker oldWork, Worker newWork){
        final Timeline timeline = new Timeline();
        Group workerSelected = pickWorker(oldWork);
        int oldHeight = mapCells[oldWork.getX()][oldWork.getY()].getBuilding();
        int newHeight = mapCells[newWork.getX()][newWork.getY()].getBuilding();

        timeline.setCycleCount(1);
        assert workerSelected != null;
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                new KeyValue (workerSelected.translateXProperty(), indexToCoordinateX(newWork.getX()), Interpolator.EASE_OUT)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                new KeyValue (workerSelected.translateZProperty(), indexToCoordinateY(newWork.getY()), Interpolator.EASE_OUT)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(250),
                new KeyValue (workerSelected.translateYProperty(), -max(deltaToCoordinateZ(0,oldHeight), deltaToCoordinateZ(0,newHeight))-0.8, Interpolator.EASE_OUT)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                new KeyValue (workerSelected.translateYProperty(), -deltaToCoordinateZ(0,newHeight), Interpolator.EASE_BOTH)));

        timeline.play();
    }

    /***
     * Update the 3d scene on a move where the two players are switched up
     * @param newWork1 the first worker new reference
     * @param oldWork2 the second worker old reference
     * @param oldWork1 the first worker old reference
     * @param newWork2 the second worker new reference
     */
    public void apolloMoveUpdate(Worker newWork1, Worker oldWork2, Worker oldWork1,Worker newWork2){

        final Timeline offsetTimeline = new Timeline();

        Group workerSelected = pickWorker(oldWork1);
        Group workerEnemy = pickWorker(oldWork2);

        final Timeline timeline = setupTimeline(workerSelected, oldWork1, newWork1,1000, 4);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                new KeyValue (workerSelected.rotateProperty(), workerSelected.getRotate()-360, Interpolator.EASE_BOTH)));

        final Timeline enemyTimeline = setupTimeline(workerEnemy,oldWork2, newWork2,500, 0.8);
        offsetTimeline.setCycleCount(1);
        offsetTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(250),
                new KeyValue (workerSelected.translateXProperty(), indexToCoordinateX(oldWork1.getX()))));

        SequentialTransition sequentialTransition = new SequentialTransition(offsetTimeline, enemyTimeline);
        ParallelTransition parallelTransition = new ParallelTransition(sequentialTransition, timeline);
        parallelTransition.play();
    }

    /***
     * Given a worker returns the object into the 3d scene that represents the worker
     * @param oldWork the old worker reference
     * @return the created worker
     */
    public Group pickWorker(Worker oldWork){
        Group workerSelected = null;
        switch (oldWork.getPlayerColor()){
            case BLUE:
                switch(oldWork.getID()){
                    case 0:
                        workerSelected = workList.get("WB1");
                        break;
                    case 1:
                        workerSelected = workList.get("WB2");
                        break;
                }
                break;

            case RED:
                switch(oldWork.getID()){
                    case 0:
                        workerSelected = workList.get("WR1");
                        break;
                    case 1:
                        workerSelected = workList.get("WR2");
                        break;
                }
                break;

            case GREEN:
                switch(oldWork.getID()){
                    case 0:
                        workerSelected = workList.get("WW1");
                        break;
                    case 1:
                        workerSelected = workList.get("WW2");
                        break;
                }
                break;
        }
        return workerSelected;
    }

    /***
     * Update the 3d scene on a move where the second player is pushed away by the first
     * @param newWork1 the first worker new reference
     * @param oldWork2 the second worker old reference
     * @param oldWork1 the first worker old reference
     * @param newWork2 the second worker new reference
     */
    public void minotaurMoveUpdate(Worker newWork1, Worker oldWork2, Worker oldWork1,Worker newWork2){

        final Timeline offsetTimeline = new Timeline();

        Group workerSelected = pickWorker(oldWork1);
        Group workerEnemy = pickWorker(oldWork2);

        final Timeline timeline = setupTimeline(workerSelected,oldWork1,newWork1,500,0.8);
        final Timeline enemyTimeline = setupTimeline(workerEnemy,oldWork2, newWork2,500, 0.8);
        offsetTimeline.setCycleCount(1);
        offsetTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(250),
                new KeyValue (workerSelected.translateXProperty(), indexToCoordinateX(oldWork1.getX()))));

        SequentialTransition sequentialTransition = new SequentialTransition(offsetTimeline, enemyTimeline);
        ParallelTransition parallelTransition = new ParallelTransition(sequentialTransition, timeline);
        parallelTransition.play();

    }

    public Timeline setupTimeline(Group workerSelected, Worker oldWork, Worker newWork, int duration, double height){

        int oldHeight = mapCells[oldWork.getX()][oldWork.getY()].getBuilding();
        int newHeight = mapCells[newWork.getX()][newWork.getY()].getBuilding();

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        assert workerSelected != null;
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration),
                new KeyValue (workerSelected.translateXProperty(), indexToCoordinateX(newWork.getX()),Interpolator.EASE_OUT)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration),
                new KeyValue (workerSelected.translateZProperty(), indexToCoordinateY(newWork.getY()), Interpolator.EASE_OUT)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis((double)duration/2),
                new KeyValue (workerSelected.translateYProperty(), -max(deltaToCoordinateZ(0,oldHeight), deltaToCoordinateZ(0,newHeight))-height, Interpolator.EASE_OUT)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration),
                new KeyValue (workerSelected.translateYProperty(), -deltaToCoordinateZ(0,newHeight), Interpolator.EASE_BOTH)));
        return timeline;
    }

    /***
     * Update the 3d scene for worker placement
     * @param worker the worker reference
     */
    public void placeWorkerUpdate(Worker worker){
        Group workerGroup = null;

        final Timeline timeline = new Timeline();

        switch (worker.getPlayerColor()){

            case BLUE:
                switch(worker.getID()){
                    case 0:
                        workerGroup = loadModel(getClass().getResource("/3DGraphics/workerBlue.obj"));
                        workList.put("WB1", workerGroup);
                        break;
                    case 1:
                        workerGroup = loadModel(getClass().getResource("/3DGraphics/workerBlue.obj"));
                        workList.put("WB2", workerGroup);
                        break;
                }
                break;

            case RED:
                switch(worker.getID()){
                    case 0:
                        workerGroup = loadModel(getClass().getResource("/3DGraphics/workerRed.obj"));
                        workList.put("WR1", workerGroup);
                        break;
                    case 1:
                        workerGroup = loadModel(getClass().getResource("/3DGraphics/workerRed.obj"));
                        workList.put("WR2", workerGroup);
                        break;
                }
                break;

            case GREEN:
                switch(worker.getID()){
                    case 0:
                        workerGroup = loadModel(getClass().getResource("/3DGraphics/workerWhite.obj"));
                        workList.put("WW1", workerGroup);
                        break;
                    case 1:
                        workerGroup = loadModel(getClass().getResource("/3DGraphics/workerWhite.obj"));
                        workList.put("WW2", workerGroup);
                        break;
                }
                break;
        }

        assert workerGroup != null;
        workerGroup.setTranslateY(-20);
        workerGroup.setTranslateX(indexToCoordinateX(worker.getX()));
        workerGroup.setTranslateZ(indexToCoordinateY(worker.getY()));
        Group finalWorkerGroup = workerGroup;
        Platform.runLater(() -> matchSceneGroup.getChildren().add(finalWorkerGroup));

        int level = mapCells[worker.getX()][worker.getY()].getBuilding();
        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                new KeyValue (workerGroup.translateYProperty(), indexToCoordinateZ(level), Interpolator.EASE_OUT)));
        timeline.play();
    }

    /***
     * Remove a worker from the map when a player has lost
     * @param worker the reference of the worker to be removed
     */
    public void removeWorker(Worker worker) {

        Group finalWorkerGroup = pickWorker(worker);
        Platform.runLater(() -> matchSceneGroup.getChildren().remove(finalWorkerGroup));
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
        if(gameMapUpdate.getLastActionX() == -1 && gameMapUpdate.getLastActionY() == -1) {
            return;
        }
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
                    if(newWorker1 == null) { // Remove the worker from the board
                        removeWorker(oldMap[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker());
                        return;
                    }
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
                    if(newWorker1 == null) { // Remove the worker from the board
                        removeWorker(oldMap[gameMapUpdate.getLastActionX()][gameMapUpdate.getLastActionY()].getWorker());
                        return;
                    }
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

    /***
     * Reload the full map on backup restore
     * @param gameMapUpdate the map reference
     */
    public void fullMapUpdate(GameMapUpdate gameMapUpdate) {
        mapCells = gameMapUpdate.getGameMap();

        for(int i=0; i<=4; i++) {
            for(int j=0; j<=4; j++) {
                // Load the buildings

                Cell cell = gameMapUpdate.getGameMap()[i][j];
                for (int k = 0; k <= cell.getBuilding(); k++) {
                    Group block = null;
                    final Timeline timeline = new Timeline();


                    if(k==1) {
                        block = loadModel(getClass().getResource("/3DGraphics/BuildingBlock01.obj"));
                    } else if (k==2) {
                        block = loadModel(getClass().getResource("/3DGraphics/BuildingBlock02.obj"));
                    } else if (k==3) {
                        block = loadModel(getClass().getResource("/3DGraphics/BuildingBlock03.obj"));
                    }

                    if(block != null) {
                        block.setTranslateY(-20);
                        block.setTranslateX(indexToCoordinateX(i));
                        block.setTranslateZ(indexToCoordinateY(j));
                        Group finalBlock = block;
                        Platform.runLater(() -> matchSceneGroup.getChildren().add(finalBlock));

                        timeline.setCycleCount(1);
                        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                                new KeyValue (block.translateYProperty(), 0,Interpolator.EASE_OUT)));
                        timeline.play();
                    }
                }

                if(cell.getDome()) {
                    buildUpdate(i, j);
                }

                if(mapCells[i][j].getWorker() != null) {
                    placeWorkerUpdate(mapCells[i][j].getWorker());
                }
            }
        }
    }

    /***
     * Set the match as started
     * @param matchStarted always true
     */
    public void setMatchStarted(boolean matchStarted) {
        this.matchStarted = matchStarted;
    }

    /***
     * Show the build possibilities of the player on the 3d scene
     * @param buildList the list of possible builds
     */
    public void showBuildList(BuildList buildList) {
        ArrayList<Point3D> coordinates = new ArrayList<>();
        for (Direction dir : buildList.getBuildlist()) {
            int xIndex = DirectionManagement.getX(buildList.getWorker().getX(), dir);
            int yIndex = DirectionManagement.getY(buildList.getWorker().getY(), dir);
            if(xIndex != -1 && yIndex != -1) { // If not out of bounds
                double newX = indexToCoordinateX(xIndex);
                double newZ = indexToCoordinateY(yIndex);
                double newY = indexToCoordinateZ(mapCells[DirectionManagement.getX(buildList.getWorker().getX(), dir)][DirectionManagement.getY(buildList.getWorker().getY(), dir)].getBuilding());
                coordinates.add(new Point3D(newX, newY, newZ));
            }
        }
        buildColor(coordinates);
    }

    /***
     * Manage the player build choice
     * @param buildList the list of possible build directions
     */
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
                    clearColor();
                    matchScene.setOnMousePressed(e2 -> {
                        matchScene.requestFocus();
                        e2.consume();
                    });
                }
            }
            e.consume();
        });
    }

    /***
     * Manage the player move choice
     * @param moveList the list of the player possible moves
     */
    public void showMoveList(MoveList moveList) {
        myWorker1 = moveList.getWorker1();
        myWorker2 = moveList.getWorker2();
        Platform.runLater(() -> hintLabel.setText("Click on a worker"));

        ArrayList<Point3D> wCoordinates = new ArrayList<>();
        wCoordinates.add(new Point3D(indexToCoordinateX(myWorker1.getX()), indexToCoordinateZ(mapCells[myWorker1.getX()][myWorker1.getY()].getBuilding()), indexToCoordinateY(myWorker1.getY())));
        wCoordinates.add(new Point3D(indexToCoordinateX(myWorker2.getX()), indexToCoordinateZ(mapCells[myWorker2.getX()][myWorker2.getY()].getBuilding()), indexToCoordinateY(myWorker2.getY())));
        workerColor(wCoordinates);

        matchScene.setOnMousePressed(e -> {
            matchScene.requestFocus();
            int[] indexes = coordinateToIndex(e.getPickResult());
            if(indexes[0] == -1 || indexes[1] == -1) {
                // Click is out of bound
                return;
            }
            Worker worker;
            AtomicReference<ArrayList<Direction>> moves = new AtomicReference<>();
            AtomicReference<Integer> index = new AtomicReference<>();
            if(indexes[0] == myWorker1.getX() && indexes[1] == myWorker1.getY()) {
                clearColor();
                ArrayList<Point3D> cCoordinates = new ArrayList<>();
                cCoordinates.add(new Point3D(indexToCoordinateX(myWorker2.getX()), indexToCoordinateZ(mapCells[myWorker2.getX()][myWorker2.getY()].getBuilding()), indexToCoordinateY(myWorker2.getY())));
                workerColor(cCoordinates);
                moves.set(moveList.getMoveList1());
                worker = moveList.getWorker1();
                index.set(0);
            } else if(indexes[0] == myWorker2.getX() && indexes[1] == myWorker2.getY()) {
                clearColor();
                ArrayList<Point3D> cCoordinates = new ArrayList<>();
                cCoordinates.add(new Point3D(indexToCoordinateX(myWorker1.getX()), indexToCoordinateZ(mapCells[myWorker1.getX()][myWorker1.getY()].getBuilding()), indexToCoordinateY(myWorker1.getY())));
                workerColor(cCoordinates);
                moves.set(moveList.getMoveList2());
                worker = moveList.getWorker2();
                index.set(1);
            } else {
                return;
            }
            Platform.runLater(() -> hintLabel.setText("Move your worker"));
            showSingleMoveList(moveList, indexes[0], indexes[1]);
            AtomicReference<Worker> finalWorker = new AtomicReference<>(worker);
            matchScene.setOnMousePressed(e2 -> {
                matchScene.requestFocus();
                int[] newIndexes = coordinateToIndex(e2.getPickResult());
                if(newIndexes[0] == -1 || newIndexes[1] == -1) {
                    // Click is out of bound
                    return;
                }
                if((newIndexes[0] == moveList.getWorker1().getX() && newIndexes[1] == moveList.getWorker1().getY())) {
                    clearColor();
                    ArrayList<Point3D> cCoordinates = new ArrayList<>();
                    cCoordinates.add(new Point3D(indexToCoordinateX(myWorker2.getX()), indexToCoordinateZ(mapCells[myWorker2.getX()][myWorker2.getY()].getBuilding()), indexToCoordinateY(myWorker2.getY())));
                    workerColor(cCoordinates);
                    finalWorker.set(moveList.getWorker1());
                    moves.set(moveList.getMoveList1());
                    showSingleMoveList(moveList, newIndexes[0], newIndexes[1]);
                    index.set(0);
                    return;
                } else if((newIndexes[0] == moveList.getWorker2().getX() && newIndexes[1] == moveList.getWorker2().getY())) {
                    clearColor();
                    ArrayList<Point3D> cCoordinates = new ArrayList<>();
                    cCoordinates.add(new Point3D(indexToCoordinateX(myWorker1.getX()), indexToCoordinateZ(mapCells[myWorker1.getX()][myWorker1.getY()].getBuilding()), indexToCoordinateY(myWorker1.getY())));
                    workerColor(cCoordinates);
                    finalWorker.set(moveList.getWorker2());
                    moves.set(moveList.getMoveList2());
                    showSingleMoveList(moveList, newIndexes[0], newIndexes[1]);
                    index.set(1);
                    return;
                }

                for(Direction direction : moves.get()) {
                    int newX = DirectionManagement.getX(finalWorker.get().getX(), direction);
                    int newY = DirectionManagement.getY(finalWorker.get().getY(), direction);

                    if(newIndexes[0] == newX && newIndexes[1] == newY) { // Found the valid direction
                        socket.sendMessage(new MoveReceiver(direction, index.get()));
                        clearColor();
                        matchScene.setOnMousePressed(e3 -> {
                            matchScene.requestFocus();
                            e3.consume();
                        });
                    }
                }
                e2.consume();
            });
            e.consume();
        });
    }

    /***
     * Show the possible moves of a player given the list and the worker original position
     * @param moveList the list of possible move directions
     * @param x the x worker coordinate
     * @param y the y worker coordinate
     */
    private void showSingleMoveList(MoveList moveList, int x, int y) {
        ArrayList<Point3D> coordinates = new ArrayList<>();
        if(moveList.getWorker1().getX() == x && moveList.getWorker1().getY() == y) {
            for (Direction dir : moveList.getMoveList1()) {
                int xIndex = DirectionManagement.getX(moveList.getWorker1().getX(), dir);
                int yIndex = DirectionManagement.getY(moveList.getWorker1().getY(), dir);
                if(xIndex != -1 && yIndex != -1) { // If not out of bounds
                    double newX = indexToCoordinateX(xIndex);
                    double newZ = indexToCoordinateY(yIndex);
                    double newY = indexToCoordinateZ(mapCells[DirectionManagement.getX(moveList.getWorker1().getX(), dir)][DirectionManagement.getY(moveList.getWorker1().getY(), dir)].getBuilding());
                    coordinates.add(new Point3D(newX, newY, newZ));
                }
            }
        } else if(moveList.getWorker2().getX() == x && moveList.getWorker2().getY() == y) {
            for (Direction dir : moveList.getMoveList2()) {
                int xIndex = DirectionManagement.getX(moveList.getWorker2().getX(), dir);
                int yIndex = DirectionManagement.getY(moveList.getWorker2().getY(), dir);
                if(xIndex != -1 && yIndex != -1) { // If not out of bounds
                    double newX = indexToCoordinateX(xIndex);
                    double newZ = indexToCoordinateY(yIndex);
                    double newY = indexToCoordinateZ(mapCells[DirectionManagement.getX(moveList.getWorker2().getX(), dir)][DirectionManagement.getY(moveList.getWorker2().getY(), dir)].getBuilding());
                    coordinates.add(new Point3D(newX, newY, newZ));
                }
            }
        } else {
            System.err.println("Click is not in a cell occupied by a worker");
            return;
        }
        moveColor(coordinates);
    }

    /***
     * Manage a build where a dome can be built at any level
     * @param buildList the list of possible build directions
     */
    public void atlasBuild(AtlasBuildList buildList) {
        Platform.runLater(() -> label1.setText("Dome"));
        showBuildList(buildList);
        Platform.runLater(() -> hintLabel.setText("Choose where to build!"));
        button1.setOnMousePressed(e -> {
            if(button1.getImage().getUrl().equals(getClass().getResource("/2DGraphics/GreenButton.png").toString())){
                Image image = new Image("/2DGraphics/RedButton.png");
                Platform.runLater(() -> button1.setImage(image));
            } else {
                Image image1 = new Image("/2DGraphics/GreenButton.png");
                Platform.runLater(() -> button1.setImage(image1));
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

                if(newIndexes[0] == newX && newIndexes[1] == newY && button1.getImage().getUrl().equals(getClass().getResource("/2DGraphics/GreenButton.png").toString())) { // Found the valid direction
                    socket.sendMessage(new AtlasBuildReceiver(direction, true));
                    button1.setOnMousePressed(e2 -> { });
                    clearColor();
                    matchScene.setOnMousePressed(e2 -> {
                        matchScene.requestFocus();
                        e2.consume();
                    });
                    Image image = new Image("/2DGraphics/RedButton.png");
                    Platform.runLater(() -> button1.setImage(image));
                } else if(newIndexes[0] == newX && newIndexes[1] == newY && button1.getImage().getUrl().equals(getClass().getResource("/2DGraphics/RedButton.png").toString())){
                    socket.sendMessage(new AtlasBuildReceiver(direction, false));
                    button1.setOnMousePressed(e2 -> { });
                    clearColor();
                    matchScene.setOnMousePressed(e2 -> {
                        matchScene.requestFocus();
                        e2.consume();
                    });
                    Image image = new Image("/2DGraphics/RedButton.png");
                    Platform.runLater(() -> button1.setImage(image));
                    Platform.runLater(() -> label1.setText(""));
                }
            }
            e1.consume();
        });
    }

    /***
     * Enable the end turn command
     */
    public void showEndTurn() {
        Platform.runLater(() -> hintLabel.setText("End your turn!"));
        Image image = new Image("/2DGraphics/GreenButton.png");
        Platform.runLater(() -> button2.setImage(image));
        button2.setOnMousePressed(e -> {
            Image image1 = new Image("/2DGraphics/RedButton.png");
            Platform.runLater(() -> button2.setImage(image1));
            Platform.runLater(() -> hintLabel.setText("Wait for your turn"));
            socket.sendMessage(new EndTurnReceiver());
            matchScene.setOnMousePressed(e2 -> {
                matchScene.requestFocus();
                e2.consume();
            });
            button2.setOnMousePressed(e2 -> { });
            e.consume();
        });
    }

    /***
     * Manage the placement of the workers into the map
     */
    public void placeWorkerInit() {
        Platform.runLater(() -> hintLabel.setText("Place the first worker"));
        matchScene.setOnMousePressed(e -> {
            matchScene.requestFocus();
            int[] indexes1 = coordinateToIndex(e.getPickResult());
            if(indexes1[0] == -1 || indexes1[1] == -1 || mapCells[indexes1[0]][indexes1[1]].getWorker() != null) {
                // Click is out of bound
                return;
            }
            ArrayList<Point3D> coordinates = new ArrayList<>();
            double newX = indexToCoordinateX(indexes1[0]);
            double newZ = indexToCoordinateY(indexes1[1]);
            double newY = indexToCoordinateZ(mapCells[indexes1[0]][indexes1[1]].getBuilding());
            coordinates.add(new Point3D(newX, newY, newZ));
            placeWorkerColor(coordinates);
            Platform.runLater(() -> hintLabel.setText("Place the second worker"));
            matchScene.setOnMousePressed(e2 -> {
                matchScene.requestFocus();
                int[] indexes2 = coordinateToIndex(e2.getPickResult());
                if(indexes2[0] == -1 || indexes2[1] == -1 || (indexes2[0] == indexes1[0] && indexes2[1] == indexes1[1]) || mapCells[indexes2[0]][indexes2[1]].getWorker() != null) {
                    // Click is out of bound or is the same position of the first worker
                    Platform.runLater(() -> hintLabel.setText("Place the second worker"));
                    return;
                }
                Platform.runLater(() -> hintLabel.setText("Wait your turn"));
                socket.sendMessage(new WorkerReceiver(indexes1[0], indexes1[1], indexes2[0], indexes2[1]));
                clearColor();
                matchScene.setOnMousePressed(e3 -> {
                    matchScene.requestFocus();
                    e3.consume();
                });
                e2.consume();
            });
            e.consume();
        });
    }

    /***
     * Manage a turn where the player can build both before and after the move
     * @param prometheusBuildList the list of possible builds before the move
     */
    public void prometheusBuildShow(PrometheusBuildList prometheusBuildList) {
        ArrayList<Point3D> coordinates = new ArrayList<>();
        for (Direction dir : prometheusBuildList.getBuildlist1()) {
            int xIndex = DirectionManagement.getX(prometheusBuildList.getWorker1().getX(), dir);
            int yIndex = DirectionManagement.getY(prometheusBuildList.getWorker1().getY(), dir);
            if(xIndex != -1 && yIndex != -1) { // If not out of bounds
                double newX = indexToCoordinateX(xIndex);
                double newZ = indexToCoordinateY(yIndex);
                double newY = indexToCoordinateZ(mapCells[DirectionManagement.getX(prometheusBuildList.getWorker1().getX(), dir)][DirectionManagement.getY(prometheusBuildList.getWorker1().getY(), dir)].getBuilding());
                coordinates.add(new Point3D(newX, newY, newZ));
            }
        }
        for (Direction dir : prometheusBuildList.getBuildlist2()) {
            int xIndex = DirectionManagement.getX(prometheusBuildList.getWorker2().getX(), dir);
            int yIndex = DirectionManagement.getY(prometheusBuildList.getWorker2().getY(), dir);
            if(xIndex != -1 && yIndex != -1) { // If not out of bounds
                double newX = indexToCoordinateX(xIndex);
                double newZ = indexToCoordinateY(yIndex);
                double newY = indexToCoordinateZ(mapCells[DirectionManagement.getX(prometheusBuildList.getWorker2().getX(), dir)][DirectionManagement.getY(prometheusBuildList.getWorker2().getY(), dir)].getBuilding());
                coordinates.add(new Point3D(newX, newY, newZ));
            }
        }
        buildColor(coordinates);

        Platform.runLater(() -> hintLabel.setText("Move or Build?"));
        Image image = new Image("/2DGraphics/GreenButton.png");
        Platform.runLater(() -> label1.setText("Move"));
        Platform.runLater(() -> button1.setImage(image));
        Platform.runLater(() -> label2.setText("Build"));
        Platform.runLater(() -> button2.setImage(image));

        button1.setOnMousePressed(e -> {
            socket.sendMessage(new PrometheusBuildReceiver(null));
            Image image2 = new Image("/2DGraphics/RedButton.png");
            Platform.runLater(() -> {label2.setText("End Turn");
            label1.setText("");
            button1.setImage(image2);
            button2.setImage(image2);});
            button1.setOnMousePressed(e2 -> { });
            button2.setOnMousePressed(e2 -> { });
            clearColor();
            e.consume();
        });

        button2.setOnMousePressed(e -> {
            button1.setOnMousePressed(e2 -> { });
            button2.setOnMousePressed(e2 -> { });
            Platform.runLater(() -> {
                label1.setText("");
                Image image3 = new Image("/2DGraphics/RedButton.png");
                button1.setImage(image3);
                button2.setImage(image3);
            });
            matchScene.requestFocus();
            Platform.runLater(() -> hintLabel.setText("Choose the worker that has  to build!"));
            myWorker1 = prometheusBuildList.getWorker1();
            myWorker2 = prometheusBuildList.getWorker2();
            clearColor();
            ArrayList<Point3D> wCoordinates = new ArrayList<>();
            wCoordinates.add(new Point3D(indexToCoordinateX(myWorker1.getX()), indexToCoordinateZ(mapCells[myWorker1.getX()][myWorker1.getY()].getBuilding()), indexToCoordinateY(myWorker1.getY())));
            wCoordinates.add(new Point3D(indexToCoordinateX(myWorker2.getX()), indexToCoordinateZ(mapCells[myWorker2.getX()][myWorker2.getY()].getBuilding()), indexToCoordinateY(myWorker2.getY())));
            workerColor(wCoordinates);
            matchScene.setOnMousePressed(e3 -> {
                matchScene.requestFocus();
                int[] indexes = coordinateToIndex(e3.getPickResult());
                if(indexes[0] == -1 || indexes[1] == -1) {
                    // Click is out of bound
                    return;
                }
                if(indexes[0] == myWorker1.getX() && indexes[1] == myWorker1.getY()) {
                    socket.sendMessage(new PrometheusBuildReceiver(0));
                    clearColor();
                    matchScene.setOnMousePressed(e4 -> {
                        matchScene.requestFocus();
                        e4.consume();
                    });
                } else if(indexes[0] == myWorker2.getX() && indexes[1] == myWorker2.getY()) {
                    socket.sendMessage(new PrometheusBuildReceiver(1));
                    clearColor();
                    matchScene.setOnMousePressed(e4 -> {
                        matchScene.requestFocus();
                        e4.consume();
                    });
                } else {
                    return;
                }
                Platform.runLater(() -> label2.setText("End Turn"));
                e3.consume();
            });
            e.consume();
        });
    }

    /***
     * Manage a move where the source of the movement is a single worker
     * @param singleMoveList the direction of possible moves of the worker
     */
    public void singleMoveUpdate(SingleMoveList singleMoveList) {
        MoveList moveList = new MoveList(singleMoveList.getMoveList(), null, singleMoveList.getWorker(), null);
        showSingleMoveList(moveList, singleMoveList.getWorker().getX(), singleMoveList.getWorker().getY());

        matchScene.setOnMousePressed(e1 -> {
            matchScene.requestFocus();
            int[] newIndexes = coordinateToIndex(e1.getPickResult());
            if(newIndexes[0] == -1 || newIndexes[1] == -1) {
                // Click is out of bound
                return;
            }
            for(Direction direction : singleMoveList.getMoveList()) {
                int newX = DirectionManagement.getX(singleMoveList.getWorker().getX(), direction);
                int newY = DirectionManagement.getY(singleMoveList.getWorker().getY(), direction);

                if(newIndexes[0] == newX && newIndexes[1] == newY) { // Found the valid direction
                    socket.sendMessage(new MoveReceiver(direction, singleMoveList.getWorkerID()));
                    clearColor();

                    button1.setOnMousePressed(e2 -> { });
                    matchScene.setOnMousePressed(e2 -> {
                        matchScene.requestFocus();
                        e2.consume();
                    });
                    Image image = new Image("/2DGraphics/RedButton.png");
                    Platform.runLater(() -> button1.setImage(image));
                }
            }
            e1.consume();
        });

        if(singleMoveList.isOptional()) {
            Platform.runLater(() -> label1.setText("Skip Move"));
            Image image = new Image("/2DGraphics/GreenButton.png");
            Platform.runLater(() -> button1.setImage(image));
            Platform.runLater(() -> hintLabel.setText("Select move or skip"));
            button1.setOnMousePressed(e -> {
                Image image2 = new Image("/2DGraphics/RedButton.png");
                Platform.runLater(() -> button1.setImage(image2));
                socket.sendMessage(new MoveReceiver(null, singleMoveList.getWorkerID()));
                button1.setOnMousePressed(e2 -> { });
                clearColor();
                matchScene.setOnMousePressed(e2 -> {
                    matchScene.requestFocus();
                    e2.consume();
                });
                e.consume();
            });
        } else {
            Platform.runLater(() -> hintLabel.setText("Select move"));
            button1.setOnMousePressed(e -> { });
        }
    }

    /***
     * Update the player data into the sidebar
     * @param players the list of players into the game
     */
    public void updatePlayers(ArrayList<PlayerData> players) {
        if (players.size() == 2) {
            for (PlayerData playerData : players) {
                updateSinglePlayerData(playerData);
                Platform.runLater(() -> {
                    nick3.setVisible(false);
                    divinity3.setVisible(false);
                    border3.setVisible(false);
                    color3.setVisible(false);
                });
            }
        } else if (players.size() == 3) {
            for (PlayerData playerData : players) {
                updateSinglePlayerData(playerData);
                if (playerData.getPlayOrder() == 2) {
                    if (!playerData.getLost()) {
                        Platform.runLater(() -> {
                            nick3.setText(playerData.getPlayerID());
                            if(playerData.getDivinity() != null) {
                                Platform.runLater(() -> {
                                    Image image2 = new Image("/2DGraphics/" + playerData.getDivinity() + ".png");
                                    divinity3.setImage(image2);
                                });
                            }
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

    /***
     * Update the data of a single player into the sidebar
     * @param playerData the player data
     */
    private void updateSinglePlayerData(PlayerData playerData) {
        if (playerData.getPlayOrder() == 0) {
            if (!playerData.getLost()) {
                Platform.runLater(() -> {
                    nick1.setText(playerData.getPlayerID());
                    if(playerData.getDivinity() != null) {
                        Platform.runLater(() -> {
                            Image image = new Image("/2DGraphics/" + playerData.getDivinity() + ".png");
                            divinity1.setImage(image);
                        });
                    }
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
            color1.setStyle("-fx-background-color: #ff0000;");
        }
        if (playerData.getPlayOrder() == 1) {
            if (!playerData.getLost()) {
                Platform.runLater(() -> {
                    nick2.setText(playerData.getPlayerID());
                    if(playerData.getDivinity() != null) {
                        Platform.runLater(() -> {
                            Image image1 = new Image("/2DGraphics/" + playerData.getDivinity() + ".png");
                            divinity2.setImage(image1);
                        });
                    }
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
    }

    /***
     * Manage a build where the player can skip the build phase
     * @param buildList the list of possible builds
     */
    public void optionalBuildUpdate(BuildListFlag buildList) {
        showBuildList(buildList);
        Platform.runLater(()->
                label1.setText("Skip Build")
                );
        Platform.runLater(()->
                hintLabel.setText("Choose where to build!")
        );
        Image image = new Image("/2DGraphics/GreenButton.png");
        button1.setImage(image);

        button1.setOnMousePressed(e -> {
            Image image2 = new Image("/2DGraphics/RedButton.png");
            button1.setImage(image2);
            clearColor();
            socket.sendMessage(new BuildReceiver(null));
            button1.setOnMousePressed(e2 -> { });
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
                    clearColor();
                    matchScene.setOnMousePressed(e2 -> {
                    });
                    Image image2 = new Image("/2DGraphics/RedButton.png");
                    button1.setImage(image2);
                    matchScene.setOnMousePressed(e2 -> {
                        matchScene.requestFocus();
                        e2.consume();
                    });
                }
            }
            e.consume();
        });

    }

    /***
     * Manage cell colouring on move cells
     * @param coordinates the list of the cells to color
     */
    private void moveColor(ArrayList<Point3D> coordinates) {
        for(Point3D coordinate : coordinates) {
            Group plane1 = loadModel(getClass().getResource("/3DGraphics/moveIndicator.obj"));
            Group plane2 = loadModel(getClass().getResource("/3DGraphics/moveIndicator2.obj"));
            /*
            Box box = new Box(2.5,1.5,2.5);
            PhongMaterial mat = new PhongMaterial();
            mat.setDiffuseColor(Color.RED);
            Group placeholder = new Group(box);
            box.setMaterial(mat);
            placeholder.setOpacity(0.0);

            Group plane1 = loadModel(getClass().getResource("/3DGraphics/moveIndicator.obj"));
            Group plane2 = loadModel(getClass().getResource("/3DGraphics/moveIndicator2.obj"));
            plane2.setVisible(false);

            onMouseOver(placeholder,plane1,plane2);
            Group plane = new Group();
            plane.getChildren().add(plane1);
            plane.getChildren().add(plane2);

            plane.setScaleX(0.9);
            plane.setScaleZ(0.9);
            plane.setTranslateX(coordinate.getX());
            plane.setTranslateY(coordinate.getY()-0.15);
            plane.setTranslateZ(coordinate.getZ());
            placeholder.setScaleX(0.9);
            placeholder.setScaleZ(0.9);
            placeholder.setTranslateX(coordinate.getX());
            placeholder.setTranslateY(coordinate.getY()-0.15);
            placeholder.setTranslateZ(coordinate.getZ());
            Platform.runLater(() -> {
                planes.getChildren().add(plane);
                planes.getChildren().add(placeholder);
            });
             */
            Group plane = setUpPlanes(coordinate, plane1, plane2);
            planesTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(0), new KeyValue(plane.translateYProperty(),coordinate.getY()-0.15,Interpolator.EASE_BOTH)) );
            planesTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(600), new KeyValue(plane.translateYProperty(),coordinate.getY()-0.7,Interpolator.EASE_BOTH)) );

        }

        Platform.runLater(() -> {
            if(!matchSceneGroup.getChildren().contains(planes)) {
                planesTimeline.play();
                matchSceneGroup.getChildren().add(planes);
            }
        });

        /*
        FadeTransition ft = new FadeTransition(Duration.millis(1000), planes);
        ft.setFromValue(10);
        ft.setToValue(0.1);
        ft.setCycleCount(Timeline.INDEFINITE);
        //ft.setAutoReverse(true);
        ParallelTransition pt = new ParallelTransition(timeline, ft);
         */
    }

    private Group setUpPlanes(Point3D coordinate, Group plane1, Group plane2){

        Box box = new Box(2.5,1.5,2.5);
        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseColor(Color.RED);
        Group placeholder = new Group(box);
        box.setMaterial(mat);
        placeholder.setOpacity(0.0);

        plane2.setVisible(false);

        onMouseOver(placeholder,plane1,plane2);
        Group plane = new Group();
        plane.getChildren().add(plane1);
        plane.getChildren().add(plane2);

        plane.setScaleX(0.9);
        plane.setScaleZ(0.9);
        plane.setTranslateX(coordinate.getX());
        plane.setTranslateY(coordinate.getY()-0.15);
        plane.setTranslateZ(coordinate.getZ());
        placeholder.setScaleX(0.9);
        placeholder.setScaleZ(0.9);
        placeholder.setTranslateX(coordinate.getX());
        placeholder.setTranslateY(coordinate.getY()-0.15);
        placeholder.setTranslateZ(coordinate.getZ());
        Platform.runLater(() -> {
            planes.getChildren().add(plane);
            planes.getChildren().add(placeholder);
        });
        return plane;
    }

    /***
     * Manage cell colouring on build cells
     * @param coordinates the list of the cells to color
     */
    private void buildColor(ArrayList<Point3D> coordinates) {
        for(Point3D coordinate : coordinates) {
            Group plane1 = loadModel(getClass().getResource("/3DGraphics/buildIndicator.obj"));
            Group plane2 = loadModel(getClass().getResource("/3DGraphics/buildIndicator2.obj"));
            /*
            Box box = new Box(2.5,1,2.5);
            PhongMaterial mat = new PhongMaterial();
            mat.setDiffuseColor(Color.RED);
            Group placeholder = new Group(box);
            box.setMaterial(mat);
            placeholder.setOpacity(0.0);


            plane2.setVisible(false);

            onMouseOver(placeholder,plane1,plane2);
            Group plane = new Group();
            plane.getChildren().add(plane1);
            plane.getChildren().add(plane2);

            placeholder.setScaleX(0.9);
            placeholder.setScaleZ(0.9);
            placeholder.setTranslateX(coordinate.getX());
            placeholder.setTranslateY(coordinate.getY()-0.15);
            placeholder.setTranslateZ(coordinate.getZ());
            plane.setScaleX(0.9);
            plane.setScaleZ(0.9);
            plane.setTranslateX(coordinate.getX());
            plane.setTranslateY(coordinate.getY()-0.15);
            plane.setTranslateZ(coordinate.getZ());
            Platform.runLater(() -> {
                planes.getChildren().add(plane);
                planes.getChildren().add(placeholder);
            });
             */
            Group plane = setUpPlanes(coordinate, plane1, plane2);

            planesTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(0), new KeyValue(plane.scaleZProperty(), 0.8,Interpolator.EASE_BOTH)));
            planesTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(0), new KeyValue(plane.scaleXProperty(), 0.8,Interpolator.EASE_BOTH)));
            planesTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(600), new KeyValue(plane.scaleZProperty(), 0.15,Interpolator.EASE_BOTH)));
            planesTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(600), new KeyValue(plane.scaleXProperty(), 0.15,Interpolator.EASE_BOTH)));
        }
        /*
        FadeTransition ft = new FadeTransition(Duration.millis(1000), planes);
        ft.setFromValue(10.0);
        ft.setToValue(0.1);
        ft.setCycleCount(Timeline.INDEFINITE);
        //ft.setAutoReverse(true);
        ParallelTransition pt = new ParallelTransition(planesTimeline, ft);
        */
        Platform.runLater(() -> {
            if(!matchSceneGroup.getChildren().contains(planes)) {
                planesTimeline.play();
                matchSceneGroup.getChildren().add(planes);
            }
        });

    }

    /***
     * Manage cell colouring on worker cells
     * @param coordinates the list of the cells to color
     */
    public void workerColor(ArrayList<Point3D> coordinates) {
        for(Point3D coordinate : coordinates) {
            Box box = new Box(2.5,1.5,2.5);
            PhongMaterial mat = new PhongMaterial();
            mat.setDiffuseColor(Color.RED);
            Group placeholder = new Group(box);
            box.setMaterial(mat);
            placeholder.setOpacity(0.0);

            Group plane1 = loadModel(getClass().getResource("/3DGraphics/circleBluIndicator.obj"));
            Group plane2 = loadModel(getClass().getResource("/3DGraphics/buildIndicator.obj"));
            plane2.setVisible(false);
            onMouseOver(placeholder,plane1,plane2);
            Group circle = new Group();
            circle.getChildren().add(plane1);
            circle.getChildren().add(plane2);

            placeholder.setScaleX(0.9);
            placeholder.setScaleZ(0.9);
            placeholder.setTranslateX(coordinate.getX());
            placeholder.setTranslateY(coordinate.getY()-0.15);
            placeholder.setTranslateZ(coordinate.getZ());

            circle.setScaleX(0.9);
            circle.setScaleZ(0.9);
            circle.setTranslateX(coordinate.getX());
            circle.setTranslateY(coordinate.getY()-0.15);
            circle.setTranslateZ(coordinate.getZ());
            planesTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(0), new KeyValue(circle.translateYProperty(),coordinate.getY()-0.15,Interpolator.EASE_BOTH)) );
            planesTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(600), new KeyValue(circle.translateYProperty(),coordinate.getY()-0.7,Interpolator.EASE_BOTH)) );
            Platform.runLater(() -> {
                planes.getChildren().add(circle);
                planes.getChildren().add(placeholder);
            });
        }

        Platform.runLater(() -> {
            if(!matchSceneGroup.getChildren().contains(planes)) {
                planesTimeline.play();
                matchSceneGroup.getChildren().add(planes);
            }
        });
    }

    /***
     * Manage cell colouring on worker init cells
     * @param coordinates the list of the cells to color
     */
    public void placeWorkerColor(ArrayList<Point3D> coordinates) {
        for(Point3D coordinate : coordinates) {
            Group circle = loadModel(getClass().getResource("/3DGraphics/circleBluIndicator.obj"));

            circle.setScaleX(0.9);
            circle.setScaleZ(0.9);
            circle.setTranslateX(coordinate.getX());
            circle.setTranslateY(coordinate.getY()-0.15);
            circle.setTranslateZ(coordinate.getZ());
            planesTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(0), new KeyValue(circle.translateYProperty(),coordinate.getY()-0.15)) );
            planesTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(600), new KeyValue(circle.translateYProperty(),coordinate.getY()-0.7)) );
            Platform.runLater(() -> {
                planes.getChildren().add(circle);
            });
        }

        Platform.runLater(() -> {
            if(!matchSceneGroup.getChildren().contains(planes)) {
                planesTimeline.play();
                matchSceneGroup.getChildren().add(planes);
            }
        });
    }

    /***
     * Clear all the cell color
     */
    private void clearColor() {
        planesTimeline.stop();
        planesTimeline.getKeyFrames().clear();
        planes.getChildren().clear();
        Platform.runLater(() -> matchSceneGroup.getChildren().remove(planes));
    }

    /***
     * Set bottom bar label when a player has lost
     */
    public void setLabelOnLost(){
        Platform.runLater(() -> {
            hintLabel.setText("You have lost!");
            label2.setText("");
        });
    }

    /***
     * Convert the cell x index into the 3d coordinate
     * @param index cell index
     * @return the 3d coordinate
     */
    private static double indexToCoordinateX(int index){
        return (index-2)*DELTA;
    }

    /***
     * Convert the cell y index into the 3d coordinate
     * @param index cell index
     * @return the 3d coordinate
     */
    private static double indexToCoordinateY(int index){
        return (index-2)*DELTA*(-1);
    }

    /***
     * Convert the building level into 3d coordiante
     * @param index building level
     * @return the 3d coordinate
     */
    private static double indexToCoordinateZ(int index){
        switch (index) {
            case 0:
                return 0;
            case 1:
                return -DELTAZ1;
            case 2:
                return -DELTAZ2;
            case 3:
                return -DELTAZ3;
        }
        return -1;
    }

    /***
     * Return the 3d coordinate delta between two building levels
     * @param oldLevel old building level
     * @param newLevel new building level
     * @return the 3d coordinate of the delta
     */
    private static double deltaToCoordinateZ(int oldLevel, int newLevel) {
        double oldHeight, newHeight;
        switch (oldLevel) {
            case 0:
                oldHeight = 0;
                break;
            case 1:
                oldHeight = DELTAZ1;
                break;
            case 2:
                oldHeight = DELTAZ2;
                break;
            case 3:
                oldHeight = DELTAZ3;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + oldLevel);
        }
        switch (newLevel) {
            case 0:
                newHeight = 0;
                break;
            case 1:
                newHeight = DELTAZ1;
                break;
            case 2:
                newHeight = DELTAZ2;
                break;
            case 3:
                newHeight = DELTAZ3;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + newLevel);
        }
        return newHeight-oldHeight;
    }

    /***
     * Convert the building level into 3d coordiante
     * @param level building level
     * @return the 3d coordinate
     */
    public static double levelToCoordZ(int level) {
        switch (level) {
            case 0:
                return 0;
            case 1:
                return -DELTAZ1;
            case 2:
                return -DELTAZ2;
            case 3:
                return -DELTAZ3;
        }
        return -1;
    }

    /***
     * Return the cell coordinates of a click into the 3d space
     * @param pick the 3d coordinates
     * @return the cell indexes, size of the array is 3, indexes are x-y-z
     */
    private static int[] coordinateToIndex(PickResult pick) {
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

    /***
     * Set correct object visible on mouse hovering
     * @param placeholder placeholder reference
     * @param node1 top node reference
     * @param node2 bottom node reference
     */
    public void onMouseOver(Node placeholder, Node node1, Node node2) {
        placeholder.setOnMouseEntered(me -> {
            node1.setVisible(false);
            node2.setVisible(true);

        });

        placeholder.setOnMouseExited(me -> {
            node1.setVisible(true);
            node2.setVisible(false);
        });
    }
}
