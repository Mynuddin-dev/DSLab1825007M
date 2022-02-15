
package rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class client
{
    public static void main(String args[]) throws RemoteException
    {
       client c = new client();
       c.connectRemote();
    }
       private void connectRemote() throws RemoteException{
        try
        {
        Scanner sc = new Scanner(System.in);
        Registry reg = LocateRegistry.getRegistry("localhost",1999);
        
        adder ad = (adder)reg.lookup("Hi Server");
        
        //System.out.println("Right???");
        System.out.println("Take two numbers: ");
        int a = sc.nextInt();
        int b = sc.nextInt();
        System.out.println("Addition of two number is = "+ad.add(a,b));
        }
       
        catch(NotBoundException|RemoteException e)
        {
            System.out.println("Found Exception in Client"+e);
        }
       }
    }

