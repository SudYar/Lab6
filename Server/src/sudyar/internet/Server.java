package sudyar.internet;

import sudyar.commands.Commands;
import sudyar.data.StudyGroupCollection;
import sudyar.utilities.Pack;
import sudyar.utilities.Serializer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.*;

public class Server {
    StudyGroupCollection studyGroupCollection;
    int port;
    static final Logger logger = Logger.getLogger(Server.class.getName());
    public static final int MAX_CONNECTION = 1;
    private boolean serverRun = true;
    private final HashSet<Socket> clientCollection = new HashSet<>();


    public Server(StudyGroupCollection collection, int port, String log) {
        this.studyGroupCollection = collection;
        this.port = port;
        try {
            FileHandler handler = new FileHandler(log);
            logger.setUseParentHandlers(false);
            handler.setFormatter(new MyFormatter());
            logger.addHandler(handler);
        }catch (Exception e){
            System.out.println("Путь к файлу, куда писать логи - неверен");
            System.exit(1);
        }
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
                        sendPack(answer, clientSocket);
                        ClientRunner newClient = new ClientRunner(clientSocket, this, clientCommands);
                        clientCollection.add(clientSocket);
                        newClient.start();
                    }
                    else {
                        answer = new Pack("Сервер переполнен, подключайтесь позже");
                        printInf("Сервер переполнен, отключаем клиента");
                        sendPack(answer, clientSocket);
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

    private BufferedReader scanner =new BufferedReader(new InputStreamReader(System.in));
    private String readLine(){
        String line;
        try {
            line = scanner.readLine();
        } catch (IOException e) {
            System.out.println("Ошибка ввода, пожалуйста не вводите это снова");
            return null;
        }
        return line;
    }
    public boolean isServerRun(){ return  serverRun;}
    public void stopServer (){
        serverRun = false;
    }
    private Thread getServerConsole(Commands commands){
        return new Thread(() -> {


            println("Доступна консоль, можете ввести help, чтобы получить список доступных команд");

            while (true) {
                String line = readLine();
                if (line == null) {
                    printInf(commands.getCommand("save").execute(null));
                    printInf("Завершение работы сервера");
                    printInf(commands.getCommand("exit").execute(null));
                }
                else {
                    line = line.trim();
                    if (commands.getCommands().containsKey(line)){
                        if ("help".equals(line)) System.out.println(commands.toString());
                        if ("exit".equals(line)) {
                            printInf(commands.getCommand("save").execute(null));
                            printInf("Завершение работы сервера");
                            printInf(commands.getCommand("exit").execute(null));
                        }
                        else println(commands.getCommand(line).execute(null));
                    }else println("Такой команды нет, введите help");

                }
            }

        });

    }
    private void sendPack(Pack pack, Socket socket) {
        OutputStream outputStream;
        try {
            outputStream = socket.getOutputStream();
            byte[] buf = Serializer.serialize(pack);
            outputStream.write(Serializer.serialize(new Pack(String.valueOf(buf.length))));
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outputStream.write(buf);
        } catch (IOException e) {
            printErr("Не получилось отправить данные клиенту, отключаем его");
            removeClient(socket);
        }

    }

    public void printInf(String line){
        System.out.println(line + "\n");
        logger.info(line);
    }
    public void println(String line){
        System.out.println(line);
    }
    public void print(String line){
        System.out.print(line);
    }

    public void printErr(String line){
        System.out.println("ERROR: " + line);
        logger.warning(line);
    }




    private static class MyFormatter extends Formatter {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd ' время:' hh:mm:ss a zzz");

        @Override
        public String format(LogRecord record){
            return formatForDateNow.format(dateNow) + "\n" + record.getLevel() + ": " + record.getMessage() + "\n\n";
        }
    }
}
