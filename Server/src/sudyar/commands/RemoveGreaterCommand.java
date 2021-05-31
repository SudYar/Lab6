package sudyar.commands;

import sudyar.data.*;
import sudyar.utilities.Pack;

import java.util.Locale;

public class RemoveGreaterCommand extends AbstractCommand{
    private StudyGroupCollection studyGroupCollection;

    public RemoveGreaterCommand(StudyGroupCollection studyGroupCollection) {
        super("remove_greater", "Удалить все элементы превышающие заданный");
        this.studyGroupCollection = studyGroupCollection;
    }


    @Override
    public String execute(Pack pack) {
        StudyGroup studyGroup = pack.getStudyGroup();
        if (studyGroup == null) return "ERROR: В пакете нет StudyGroup";

        for (int i : studyGroupCollection.getCollection().keySet()) {
            if (studyGroup.compareTo(studyGroupCollection.getById(i)) < 0) studyGroupCollection.remove(i);
        }


        return  "Все элементы коллекции, превышающие заданный, удалены";

    }
}
