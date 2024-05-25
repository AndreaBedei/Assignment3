package part2.esempioDato;

import java.rmi.*;

public interface MyClass2 extends Remote {

	int get() throws RemoteException;

	void update(int c) throws RemoteException;
	
}