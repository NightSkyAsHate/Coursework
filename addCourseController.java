package sample;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class addCourseController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField hoursText;

    @FXML
    private TextField categoryText;

    @FXML
    private Button createButton;

    @FXML
    private Button closeButton;

    @FXML
    void initialize() {
        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
        createButton.setOnAction(event -> {
            if (checkValidInput()){
                try {
                    Course course = new Course(Course.creatIdCourse(),Integer.parseInt(hoursText.getText()), Controller.getInstance().getName(), Controller.getInstance().getSecondName(), Controller.getInstance().getThirdName(), categoryText.getText(), LocalDate.now());
                    Course.addCourseToFile(course);
                    Course.addCourseToFileUser(course);
                    Course.addCourseToAssessment(course);
                    Stage stage = (Stage) createButton.getScene().getWindow();
                    stage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка ввода");
                alert.setContentText("Ошибка в вводе количества часов");
                alert.showAndWait();
            }
        });
    }

    //Проверка на правильность ввода
    public boolean checkValidInput(){
        if (hoursText.getText().matches("^[0-9]+$") && !hoursText.getText().equals("") && !categoryText.getText().equals("")) return true;
        else return false;
    }
}
