package part2.esempioDato;

import java.rmi.*;

public interface Counter extends Remote {
	void inc() throws RemoteException;
	int getValue() throws RemoteException;
}
