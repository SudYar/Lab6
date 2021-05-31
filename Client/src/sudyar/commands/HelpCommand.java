package sudyar.commands;

public class HelpCommand extends AbstractCommand{


    public HelpCommand(String name, String description) {
        super(name, description);
    }

    public HelpCommand(){
        super("help", "Вывод описания команд");
    }
}
