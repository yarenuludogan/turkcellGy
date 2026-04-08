package com.turkcell;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerRepo customerRepo = new CustomerRepo();
        boolean run = true;


        customerRepo.addCustomer(new Customer(1,"Pam", 1000.0, "CasinoNight"));
        customerRepo.addCustomer(new Customer(2,"Jim", 1500.0, "FunRun"));
        customerRepo.addCustomer(new Customer(1,"Michale", 500.0, "holly"));
        customerRepo.addCustomer(new Customer(2,"Dwight", 1200.0, "angela"));
        while(run){
            System.out.println("\n=== Banking App ===");
            System.out.println("1. List Customers");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Log out");
            System.out.print("Choose your operation: ");
        int operation = scanner.nextInt();
        switch(operation){
            case 1: 
            for(Customer cus: customerRepo.getAllCustomers()){
                System.out.println(cus);
            }
            break;
            case 2:
                    System.out.print("Customer ID: ");
                    int idDeposit = scanner.nextInt();
                    System.out.print("Amount: ");
                    double dep = scanner.nextDouble();
                    Customer cusDep = customerRepo.findId(idDeposit);
                    if (cusDep != null) {
                        cusDep.deposit(dep);
                        System.out.println("New balance: " + cusDep.getBalance());
                    } else {
                        System.out.println("Couldn't fing any customers");
                    }
                    break;
            case 3:
                    System.out.print("Customer ID: ");
                    int idWithdraw = scanner.nextInt();
                    System.out.print("Amount: ");
                    double wd = scanner.nextDouble();
                    Customer cusWd = customerRepo.findId(idWithdraw);
                    if (cusWd != null) {
                        cusWd.withdraw(wd);
                        System.out.println("New balance: " + cusWd.getBalance());
                    } else {
                        System.out.println("Couldn't fing any customers");
                    }
                    break;
            case 4:
                    run = false;
                    System.out.println("Logging out");
                    break;
                default:
                    System.out.println("Error");
            }
        }

        scanner.close();
    }
}