package part2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Player extends Remote, CallbackInterface{

    /**
     * Ottiene la scacchiera del Sudoku associata al giocatore.
     *
     * @return la scacchiera del Sudoku del giocatore.
     * @throws RemoteException se si verifica un errore durante la chiamata remota.
     */
    public SudokuBoard getSudokuBoard() throws RemoteException;

    /**
     * Metodo heartbeat per verificare la connettività del giocatore.
     * Questo metodo viene chiamato periodicamente per assicurarsi che il giocatore sia ancora connesso.
     *
     * @throws RemoteException se si verifica un errore durante la chiamata remota.
     */
    public void heartbeat() throws RemoteException;
}
