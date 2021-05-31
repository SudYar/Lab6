package sudyar.commands;

import sudyar.utilities.Pack;

public class ExitCommand  extends AbstractCommand{

    public ExitCommand() {
        super("exit", "Завершение программы (с сохраненением в файл)");
    }

    @Override
    public String execute(Pack pack) {
        System.exit(0);
        return "Завершение программы";
    }

}
