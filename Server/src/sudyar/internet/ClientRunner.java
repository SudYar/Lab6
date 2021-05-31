package sudyar.internet;

import sudyar.commands.Commands;
import sudyar.utilities.CommandsExecute;
import sudyar.utilities.Pack;
import sudyar.utilities.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ClientRunner extends Thread {
    private Socket socket;
    private Server server;
    private Commands commands;

    public ClientRunner(Socket socket, Server server, Commands commands) {
        this.socket = socket;
        this.server = server;
        this.commands = commands;
    }

    public void run(){
        CommandsExecute commandsExecute = new CommandsExecute(commands);
        while (server.getClientCollection().contains(socket) && server.isServerRun()) {

            try {
                Pack request = readPack();
                String answer = commandsExecute.execute(request);
                Pack newPack = new Pack(answer);
                sendPack(newPack);
            } catch (IOException e) {
                if ("Connection reset".equals(e.getMessage())) server.printInf("Клиент отключился");
                else server.printErr(e.getMessage());
                try {
                    server.removeClient(socket);
                    socket.close();
                } catch (IOException ioException) {
                    server.printErr("Не получилось закрыть сокет");
                }
            } catch (ClassNotFoundException e) {
                sendPack(new Pack("Пакет сломался при передаче"));

            }
        }

    }

    private Pack readPack() throws IOException, ClassNotFoundException {
        InputStream inputStream = socket.getInputStream();
        return Serializer.deserialize(inputStream);
    }

    private void sendPack(Pack pack) {
        OutputStream outputStream;
        try {
            outputStream = socket.getOutputStream();
            outputStream.write(Serializer.serialize(pack));
        } catch (IOException e) {
            server.printErr("Не получилось отправить данные клиенту, отключаем его");
            server.removeClient(socket);
        }

    }
}
