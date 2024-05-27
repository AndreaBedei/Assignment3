package part2;

import java.util.ArrayList;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SudokuBoard extends Remote {
    /**
     * Imposta il valore di una cella specificata dalla posizione (x, y).
     *
     * @param x     la coordinata x (riga) della cella.
     * @param y     la coordinata y (colonna) della cella.
     * @param value il valore da impostare nella cella.
     * @throws RemoteException se si verifica un errore durante la chiamata remota.
     */
    public void setCell(int x, int y, int value) throws RemoteException;

    /**
     * Ottiene tutte le celle con i loro valori.
     *
     * @return una lista di coppie (cella, valore) per tutte le celle della scacchiera.
     * @throws RemoteException se si verifica un errore durante la chiamata remota.
     */
    public ArrayList<Pair<Pair<Integer, Integer>, Integer>> getCells() throws RemoteException;

    /**
     * Ottiene tutte le celle selezionate.
     *
     * @return una lista di coppie (riga, colonna) per tutte le celle selezionate.
     * @throws RemoteException se si verifica un errore durante la chiamata remota.
     */
    public ArrayList<Pair<Integer, Integer>> getSelectedCells() throws RemoteException;

    /**
     * Seleziona una cella specificata dalla posizione (x, y).
     *
     * @param x la coordinata x (riga) della cella.
     * @param y la coordinata y (colonna) della cella.
     * @throws RemoteException se si verifica un errore durante la chiamata remota.
     */
    public void selectedCell(int x, int y) throws RemoteException;

     /**
     * Registra un callback per essere notificato dei cambiamenti sulla scacchiera.
     *
     * @param callback l'interfaccia di callback da registrare.
     * @throws RemoteException se si verifica un errore durante la chiamata remota.
     */
    void registerCallback(CallbackInterface callback) throws RemoteException;

    /**
     * Deseleziona una cella specificata dalla posizione (x, y).
     *
     * @param x la coordinata x (riga) della cella.
     * @param y la coordinata y (colonna) della cella.
     * @throws RemoteException se si verifica un errore durante la chiamata remota.
     */
    public void deselectedCell(int x, int y) throws RemoteException;
}
