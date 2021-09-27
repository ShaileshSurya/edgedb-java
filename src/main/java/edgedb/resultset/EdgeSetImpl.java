package edgedb.resultset;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class EdgeSetImpl implements EdgedSet{

    @Override
    public int length() {
        return 0;
    }

    @Override
    public Iterator<EdgedSet> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super EdgedSet> consumer) {

    }

    @Override
    public Spliterator<EdgedSet> spliterator() {
        return null;
    }
}
