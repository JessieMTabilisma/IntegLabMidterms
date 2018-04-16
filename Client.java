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
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
public class Client {
//   field
    private static Scanner usrInput = new Scanner(System.in);
//    Utility method
    public static void displayLeaderMenu(){
        System.out.println("[1]. Display my project/s as a leader.");
        System.out.println("[2]. Display my project/s as a member.");
        System.out.println("[3]. Assign members to project.");
        System.out.println("[4]. Unassign member to prject.");
        System.out.println("[5]. Change project status.");
        System.out.println("[6]. Upload file.");
        System.out.println("[7]. Log out");
    }
    public static void displayMemberMenu(){
        System.out.println("[1]. Display my project/s.");
        System.out.println("[2]. Upload file.");
        System.out.println("[3]. Log out");
    }
    public static void pressToContinue() {
        System.out.println("Press ENTER to continue..");
        usrInput.nextLine();
    }
    public static void titleized(String title) {
        System.out.println("===================");
        System.out.println("\t" + title);
        System.out.println("===================");
    }
    public static void childtitleized(String title) {
        System.out.println("____________________");
        System.out.println(title);
        System.out.println("--------------------");
    }
    
    public static void main(String[] args) throws Exception {
        titleized("LOGIN");
        System.out.print("Enter username:  ");
        String usrname = usrInput.nextLine();
        System.out.print("Enter password:  ");
        String pswd = usrInput.nextLine();
        
        System.out.println("");
        
        System.out.print("Enter ip address of the server: ");
        String ipAddress = usrInput.nextLine();
        System.out.print("Enter port number: ");
        int port = Integer.parseInt(usrInput.nextLine());
        
        Registry reg = LocateRegistry.getRegistry(ipAddress, port);
        Project stub = (Project) reg.lookup("groupFive");
        System.out.println("Succesfully logged in !");
        System.out.println("");
        boolean login = stub.userLogin(usrname, pswd);
        if(login) {
            System.out.println("Good day " + usrname);
            ArrayList<String> listProj = new ArrayList<>();
            listProj = stub.displayAllProjects();
            childtitleized("Current project");
            for (int i = 0; i < listProj.size(); i++) {
                System.out.println(listProj.get(i));
            }
            System.out.println("");
            System.out.println("Enter project name: ");
            String projChoice = usrInput.nextLine();
            boolean validLeader = stub.userType(usrname, projChoice);
            if(!validLeader){
                childtitleized("LEADER MODE");
                int choice = 0;
                do {
                    displayLeaderMenu();
                    System.out.print("Enter you choice: ");
                    choice  = Integer.parseInt(usrInput.nextLine());
                    switch(choice) {
                        case 1: 
                            ArrayList<String> myProjLead = new ArrayList<>();
                            myProjLead = stub.displayProjLead(usrname);
                            System.out.println("List of project you lead");
                            for (int i = 0; i < myProjLead.size(); i++){
                                System.out.println(myProjLead.get(i));
                            }
                            pressToContinue();
                            break;
                        case 2:
                            ArrayList<String> myProj = new ArrayList<>();
                            myProj = stub.displayProject(usrname);
                            System.out.println("List of your project");
                            for (int i = 0; i < myProj.size(); i++){
                                System.out.println(myProj.get(i));
                            }
                            pressToContinue();
                            break;
                        case 3:
                             int projID = stub.getProjectID(projChoice);
                             System.out.print("Enter username of member to be added to this project: ");
                             String member = usrInput.nextLine();
                             String z = stub.addMember(member, projID);
                             System.out.println(z);
                             pressToContinue();
                             break;
                        case 4: 
                            int projIDToo = stub.getProjectID(projChoice);
                            System.out.print("Enter username of member to be removed from this project: ");
                            String dMember = usrInput.nextLine();
                            String y = stub.removeMember(dMember, projIDToo);
                            System.out.println(y);
                            pressToContinue();
                            break;
                        case 5:
                            int stat = stub.getProjectID(projChoice);
                            String t = stub.completedProject(stat);
                            System.out.println(t);
                            pressToContinue();
                            break;
                        case 6: 
                            int upload = stub.getProjectID(projChoice);
                            System.out.print("Enter file path of file to be uploaded: ");
                            String up = usrInput.nextLine();
                            String load = stub.uploadFile(up, usrname, upload);
                            System.out.println(load);
                            pressToContinue();
                            break;
                        case 7:
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Your choice didnt match to menu !");
                    }
                }while(choice != 7);
            } else {
                childtitleized("USER MODE");
                int myChoice = 0;
                do {
                    displayMemberMenu();
                    System.out.print("Enter your choice: ");
                    myChoice = Integer.parseInt(usrInput.nextLine());
                    switch(myChoice) {
                        case 1:
                            ArrayList<String> myprojects = new ArrayList<>();
                            myprojects = stub.viewUserProjects(usrname);
                            System.out.println("Your projects:");
                            for(int i = 0; i < myprojects.size(); i++) {
                                System.out.println(myprojects.get(i));
                            }
                            pressToContinue();
                            break;
                        case 2:
                            int projID = stub.getProjectID(projChoice);
                            System.out.print("Enter file path of file to be uploaded: ");
                            String up = usrInput.nextLine();
                            String load = stub.uploadFile(up, usrname, projID);
                            System.out.println(load);
                            pressToContinue();
                            break;
                        case 3:
                            System.exit(0);
                            break;
                        default: 
                            System.out.println("Your choice didnt match to menu !");
                    }
                }while(myChoice != 3);
            }
        } else {
            System.out.println("Your credentials didnt match to our record");
        }
    }
}
