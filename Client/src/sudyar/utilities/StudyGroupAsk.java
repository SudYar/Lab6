package sudyar.utilities;

import sudyar.data.*;
import sudyar.exception.DuplicateException;

import java.util.Locale;
import sudyar.internet.Client;

public class StudyGroupAsk {

    public StudyGroup getStudyGroup(Client client){

        String line;
        String name = null;
        System.out.print("Введите имя группы\n>");
        line = client.readLine();
        if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) {
            name = StudyGroupParser.parseName(line.trim());
        }
        while (name == null){
            System.out.print("Неверно введено имя, пожалуйста, введите непустую строку без разделяющих пробелов\n>");
            line = client.readLine();
            if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) {
                name = StudyGroupParser.parseName(line.trim());
            }
        }
        Double x = null;
        System.out.print("Введите x-ую координату типа double\n>");
        line = client.readLine();
        if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) x = StudyGroupParser.parseX(line.trim());
        while (x == null){
            System.out.print("Неверно введён x, пожалуйста, введите значение типа double\n>");
            line = client.readLine();
            if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) {
                x = StudyGroupParser.parseX(line.trim());
            }
        }

        Float y = null;
        System.out.print("Введите y-ую координату типа float, больше " + Coordinates.yMinValue+ "\n>");
        line = client.readLine();
        if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) y = StudyGroupParser.parseY(line.trim());
        while (y == null){
            System.out.print("Неверно введён y, пожалуйста, введите значение типа float, больше " + Coordinates.yMinValue+ "\n>");
            line = client.readLine();
            if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) {
                y = StudyGroupParser.parseY(line.trim());
            }
        }

        Integer studentCount = null;
        System.out.print("Введите количество студентов (натуральное число)\n>");
        line = client.readLine();
        if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) studentCount = StudyGroupParser.parseStudentsCount(line.trim());
        while (studentCount == null){
            System.out.print("Неверно введено количество студентов, пожалуйста, введите целое значение больше 0 \n>");
            line = client.readLine();
            if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) {
                studentCount = StudyGroupParser.parseStudentsCount(line.trim());
            }
        }

        FormOfEducation formOfEducation  = null;
        System.out.print("Введите форму обучения, возможные варианты: " + FormOfEducation.nameList() + "" +
                "; Либо введите null, чтобы не запоминать форму обучения\n>");
        line = client.readLine();
        if (!"NULL".equals(line.toUpperCase(Locale.ROOT))) {
            if (!"".equals(line.trim()) && line.trim().split(" ").length < 2)
                formOfEducation = StudyGroupParser.parseFormOfEducation(line.trim());
            while (formOfEducation == null) {
                System.out.print("Неверно введена форма обучения, возможные варианты: " + FormOfEducation.nameList() + "" +
                        "; Либо введите null, чтобы не запоминать форму обучения\n>");
                line = client.readLine();
                if ("NULL".equals(line.toUpperCase(Locale.ROOT))) break;
                if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) {
                    formOfEducation = StudyGroupParser.parseFormOfEducation(line.trim());
                }
            }
        }

        Semester semester = null;
        System.out.print("Введите семестр, возможные варианты: " + Semester.nameList() + "\n>");
        line = client.readLine();
        if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) semester = StudyGroupParser.parseSemesterEnum(line.trim());
        while (semester == null){
            System.out.print("Неверно введен семестр, возможные варианты: " + Semester.nameList() + "\n>");
            line = client.readLine();
            if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) {
                semester = StudyGroupParser.parseSemesterEnum(line.trim());
            }
        }

        Person groupAdmin = null;
        System.out.print("Хотите ли добавить админа группы? Введите +/- ");
        line = client.readLine();
        while (!"+".equals(line) && !"-".equals(line)) {
            System.out.print("Пожалуйста, введите только + или -, которые означают хотите ли вы добавить админа группы или нет соответственно ");
            line = client.readLine();
        }
        if ("+".equals(line)) {

            String nameAdmin = null;
            System.out.print("Введите имя админа группы, не может быть пустым и содержать пробелы\n>");
            line = client.readLine();
            if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) nameAdmin = StudyGroupParser.parseNameAdmin(line.trim());
            while (nameAdmin == null){
                System.out.print("Неверно введено имя админа группы, оно не может быть пустым и содержать пробелы\n>");
                line = client.readLine();
                if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) {
                    nameAdmin = StudyGroupParser.parseNameAdmin(line.trim());
                }
            }

            Double weigh = null;
            System.out.print("Введите вес админа типа double, больше 0\n>");
            line = client.readLine();
            if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) weigh = StudyGroupParser.parseWeigh(line.trim());
            while (weigh == null){
                System.out.print("Неверно введен вес админа группы, он должен быть типа double, больше 0\n>");
                line = client.readLine();
                if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) {
                    weigh = StudyGroupParser.parseWeigh(line.trim());
                }
            }

            String passportId = null;
            System.out.print("Введите passportId админа (непустая уникальная строка без пробелов)\n>");
            line = client.readLine();
            try {
                if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) {
                    passportId = StudyGroupParser.parsePassportId(line.trim());
                }
            } catch (DuplicateException e){
                System.out.print(e.getMessage()+"\n");
            }
            while (passportId == null){
                System.out.print("Неверно введен passportId админа (должна быть непустая уникальная строка без пробелов)\n>");
                line = client.readLine();
                try {
                    if (!"".equals(line.trim()) && line.trim().split(" ").length < 2) {
                        passportId = StudyGroupParser.parsePassportId(line.trim());
                    }
                } catch (DuplicateException e){
                    System.out.print(e.getMessage()+"\n");
                }
            }
            groupAdmin = StudyGroupParser.parsePerson(nameAdmin, weigh, passportId);
        }
        return new StudyGroup(name,new Coordinates(x, y), studentCount, formOfEducation, semester, groupAdmin);

    }

}
