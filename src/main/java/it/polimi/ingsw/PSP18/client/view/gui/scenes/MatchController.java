package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import javafx.animation.KeyValue;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

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
        Group gameMap = loadModel(getClass().getResource("Board #19612.obj"));
        Group sea = loadModel(getClass().getResource("Sea.obj"));
        Group Cliff = loadModel(getClass().getResource("Cliff.obj"));
        PhongMaterial mapMaterial = new PhongMaterial();

        //setup Scene and camera
        Group root = new Group(gameMap);
        root.getChildren().add(sea);
        root.getChildren().add(Cliff);

        Camera camera= new PerspectiveCamera(true);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.AQUA);
        scene.setCamera(camera);

        camera.setNearClip(10);
        camera.setFarClip(5000);

        camera.translateXProperty().set(0);
        camera.translateYProperty().set(0);
        camera.translateZProperty().set(0);

        camera.translateZProperty().set(-100);

        Translate pivot = new Translate(0,0,100);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

        camera.getTransforms().addAll (
                pivot,
                yRotate,
                new Rotate(-20, Rotate.X_AXIS),
                new Translate(0, 0, -100)
        );

        //controller

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
            switch (event.getCode()){
                case RIGHT:
                    camera.getTransforms().addAll (
                            pivot,
                            new Rotate(20, Rotate.X_AXIS),
                            new Rotate(-45, Rotate.Y_AXIS),
                            new Rotate(-20, Rotate.X_AXIS),
                            new Translate(0, 0, -100)
                    );
                    break;
                case LEFT:
                    camera.getTransforms().addAll (
                            pivot,
                            new Rotate(20, Rotate.X_AXIS),
                            new Rotate(45, Rotate.Y_AXIS),
                            new Rotate(-20, Rotate.X_AXIS),
                            new Translate(0, 0, -100)
                    );
                    break;
            }
        });

        primaryStage.setTitle("main stage");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Group loadModel(URL url){

        Group modelRoot = new Group();
        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for(MeshView view : importer.getImport()){
            modelRoot.getChildren().add(view);
        }

        return modelRoot;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
