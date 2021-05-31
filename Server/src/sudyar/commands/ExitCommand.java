package sudyar.commands;

import sudyar.internet.Server;
import sudyar.utilities.Pack;

public class ExitCommand  extends AbstractCommand{
    Server server;

    public ExitCommand(Server server) {
        super("exit", "Завершение программы (с сохраненением в файл)");
        this.server = server;
    }

    @Override
    public String execute(Pack pack) {
        System.exit(0);
        return "Завершение программы";
    }

}
