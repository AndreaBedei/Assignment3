package part2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.List;
import java.util.Set;

public interface Server extends Remote{
    public List<String> getPlayers() throws RemoteException;
    public Set<String> getCreators() throws RemoteException;
    public void setPlayer(String name, Player remotePlayer) throws RemoteException;
    public void setCreators(String name) throws RemoteException;

}
