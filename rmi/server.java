
package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class server extends UnicastRemoteObject implements adder
{
    
    public server() throws RemoteException
    {
    //super
    }
    
    @Override
    public int add(int n1, int n2) throws RemoteException{
    return n1+n2;
    }
    
    
    public static void main(String args[]) throws RemoteException
    {
        try
        {
            Registry reg = LocateRegistry.createRegistry(1999);
            reg.rebind("Hi Server", new server());
            System.out.println("Server is ready");
        }
        catch(Exception e)
        {
            System.out.println("Exception found in server"+e);
        }
    }

}
