package org.modules.command;

import org.modules.Booking;
import org.modules.Show;
import org.modules.Store;
import org.modules.command.*;

import java.util.List;

public class CommandHandler {
    public static void execute(String commandName, String[] params, Store<Show> showStore, Store<Booking> bookingStore) throws Exception {
        switch (commandName) {
            case "SETUP": {
                if (params.length != 4) throw new IllegalArgumentException("SETUP command requires 4 arguments");
                Integer showNumber = Integer.valueOf(params[0]);
                Integer rows = Integer.valueOf(params[1]);
                Integer seatsPerRow = Integer.valueOf(params[2]);
                Integer cancellationWindow = Integer.valueOf(params[3]);
                Show show = new Show(showNumber, rows, seatsPerRow, cancellationWindow);
                Command<Show, Show> setup = new Setup(show);
                setup.execute(showStore);
                System.out.println("Setup successful");
                break;
            }
            case "VIEW": {
                if (params.length != 1) throw new IllegalArgumentException("VIEW command require 1 argument");
                Integer showNumber = Integer.valueOf(params[0]);
                Command<Show, Show> view = new View(showNumber);
                Show show = view.execute(showStore);
                System.out.println(show);
                break;
            }
            case "BOOK": {
                if (params.length != 3) throw new IllegalArgumentException("BOOK command requires 2 arguments");
                Integer showNumber = Integer.valueOf(params[0]);
                Integer phoneNumber = Integer.valueOf(params[1]);
                List<String> seatList = List.of(params[2].split(","));
                Command<Booking, Show> book = new Book(showNumber, phoneNumber, seatList);
                Booking booked = book.execute(showStore);
                bookingStore.put(booked.getBookingId(), booked);
                System.out.println("Ticket #:" + booked.getBookingId());
                break;
            }
            case "AVAILABILITY": {
                if (params.length != 1) throw new IllegalArgumentException("AVAILABLE command require 1 argument");
                Integer showNumber = Integer.valueOf(params[0]);
                Command<List<String>, Show> available = new Available(showNumber);
                List<String> availabilityList = available.execute(showStore);
                System.out.println("Available Tickets:" + availabilityList);
                break;
            }
            case "CANCEL": {
                if (params.length != 2) throw new IllegalArgumentException("CANCEL command requires 2 arguments");
                String bookingId = params[0]; // ticket number
                Integer phoneNumber = Integer.valueOf(params[1]);
                Booking booked = bookingStore.get(bookingId);
                Cancel cancel = new Cancel(booked.getShowNumber(), phoneNumber);
                Boolean isCancelSuccess = cancel.execute(showStore);
                if (isCancelSuccess) {
                    bookingStore.remove(bookingId);
                    System.out.println("Cancelled successfully for ticket: " + booked.getBookingId());
                } else {
                    System.out.println("Cancelled unsuccessfully for ticket: " + booked.getBookingId());
                }
                break;
            }
            default:
                throw new RuntimeException("Invalid command");
        }
    }
}
