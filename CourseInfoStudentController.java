package sample;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CourseInfoStudentController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exit;

    @FXML
    private Button fullScreen;

    @FXML
    private Button toBar;

    @FXML
    private TextField fullNameTeacherText;

    @FXML
    private DatePicker datepicker;

    @FXML
    private Label studentFullNameText;

    @FXML
    private TextField markText;

    @FXML
    private TextField posechText;

    @FXML
    private TextArea infoText;

    @FXML
    private Button backButton;

    @FXML
    void draged(MouseEvent event) {
        Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX()-x);
        stage.setY(event.getScreenY()-y);
    }

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    private double x,y;
    private Stage stage;
    private Course course;

    @FXML
    void initialize() {
        //Кнопка выхода
        Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        datepicker.setDayCellFactory(dayCellFactory);
        datepicker.setOnAction(event -> {
            try {
                showMark();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        infoText.setWrapText(true);
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

    }

    public void setAllContent(Stage stage,Course string){
        this.stage = stage;
        this.course = string;
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

    //Показ всей информации
    public void showAllInfo(){
        fullNameTeacherText.setText("Преподаватель: "+this.course.getSecondNameTeacher()+" "+this.course.getNameTeacher()+" "+this.course.getThirdNameTeacher());
        studentFullNameText.setText("Я "+Controller.getInstance().getSecondName()+" "+Controller.getInstance().getName()+" "+Controller.getInstance().getThirdName());
        infoText.setText("Ваш экзамен будет проходить "+Assessment.getTopBorder(course)+" постарайтесь чтобы сдать с первого раза :)"+"\n"+"Работает 5-ти бальная шкала(1-5 - оценки), если у вас стоит 6, значит вы были на занятии, но не получили оценку, 7 значит вы пропустили занятие");
    }

    //Показ оценки
    public void showMark() throws IOException {
        Assessment assessment = new Assessment();
        assessment.setUserId(Controller.getInstance().getLogin());
        String mark = Assessment.getMarkForDefDate(assessment,course,datepicker.getValue());
        if (mark.equals("0")){
            markText.setText("Преподаватель не проставил");
            posechText.setText("Преподаватель не поставил");
        }else{
            markText.setText(mark);
            if (mark.equals("1")||mark.equals("2")||mark.equals("3")||mark.equals("4")||mark.equals("5")||mark.equals("6")){
                posechText.setText("Присутствовал");
            }else{
                posechText.setText("Не присутствовал");
            }
        }
    }
}
