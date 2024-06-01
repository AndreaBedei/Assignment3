package part2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


public class PlayerImpl implements Player{

    // Board dell'utente.
    private SudokuBoard board;
    private transient SudokuGUI gui;  // Variabile transiente per l'interfaccia grafica del Sudoku (non sarà serializzata).

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

    // Metodo per iniziare il gioco.
    public void startGame(String selectedGame, String myGame){
        try {
            PlayerImpl player = new PlayerImpl();

            // Esporta l'oggetto player per renderlo disponibile alle chiamate remote.
            Player playerStub = (Player) UnicastRemoteObject.exportObject(player, 0);
            // Ottiene il registro RMI.
            Registry registry = LocateRegistry.getRegistry();

            // Associa (rebind) il nome del gioco al playerStub nel registro RMI.
            registry.rebind(myGame, playerStub);
            System.out.println("Player created.");

            // Cerca l'oggetto Server nel registro RMI usando il nome del server.
            Server server = (Server) registry.lookup(ServerImpl.SERVER_NAME);

            // Registra il playerStub presso il server.
            server.setPlayer(myGame, playerStub);

            // Se il gioco selezionato è uguale al proprio gioco, allora il player è il creatore del gioco.
            if(selectedGame.equals(myGame)){
                // Imposta il player come creatore del gioco.
                server.setCreators(myGame);
                mainBoard = new SudokuBoardImpl();
                // Esporta la scacchiera per renderla disponibile alle chiamate remote.
                player.setBoard((SudokuBoard)UnicastRemoteObject.exportObject(mainBoard, 0));
            } else {
                // Cerca l'altro player nel registro RMI.
                Player p = (Player) registry.lookup(selectedGame);
                // Imposta la scacchiera del player corrente con quella dell'altro player.
                player.setBoard(p.getSudokuBoard());
            }

            // Imposta l'interfaccia grafica del Sudoku per il player.
            player.setSudokuGUI(new SudokuGUI(player));

             // Registra il playerStub come callback per la scacchiera del Sudoku.
            player.getSudokuBoard().registerCallback(playerStub);   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo per notificare il client di un cambiamento sulla scacchiera
    @Override
    public void notifyClient(Pair<Integer, Integer> position, Integer value, Boolean selected) throws RemoteException {
        if(position==null) {
            gui.resetFocus();
        } else if(value != null){
            gui.newCellWritten(position, value);
        } else {
            gui.newCellSelected(position, selected);
        }
    }

    // Metodo heartbeat per verificare la connettività del client.
    @Override
    public void heartbeat() throws RemoteException {
        return;
    }
}
