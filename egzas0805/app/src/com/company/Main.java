package com.company;

import com.company.DAO.MenuDAO;
import com.company.DAO.UserDAO;
import com.company.Entity.Menu;
import com.company.Entity.User;
import com.company.Utils.HibernateUtil;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String loginUsername;
        String loginPassword;
        String regUsername;
        String regPassword;
        int welcomeNumber;
        int firstTime;
        boolean exit = false;
        UserDAO userDAO = new UserDAO();
        Scanner scanner = new Scanner(System.in);
        MenuDAO menuDAO = new MenuDAO();


        System.out.println("First time using the application? (asking to make intial users and dishes), 1 for yes, 2 for no -");
        firstTime = scanner.nextInt();
        if (firstTime == 1){
            User customer = new User("customer", "customer", "user");
            User admin = new User("admin", "admin","admin");
            userDAO.addUser(customer);
            userDAO.addUser(admin);
            Menu dish1 = new Menu("Cepelinai", "Su mesa");
            Menu dish2 = new Menu("Saltibarsciai", "Salta buroku sriuba");
            Menu dish3 = new Menu("Blynai", "Su kumpiu ir suriu");
            menuDAO.initialAdd(dish1);
            menuDAO.initialAdd(dish2);
            menuDAO.initialAdd(dish3);
        }

        while (!exit) {
            System.out.println("Welcome! Press 1 for login, 2 to register a new user, 3 to exit the application: ");
            welcomeNumber = scanner.nextInt();
            if (welcomeNumber == 1) {
                System.out.println("Enter your username: ");
                loginUsername = scanner.next();
                System.out.println("Enter your password: ");
                loginPassword = scanner.next();
                if (userDAO.login(loginUsername, loginPassword)) {
                    if (userDAO.getUserRole(loginUsername).equals("user")) {
                        userMenu();
                    } else if (userDAO.getUserRole(loginUsername).equals("admin")) {
                        adminMenu();
                    }
                }

            } else if (welcomeNumber == 2) {
                System.out.println("Enter your username: ");
                regUsername = scanner.next();
                System.out.println("Enter your password: ");
                regPassword = scanner.next();
                userDAO.register(regUsername, regPassword);
            } else if (welcomeNumber == 3) {
                exit = true;
            }

            HibernateUtil.getSessionFactory().close();
            }
        }




    public static void userMenu() {
        MenuDAO menuDAO = new MenuDAO();
        Scanner scanner = new Scanner(System.in);
        int dish;
        int count;
        int orderAgain = 1;
        System.out.println("Hello here is the menu :)");

        while(orderAgain == 1) {
            System.out.println("What would you like to order?");
            menuDAO.allDishesUser();
            System.out.println("Enter the id of the dish u want to order - ");
            dish = scanner.nextInt();
            System.out.println("How many dishes? - ");
            count = scanner.nextInt();
            menuDAO.orderFood(dish, count);
            System.out.println("Order complete! Would you like to order something else? 1 - for yes, 2 - for no");
            orderAgain = scanner.nextInt();
        }
    }

    public static void adminMenu() {

        MenuDAO menuDAO = new MenuDAO();
        Scanner scanner = new Scanner(System.in);
        int operation;
        int dishDelete;
        int dishEdit;
        int approveOrder;
        String editName;
        String editDesc;
        String addDish;
        String descDish;
        System.out.println("Hello admin, nice to see you!");
        while (true) {
            System.out.println("Select an operation, 1 - to add dish, 2 - to delete a dish, 3 - edit a dish, 4 - approve orders, 5 - log out\n" +
                    "Enter an opertions number: ");
            operation = scanner.nextInt();
            if (operation == 1) {
                System.out.println("Adding new dish");
                System.out.println("Name - ");
                scanner.nextLine();
                addDish = scanner.nextLine();
                System.out.println("Description - ");
                descDish = scanner.nextLine();
                System.out.println("New dish added!");
                Menu newDish = new Menu(addDish, descDish);
                menuDAO.addDish(newDish);
            } else if (operation == 2) {
                menuDAO.allDishesUser();
                System.out.println("Showing all dishes that are not ordered");
                System.out.println("Enter id of the dish u want to delete - ");
                dishDelete = scanner.nextInt();
                menuDAO.deleteDishByID(dishDelete);
            } else if (operation == 3) {
                menuDAO.allDishesUser();
                System.out.println("Showing all dishes that are not ordered");
                System.out.println("Enter id of the dish u want to edit - ");
                dishEdit = scanner.nextInt();
                System.out.println("Enter new name - ");
                scanner.nextLine();
                editName = scanner.nextLine();
                System.out.println("Enter new description - ");
                editDesc = scanner.nextLine();
                menuDAO.editDish(dishEdit, editName, editDesc);
                System.out.println("Dish edited!");
            } else if (operation == 4) {
                System.out.println("Orders that need approval : ");
                menuDAO.allDishesAdmin();
                System.out.println("Enter id of the order u want to approve ");
                approveOrder = scanner.nextInt();
                menuDAO.approveOrder(approveOrder, 0);

            } else if (operation == 5) {
                System.out.println("Logging out");
                break;
            }
        }


    }
}