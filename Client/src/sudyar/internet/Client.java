package sudyar.internet;

import sudyar.commands.Commands;
import sudyar.utilities.Pack;
import sudyar.utilities.Serializer;

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
                                System.out.println(readPack().getAnswer());
                            } catch (IOException e) {
                                System.out.println("Сервер отключен");
                                isConnected = false;
                                break;
                            } catch (ClassNotFoundException e) {
                                System.out.println("Пакет сломался при передаче");
                                ;
                            }
                        }
                    } else {
                        sendPack(new Pack(commands.getCommand(command[0]), argument));
                        try {
                            System.out.println(readPack().getAnswer());
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

    private Pack readPack() throws IOException, ClassNotFoundException {
        byte[] b = new byte[10000];
        ByteBuffer buf = ByteBuffer.wrap(b);
        buf.clear();
        socketChannel.read(buf);
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
