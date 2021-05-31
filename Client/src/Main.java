import sudyar.commands.*;
import sudyar.data.StudyGroupCollection;
import sudyar.internet.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {

        HashSet<Command> set1 = new HashSet<>();
        HashSet<Command> set2 = new HashSet<>();
        StudyGroupCollection collection = null;
        set1.add(new HelpCommand());
        set1.add(new InfoCommand(collection));
        set1.add(new ExitCommand());
        set1.add(new ShowCommand(collection));
        set2.add(new InsertCommand(collection));
        set2.add(new UpdateCommand(collection));
        set1.add(new RemoveKeyCommand(collection));
        set1.add(new ClearCommand(collection));
        set2.add(new RemoveGreaterCommand(collection));
        set2.add(new ReplaceIfLowe(collection));
        set1.add(new RemoveGreaterKeyCommand(collection));
        set1.add(new RemoveAllByFormOfEducationCommand(collection));
        set1.add(new SumOfStudentsCountCommand(collection));
        set1.add(new CountByStudentsCountCommand(collection));
        set1.add(new ExecuteScriptCommand());

        Commands clientCommands = new Commands(set1, set2);
        try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 31174))) {

            new Client(socketChannel).run(clientCommands);

        } catch (IOException  ioe) {
            System.out.println("Сервер не работает");;
        }
    }
}
