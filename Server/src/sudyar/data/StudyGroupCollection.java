package sudyar.data;

import sudyar.exception.DuplicateException;
import sudyar.utilities.StudyGroupParser;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class StudyGroupCollection {
    private final HashMap<Integer, StudyGroup > collection;
    private final HashSet<String> passportIdSet;
    private int maxId = 0;


    public StudyGroupCollection() {
        collection = new HashMap<>();
        passportIdSet = new HashSet<>();
    }

    public void add(StudyGroup s) throws DuplicateException {
        if (collection.containsKey(s.getId())) throw new DuplicateException("ERROR: Повторение id");
        if ((s.getGroupAdmin() != null) && (passportIdSet.contains(s.getGroupAdmin().getPassportID())))
            throw new DuplicateException("ERROR: Повторение passportId админа");
        collection.put(s.getId(), s);
        if (s.getGroupAdmin() != null) passportIdSet.add(s.getGroupAdmin().getPassportID());
    }
    public void update(int id, StudyGroup s) throws DuplicateException{
        s.setId(id);
        if (s.getGroupAdmin() != null) {
            if (passportIdSet.contains(s.getGroupAdmin().getPassportID()) &&
                    ( collection.get(id).getGroupAdmin().getPassportID() == null ||
                    !s.getGroupAdmin().getPassportID().equals(collection.get(id).getGroupAdmin().getPassportID()))) {

                throw new DuplicateException("ERROR: Повторение passportId админа");
            }
        }
        if (collection.get(id).getGroupAdmin() != null) passportIdSet.remove(collection.get(id).getGroupAdmin().getPassportID());
        collection.put(s.getId(), s);
        passportIdSet.add(s.getGroupAdmin().getPassportID());
    }

    public void insert(StudyGroup s) throws DuplicateException{
        s.setId(++maxId);
        add(s);
    }

    public void remove(int id){

        if (collection.containsKey(id)) {
            if (collection.get(id).getGroupAdmin() != null) passportIdSet.remove(collection.get(id).getGroupAdmin().getPassportID());
            collection.remove(id);
        }
        if (id == maxId) maxId = Collections.max(collection.keySet());
    }

    public void clear(){
        collection.clear();
        passportIdSet.clear();
        maxId = 0;
    }


    public HashMap<Integer, StudyGroup> getCollection() {
        return collection;
    }

    public StudyGroup getById(int id){
        return collection.get(id);
    }

    public String getInfo(){
        return "HashMap коллекция, размер: " + collection.size();
    }

    public boolean containsPassportId(String passportId) {
        return passportIdSet.contains(passportId);
    }

    public boolean isEmpty(){
        return collection.isEmpty();
    }

    @Override
    public String toString() {
        String result = "";
        for (int i: collection.keySet()) {
            result += collection.get(i) + "\n";
        }
        if (isEmpty()) return "Коллекция пуста";
        else return result.trim();
    }
}
