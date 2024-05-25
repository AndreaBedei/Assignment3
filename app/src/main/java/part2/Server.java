package part2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.List;

public interface Server extends Remote{
    public List<String> getPlayers() throws RemoteException;
    public void setPlayer(String name) throws RemoteException;
}
