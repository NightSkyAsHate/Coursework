package sample;

import java.io.*;

public class User {
    String login , password , name , secondName , thirdName , access;//логин и пароль пользователя, а также
    //его имя фамилия имя и отчество, access-это обозначение уровня доступа пользователя
    int[] course;//курсы, на которые записан пользователь

    //Конструктор админа
    public User(String login, String password, String access) {
        this.login = login;
        this.password = password;
        this.access = access;
    }

    public User() {
    }

    //Конструктор для студента и преподавателя
    public User(String login, String password, String name, String secondName, String thirdName, String access, int[] course) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.access = access;
        this.course = course;
    }

    //Конструктор для журнала
    public User(String login, String name, String secondName, String thirdName) {
        this.login = login;
        this.name = name;
        this.secondName = secondName;
        this.thirdName = thirdName;
    }

    //Конструктор для авторизации
    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public int[] getCourse() {
        return course;
    }

    public void setCourse(int[] course) {
        this.course = course;
    }

    //Вход пользователя
    public String checkUser() throws IOException {
        File file = new File("src\\sample\\System\\users.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        String[] logink = new String[7];
        while (s != null){
            logink = s.split("&");
            if (logink[0].equals(this.login) && logink[1].equals(this.password)){
                this.name = logink[2];
                this.secondName = logink[3];
                this.thirdName = logink[4];
                this.access = logink[5];
                int[] arr = new int[2];
                String[] ark = logink[6].split("!");
                arr[0] = Integer.parseInt(ark[0]);
                arr[1] = Integer.parseInt(ark[1]);
                this.course = arr;
                return logink[5];
            }
            s = reader.readLine();
        }
        return "ошибка";
    }

    //Добавление нового пользователя(в файл)
    public static void addNewUser(String[] arr) throws IOException {
        File file = new File("src\\sample\\System\\users.txt");
        FileWriter fw = new FileWriter(file,true);
        String a = "&";
        fw.write(arr[0] + a + arr[1] + a + arr[2] + a + arr[3] + a + arr[4] + a + arr[5]+ a + "0!0" +"\n");
        fw.flush();
        fw.close();
    }

    //Проверка на то, есть ли такой логин уже в системе
    public static boolean checkLogin(String string) throws IOException {
        File file = new File("src\\sample\\System\\users.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        String[] logink = new String[7];
        while (s != null){
            logink = s.split("&");
            if (logink[0].equals(string)){
                return false;
            }
            s = reader.readLine();
        }
        return true;
    }

    //Получить массив Id курсов на которые записан студент
    public int[] returnCourses() throws IOException {
        File file = new File("src\\sample\\System\\users.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        String[] logink;
        int[] ark = null;
        while (s != null){
            logink = s.split("&");
            if (logink[0].equals(Controller.getInstance().getLogin())){
                ark = new int[2];
                String[] kk = logink[6].split("!");
                ark[0] = Integer.parseInt(kk[0]);
                ark[1] = Integer.parseInt(kk[1]);
            }
            s = reader.readLine();
        }
        return ark;
    }

    //Проверка на возможность записи на курс
    public static boolean checkPossibilityToJoinYou(Course course) throws IOException {
        File file = new File("src\\sample\\System\\users.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        String[] logink;
        while (s != null){
            logink = s.split("&");
            if (Controller.getInstance().getLogin().equals(logink[0])) {
                String[] ark = logink[6].split("!");
                if (ark[0].equals("0") || ark[1].equals("0")) {
                    return true;
                }
            }
            s = reader.readLine();
        }
        return false;
    }

    //Проверка на то, записан ли студент на данный курс или нет
    public static boolean checkUser(Course course) throws IOException {
        File file = new File("src\\sample\\System\\users.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        String[] logink;
        while (s != null){
            logink = s.split("&");
            if (Controller.getInstance().getLogin().equals(logink[0])) {
                String[] ark = logink[6].split("!");
                if (ark[0].equals(String.valueOf(course.getId())) || ark[1].equals(String.valueOf(course.getId()))) {
                    return false;
                }
            }
            s = reader.readLine();
        }
        return true;
    }
}
