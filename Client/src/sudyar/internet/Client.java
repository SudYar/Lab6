package sudyar.internet;

import sudyar.commands.AbstractCommand;
import sudyar.commands.Command;
import sudyar.commands.Commands;
import sudyar.utilities.Pack;
import sudyar.utilities.Serializer;
import sudyar.utilities.StudyGroupAsk;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;


public class Client {

    SocketChannel socketChannel;
    BufferedReader scanner =new BufferedReader(new InputStreamReader(System.in));
    Commands commands;
    boolean isConnected = false;

    public Client(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public String readLine(){
        String line;
        try {
            line = scanner.readLine();
        } catch (IOException e) {
            System.out.println("Ошибка ввода, пожалуйста не вводите это снова");
            return "";
        }
        return line;
    }

    public void run (Commands comm){
        this.commands = comm;
        String answer = readPack().getAnswer();
        if ("Подключение удалось".equals(answer)) isConnected = true;
        System.out.println(answer);


        String line;
        while (isConnected){
            System.out.print("$");
            line = readLine();
            String[] command = line.trim().split(" ", 3);
            String argument;
            if (command.length>1) argument = command[1];
            else argument = null;
            for (int i = 0; i < command.length; i++) command[i] = command[i].trim();

            if (commands.getCommands().containsKey(command[0])) {
                if ("help".equals(command[0])) {
                    System.out.println(commands);
                }else if ("exit".equals(command[0])) {
                    commands.getCommand(command[0]).execute(null);
                }else
                if ("execute_script".equals(command[0])) {
                    Pack pack = readScript(argument);
                    if (pack != null) {
                        sendPack(pack);

                        Pack answerPack = readPack();
                        if (answerPack!= null && !"".equals(answerPack.getAnswer())) System.out.println(answerPack.getAnswer());
                        else System.out.println("Ошибка при получении пакета");

                    }
                } else{
                    Pack request;
                    Command thisCommand = commands.getCommand(command[0]);
                    if (!"VALID".equals(thisCommand.isValidArgument(argument))) {
                        System.out.println(thisCommand.isValidArgument(argument));
                        continue;
                    }
                    if (commands.isNeedStudyGroup(command[0])) {
                        if ("update".equals(command[0]) || "replace_if_lowe".equals(command[0])) {
                            Pack testRequest = new Pack(commands.getCommand("show_one"), argument);
                            sendPack(testRequest);
                            Pack testAnswer = readPack();
                            if (testAnswer != null && !"".equals( testAnswer.getAnswer())) {
                                if (!testAnswer.getAnswer().contains("\n")) {
                                    System.out.println(testAnswer.getAnswer());
                                    continue;
                                } else System.out.println("Найден такой Id у группы\n" + testAnswer.getAnswer());
                            }
                            else System.out.println("Ошибка при получении пакета");
                        }
                        request = new Pack(commands.getCommand(command[0]), argument,
                                new StudyGroupAsk().getStudyGroup(this));
                    }
                    else request = new Pack(commands.getCommand(command[0]), argument);
                    sendPack(request);

                    Pack answerPack = readPack();
                    if (answerPack != null && !"".equals( answerPack.getAnswer())) System.out.println(answerPack.getAnswer());
                    else System.out.println("Ошибка при получении пакета");

                }
            }
            else System.out.println("Error: Нет такой команды");

        }
        System.out.println("Завершение программы");


    }

    private Pack readPack() {
        byte[] b = new byte[500];
        ByteBuffer buf = ByteBuffer.wrap(b);
        buf.clear();
        Pack packWithCount;
        try {
            socketChannel.read(buf);
            packWithCount = Serializer.deserialize(b);
        } catch (ClassNotFoundException e ) {
            packWithCount = new Pack("20000");
        } catch (IOException e) {
            System.out.println("Ошибка при получении данных с сервера. Возможно, сервер отключился");
            packWithCount = new Pack("20000");
        }
        b = new byte[Integer.parseInt(packWithCount.getAnswer())];
        buf = ByteBuffer.wrap(b);
        Pack serverAnswer;
        try {
            socketChannel.read(buf);
            serverAnswer= Serializer.deserialize(b);
        } catch (IOException | ClassNotFoundException e) {
            serverAnswer = new Pack("");
        }
        return serverAnswer;
    }

    private Pack readScript(String path){
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException | NullPointerException e) {
            System.out.println("Проблемы с файлом скрипта");
        }
        if (lines != null) {
            return new Pack(commands.getCommand("execute_script"), lines);
        } else return null;
    }
    private void sendPack(Pack pack) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(Serializer.serialize(pack));
            socketChannel.write(buffer);
        } catch (IOException e) {
            System.out.println("Сервер не доступен. Завершение работы");
            isConnected = false;
        }

    }

}
