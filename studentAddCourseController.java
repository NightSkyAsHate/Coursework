package sample;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class studentAddCourseController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Course, String> hoursTable;

    @FXML
    private TableColumn<Course, String> nameTable;

    @FXML
    private TableColumn<Course, String> thirdNameTable;

    @FXML
    private TableColumn<Course, String> dateTable;

    @FXML
    private Button toBar;

    @FXML
    private Button fullScreen;

    @FXML
    private Button exit;

    @FXML
    private TableColumn<Course, String> idTable;

    @FXML
    private TableView<Course> coursaCatalog;

    @FXML
    private Button backButton;

    @FXML
    private Button joinButton;

    @FXML
    private TableColumn<Course, String> secondNameTable;

    @FXML
    private AnchorPane first;

    @FXML
    private TableColumn<Course, String> categoryTable;

    @FXML
    void draged(ActionEvent event) {

    }

    @FXML
    void pressed(ActionEvent event) {

    }

    private double x,y;
    private ObservableList<Course> courseData = FXCollections.observableArrayList();

    @FXML
    void initialize() throws IOException {
        showAllCourses();
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
        backButton.setOnAction(event -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Main.changeScene(stage,"mainMenu_S");
        });
        joinButton.setOnAction(event -> {
            if (coursaCatalog.getSelectionModel().getSelectedItem() != null) {
                try {
                    if (Course.checkValidCourseToAddStudent(coursaCatalog.getSelectionModel().getSelectedItem())){
                        if (User.checkPossibilityToJoinYou(coursaCatalog.getSelectionModel().getSelectedItem())) {
                            if (User.checkUser(coursaCatalog.getSelectionModel().getSelectedItem())) {
                                Course.addCourseToFileUser(coursaCatalog.getSelectionModel().getSelectedItem());
                                Assessment.writeToAssessment(coursaCatalog.getSelectionModel().getSelectedItem());
                                Controller.setId(coursaCatalog.getSelectionModel().getSelectedItem());
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.initOwner(null);
                                alert.setTitle("Регистрация");
                                alert.setHeaderText("Успешная записаь");
                                alert.setContentText("Вы успешно записались на курс");
                                alert.showAndWait();
                                Stage stage = (Stage) backButton.getScene().getWindow();
                                Main.changeScene(stage,"mainMenu_S");
                            }else{
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.initOwner(null);
                                alert.setTitle("Ошибка");
                                alert.setHeaderText("Ошибка записи на курс");
                                alert.setContentText("Вы уже записаны на данный курс");
                                alert.showAndWait();
                            }
                        }else{
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.initOwner(null);
                            alert.setTitle("Ошибка");
                            alert.setHeaderText("Ошибка записи на курс");
                            alert.setContentText("Вы не можете записаться на 3 и более курсов");
                            alert.showAndWait();
                        }
                    }else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.initOwner(null);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Ошибка записи на курс");
                        alert.setContentText("На выбраном вами курсе уже нет доступных мест");
                        alert.showAndWait();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(null);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка записи на курс");
                alert.setContentText("Пожалуйства выберите один из представленных курсов");
                alert.showAndWait();
            }
        });
    }

    public void draged(MouseEvent mouseEvent) {
        Stage stage = (Stage)((javafx.scene.Node)mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX()-x);
        stage.setY(mouseEvent.getScreenY()-y);
    }

    public void pressed(MouseEvent mouseEvent) {
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    //Показывает все курсы в таблице
    public void showAllCourses() throws IOException {
        courseData.removeAll();
        File file = new File("src\\sample\\System\\courses.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        String[] logink = new String[7];
        while (s != null){
            logink = s.split("&");
            String[] ark = logink[6].split("!");
            Course course = new Course(Integer.parseInt(logink[0]) , Integer.parseInt(logink[1]) , logink[2] , logink[3] , logink[4] , logink[5] , LocalDate.of(Integer.parseInt(ark[0]) , Integer.parseInt(ark[1]), Integer.parseInt(ark[2])));
            courseData.add(course);
            idTable.setCellValueFactory(new PropertyValueFactory<>("id"));
            secondNameTable.setCellValueFactory(new PropertyValueFactory<>("secondNameTeacher"));
            nameTable.setCellValueFactory(new PropertyValueFactory<>("nameTeacher"));
            thirdNameTable.setCellValueFactory(new PropertyValueFactory<>("thirdNameTeacher"));
            hoursTable.setCellValueFactory(new PropertyValueFactory<>("hours"));
            categoryTable.setCellValueFactory(new PropertyValueFactory<>("category"));
            dateTable.setCellValueFactory(new PropertyValueFactory<>("date"));
            coursaCatalog.setItems(courseData);
            s = reader.readLine();
        }
    }
}

