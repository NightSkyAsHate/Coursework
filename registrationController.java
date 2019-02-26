package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class registrationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField nameText;

    @FXML
    private TextField secondNameText;

    @FXML
    private TextField thirdNameText;

    @FXML
    private TextField loginText;

    @FXML
    private TextField passwordText;

    @FXML
    private ComboBox<String> chooseUser;

    @FXML
    private TextArea areaText;

    @FXML
    private Button registr;

    ObservableList<String> list = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        list.add("Студент");
        list.add("Преподаватель");
        chooseUser.setItems(list);
        areaText.setWrapText(true);
        registr.setOnAction(event -> {
            String s = null;
            try {
                s = checkValidaality();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (s.equals("")){
                String[] arr = new String[6];
                arr[0] = loginText.getText();
                arr[1] = passwordText.getText();
                arr[2] = nameText.getText();
                arr[3] = secondNameText.getText();
                arr[4] = thirdNameText.getText();
                if (chooseUser.getSelectionModel().getSelectedItem().equals("Студент")){
                    arr[5] = "student";
                }else{
                    arr[5] = "teacher";
                }
                try {
                    User.addNewUser(arr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(null);
                alert.setTitle("Регистрация");
                alert.setHeaderText("Успешно создан");
                alert.setContentText("Пользователь был успешно создан");
                alert.showAndWait();
                loginText.setText("");
                passwordText.setText("");
                nameText.setText("");
                secondNameText.setText("");
                thirdNameText.setText("");
                chooseUser.getSelectionModel().select(null);
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(null);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка ввода");
                alert.setContentText(s);
                alert.showAndWait();
            }
        });
    }

    //Проверка на корректность ввода информации
    public String checkValidaality() throws IOException {
        String result = "Неправильно: ";
        if (!nameText.getText().matches("^[a-zA-Z]+$")) result += "имя пользователя ";
        if (!secondNameText.getText().matches("^[a-zA-Z]+$")) result += "фамилия пользователя ";
        if (!thirdNameText.getText().matches("^[a-zA-Z]+$")) result += "отчество пользователя ";
        if (!loginText.getText().matches("^[a-zA-Z0-9]+$")) result += "логин пользователя ";
        if (!passwordText.getText().matches("^[a-zA-Z0-9]+$")) result += "пароль пользователя ";
        if (chooseUser.getSelectionModel().getSelectedItem() == null) result += "выбран тип пользователя ";
        if (!User.checkLogin(loginText.getText())) result += "такой логин уже используется";
        if (result.equals("Неправильно: ")){
            return "";
        }else{
            return result;
        }
    }
}