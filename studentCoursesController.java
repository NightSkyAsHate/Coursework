package sample;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.jws.soap.SOAPBinding;

public class studentCoursesController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label category2;

    @FXML
    private Label thirdName2;

    @FXML
    private Label secondName2;

    @FXML
    private Label name2;

    @FXML
    private Label name1;

    @FXML
    private Label thirdName1;

    @FXML
    private Label categotyLabel1;

    @FXML
    private Label secondName1;

    @FXML
    private Label number2;

    @FXML
    private Label number1;

    @FXML
    private Button qq;

    private Course course1,course2;

    @FXML
    void click1(MouseEvent event) {
        //2
        if (number2.getText().equals("X")){
            Stage stage = (Stage) qq.getScene().getWindow();
            Main.changeScene(stage,"studentAddCourse");
        }else{
            showCourseInfo(course2);
        }
    }

    @FXML
    void click2(MouseEvent event) {
        //1
        if (number1.getText().equals("X")){
            Stage stage = (Stage) qq.getScene().getWindow();
            Main.changeScene(stage,"studentAddCourse");
        }else{
            showCourseInfo(course1);
        }
    }

    @FXML
    void initialize() throws IOException {
        showCourses();
    }

    //Показ курсов а ячейках
    private void showCourses() throws IOException {
        File file = new File("src\\sample\\System\\courses.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        String[] logink;
        while (s != null){
            logink = s.split("&");
            if (logink[0].equals(String.valueOf(Controller.getInstance().getCourse()[0])) || logink[0].equals(String.valueOf(Controller.getInstance().getCourse()[1]))){
                if (categotyLabel1.getText().equals("Label")){
                    String[] ark = logink[6].split("!");
                    course1 = new Course(Integer.parseInt(logink[0]), Integer.parseInt(logink[1]) , logink[2] , logink[3] , logink[4] , logink[5] , LocalDate.of(Integer.parseInt(ark[0]) , Integer.parseInt(ark[1]) , Integer.parseInt(ark[2])));
                    categotyLabel1.setText(logink[5]);
                    secondName1.setText(logink[3]);
                    name1.setText(logink[2]);
                    thirdName1.setText(logink[4]);
                    number1.setText(logink[0]);
                }else{
                    String[] ark = logink[6].split("!");
                    course2 = new Course(Integer.parseInt(logink[0]), Integer.parseInt(logink[1]) , logink[2] , logink[3] , logink[4] , logink[5] , LocalDate.of(Integer.parseInt(ark[0]) , Integer.parseInt(ark[1]) , Integer.parseInt(ark[2])));
                    category2.setText(logink[5]);
                    secondName2.setText(logink[3]);
                    name2.setText(logink[2]);
                    thirdName2.setText(logink[4]);
                    number2.setText(logink[0]);
                }
            }
            if (Controller.getInstance().getCourse()[0] == 0){
                categotyLabel1.setText("X");
                secondName1.setText("Курс не выбрал");
                name1.setText("Курс не выбран");
                thirdName1.setText("Курс не выбран");
                number1.setText("X");
            }
            if (Controller.getInstance().getCourse()[1] == 0){
                category2.setText("Х");
                secondName2.setText("Курс не выбран");
                name2.setText("Курс не выбран");
                thirdName2.setText("Курс не выбран");
                number2.setText("X");
            }
            s = reader.readLine();
        }
    }

    private void showCourseInfo(Course course){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(CourseInfoStudentController.class.getResource("courseInfoStudent.fxml"));
        AnchorPane page = null;
        try{
            page = (AnchorPane) loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Курс NightSkyCorp");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(null);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        Scene scene = new Scene(page);
        stage.setScene(scene);
        CourseInfoStudentController controller = loader.getController();
        controller.setAllContent(stage,course);
        controller.showAllInfo();
        stage.show();
        Stage stage1 = (Stage) qq.getScene().getWindow();
        stage1.close();
    }
}