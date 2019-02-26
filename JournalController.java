package sample;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JournalController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Course> coursaCatalog;

    @FXML
    private TableColumn<Course, String> idTable;

    @FXML
    private TableColumn<Course, String> secondNameTable;

    @FXML
    private TableColumn<Course, String> nameTable;

    @FXML
    private TableColumn<Course, String> thirdNameTable;

    @FXML
    private TableColumn<Course, String> hoursTable;

    @FXML
    private TableColumn<Course, String> categoryTable;

    @FXML
    private TableColumn<Course, LocalDate> dateTable;

    @FXML
    private Button showJournalButton;

    @FXML
    private Button addCourseButton;

    @FXML
    private Button deleteCourseButton;

    @FXML
    private ObservableList<Course> courseData = FXCollections.observableArrayList();
    Course course77;

    @FXML
    void initialize() throws IOException {
        showAllCourses();
        deleteCourseButton.setOnAction(event -> {
            if (coursaCatalog.getSelectionModel().selectedItemProperty() != null) {
                try {
                    if (Course.checkOwner(coursaCatalog.getSelectionModel().getSelectedItem())) {
                        event.consume();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Удаление");
                        alert.setHeaderText("Удаление курса");
                        alert.setContentText("Вы уверены?");
                        Optional<ButtonType> result = alert.showAndWait();
                        this.course77 = coursaCatalog.getSelectionModel().getSelectedItem();
                        if (result.get() == ButtonType.OK) {
                            try {
                                Course.deleteCourse(String.valueOf(course77.getId()));
                                Course.deleteCourseUsers(String.valueOf(course77.getId()));
                                Course.deleteCourseAssessment(String.valueOf(course77.getId()));
                                cleanTable();
                                showAllCourses();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Ошибка удаления");
                        alert.setContentText("Вы не можете удалить курс, если не являетесь его владельцем");
                        alert.showAndWait();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка удаления");
                alert.setContentText("Выберите курс, который хотите удалить");
                alert.showAndWait();
            }
        });
        addCourseButton.setOnAction(event -> {
            try {
                if (Course.checkPosibilityToCreateCourse()) {
                    Main.openStage("addCourse");
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Ошибка создания курса");
                    alert.setContentText("Вы не можете создать курс так как превышен лимит создания курсов");
                    alert.showAndWait();
                }
                cleanTable();
                showAllCourses();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        showJournalButton.setOnAction(event -> {
            if (coursaCatalog.getSelectionModel().getSelectedItem() != null){
                try {
                    showjournalMark(coursaCatalog.getSelectionModel().getSelectedItem());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка просмотра");
                alert.setContentText("Пожалуйста выберите курс из предложенного каталога");
                alert.showAndWait();
            }
        });
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

    //Отчистить таблицу
    public void cleanTable(){
        for ( int i = 0; i<coursaCatalog.getItems().size(); i++) {
            coursaCatalog.getItems().clear();
        }
    }

    //Открытие журнала(новой сцены)
    public void showjournalMark(Course course) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(journalMarksController.class.getResource("journalMarks.fxml"));
        AnchorPane page = null;
        try{
            page = (AnchorPane) loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Журнал NightSkyCorp");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(null);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        Scene scene = new Scene(page);
        stage.setScene(scene);
        journalMarksController controller = loader.getController();
        controller.setStage(stage);
        controller.setItems(course);
        controller.showAllStudents();
        controller.checkOwner();
        stage.show();
        Stage stage1 = (Stage) showJournalButton.getScene().getWindow();
        stage1.close();
    }
}
