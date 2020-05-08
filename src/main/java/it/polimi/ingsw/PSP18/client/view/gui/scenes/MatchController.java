package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;
import it.polimi.ingsw.PSP18.networking.messages.toclient.GameMapUpdate;
import it.polimi.ingsw.PSP18.server.model.Cell;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.model.Worker;
import javafx.animation.KeyValue;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
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

    private Cell[][] mapCells;
    private boolean followMessage = false, standardMove = true, matchStarted = false;
    private Worker newWorker1, newWorker2, oldWorker1, oldWorker2;

    private ArrayList<Direction> directionList1, directionList2;
    private Worker worker1, worker2;
    private int workerID;

    private static final int WIDTH= 1280;
    private static final int HEIGHT= 720;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "Match";

        //import models
        Group map = loadModel(getClass().getResource("/3DGraphics/mappa.obj"));
        Group cliff = loadModel(getClass().getResource("/3DGraphics/cliff.obj"));
        Group lava = loadModel(getClass().getResource("/3DGraphics/lava.obj"));
        Group walls = loadModel(getClass().getResource("/3DGraphics/mura.obj"));
        //Group islands = loadModel(getClass().getResource("/3DGraphics/isole.obj"));
        Group redWorker1 = loadModel(getClass().getResource("/3DGraphics/MaleBuilder.obj"));

        //setup Scene and camera
        matchSceneGroup.getChildren().add(lava);
        matchSceneGroup.getChildren().add(redWorker1);
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

    public void buildUpdate( int oldX, int oldY, Boolean dome){

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
     * @param gameMapUpdate Contains contains the new map, the last diretction, the last x and y coordinate and a boolean that signals if the move is a build or a move
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
                buildUpdate(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY(), true);
            } else {
                buildUpdate(gameMapUpdate.getLastActionX(), gameMapUpdate.getLastActionY(), false);
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
}
