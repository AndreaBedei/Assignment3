package part2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SudokuBoard extends Remote {
    public boolean setCell(int x, int y, int value) throws RemoteException;
    public boolean selectCell(int x, int y) throws RemoteException;
}
