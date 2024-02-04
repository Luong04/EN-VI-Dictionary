package Commandline;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    private double x = 0;
    private double y = 0;
    public static  Stage stageRefer;

    DictionaryCommandLine dictionaryCommandLine = DictionaryCommandLine.getInstance();
    @Override
    public void start(Stage stage) throws IOException {
        stageRefer = stage;
        Parent root_load = FXMLLoader.load(getClass().getResource("LoadGui.fxml"));
        try {
            dictionaryCommandLine.dictionaryBasic();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } 
        Scene scene_load = new Scene(root_load,788,550); 
        stage.setScene(scene_load);
        stage.initStyle(StageStyle.TRANSPARENT);
        moveScreen(root_load,stage);
        stage.show();

        Parent root = FXMLLoader.load(getClass().getResource("DictionaryGui.fxml"));
        moveScreen(root,stage);

        Task<Scene> renderTask = new Task<>() {
            @Override
            protected Scene call() throws Exception {
                // Simulate a long-running task by pausing for a few seconds
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Create the main scene
                Scene scene = new Scene(root,1030,679);
                scene.setCamera(new PerspectiveCamera());
                return scene;
            }
        };
        renderTask.setOnSucceeded(event -> stage.setScene(renderTask.getValue()));
        moveScreen(root,stage);

        new Thread(renderTask).start();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void moveScreen(Parent root,Stage stage){
        final double[] x = new double[1];
        final double[] y = new double[1];
        root.setOnMousePressed(MouseEvent -> {
            x[0] = MouseEvent.getSceneX();
            y[0] = MouseEvent.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x[0]);
            stage.setY(event.getScreenY() - y[0]);

        });
    }
}