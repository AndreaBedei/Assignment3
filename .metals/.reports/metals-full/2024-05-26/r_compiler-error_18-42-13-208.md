file:///C:/Users/fabio/Desktop/Assignment3/app/src/main/java/part2/PlayerImpl.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala3-library_3\3.3.3\scala3-library_3-3.3.3.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.12\scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 1658
uri: file:///C:/Users/fabio/Desktop/Assignment3/app/src/main/java/part2/PlayerImpl.java
text:
```scala
package part2;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;
import java.rmi.server.UnicastRemoteObject;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


public class PlayerImpl implements Player{

    private SudokuBoard board;

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

            player.getSudokuBoard().registerCallback(new CallbackInterface() {
                n@@
            });

            li.forEach(el -> System.out.println("Player: " + el));

            
        } catch (Exception e) {
            e.printStackTrace();
        }
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