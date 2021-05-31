package sudyar.utilities;

import java.io.*;

public class Serializer {

    public static byte[] serialize (Pack pack) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(pack);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public static Pack deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (Pack) inputStream.readObject();
    }




}
