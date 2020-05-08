package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.server.model.Direction;
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
import java.util.ResourceBundle;

public class MatchController extends Controller {
    @FXML
    private SubScene matchScene;
    private Group matchSceneGroup = new Group();

    private int cameraDistance = 40;
    private int cameraXAngle = 45;
    private Translate pivot = new Translate(0,0,cameraDistance);
    private Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

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
    public void moveUpdate(Worker oldWork, Worker newWork){
        oldWork.getPlayerColor();
    }
    public void apolloMoveUpdate(Worker oldWork, Worker newWork){

    }
    public void minotaurMoveUpdate(Worker oldWork, Worker newWork){

    }
    public void placeWorkerUpdate(Worker worker){

    }
}
