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
    private transient SudokuGUI gui; 

    public PlayerImpl(){}

    public void setBoard(SudokuBoard sb){
        this.board = sb;
    }

    @Override
    public SudokuBoard getSudokuBoard() throws RemoteException {
        return board;
    }

    public SudokuGUI getSudokuGUI(){
        return this.gui;
    }

    public void setSudokuGUI(SudokuGUI gui){
        this.gui = gui;
    }

    private static SudokuBoard mainBoard;

    public static void main(String[] args) {
        try {
            PlayerImpl player = new PlayerImpl();

            Player playerStub = (Player) UnicastRemoteObject.exportObject(player, 0);
            Registry registry = LocateRegistry.getRegistry();

            registry.rebind(args[0], playerStub);
            System.out.println("Player created.");

            Server server = (Server) registry.lookup(ServerImpl.SERVER_NAME);

            server.setPlayer(args[0], playerStub);
            List<String> li = server.getPlayers();


            if(li.size() == 1){
                System.out.println("Player has created the game");
                mainBoard = new SudokuBoardImpl();
                player.setBoard((SudokuBoard)UnicastRemoteObject.exportObject(mainBoard, 0));

            } else {
                String playerName = li.stream().filter(x -> !x.equals(args[0])).findFirst().get();
                Player p = (Player) registry.lookup(playerName);
                player.setBoard(p.getSudokuBoard());
                System.out.println("Giocatore si è unito alla partita");
            }

            player.setSudokuGUI(new SudokuGUI(player));

            player.getSudokuBoard().registerCallback(playerStub);

            li.forEach(el -> System.out.println("Player: " + el));

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyClient(Pair<Integer, Integer> position, Integer value, Boolean selected) throws RemoteException {
        if(value != null){
            gui.newCellWritten(position, value);
        } else {
            gui.newCellSelected(position, selected);
        }
    }

    @Override
    public void heartbeat() throws RemoteException {
        return;
    }
}
