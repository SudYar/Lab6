package sudyar.commands;

import sudyar.data.StudyGroup;
import sudyar.data.StudyGroupCollection;
import sudyar.exception.DuplicateException;
import sudyar.utilities.Pack;
import sudyar.utilities.StudyGroupParser;


public class ReplaceIfLowe extends AbstractCommand {
    private StudyGroupCollection studyGroupCollection;

    public ReplaceIfLowe(StudyGroupCollection studyGroupCollection) {
        super("replace_if_lowe", "id", "заменить значение по id, если новое значение меньше старого");
        this.studyGroupCollection = studyGroupCollection;
    }

    @Override
    public String execute(Pack pack) {
        String argument = pack.getArgument();
        StudyGroup s = pack.getStudyGroup();
        if (argument == null) return "Нет аргументов, требуется id типа int";
        if (s == null) return "В пакете нет StudyGroup";
        Integer id = StudyGroupParser.parseId(argument);
        if (id == null) return "Аргумент не является int > 0";
        StudyGroup s2 = studyGroupCollection.getById(id);
        if ( s2 == null) return "Нет элемента с таким id";
        else if (s2.compareTo(s)< 0) {
            try {
                studyGroupCollection.update(id, s);
                return "Элемент заменен";
            } catch (DuplicateException e) {
                return e.getMessage();
            }
        } else return "Элемент не меньше старого";

    }
}
