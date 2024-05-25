package part2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;

public interface Player extends Remote{
    public Optional<SudokuBoard> getSudokuBoard() throws RemoteException;
}
