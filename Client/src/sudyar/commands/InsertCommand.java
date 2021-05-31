package sudyar.commands;

import sudyar.data.StudyGroupCollection;
import sudyar.exception.DuplicateException;
import sudyar.utilities.Pack;

public class InsertCommand extends AbstractCommand{
    private StudyGroupCollection studyGroupCollection;

    public InsertCommand(StudyGroupCollection studyGroupCollection) {
        super("insert", "Добавить новую учебную группу в коллекицю");
        this.studyGroupCollection = studyGroupCollection;
    }

    @Override
    public String  execute(Pack pack) {
        try {
            if (pack.getStudyGroup() != null){
                studyGroupCollection.insert(pack.getStudyGroup());
                return "Группа добавлена";
            }
            return "ERROR: в пакете нет StudyGroup";

        } catch (DuplicateException e) {
            return e.getMessage();
        }
    }
    
}
