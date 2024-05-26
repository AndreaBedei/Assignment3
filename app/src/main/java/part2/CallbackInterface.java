package part2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallbackInterface extends Remote {
    void notifyClient(Pair<Integer, Integer> position, Integer value, Boolean selected) throws RemoteException;
}
