package part2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallbackInterface extends Remote {

    /**
     * Notifica il client di un cambiamento sulla scacchiera del Sudoku.
     *
     * @param position la posizione della cella modificata, rappresentata da una coppia di coordinate (riga, colonna).
     * @param value il nuovo valore della cella, può essere null se la notifica riguarda una selezione.
     * @param selected indica se la cella è stata selezionata (true) o deselezionata (false), ignorato se value non è null.
     * @throws RemoteException se si verifica un errore durante la chiamata remota.
     */
    void notifyClient(Pair<Integer, Integer> position, Integer value, Boolean selected) throws RemoteException;
}
