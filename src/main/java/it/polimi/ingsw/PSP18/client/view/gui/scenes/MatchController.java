package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import javafx.animation.KeyValue;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
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

public class MatchController extends Application {

    private static final int WIDTH= 1280;
    private static final int HEIGHT= 720;
   // @Override
   // public void initialize(URL location, ResourceBundle resources) {
    //    super.initialize(location, resources);
    //}
    @Override
    public void start(Stage primaryStage) throws Exception{

        //import models
        Group map = loadModel(getClass().getResource("/3DGraphics/mappa.obj"));
        Group cliff = loadModel(getClass().getResource("/3DGraphics/cliff.obj"));
        Group sea = loadModel(getClass().getResource("/3DGraphics/Sea.obj"));
        Group walls = loadModel(getClass().getResource("/3DGraphics/mura.obj"));
        Group islands = loadModel(getClass().getResource("/3DGraphics/isole.obj"));

        //setup Scene and camera
        Group root = new Group();
        root.getChildren().add(sea);
        root.getChildren().add(map);
        root.getChildren().add(walls);
        root.getChildren().add(cliff);
        root.getChildren().add(islands);
        PointLight pointLight = new PointLight();
        pointLight.getTransforms().addAll(new Translate(0,-50,0));
        root.getChildren().add(pointLight);

        Camera camera= new PerspectiveCamera(true);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.AQUA);
        scene.setCamera(camera);

        camera.setNearClip(1);
        camera.setFarClip(50);

        camera.translateXProperty().set(0);
        camera.translateYProperty().set(0);

        camera.translateZProperty().set(0);
        int cameraDistance = 30;
        int cameraXAngle = 20;

        camera.translateZProperty().set(-cameraDistance);

        Translate pivot = new Translate(0,0,cameraDistance);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

        camera.getTransforms().addAll (
                pivot,
                yRotate,
                new Rotate(-cameraXAngle, Rotate.X_AXIS),
                new Translate(0, 0, -cameraDistance)
        );

        //controller

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
            switch (event.getCode()){
                case RIGHT:
                    camera.getTransforms().addAll (
                            pivot,
                            new Rotate(cameraXAngle, Rotate.X_AXIS),
                            new Rotate(-45, Rotate.Y_AXIS),
                            new Rotate(-cameraXAngle, Rotate.X_AXIS),
                            new Translate(0, 0, -cameraDistance)
                    );
                    break;
                case LEFT:
                    camera.getTransforms().addAll (
                            pivot,
                            new Rotate(cameraXAngle, Rotate.X_AXIS),
                            new Rotate(45, Rotate.Y_AXIS),
                            new Rotate(-cameraXAngle, Rotate.X_AXIS),
                            new Translate(0, 0, -cameraDistance)
                    );
                    break;
            }
        });

        primaryStage.setTitle("GameON");
        primaryStage.setScene(scene);
        primaryStage.show();
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
    public static void main(String[] args) {
        launch(args);
    }
}
