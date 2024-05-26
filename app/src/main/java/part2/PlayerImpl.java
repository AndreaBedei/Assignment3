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

            li.forEach(el -> System.out.println("Player: " + el));

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        new SudokuGUI(player);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
