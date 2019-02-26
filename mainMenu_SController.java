package sample;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class mainMenu_SController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Tab coursesJournal;

    @FXML
    private Button exit;

    @FXML
    private AnchorPane first;

    @FXML
    private Button toBar;

    @FXML
    private Button fullScreen;

    @FXML
    void draged(ActionEvent event) {

    }

    @FXML
    void pressed(ActionEvent event) {

    }
    private double x,y;

    @FXML
    void initialize() throws IOException {
        setContent("studentCours",coursesJournal);
        //Кнопка выхода
        exit.setOnAction(event -> {
            event.consume();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Выход");
            alert.setHeaderText("Выход из программы");
            alert.setContentText("Вы уверены?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) exit.getScene().getWindow();
                stage.close();
            }
        });
        //Кнопка свернуть
        toBar.setOnAction(event -> {
            Stage stage = (Stage) toBar.getScene().getWindow();
            stage.setIconified(true);
        });
        fullScreen.setOnAction(event -> {
            event.consume();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Выход");
            alert.setHeaderText("Выход из аккаунта");
            alert.setContentText("Вы уверены?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) fullScreen.getScene().getWindow();
                Main.changeScene(stage,"sample");
            }
        });

    }

    //2 метода для того чтобы передвигать программу
    public void draged(MouseEvent mouseEvent) {
        Stage stage = (Stage)((javafx.scene.Node)mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX()-x);
        stage.setY(mouseEvent.getScreenY()-y);
    }

    public void pressed(MouseEvent mouseEvent) {
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    //Вставка сцен во вкладки
    public void setContent(String s,Tab tab) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        s += ".fxml";
        loader.setLocation(getClass().getResource(s));
        tab.setContent(loader.load());
    }
}
