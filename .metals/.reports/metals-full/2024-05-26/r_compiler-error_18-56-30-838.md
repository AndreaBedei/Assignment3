file:///C:/Users/fabio/Desktop/Assignment3/app/src/main/java/part2/PlayerImpl.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala3-library_3\3.3.3\scala3-library_3-3.3.3.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.12\scala-library-2.13.12.jar [exists ]
Options:



action parameters:
uri: file:///C:/Users/fabio/Desktop/Assignment3/app/src/main/java/part2/PlayerImpl.java
text:
```scala
package part2;

import javax.swing.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import java.rmi.server.UnicastRemoteObject;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


public class PlayerImpl implements Player{

    private SudokuBoard board;
    private SudokuGUI gui transient

    public PlayerImpl(){}

    public void setBoard(SudokuBoard sb){
        this.board = sb;
    }

    @Override
    public SudokuBoard getSudokuBoard() throws RemoteException {
        return board;
    }

    public static void main(String[] args) {
        try {
            PlayerImpl player = new PlayerImpl();

            Player playerStub = (Player) UnicastRemoteObject.exportObject(player, 0);
            Registry registry = LocateRegistry.getRegistry();

            registry.rebind(args[0], playerStub);
            System.out.println("Player created.");

            Server server = (Server) registry.lookup(ServerImpl.SERVER_NAME);

            server.setPlayer(args[0]);
            List<String> li = server.getPlayers();


            if(li.size() == 1){
                System.out.println("Giocatore Ha creato partita");
                player.setBoard(new SudokuBoardImpl());

            } else {
                String playerName = li.stream().filter(x -> !x.equals(args[0])).findFirst().get();
                Player p = (Player) registry.lookup(playerName);
                player.setBoard(p.getSudokuBoard());
                System.out.println("Giocatore si è unito alla partita");
            }

            var gui = new SudokuGUI(player);

            // player.getSudokuBoard().registerCallback(new SerializableCallbackInterface(){
            //     @Override
            //     public void notifyClient(Pair<Integer, Integer> position, Integer value, Boolean selected) throws RemoteException{
            //         if(value != null){
            //             gui.newCellWritten(position, value);
            //         } else {
            //             gui.newCellSelected(position, selected);
            //         }
            //     }
            // });

            li.forEach(el -> System.out.println("Player: " + el));

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SudokuGUI getSudokuGUI(){
        return this.getSudokuGUI()
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
	scala.meta.internal.pc.PcCollector.<init>(PcCollector.scala:44)
	scala.meta.internal.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:90)
	scala.meta.internal.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:110)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator