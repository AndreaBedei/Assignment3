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
