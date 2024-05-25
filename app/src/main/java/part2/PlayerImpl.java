package part2;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.rmi.server.UnicastRemoteObject;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


public class PlayerImpl implements Player{

    private SudokuBoard board;

    public PlayerImpl(){}

    @Override
    public Optional<SudokuBoard> getSudokuBoard() throws RemoteException {
        return Optional.of(board);
    }

    public static void main(String[] args) {

        try {
            Player player = new PlayerImpl();

            Player playerStub = (Player) UnicastRemoteObject.exportObject(player, 0);
            Registry registry = LocateRegistry.getRegistry();

            registry.rebind(args[0], playerStub);
            System.out.println("Player created.");

            Server server = (Server) registry.lookup(ServerImpl.SERVER_NAME);

            server.setPlayer(args[0]);
            List<String> li = server.getPlayers();

            li.forEach(el -> System.out.println("Player: " + el));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

    }
    
}
