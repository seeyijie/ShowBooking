package org.modules;


import lombok.Getter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Show {
    private Integer showNumber;
    private Integer numOfRows;
    private Integer seatsPerRow;
    private Integer cancellationWindow; // in minutes
    private boolean[][] seats; // number of rows and seats per row
    private Map<Integer, Booking> phoneToBookingMap; // phone number to booking

    public Show(Integer showNumber, Integer numOfRows, Integer seatsPerRow, Integer cancellationWindow) {
        if (showNumber < 0) {
            throw new IllegalArgumentException("Show number must be a positive integer");
        }
        if (numOfRows < 1 || numOfRows > 26) {
            throw new IllegalArgumentException("Number of rows must be in the range 1 to 26");
        }
        if (seatsPerRow < 1 || seatsPerRow > 10) {
            throw new IllegalArgumentException("Number of seats per row must be in the range 1 to 10");
        }
        if (cancellationWindow < 1 || cancellationWindow > 10) {
            throw new IllegalArgumentException("Cancellation window must be in the range 1 to 10");
        }
        this.showNumber = showNumber;
        this.numOfRows = numOfRows;
        this.seatsPerRow = seatsPerRow;
        this.cancellationWindow = cancellationWindow;
        this.seats = new boolean[numOfRows][seatsPerRow];
        // initialise them as available
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < seatsPerRow; j++) {
                this.seats[i][j] = true;
            }
        }
        this.phoneToBookingMap = new HashMap<>();
    }

    private char mapNumberToLetter(int number) {
        return (char)('A' + number);
    }
    private int mapLetterToNumber(char letter) {
        // Subtract the ASCII value of 'A' to start mapping from 0
        return letter - 'A';
    }

    public List<String> viewSeating() {
        boolean[][] seats = this.getSeats();
        List<String> seating = new ArrayList<>();
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j]) {
                    String row = String.valueOf(mapNumberToLetter(i)) + j;
                    seating.add(row);
                }
            }
        }
        return seating;
    }

    public Booking putBooking(Integer phoneNumber, List<String> seatNumberList, Show show) {
        try {
            Booking booking = new Booking(this.showNumber, seatNumberList, show);
            for (String s : seatNumberList) {
                char rowChar = s.charAt(0);
                int seatNum = Integer.parseInt(String.valueOf(s.charAt(1)));
                int rowIndex = mapLetterToNumber(rowChar);
                if (!this.seats[rowIndex][seatNum]) {
                    throw new RuntimeException("Seat " + s + " is not available.");
                }
                this.seats[rowIndex][seatNum] = false;
                this.phoneToBookingMap.put(phoneNumber, booking);
            }
            return booking;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Booking getBooking(Integer phoneNumber) {
        return this.phoneToBookingMap.get(phoneNumber);
    }

    public boolean removeBooking(Integer phoneNumber) throws Exception {
        Booking booking = this.phoneToBookingMap.get(phoneNumber);
        if (booking == null) {
            throw new Exception("Invalid phone number. Please key in the correct phone number.");
        }
        Long timestamp = booking.getTimestamp();
        if (timestamp + this.cancellationWindow * 60 * 1000 < System.currentTimeMillis()) {
            throw new Exception("Cancellation window has passed. Cannot cancel booking.");
        }
        List<String> bookingSeatNumber = booking.getSeatNumber();
        for (String s : bookingSeatNumber) {
            char rowChar = s.charAt(0);
            int seatNum = Integer.parseInt(String.valueOf(s.charAt(1)));
            int rowIndex = mapLetterToNumber(rowChar);
            this.seats[rowIndex][seatNum] = true;
        }
        this.phoneToBookingMap.remove(phoneNumber);
        return true;
    }

    @Override
    public String toString() {
        return "Show{" +
                "showNumber=" + showNumber +
                ", numOfRows=" + numOfRows +
                ", seatsPerRow=" + seatsPerRow +
                ", cancellationWindow=" + cancellationWindow +
                ", seats=" + Arrays.deepToString(seats) +
                ", phoneToBookingMapLength=" + phoneToBookingMap.entrySet().toArray().length +
                '}';
    }
}
