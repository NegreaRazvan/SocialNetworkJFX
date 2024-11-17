package map.ui;

import map.domain.validators.ValidationException;
import map.service.ServiceInterface;

import java.util.Scanner;

public class Ui {
    private ServiceInterface service;
    private Scanner sc;

    public Ui(ServiceInterface service) {
        this.service = service;
        sc = new Scanner(System.in);
    }

    private static final String options="1.Add user\n2.Delete user\n3.Update user\n4.Find a user\n5.Show all user\n" +
            "6.Add friend link\n7.Delete friend link\n8.Show all friend links\n9.Number of components\n10.Longest path\n0.Exit\n";

    private void findOneUser(){
        System.out.println("Choose an id: ");
        long id = sc.nextLong();
        var u = service.findOneUser(id);
        System.out.println("User: " + u);
    }

    private void findAllUsers(){
        var users = service.findAllUsers();
        System.out.println("Users:");
        users.forEach(System.out::println);
    }

    private void saveUser() throws ValidationException{
        System.out.println("Enter firstName: ");
        String firstName = sc.next();
        System.out.println("Enter lastName: ");
        String lastName = sc.next();
        System.out.println("Enter password: ");
        String password = sc.next();
        service.saveUser(firstName, lastName, password);
        System.out.println("User saved");
    }

    private void deleteUser() {
        System.out.println("Enter id: ");
        long id = sc.nextLong();
        service.deleteUser(id);
        System.out.println("User deleted");
    }

    private void updateUser() {
        System.out.println("Enter id: ");
        long id = sc.nextLong();
        System.out.println("Enter firstName: ");
        String firstName = sc.next();
        System.out.println("Enter lastName: ");
        String lastName = sc.next();
        System.out.println("Enter password: ");
        String password = sc.next();
        service.updateUser(id, firstName, lastName,password);
        System.out.println("User updated");
    }

    private void saveFriend() {
        System.out.println("Enter first user: ");
        long id = sc.nextLong();
        System.out.println("Enter second user: ");
        long friendId = sc.nextLong();
        service.saveFriend(id, friendId);
        System.out.println("Friend link saved");
    }

    private void deleteFriend() {
        System.out.println("Enter id: ");
        long id = sc.nextLong();
        service.deleteFriend(id);
        System.out.println("Friend link deleted");
    }

    private void findAllFriends() {
        var friends = service.findAllFriends();
        System.out.println("Friends: ");
        friends.forEach(System.out::println);
    }

    private void nrComponents(){
        System.out.println("Number of components: " + service.numberOfConnectedComponents());
    }

    private void longestPath(){
        var path = service.longestPath();
        System.out.println("Longest path: " + path);
    }

    public void start(){
        while(true){
            System.out.println(options);
            System.out.print("Enter command: ");
            int op=sc.nextInt();
            try {
                switch(op){
                    case 0 -> {
                        return;
                    }
                    case 1 ->saveUser();
                    case 2 ->deleteUser();
                    case 3 ->updateUser();
                    case 4 ->findOneUser();
                    case 5 ->findAllUsers();
                    case 6 ->saveFriend();
                    case 7 ->deleteFriend();
                    case 8 ->findAllFriends();
                    case 9 ->nrComponents();
                    case 10 ->longestPath();
                }
            }catch (ValidationException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
