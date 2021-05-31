import sudyar.commands.*;
import sudyar.data.StudyGroupCollection;
import sudyar.internet.Server;
import sudyar.utilities.FileParser;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        int port = 31174;
        String filePath = "Test.txt";

        String log = "log.txt";
        FileParser fileParser = null;
        if (args != null && args.length>0) {
            fileParser = getFile(args[0]);
        }
        while (fileParser == null){
            System.out.println("Хотите ввести другой путь до файла? Если нет, введите пустую строку");
            Scanner scan = new Scanner(System.in);
            String line = scan.nextLine();
            if ((line == null) || ("".equals(line))) {
                fileParser = new FileParser();
            } else fileParser = getFile(line.trim());
        }
        StudyGroupCollection collection;

        if (fileParser.canRead) {
            collection = fileParser.parse();
        }
        else collection = new StudyGroupCollection();

        Server server = new Server(collection,port, log);

        HashSet<Command> set1 = new HashSet<>();
        HashSet<Command> set2 = new HashSet<>();
        HashSet<Command> set3 = new HashSet<>();
        set1.add(new HelpCommand());
        set2.add(new HelpCommand());
        set1.add(new InfoCommand(collection));
        set1.add(new ShowCommand(collection));
        set3.add(new InsertCommand(collection));
        set3.add(new UpdateCommand(collection));
        set1.add(new RemoveKeyCommand(collection));
        set1.add(new ClearCommand(collection));
        set3.add(new RemoveGreaterCommand(collection));
        set3.add(new ReplaceIfLowe(collection));
        set1.add(new RemoveGreaterKeyCommand(collection));
        set1.add(new RemoveAllByFormOfEducationCommand(collection));
        set1.add(new SumOfStudentsCountCommand(collection));
        set1.add(new CountByStudentsCountCommand(collection));
        set1.add(new ExecuteScriptCommand());
        set2.add(new SaveCommand(collection, fileParser));
        set2.add(new ExitCommand(server));

        Commands clientCommands = new Commands(set1, set3);
        Commands serverCommands = new Commands(set2);

        server.run(clientCommands, serverCommands);
    }

    private static FileParser getFile (String filePath){
        try {
            FileParser newFile = new FileParser(filePath);
            if (!newFile.canRead) System.out.println("Из файла невозможно прочесть, он не подходит");
            else if (!newFile.canWrite) System.out.println("В файл нельзя записать, он не подходит");
            else if (!newFile.isFile) System.out.println("Это не файл, не подходит");
            else return newFile;
            return null;
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Файл не найден");
            return null;
        }

    }
}
