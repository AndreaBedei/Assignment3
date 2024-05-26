package part2;

import java.util.ArrayList;
import java.util.Map;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SudokuBoard extends Remote {
    public void setCell(int x, int y, int value) throws RemoteException;
    public ArrayList<Map.Entry<Pair<Integer, Integer>, Integer>> getCells() throws RemoteException;
    public ArrayList<Pair<Integer, Integer>> getSelectedCells() throws RemoteException;
    public void toggleSelectedCell(int x, int y) throws RemoteException;
    void registerCallback(CallbackInterface callback) throws RemoteException;
}
