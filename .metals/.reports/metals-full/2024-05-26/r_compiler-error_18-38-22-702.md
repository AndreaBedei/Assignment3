file:///C:/Users/fabio/Desktop/Assignment3/app/src/main/java/part2/ServerImpl.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala3-library_3\3.3.3\scala3-library_3-3.3.3.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.12\scala-library-2.13.12.jar [exists ]
Options:



action parameters:
uri: file:///C:/Users/fabio/Desktop/Assignment3/app/src/main/java/part2/ServerImpl.java
text:
```scala
package part2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashSet;
import java.util.List;

import java.util.Set;
import java.rmi.server.UnicastRemoteObject;



public class ServerImpl implements Server{

    public static final String SERVER_NAME = "serverObj";

    private Set<String> playerNames = new HashSet<>();
    
    public ServerImpl(){}

    @Override
    public List<String> getPlayers(){
        return this.playerNames.stream().toList();
    }

    @Override
    public void setPlayer(String name) throws RemoteException {
        if(!playerNames.add(name)){
            throw new RemoteException("Already defined.");
        }
    }


    public static void main(String[] args) {
        try {
            Server server = new ServerImpl();

            Server serverStub = (Server) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.getRegistry();

            registry.rebind(SERVER_NAME, serverStub); //prova rebind
            System.out.println("Server created.");
        } catch (RemoteException e) {
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
	scala.meta.internal.pc.PcCollector.<init>(PcCollector.scala:44)
	scala.meta.internal.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:90)
	scala.meta.internal.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:110)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator