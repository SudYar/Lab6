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
    Scanner scanner = new Scanner(System.in);
    Commands commands;
    boolean isConnected = false;

    public Client(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public String readLine(){
        return scanner.nextLine();
    }

    public void run (Commands comm){
        this.commands = comm;
        try {
            String answer = readPack().getAnswer();
            if ("Подключение удалось".equals(answer)) isConnected = true;
            System.out.println(answer);
        } catch (IOException e) {
            System.out.println("Сервер недоступен");
        } catch (ClassNotFoundException e) {
            System.out.println("Пакет сломался при передаче");
        }
        String line;
        while (isConnected){
            System.out.print("$");
            line = readLine();
            String[] command = line.trim().split(" ", 3);
            String argument;
            if (command.length>1) argument = command[1];
            else argument = null;
            for (int i = 0; i < command.length; i++) command[i] = command[i].trim();
            try {
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
                            try {
                                Pack answer = readPack();
                                if (answer!= null) System.out.println(answer.getAnswer());
                                else System.out.println("Ошибка при получении пакета");
                            } catch (IOException e) {
                                System.out.println("Ошибка сериализации, сломан пакет");
                            } catch (ClassNotFoundException e) {
                                System.out.println("Пакет сломался при передаче");
                            }
                        }
                    } else{
                        Pack request;
                        Command thisCommand = commands.getCommand(command[0]);
                        if (!"VALID".equals(thisCommand.isValidArgument(argument))) {
                            System.out.println(thisCommand.isValidArgument(argument));
                            continue;
                        }
                        if (commands.isNeedStudyGroup(command[0])) request = new Pack(commands.getCommand(command[0]), argument,
                                new StudyGroupAsk().getStudyGroup(this));
                        else request = new Pack(commands.getCommand(command[0]), argument);
                        sendPack(request);
                        try {
                            Pack answer = readPack();
                            if (answer!= null) System.out.println(answer.getAnswer());
                            else System.out.println("Ошибка при получении пакета");
                        } catch (IOException e) {
                            System.out.println("Непредвиденная ошибка при получении данных");
                        } catch (ClassNotFoundException e) {
                            System.out.println("Пакет сломался при передаче");
                        }
                    }
                }
                else System.out.println("Error: Нет такой команды");

            } catch (IOException e) {
                System.out.println("Сервер отключился, завершение программы");
                isConnected = false;
            }

        }
        System.out.println("Завершение программы");


    }

    private Pack readPack() throws ClassNotFoundException, IOException {
        byte[] b = new byte[500];
        ByteBuffer buf = ByteBuffer.wrap(b);
        buf.clear();
        try {
            socketChannel.read(buf);
        } catch (IOException e) {
            return null;
        }
        Pack packWithCount = Serializer.deserialize(b);
        b = new byte[Integer.parseInt(packWithCount.getAnswer())];
        buf = ByteBuffer.wrap(b);
        try {
            socketChannel.read(buf);
        } catch (IOException e) {
            return null;
        }
        Pack serverAnswer = Serializer.deserialize(b);
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
    private void sendPack(Pack pack) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(Serializer.serialize(pack));
        socketChannel.write(buffer);
    }

}
