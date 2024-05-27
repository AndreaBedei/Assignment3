package part2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.List;
import java.util.Set;

public interface Server extends Remote{
     /**
     * Ottiene la lista dei nomi dei giocatori registrati.
     *
     * @return una lista contenente i nomi dei giocatori registrati.
     * @throws RemoteException se si verifica un errore durante la chiamata remota.
     */
    public List<String> getPlayers() throws RemoteException;

    /**
     * Ottiene il set dei nomi dei giocatori creatori.
     *
     * @return un set contenente i nomi dei giocatori creatori.
     * @throws RemoteException se si verifica un errore durante la chiamata remota.
     */
    public Set<String> getCreators() throws RemoteException;

    /**
     * Registra un nuovo giocatore con il nome specificato e il riferimento remoto al player.
     *
     * @param name il nome del giocatore da registrare.
     * @param remotePlayer il riferimento remoto al player.
     * @throws RemoteException se si verifica un errore durante la chiamata remota o se il nome è già registrato.
     */
    public void setPlayer(String name, Player remotePlayer) throws RemoteException;

    /**
     * Registra un giocatore come creatore del gioco.
     *
     * @param name il nome del giocatore da registrare come creatore.
     * @throws RemoteException se si verifica un errore durante la chiamata remota o se il nome è già registrato come creatore.
     */
    public void setCreators(String name) throws RemoteException;

}
