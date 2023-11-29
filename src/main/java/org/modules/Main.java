package org.modules;

import org.modules.command.*;

import java.util.Arrays;
import java.util.List;
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
                // args
                // every after index 0 is args
                String[] params = Arrays.copyOfRange(commandline, 1, commandline.length);
                switch (command) {
                    case "SETUP": {
                        if (params.length != 4) throw new IllegalArgumentException("SETUP command requires 4 arguments");
                        Integer showNumber = Integer.valueOf(params[0]);
                        Integer rows = Integer.valueOf(params[1]);
                        Integer seatsPerRow = Integer.valueOf(params[2]);
                        Integer cancellationWindow = Integer.valueOf(params[3]);
                        Show show = new Show(showNumber, rows, seatsPerRow, cancellationWindow);
                        Command<Show, Show> setup = new Setup(show);
                        setup.execute(store);
                        System.out.println("Setup successful");
                        break;
                    }
                    case "VIEW": {
                        if (params.length != 1) throw new IllegalArgumentException("VIEW command require 1 argument");
                        Integer showNumber = Integer.valueOf(params[0]);
                        Command<Show, Show> view = new View(showNumber);
                        Show show = view.execute(store);
                        System.out.println(show);
                        break;
                    }
                    case "BOOK": {
                        if (params.length != 3) throw new IllegalArgumentException("BOOK command requires 2 arguments");
                        Integer showNumber = Integer.valueOf(params[0]);
                        Integer phoneNumber = Integer.valueOf(params[1]);
                        List<String> seatList = List.of(params[2].split(","));
                        Command<Booking, Show> book = new Book(showNumber, phoneNumber, seatList);
                        Booking booked = book.execute(store);
                        bookingStore.put(booked.getBookingId(), booked);
                        System.out.println("Ticket #:" + booked.getBookingId());
                        break;
                    }
                    case "AVAILABLE": {
                        if (params.length != 1) throw new IllegalArgumentException("VIEW command require 1 argument");
                        Integer showNumber = Integer.valueOf(params[0]);
                        Command<List<String>, Show> available = new Available(showNumber);
                        List<String> availabilityList = available.execute(store);
                        System.out.println("Available Tickets:" + availabilityList);
                        break;
                    }
                    case "CANCEL": {
                        if (params.length != 2) throw new IllegalArgumentException("CANCEL command requires 2 arguments");
                        String bookingId = params[0]; // ticket number
                        Integer phoneNumber = Integer.valueOf(params[1]);
                        Booking booked = bookingStore.get(bookingId);
                        Cancel cancel = new Cancel(booked.getShowNumber(), phoneNumber);
                        Boolean isCancelSuccess = cancel.execute(store);
                        if (isCancelSuccess) {
                            bookingStore.remove(bookingId);
                            System.out.println("Cancelled successfully for ticket: " + booked.getBookingId());
                        } else {
                            System.out.println("Cancelled unsuccessfully for ticket: " + booked.getBookingId());
                        }
                        break;
                    }
                    default:
                        throw new Exception("Invalid command");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}