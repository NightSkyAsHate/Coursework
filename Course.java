package sample;

import java.io.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Course {
    int id, hours;//Описан номер курса и количество часов
    String nameTeacher, secondNameTeacher, thirdNameTeacher, category;//Имя, фамилия, отчество и категория преподавателя
    LocalDate date;//Дата создания курса

    //Конструктор создания курса
    public Course(int id, int hours, String nameTeacher, String secondNameTeacher, String thirdNameTeacher, String category, LocalDate date) {
        this.id = id;
        this.hours = hours;
        this.nameTeacher = nameTeacher;
        this.secondNameTeacher = secondNameTeacher;
        this.thirdNameTeacher = thirdNameTeacher;
        this.category = category;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", hours=" + hours +
                ", nameTeacher='" + nameTeacher + '\'' +
                ", secondNameTeacher='" + secondNameTeacher + '\'' +
                ", thirdNameTeacher='" + thirdNameTeacher + '\'' +
                ", category='" + category + '\'' +
                ", date=" + date +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getNameTeacher() {
        return nameTeacher;
    }

    public void setNameTeacher(String nameTeacher) {
        this.nameTeacher = nameTeacher;
    }

    public String getSecondNameTeacher() {
        return secondNameTeacher;
    }

    public void setSecondNameTeacher(String secondNameTeacher) {
        this.secondNameTeacher = secondNameTeacher;
    }

    public String getThirdNameTeacher() {
        return thirdNameTeacher;
    }

    public void setThirdNameTeacher(String thirdNameTeacher) {
        this.thirdNameTeacher = thirdNameTeacher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    //Удаление курса из файла courses.txt
    public static void deleteCourse(String idCourse) throws IOException {
        File file = new File("src\\sample\\System\\courses.txt");
        reWriteToTimely(file);
        File filek = new File("src\\sample\\System\\timely.txt");
        FileReader fr = new FileReader(filek);
        BufferedReader reader = new BufferedReader(fr);
        FileWriter fw = new FileWriter(file);
        String s = reader.readLine();
        String[] logink = new String[7];
        while (s != null){
            logink = s.split("&");
            if (!logink[0].equals(idCourse)){
                fw.write(s+"\n");
            }
            s = reader.readLine();
        }
        fw.flush();
        fw.close();
    }

    //Удаление курса из файла users.txt
    public static void deleteCourseUsers(String idCourse) throws IOException {
        File file = new File("src\\sample\\System\\users.txt");
        reWriteToTimely(file);
        File filek = new File("src\\sample\\System\\timely.txt");
        FileReader fr = new FileReader(filek);
        BufferedReader reader = new BufferedReader(fr);
        FileWriter fw = new FileWriter(file);
        String s = reader.readLine();
        String[] logink = new String[7];
        while (s != null){
            logink = s.split("&");
            String[] ark = logink[6].split("!");
            if (ark[0].equals(idCourse)){
                fw.write(logink[0] + "&" + logink[1] + "&" + logink[2] + "&" + logink[3] + "&" + logink[4] + "&" + logink[5] + "&" + "0" + "!" + ark[1] + "\n");
            }else {
                if (ark[1].equals(idCourse)) {
                    fw.write(logink[0] + "&" + logink[1] + "&" + logink[2] + "&" + logink[3] + "&" + logink[4] + "&" + logink[5] + "&" + ark[0] + "!" + "0" + "\n");
                }else{
                    fw.write(s + "\n");
                }
            }
            s = reader.readLine();
        }
        fw.flush();
        fw.close();
    }

    //Удаление курса из файла assessment.txt
    public static void deleteCourseAssessment(String idCourse) throws IOException {
        File file = new File("src\\sample\\System\\assessment.txt");
        reWriteToTimely(file);
        File filek = new File("src\\sample\\System\\timely.txt");
        FileReader fr = new FileReader(filek);
        BufferedReader reader = new BufferedReader(fr);
        FileWriter fw = new FileWriter(file);
        String s = reader.readLine();
        while(s != null){
            if (!s.equals(idCourse)){
                fw.write(s+"\n");
                for (int i = 0 ; i < 10 ; i ++) {
                    s = reader.readLine();
                    fw.write(s + "\n");
                }
                s = reader.readLine();
            }else{
                for (int i = 0 ; i < 11 ; i ++){
                    s = reader.readLine();
                }
            }
        }
        fw.flush();
        fw.close();
    }

    //Перепись во временный файл
    public static void reWriteToTimely(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        File filek = new File("src\\sample\\System\\timely.txt");
        FileWriter fw = new FileWriter(filek);
        String s = reader.readLine();
        while (s != null){
            fw.write(s+"\n");
            s = reader.readLine();
        }
        fw.flush();
        fw.close();
    }

    //Проверка на возможность создания нового курса
    public static boolean checkPosibilityToCreateCourse() throws IOException {
        File file = new File("src\\sample\\System\\users.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        String[] logink = new String[7];
        while (s != null){
            logink = s.split("&");
            String[] ark = logink[6].split("!");
            if (Controller.getInstance().getLogin().equals(logink[0]) && (ark[0].equals("0") || ark[1].equals("0"))){
                return true;
            }
            s = reader.readLine();
        }
        return false;
    }

    //Автоматическая выборка номера курса при его создании
    public static int creatIdCourse() throws IOException {
        int k = 0;
        File file = new File("src\\sample\\System\\courses.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        String[] logink = new String[7];
        while (s != null){
            logink = s.split("&");
            if (Integer.parseInt(logink[0]) > k){
                k = Integer.parseInt(logink[0]);
            }
            s = reader.readLine();
        }
        return ++k;
    }

    //Добавление курса в файл courses.txt
    public static void addCourseToFile(Course course) throws IOException {
        File file = new File("src\\sample\\System\\courses.txt");
        FileWriter fw = new FileWriter(file,true);
        fw.write(String.valueOf(course.getId()) + "&" + String.valueOf(course.getHours()) + "&" + course.getNameTeacher() + "&" + course.getSecondNameTeacher() + "&" + course.getThirdNameTeacher() + "&" + course.getCategory() + "&" + course.getDate().getYear() + "!" + course.getDate().getMonthValue() + "!" + course.getDate().getDayOfMonth() + "\n");
        fw.flush();
        fw.close();
    }

    //Добавление курса в файл users.txt
    public static void addCourseToFileUser(Course course) throws IOException {
        File file = new File("src\\sample\\System\\users.txt");
        Course.reWriteToTimely(file);
        File filek = new File("src\\sample\\System\\timely.txt");
        FileReader fr = new FileReader(filek);
        BufferedReader reader = new BufferedReader(fr);
        FileWriter fw = new FileWriter(file);
        String s = reader.readLine();
        String[] logink = new String[7];
        boolean flag = true;
        while (s != null){
            logink = s.split("&");
            String[] ark = logink[6].split("!");
            if (logink[0].equals(Controller.getInstance().getLogin())) {
                if (ark[0].equals("0") && flag) {
                    fw.write(logink[0] + "&" + logink[1] + "&" + logink[2] + "&" + logink[3] + "&" + logink[4] + "&" + logink[5] + "&" + String.valueOf(course.getId()) + "!" + ark[1] + "\n");
                    flag = false;
                }else{
                    if (ark[1].equals("0") && flag) {
                        fw.write(logink[0] + "&" + logink[1] + "&" + logink[2] + "&" + logink[3] + "&" + logink[4] + "&" + logink[5] + "&" + ark[0] + "!" + String.valueOf(course.getId()) + "\n");
                        flag = false;
                    }
                }
            }else {
                fw.write(s + "\n");
            }
            s = reader.readLine();
        }
        fw.flush();
        fw.close();
    }

    //Добавление курса в файл assessment.txt
    public static void addCourseToAssessment(Course course) throws IOException {
        File file = new File("src\\sample\\System\\assessment.txt");
        FileWriter fw = new FileWriter(file,true);
        fw.write(String.valueOf(course.getId())+"\n");
        String s = "0";
        for (int i = 1 ; i < course.hours ; i ++){
            s += "!0";
        }
        for (int i = 0 ; i < 10 ; i ++){
            fw.write("0:"+s+"\n");
        }
        fw.flush();
        fw.close();
    }

    //Проверка на принадлежность курса преподавателю
    public static boolean checkOwner(Course course) throws IOException {
        File file = new File("src\\sample\\System\\users.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        String[] logink = new String[7];
        while (s != null){
            logink = s.split("&");
            String[] ark = logink[6].split("!");
            if (logink[0].equals(Controller.getInstance().getLogin())) {
                if (ark[0].equals(String.valueOf(course.getId()))) {
                    return true;
                }
                if (ark[1].equals(String.valueOf(course.getId()))) {
                    return true;
                }
            }
            s = reader.readLine();
        }
        return false;
    }

    //Проверка на вместимость курса
    public static boolean checkValidCourseToAddStudent(Course course) throws IOException {
        File file = new File("src\\sample\\System\\assessment.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        String[] logink;
        while (s != null){
            if (s.equals(String.valueOf(course.getId()))){
                s = reader.readLine();
                for (int i = 0 ; i < 10 ; i ++){
                    logink = s.split(":");
                    if (logink[0].equals("0")){
                        return true;
                    }
                    s = reader.readLine();
                }
            }else{
                for (int i = 0 ; i < 11 ; i ++){
                    s = reader.readLine();
                }
            }
        }
        return false;
    }
}
