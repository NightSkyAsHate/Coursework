package sample;

import java.io.*;
import java.time.LocalDate;

public class Assessment {
    int idCourse;//Указывает на то, какому курсу принадлежит данная оценка
    String userId;//Указывает на то, кому принадлежит данная оценка
    String name, secondName, thirdName;//Имя, фамилия, отчество владельца данной оценки
    String assessment;//Сама оценка

    public Assessment(int idCourse, String userId, String name, String secondName, String thirdName) {
        this.idCourse = idCourse;
        this.userId = userId;
        this.name = name;
        this.secondName = secondName;
        this.thirdName = thirdName;
    }

    public Assessment() {
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getAssessment() {
        return assessment;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "idCourse=" + idCourse +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", thirdName='" + thirdName + '\'' +
                ", assessment='" + assessment + '\'' +
                '}';
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    //Получение фио для работы с журналом
    public static Assessment getAllInfo(String string,Course course) throws IOException {
        File file = new File("src\\sample\\System\\users.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        String[] logink;
        while (s != null){
            logink = s.split("&");
            if (logink[0].equals(string)){
                Assessment assessmentk = new Assessment(course.getId(),logink[0],logink[2],logink[3],logink[4]);
                return assessmentk;
            }
            s = reader.readLine();
        }
        return null;
    }

    //Получить оценку за определенную дату
    public static String getMarkForDefDate(Assessment assessment, Course course, LocalDate localDate) throws IOException {
        File file = new File("src\\sample\\System\\assessment.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        while (s != null){
            if (s.equals(String.valueOf(course.getId()))){
                s = reader.readLine();
                for (int i = 0 ; i < 10 ; i ++){
                    String[] logink = s.split(":");
                    if (assessment.getUserId().equals(logink[0]) && !logink[0].equals("0")){
                        String[] ark = logink[1].split("!");
                        LocalDate localDate1 = course.getDate();
                        localDate1 = localDate1.plusDays(7);
                        localDate1 = localDate1.plusDays(course.getHours());
                        int k = localDate1.getDayOfYear()- localDate.getDayOfYear();
                        if (k < 0) k += 365;
                        return ark[ark.length - k];
                    }
                    s = reader.readLine();
                }
            }else{
                for (int i = 0 ; i < 11 ; i++){
                    s = reader.readLine();
                }
            }
        }
        return null;
    }

    //Получение нижней границы времени
    public static LocalDate getDownBorder(Course course){
        return course.getDate().plusDays(7);
    }

    //Получение верхней границы времени
    public static LocalDate getTopBorder(Course course){
        return course.getDate().plusDays(7).plusDays(course.getHours());
    }

    //Выставление оценок в файл assessment.txt
    public static void placeMarksAssessment(Assessment assessment, Course course, LocalDate localDate,String mark) throws IOException {
        File filek = new File("src\\sample\\System\\assessment.txt");//определение файла filek как файл оценок
        Course.reWriteToTimely(filek);//Метод переписи во временный файл timely.txt
        File file = new File("src\\sample\\System\\timely.txt");//определение файла file как временный файл
        FileReader fr = new FileReader(file);//инициализиурем FileReader для файла timely.txt
        BufferedReader reader = new BufferedReader(fr);//инициализиурем BufferedReader для файла timely.txt
        FileWriter fw = new FileWriter(filek);//инициализируем FileWriter для файла assessment.txt
        String str = "";//Строка необхадивамая для записи всех оценок по мимо той что мы ставим
        String s = reader.readLine();//основная строка, относительно которой и проводим перепись
        while (s != null){//Проверка на то, что файл не закончился
            if (s.equals(String.valueOf(course.getId()))){//Проверка на то, тот ли это курс
                fw.write(s+"\n");//записываем id курса
                s = reader.readLine();//считываем следующую строку
                for (int i = 0 ; i < 10 ; i ++){//цикл из 10 раз
                    String[] logink = s.split(":");//сплитуется строка по символу :
                    if (assessment.getUserId().equals(logink[0]) && !logink[0].equals("0")){//если это там строка и и пользователь соответсвует
                        String[] ark = logink[1].split("!");//сплитует по датам
                        LocalDate localDate1 = course.getDate();//получаем дату курса
                        localDate1 = localDate1.plusDays(7);//прибавляем отсрочку
                        localDate1 = localDate1.plusDays(course.getHours());//прибавляем часы курсам, чтобы получить конечную дату курса
                        int k = localDate1.getDayOfYear()- localDate.getDayOfYear();//вычитаем дни недели чтобы получить конкретную дату за которую ставим оценку
                        if (k < 0) k += 365;//если так вышло что год начался другой и получилось отрицательное значение значит прибавляем 365
                        ark[ark.length - k] = mark;//найдя разницу присваиваем ей оценку
                        str = ark[0];//начинаем запись присваивая строке первое значение
                        for (int j = 1 ; j < ark.length ; j ++){
                            str += "!"+ark[j];//цикл прибавления и получения единой строки со всеми оценками
                        }
                        fw.write(logink[0]+":"+str+"\n");//запись той строки с переходом на след строку
                    }else{
                        fw.write(s+"\n");//если условие не выполнилось просто переписываем строку
                    }
                    s = reader.readLine();//считываем следующую строку
                }
            }else{
                for (int i = 0 ; i < 11 ; i++){//Если первое наше условие не выполнилось то просто пропускаем данныый курс
                    fw.write(s+"\n");
                    s = reader.readLine();
                }
            }
        }
        fw.flush();
        fw.close();
    }

    //Получить статистические значения
    public static double getStatisticOfCourse(Course course) throws IOException {
        File file = new File("src\\sample\\System\\assessment.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        int k = 0;
        double f = 0;
        while(s != null){
            if (s.equals(String.valueOf(course.getId()))){
                s = reader.readLine();
                for (int i = 0 ; i < 10 ; i ++){
                    String[] logink = s.split(":");
                    if (!logink[0].equals("0")){
                        String[] ark = logink[1].split("!");
                        for (int j = 0 ; j < ark.length ; j ++){
                            if (!ark[j].equals("0")){
                                if (ark[j].equals("1") || ark[j].equals("2") || ark[j].equals("3") || ark[j].equals("4") || ark[j].equals("5")){
                                    k++;
                                    f += Integer.valueOf(ark[j]);
                                }
                                if (ark[j].equals("6")){
                                    k++;
                                    f += 3;
                                }
                                if (ark[j].equals("7")){
                                    k++;
                                    f += 2;
                                }
                            }
                        }
                    }
                    s = reader.readLine();
                }
            }else{
                for (int i = 0 ; i < 11 ; i++){
                    s = reader.readLine();
                }
            }
        }
        return f/k;
    }

    //Получение среднего значения оценок за экзамен
    public static double getStatisticOfCourseExam(Course course) throws IOException {
        File file = new File("src\\sample\\System\\assessment.txt");
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        String s = reader.readLine();
        int k = 0;
        double f = 0;
        while(s != null){
            if (s.equals(String.valueOf(course.getId()))){
                s = reader.readLine();
                for (int i = 0 ; i < 10 ; i ++){
                    String[] logink = s.split(":");
                    if (!logink[0].equals("0")){
                        String[] ark = logink[1].split("!");
                        if (ark[ark.length-1].equals("1") || ark[ark.length-1].equals("2") || ark[ark.length-1].equals("3") || ark[ark.length-1].equals("4") || ark[ark.length-1].equals("5")){
                            k++;
                            f += Integer.valueOf(ark[ark.length-1]);
                        }
                        if (ark[ark.length-1].equals("6")){
                            k++;
                            f += 3;
                        }
                        if (ark[ark.length-1].equals("7")){
                            k++;
                            f += 2;
                        }
                    }
                    s = reader.readLine();
                }
            }else{
                for (int i = 0 ; i < 11 ; i++){
                    s = reader.readLine();
                }
            }
        }
        return f/k;
    }

    //Добавление информации о том, что записался студент в файл assessment.txt
    public static void writeToAssessment(Course course) throws IOException {
        File file = new File("src\\sample\\System\\assessment.txt");
        Course.reWriteToTimely(file);
        File filek = new File("src\\sample\\System\\timely.txt");
        FileReader fr = new FileReader(filek);
        BufferedReader reader = new BufferedReader(fr);
        FileWriter fw = new FileWriter(file);
        String s = reader.readLine();
        String[] logink;
        boolean flag = true;
        while (s != null){
            if (s.equals(String.valueOf(course.getId()))){
                fw.write(s+"\n");
                s = reader.readLine();
                for (int i = 0 ; i < 10 ; i ++) {
                    logink = s.split(":");
                    if (logink[0].equals("0") && flag) {
                        fw.write(Controller.getInstance().getLogin() + ":0");
                        String[] ark = logink[1].split("!");
                        for (int j = 0; j < ark.length - 1; j++) {
                            fw.write("!0");
                        }
                        fw.write("\n");
                        flag = false;
                    }else{
                        fw.write(s+"\n");
                    }
                    s = reader.readLine();
                }
            }else{
                for (int i = 0 ; i < 11 ; i++){
                    fw.write(s+"\n");
                    s = reader.readLine();
                }
            }
        }
        fw.flush();
        fw.close();
    }
}