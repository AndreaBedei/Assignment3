package part2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.rmi.server.UnicastRemoteObject;



public class ServerImpl implements Server{

    // Nome costante del server da usare per il binding nel registro RMI.
    public static final String SERVER_NAME = "serverObj";

    // Mappa per memorizzare i giocatori registrati, con il nome del giocatore come chiave.
    private Map<String, Player> players = new HashMap<>();

    // Set per memorizzare i nomi dei giocatori creatori.
    private Set<String> playerCreators = new HashSet<>();
    
    public ServerImpl(){
        // Loop infinito per monitorare lo stato dei giocatori, questo thread permette di verificare se il giocatore è ancora online, altrimenti lo rimuove.
        new Thread(() -> {
            try {
                while(true) {
                    Set<String> toRemove = new HashSet<>();
                    synchronized (players) {
                        players.forEach((name, pl) -> {
                            try {
                                pl.heartbeat();
                            } catch (RemoteException e) {
                                toRemove.add(name);
                            }
                        });
                        toRemove.forEach(players::remove);
                        toRemove.forEach(playerCreators::remove);
                    } 
                    Thread.sleep(5000); // Heartbeat ogni 5 secondi a tutti.
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Metodo sincronizzato per ottenere la lista dei giocatori registrati.
    @Override
    public synchronized List<String> getPlayers(){
        return this.players.keySet().stream().toList();
    }

    // Metodo sincronizzato per registrare un nuovo giocatore.
    @Override
    public synchronized void setPlayer(String name, Player remotePlayer) throws RemoteException {
        if(players.containsKey(name)){
            throw new RemoteException("Already defined.");
        }
        players.put(name, remotePlayer);
    }

    // Metodo sincronizzato per registrare un giocatore come creatore.
    @Override
    public synchronized void setCreators(String name) throws RemoteException {
        if(!playerCreators.add(name)){
            throw new RemoteException("Already defined.");
        }
    }

    // Metodo per ottenere il set dei creatori.
    @Override
    public Set<String> getCreators(){
        return playerCreators;
    }

    // Avvia il server.
    public static void main(String[] args) {
        try {
            Server server = new ServerImpl();

            // Esporta l'oggetto server per renderlo disponibile alle chiamate remote.
            Server serverStub = (Server) UnicastRemoteObject.exportObject(server, 0);
            // Ottiene il registro RMI.
            Registry registry = LocateRegistry.getRegistry();

            // Associa (rebind) il nome del server al serverStub nel registro RMI.
            registry.rebind(SERVER_NAME, serverStub);
            System.out.println("Server created.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
