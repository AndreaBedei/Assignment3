file:///C:/Users/fabio/Desktop/Assignment3/app/src/main/java/part2/SudokuBoardImpl.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala3-library_3\3.3.3\scala3-library_3-3.3.3.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.12\scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 1484
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


public class SudokuBoardImpl implements SudokuBoard, Serializable, RegisterInterface, CallbackInterface{
    
    private Map<Pair<Integer,Integer>, Integer> board = new HashMap<>();
    private Set<Pair<Integer,Integer>> selected = new HashSet<>();
    private List<CallbackInterface> callbacks = new ArrayList<>();

    public SudokuBoardImpl(){
    }

    @Override
    public void setCell(int x, int y, int value) {
        this.board.put(new Pair<>(x,y), value);
    }

    @Override
    public void toggleSelectedCell(int x, int y) {
        var p = new Pair<>(x,y);
        if(this.selected.contains(p)){
            this.selected.add(p);
        } else {
            this.selected.remove(p);
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
    public void notifyClient(String message) throws RemoteExc@@eption {
        callbacks.forEach(CallbackInterface::notifyClient());
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
	scala.meta.internal.pc.HoverProvider$.hover(HoverProvider.scala:36)
	scala.meta.internal.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:366)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator