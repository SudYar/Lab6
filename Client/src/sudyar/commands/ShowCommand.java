package sudyar.commands;

import sudyar.data.StudyGroupCollection;
import sudyar.utilities.Pack;

public class ShowCommand extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;

    public ShowCommand(StudyGroupCollection studyGroupCollection) {
        super("show", "Вывести все элементы коллекции");
        this.studyGroupCollection = studyGroupCollection;
    }

    public ShowCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public String execute(Pack pack) {
        return studyGroupCollection.toString();

    }
}
