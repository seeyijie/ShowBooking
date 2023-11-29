package org.modules;

import org.modules.command.*;
import org.modules.store.InMemBookingStore;
import org.modules.store.InMemShowStore;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("====");
        System.out.println("Welcome to Show Booking System");
        System.out.println("====");
        System.out.println("Supported Commands:");
        System.out.println("1. Setup <Show Number> <Number of Rows> <Number of Seats per row> <Cancellataion Window in Minutes>");
        System.out.println("2. View <Show Number>");
        System.out.println("3. Available <Show Number>");
        System.out.println("4. Book <Show Number> <Phone #> <Comma separated list of seats>");
        System.out.println("5. Cancel <Ticket #> <Phone #>");
        System.out.println("====");
        System.out.println("Enter your command:");
        System.out.println("====");
        Scanner scanner = new Scanner(System.in);
        InMemShowStore store = new InMemShowStore();
        InMemBookingStore bookingStore = new InMemBookingStore();
        while (scanner.hasNextLine()) {
            try {
                String[] commandline = scanner.nextLine().split(" ");
                String command = commandline[0].toUpperCase();
                String[] params = Arrays.copyOfRange(commandline, 1, commandline.length);
                CommandHandler.execute(command, params, store, bookingStore);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}