package part2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.rmi.server.UnicastRemoteObject;



public class ServerImpl implements Server{

    public static final String SERVER_NAME = "serverObj";

    private Map<String, Player> players = new HashMap<>();

    private Set<String> playerCreators = new HashSet<>();
    
    public ServerImpl(){
        new Thread(() -> {
            try {
                while(true) {
                    Set<String> toRemove = new HashSet<>();
                    synchronized (players) {
                        players.forEach((name, pl) -> {
                            try {
                                pl.heartbeat();
                            } catch (RemoteException e) {
                                System.out.println("Player " + name + " timed out");
                                toRemove.add(name);
                            }
                        });
                        toRemove.forEach(players::remove);
                        toRemove.forEach(playerCreators::remove); //TODO bertu
                    } 
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public synchronized List<String> getPlayers(){
        return this.players.keySet().stream().toList();
    }

    @Override
    public synchronized void setPlayer(String name, Player remotePlayer) throws RemoteException {
        if(players.containsKey(name)){
            throw new RemoteException("Already defined.");
        }
        players.put(name, remotePlayer);
    }

    @Override
    public synchronized void setCreators(String name) throws RemoteException {
        if(!playerCreators.add(name)){
            throw new RemoteException("Already defined.");
        }
    }


    @Override
    public Set<String> getCreators(){
        return playerCreators;
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
