package sudyar.commands;

import java.util.HashMap;
import java.util.HashSet;

public class Commands {
    HashMap<String, Command> commands = new HashMap<>();
    HashSet<String> commandsNeedStudyGroup = new HashSet<>();

    public Commands(ClearCommand clearCommand,
                    CountByStudentsCountCommand countByStudentsCountCommand,
                    ExecuteScriptCommand executeScriptCommand,
                    ExitCommand exitCommand,
                    HelpCommand helpCommand,
                    InfoCommand infoCommand,
                    RemoveAllByFormOfEducationCommand removeAllByFormOfEducationCommand,
                    RemoveGreaterCommand removeGreaterCommand,
                    RemoveKeyCommand removeKeyCommand,
                    ReplaceIfLowe replaceIfLowe,
                    ShowCommand showCommand,
                    SumOfStudentsCountCommand sumOfStudentsCountCommand,
                    UpdateCommand updateCommand) {
        commands.put(clearCommand.getName(), clearCommand);
        commands.put(countByStudentsCountCommand.getName(), countByStudentsCountCommand);
        commands.put(executeScriptCommand.getName(), executeScriptCommand);
        commands.put(exitCommand.getName(), exitCommand);
        commands.put(helpCommand.getName(), helpCommand);
        commands.put(infoCommand.getName(), infoCommand);
        commands.put(removeAllByFormOfEducationCommand.getName(), removeAllByFormOfEducationCommand);
        commands.put(removeGreaterCommand.getName(), removeGreaterCommand);
        commands.put(removeKeyCommand.getName(), removeKeyCommand);
        commands.put(replaceIfLowe.getName(), replaceIfLowe);
        commands.put(showCommand.getName(), showCommand);
        commands.put(sumOfStudentsCountCommand.getName(), sumOfStudentsCountCommand);
        commands.put(updateCommand.getName(), updateCommand);
    }

    public Commands(HashSet<Command> set){
        System.out.println("Введите help, чтобы вывести справку по доступным командам");
        for (Command command: set) {
            commands.put(command.getName(), command);
        }
    }
    public Commands(HashSet<Command> set1, HashSet<Command> set2){
        System.out.println("Введите help, чтобы вывести справку по доступным командам");
        for (Command command: set1) {
            commands.put(command.getName(), command);
        }
        for (Command command: set2) {
            commands.put(command.getName(), command);
            commandsNeedStudyGroup.add(command.getName());
        }
    }

//    public Commands() {
//        ClearCommand clearCommand = new ClearCommand();
//        CountByStudentsCountCommand countByStudentsCountCommand = new CountByStudentsCountCommand();
//        ExecuteScriptCommand executeScriptCommand = new ExecuteScriptCommand();
//        ExitCommand exitCommand = new ExitCommand();
//        HelpCommand helpCommand = new HelpCommand();
//        InfoCommand infoCommand = new InfoCommand();
//        RemoveAllByFormOfEducationCommand removeAllByFormOfEducationCommand = new RemoveAllByFormOfEducationCommand();
//        RemoveGreaterCommand removeGreaterCommand = new RemoveGreaterCommand();
//        RemoveKeyCommand removeKeyCommand = new RemoveKeyCommand();
//        ReplaceIfLowe replaceIfLowe = new ReplaceIfLowe();
//        SaveCommand saveCommand = new SaveCommand();
//        ShowCommand showCommand = new ShowCommand();
//        SumOfStudentsCountCommand sumOfStudentsCountCommand = new SumOfStudentsCountCommand();
//        UpdateCommand updateCommand = new UpdateCommand();
//
//        commands.put(clearCommand.getName(), clearCommand);
//        commands.put(countByStudentsCountCommand.getName(), countByStudentsCountCommand);
//        commands.put(executeScriptCommand.getName(), executeScriptCommand);
//        commands.put(exitCommand.getName(), exitCommand);
//        commands.put(helpCommand.getName(), helpCommand);
//        commands.put(infoCommand.getName(), infoCommand);
//        commands.put(removeAllByFormOfEducationCommand.getName(), removeAllByFormOfEducationCommand);
//        commands.put(removeGreaterCommand.getName(), removeGreaterCommand);
//        commands.put(removeKeyCommand.getName(), removeKeyCommand);
//        commands.put(replaceIfLowe.getName(), replaceIfLowe);
//        commands.put(saveCommand.getName(), saveCommand);
//        commands.put(showCommand.getName(), showCommand);
//        commands.put(sumOfStudentsCountCommand.getName(), sumOfStudentsCountCommand);
//        commands.put(updateCommand.getName(), updateCommand);
//
//    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public Command getCommand(String name){
        return commands.get(name);
    }

    public boolean isNeedStudyGroup(String nameCommand){
        return commandsNeedStudyGroup.contains(nameCommand);
    }

    @Override
    public String toString() {
        String result = "";
        for (String i: commands.keySet()) {
            result+= i + (commands.get(i).getDescriptionArgument() == null ? "" : " "+ commands.get(i).getDescriptionArgument() ) + "\t\t" + commands.get(i).getDescription() + "\n";
        }

        return result.trim();
    }
}
