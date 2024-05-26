package part2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Player extends Remote{
    public SudokuBoard getSudokuBoard() throws RemoteException;
}
