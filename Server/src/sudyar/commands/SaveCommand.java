package sudyar.commands;

import sudyar.data.StudyGroupCollection;
import sudyar.utilities.FileParser;
import sudyar.utilities.Pack;

public class SaveCommand extends AbstractCommand {
    private final StudyGroupCollection studyGroupCollection;
    private final FileParser fileParser;
    public SaveCommand(StudyGroupCollection studyGroupCollection, FileParser fileParser) {
        super("save", "Сохранение коллекции в файл");
        this.studyGroupCollection = studyGroupCollection;
        this.fileParser = fileParser;

    }

    @Override
    public String execute(Pack pack) {
        if (fileParser.canWrite) return fileParser.unParse(studyGroupCollection);
        else return "Нет возможности сохранить в файл";
    }
}
