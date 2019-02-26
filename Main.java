package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Автошкола NightSkyCorp");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        primaryStage.show();
    }

    //Метод смены сцен
    public static void changeScene(Stage stage,String s){
        stage.close();
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setTitle("Автошкола NightSkyCorp");
        s = s+".fxml";
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
        stage.show();
    }

    //открытие дополнительного окна
    public static void openStage(String s){
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setTitle("Автошкола NightSkyCorp");
        s = s+".fxml";
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
        stage.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
