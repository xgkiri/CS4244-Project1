import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;

class CloneUnit {
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deepClone(T src) {
        T dst = null;
        PipedOutputStream out=new PipedOutputStream();
        PipedInputStream in=new PipedInputStream();
        try {
            in.connect(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(ObjectOutputStream bo = new ObjectOutputStream(out);
                ObjectInputStream bi = new ObjectInputStream(in);) {
            bo.writeObject(src);
            dst = (T) bi.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dst;
    }
}