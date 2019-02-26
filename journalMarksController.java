package sample;

import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

public class journalMarksController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Assessment, String> nameTable;

    @FXML
    private TableColumn<Assessment, String> thirdNameTable;

    @FXML
    private TextField thirdNameLabel;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField markLabel;

    @FXML
    private TableView<Assessment> catalogStudents;

    @FXML
    private Button changeMarkButton;

    @FXML
    private Button toBar;

    @FXML
    private Button fullScreen;

    @FXML
    private Button exit;

    @FXML
    private TextField secondNameLabel;

    @FXML
    private Button backButton;

    @FXML
    private TextField appcentyLabel;

    @FXML
    private TableColumn<Assessment, String> secondNameTable;

    @FXML
    private TableColumn<Assessment, String> markTable;

    @FXML
    private TextField nameLabel;

    @FXML
    private TextArea infoArea;

    @FXML
    void draged(ActionEvent event) {

    }

    @FXML
    void pressed(ActionEvent event) {

    }

    private double x, y;
    private Stage stage;
    private Course course;
    private ObservableList<Assessment> courseStudent = FXCollections.observableArrayList();
    private boolean flag;

    @FXML
    void initialize() throws IOException {
        infoArea.setWrapText(true);
        flag = false;
        Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        datePicker.setDayCellFactory(dayCellFactory);
        catalogStudents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showStudentDetail(newValue));
        datePicker.setOnAction(event -> {
            cleanTable();
            try {
                showAllStudents();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //Кнопка выйти
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
        backButton.setOnAction(event -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
            if (Controller.getInstance().getAccess().equals("admin")){
                Main.openStage("mainMenu_A");
            }else {
                Main.openStage("mainMenu_T");
            }
        });
        changeMarkButton.setOnAction(event -> {
            if (flag == false){
                flag = true;
                changeMarkButton.setText("Сохранить");
                markLabel.setEditable(true);
                infoArea.setVisible(true);
            }else{
                flag = false;
                changeMarkButton.setText("Изменить оценку");
                markLabel.setEditable(false);
                infoArea.setVisible(false);
                if (checkValidChanging()){
                    try {
                        Assessment.placeMarksAssessment(catalogStudents.getSelectionModel().getSelectedItem(),this.course,datePicker.getValue(),markLabel.getText());
                        cleanTable();
                        showAllStudents();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(null);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Ошибка ввода");
                    alert.setContentText("Была неправильно введена оценка");
                    alert.showAndWait();
                }
            }
        });
    }

    //Показ всех студентов курса в таблице
    public void showAllStudents() throws IOException {
        File file = new File("src\\sample\\System\\assessment.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        while (s != null) {
            if (s.equals(String.valueOf(this.course.getId()))) {
                s = reader.readLine();
                for (int i = 0; i < 10; i++) {
                    String[] logink = s.split(":");
                    if (!logink[0].equals("0")) {
                        Assessment assessment = Assessment.getAllInfo(logink[0], this.course);
                        courseStudent.add(assessment);
                        secondNameTable.setCellValueFactory(new PropertyValueFactory<>("secondName"));
                        nameTable.setCellValueFactory(new PropertyValueFactory<>("name"));
                        thirdNameTable.setCellValueFactory(new PropertyValueFactory<>("thirdName"));
                        if (datePicker.getValue() != null) {
                            if (Assessment.getMarkForDefDate(assessment, this.course, datePicker.getValue()).equals("0")) {
                                assessment.setAssessment("X");
                            } else {
                                assessment.setAssessment(Assessment.getMarkForDefDate(assessment, this.course, datePicker.getValue()));
                            }
                        }
                        markTable.setCellValueFactory(new PropertyValueFactory<>("assessment"));
                        catalogStudents.setItems(courseStudent);
                    }
                    s = reader.readLine();
                }
            } else {
                for (int i = 0; i < 11; i++) {
                    s = reader.readLine();
                }
            }
        }
    }

    //Установка сцены
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //Установка на сцене курса, относительно которого будут идти операции
    public void setItems(Course course) {
        this.course = course;
    }

    public void draged(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - x);
        stage.setY(mouseEvent.getScreenY() - y);
    }

    public void pressed(MouseEvent mouseEvent) {
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    //Отчистка таблицы
    public void cleanTable() {
        for (int i = 0; i < catalogStudents.getItems().size(); i++) {
            catalogStudents.getItems().clear();
        }
    }

    //Показывает дополнительную информацию о студенте
    private void showStudentDetail(Assessment assessment) {
        if (assessment != null) {
            secondNameLabel.setText(assessment.getSecondName());
            nameLabel.setText(assessment.getName());
            thirdNameLabel.setText(assessment.getThirdName());
            markLabel.setText(assessment.assessment);
            if (assessment.getAssessment() != null) {
                if (assessment.getAssessment().equals("X") || assessment.getAssessment().equals("7")) {
                    appcentyLabel.setText("Не присутствовал");
                } else {
                    appcentyLabel.setText("Присутствовал");
                }
            }
        } else {
            secondNameLabel.setText("");
            nameLabel.setText("");
            thirdNameLabel.setText("");
            markLabel.setText("");
            appcentyLabel.setText("");
        }
    }

    //Ограничение дат datePicker
    private Callback<DatePicker, DateCell> getDayCellFactory() {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.getDayOfYear() < Assessment.getDownBorder(course).getDayOfYear() &&
                                                        item.getDayOfYear() > Assessment.getTopBorder(course).getDayOfYear()-1) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        return dayCellFactory;
    }

    //Проверка на правильность изменения оценки
    private boolean checkValidChanging() {
        if (markLabel.getText().length() == 1 && markLabel.getText().matches("^[1-7]+$")){
            return true;
        }
        return false;
    }

    //Проверка на доступ
    public void checkOwner() throws IOException {
        if (!Course.checkOwner(this.course)){
            changeMarkButton.setDisable(true);
        }
    }
}
