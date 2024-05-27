package part2;

import java.util.ArrayList;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SudokuBoard extends Remote {
    public void setCell(int x, int y, int value) throws RemoteException;
    public ArrayList<Pair<Pair<Integer, Integer>, Integer>> getCells() throws RemoteException;
    public ArrayList<Pair<Integer, Integer>> getSelectedCells() throws RemoteException;
    public void selectedCell(int x, int y) throws RemoteException;
    void registerCallback(CallbackInterface callback) throws RemoteException;
    public void deselectedCell(int x, int y) throws RemoteException;
}
