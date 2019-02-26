package sample;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exit;

    @FXML
    private TextField log;

    @FXML
    private PasswordField pass;

    @FXML
    private Button join;

    @FXML
    private AnchorPane first;

    @FXML
    private HBox dddd;

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

    private static volatile User user = null;
    private double x,y;

    @FXML
    void initialize() {
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
        //Кнопка войти
        join.setOnAction(event -> {
            user = new User(log.getText(),pass.getText());
            try {
                String s = user.checkUser();
                if (s.equals("ошибка")){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Ошибка входа");
                    alert.setContentText("Логин или пароль неправильны");
                    alert.showAndWait();
                }else{
                    Stage stage;
                    switch (s){
                        case "admin":
                            stage = (Stage) join.getScene().getWindow();
                            Main.changeScene(stage,"mainMenu_A");
                            break;
                        case "student":
                            stage = (Stage) join.getScene().getWindow();
                            Main.changeScene(stage,"mainMenu_S");
                            break;
                        case "teacher":
                            stage = (Stage) join.getScene().getWindow();
                            Main.changeScene(stage,"mainMenu_T");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //2 метода для перемещения окна зажав мышь на шапке программы
    public void draged(MouseEvent mouseEvent) {
        Stage stage = (Stage)((javafx.scene.Node)mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX()-x);
        stage.setY(mouseEvent.getScreenY()-y);
    }

    public void pressed(MouseEvent mouseEvent) {
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    //Доступ к user, для кода
    public static User getInstance() {
        if (user == null) {
            synchronized(User.class) {
                if (user == null) {
                    user = new User();
                }
            }
        }
        return user;
    }

    public static void setId(Course course){
        boolean flag = true;
        if (String.valueOf(user.getCourse()[0]).equals("0") && flag){
            user.getCourse()[0] = course.getId();
            flag = false;
        }
        if (String.valueOf(user.getCourse()[1]).equals("0") && flag){
            user.getCourse()[1] = course.getId();
            flag = false;
        }
    }
}