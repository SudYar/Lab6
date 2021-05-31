package sudyar.internet;

import com.sun.org.slf4j.internal.LoggerFactory;
import sudyar.commands.Commands;
import sudyar.data.StudyGroupCollection;
import sudyar.utilities.Pack;
import sudyar.utilities.Serializer;
import sun.rmi.runtime.NewThreadAction;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.*;

public class Server {
    StudyGroupCollection studyGroupCollection;
    int port;
//    static final Logger logger = Logger.getLogger(Server.class.getName());
    public static final int MAX_CONNECTION = 3;
    private boolean serverRun = true;
    private HashSet<Socket> clientCollection = new HashSet<>();


    public Server(StudyGroupCollection collection, int port, String log) {
        this.studyGroupCollection = collection;
        this.port = port;
//        try {
//            Handler handler = new FileHandler(log);
//            logger.setUseParentHandlers(false);
//            handler.setFormatter(new MyFormatter());
//            logger.addHandler(handler);
//        }catch (Exception e){
//            System.out.println("Путь к файлу, куда писать логи - неверен");
//            System.exit(1);
//        }
    }

    public void run(Commands clientCommands, Commands serverCommands) {

        getServerConsole(serverCommands).start();
        try(ServerSocket serverSocket = new ServerSocket(31174, 1)) {
            printInf("Сервер запущен");
            Socket clientSocket;
            while (serverRun){
                try {
                    clientSocket = serverSocket.accept();

                    printInf("Подключился клиент");
                    Pack answer;
                    if (clientCollection.size() < MAX_CONNECTION){
                        answer = new Pack("Подключение удалось");
                        OutputStream outputStream = clientSocket.getOutputStream();
                        outputStream.write(Serializer.serialize(answer));
                        ClientRunner newClient = new ClientRunner(clientSocket, this, clientCommands);
                        clientCollection.add(clientSocket);
                        newClient.start();
                    }
                    else {
                        answer = new Pack("Сервер переполнен, подключайтесь позже");
                        printInf("Сервер переполнен, отключаем клиента");
                        OutputStream outputStream = clientSocket.getOutputStream();
                        outputStream.write(Serializer.serialize(answer));
                        clientSocket.close();
                    }

                }catch (IOException e){
                    printInf("Клиент отключился");
                }

            }

            serverCommands.getCommand("save");

        }catch (IOException e){
            println("Ошибка подключения сервера");
//            logger.warning(e.getMessage());
        }

    }

    public void removeClient(Socket socket){
        clientCollection.remove(socket);
    }

    public HashSet<Socket> getClientCollection() {
        return clientCollection;
    }

    public boolean isServerRun(){ return  serverRun;}
    public void stopServer (){
        serverRun = false;
    }
    private Thread getServerConsole(Commands commands){
        return new Thread(() -> {

            Scanner scanner = new Scanner(System.in);
            printInf("Доступна консоль, можете ввести help, чтобы получить список доступных команд");

            while (true) {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line == null) println("Вы ввели Ctrl + D");
                    else {
                        line = line.trim();
                        if (commands.getCommands().containsKey(line)){
                            if ("help".equals(line)) System.out.println(commands.toString());
                            if ("exit".equals(line)) {
                                printInf(commands.getCommand("save").execute(null));
                                printInf("Завершение сервера");
                                printInf(commands.getCommand("exit").execute(null));
                            }
                            else println(commands.getCommand(line).execute(null));
                        }else println("Такой команды нет, введите help");

                    }
                }
            }

        });

    }

    public void printInf(String line){
        System.out.println(line);
//        logger.info(line);
    }
    public void println(String line){
        System.out.println(line);
    }

    public void printErr(String line){
        System.out.println("ERROR: " + line);
//        logger.warning(line);
    }




    private static class MyFormatter extends Formatter {

        @Override
        public String format(LogRecord record) {
            return null;
        }
    }
}
