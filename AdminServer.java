/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integlab.IntegLabMidterms;

/**
 *
 * @author jessietabilisma
 */
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;

public class AdminServer {
//    fields
    private Registry registry;
//    Starting the server
    public static String serverStart() throws RemoteException {
        
    }
//    menu
    public static void adminMenu() {
        System.out.println("=======================");
        System.out.println("\t Menu");
        System.out.println("=======================\n");
        System.out.println("[1] Start the server.");
        System.out.println("[2] Register user.");
        System.out.println("[3] Remove user.");
        System.out.println("[4] Update user status.");
        System.out.println("[5] Create/Add new project.");
        System.out.println("[6] Display on goig project/s.");
        System.out.println("[7] Display completed project/s.");
        System.out.println("[8] Exit session.");
    }
}
