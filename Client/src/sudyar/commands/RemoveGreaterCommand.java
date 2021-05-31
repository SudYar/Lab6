package sudyar.commands;

import sudyar.data.StudyGroup;
import sudyar.data.StudyGroupCollection;
import sudyar.utilities.Pack;

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
