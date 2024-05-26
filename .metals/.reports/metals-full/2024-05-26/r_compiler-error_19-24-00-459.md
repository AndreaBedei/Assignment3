file:///C:/Users/fabio/Desktop/Assignment3/app/src/main/java/part2/SudokuBoardImpl.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala3-library_3\3.3.3\scala3-library_3-3.3.3.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.12\scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 1769
uri: file:///C:/Users/fabio/Desktop/Assignment3/app/src/main/java/part2/SudokuBoardImpl.java
text:
```scala
package part2;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class SudokuBoardImpl implements SudokuBoard, Serializable, CallbackInterface{
    
    private Map<Pair<Integer,Integer>, Integer> board = new HashMap<>();
    private Set<Pair<Integer,Integer>> selected = new HashSet<>();
    private transient List<CallbackInterface> callbacks = new ArrayList<>();

    public SudokuBoardImpl(){
    }

    @Override
    public void setCell(int x, int y, int value) throws RemoteException{
        this.board.put(new Pair<>(x,y), value);
        this.notifyClient(new Pair<>(x,y), value, null);
    }

    @Override
    public void toggleSelectedCell(int x, int y) throws RemoteException {
        var p = new Pair<>(x,y);
        if(this.selected.contains(p)){
            this.selected.add(p);
            this.notifyClient(new Pair<>(x,y), null, true);
        } else {
            this.selected.remove(p);
            this.notifyClient(new Pair<>(x,y), null, false);
        }
    }

    @Override
    public ArrayList<Entry<Pair<Integer, Integer>, Integer>> getCells() throws RemoteException {
        return board.entrySet().stream().collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> getSelectedCells() throws RemoteException {
        return selected.stream().collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void notifyClient(Pair<Integer, Integer> position, Integer value, Boolean selected) throws RemoteException {
        if(c@@)
        callbacks.forEach(c -> {
            try {
                c.notifyClient(position, value, selected);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void registerCallback(CallbackInterface callback) throws RemoteException {
        callbacks.add(callback);
    }
}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:933)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:168)
	scala.meta.internal.pc.MetalsDriver.run(MetalsDriver.scala:45)
	scala.meta.internal.pc.completions.CompletionProvider.completions(CompletionProvider.scala:46)
	scala.meta.internal.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:147)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator