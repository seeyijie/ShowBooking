package org.modules.command;

import org.junit.jupiter.api.Test;
import org.modules.Booking;
import org.modules.store.InMemShowStore;
import org.modules.Show;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookTest {
    private static final InMemShowStore store = new InMemShowStore();

    @Test
    public void testBookGivenNoShowNumberFound() {
        Book book = new Book(1, 1, List.of("A1", "A2"));
        Exception e = assertThrows(RuntimeException.class, () -> book.execute(store));
        assert e.getMessage().equals("Show does not exist");
    }

    @Test
    public void testBookGivenShowNumberExists() {
        Long startTime = System.currentTimeMillis();
        int showNumber = 2;
        int numRows = 3;
        int seatsPerRows = 3;
        store.put(showNumber, new Show(showNumber, numRows, seatsPerRows, 10));
        Book book = new Book(showNumber, 912341234, List.of("B1", "A2"));
        book.execute(store);

        Booking savedBooking = store.get(showNumber).getBooking(912341234);
        assert savedBooking.getTimestamp() >= startTime;
        assert savedBooking.getTimestamp() <= System.currentTimeMillis();
        assert savedBooking.getShowNumber() == showNumber;
        assert savedBooking.getSeatNumber().containsAll(List.of("B1", "A2"));

        // set up expected seats
        boolean[][] expectedSeats = new boolean[3][3];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < seatsPerRows; j++) {
                expectedSeats[i][j] = true;
            }
        }
        // define seats to be booked
        expectedSeats[0][2] = false;
        expectedSeats[1][1] = false;
        assertArrayEquals(expectedSeats, store.get(showNumber).getSeats());
    }

    @Test
    public void shouldFailGivenThatADifferentPhoneNumberIsUsedToBookAnUnavailableSeat() {
        Long startTime = System.currentTimeMillis();
        int showNumber = 3;
        int numRows = 3;
        int seatsPerRows = 3;
        int phoneNumberA = 912341234;
        int phoneNumberB = 812341234;
        store.put(showNumber, new Show(showNumber, numRows, seatsPerRows, 10));
        Book book = new Book(showNumber, phoneNumberA, List.of("B1", "A2"));
        book.execute(store);

        Booking savedBooking = store.get(showNumber).getBooking(phoneNumberA);
        assert savedBooking.getTimestamp() >= startTime;
        assert savedBooking.getTimestamp() <= System.currentTimeMillis();
        assert savedBooking.getShowNumber() == showNumber;
        assert savedBooking.getSeatNumber().containsAll(List.of("B1", "A2"));

        Book book2 = new Book(showNumber, phoneNumberB, List.of("B1"));
        Exception e = assertThrows(RuntimeException.class, () -> book2.execute(store));
        assert e.getMessage().equals("One or more seats are unavailable.");
    }

    @Test
    public void shouldFailAtomicallyIfAnyOfTheSeatsIsUnavailable() {
        Long startTime = System.currentTimeMillis();
        int showNumber = 4;
        int numRows = 3;
        int seatsPerRows = 3;
        int phoneNumberA = 912341234;
        int phoneNumberB = 812341234;
        store.put(showNumber, new Show(showNumber, numRows, seatsPerRows, 10));
        Book book = new Book(showNumber, phoneNumberA, List.of("B1", "A2"));
        book.execute(store);

        Booking savedBooking = store.get(showNumber).getBooking(phoneNumberA);
        assert savedBooking.getTimestamp() >= startTime;
        assert savedBooking.getTimestamp() <= System.currentTimeMillis();
        assert savedBooking.getShowNumber() == showNumber;
        assert savedBooking.getSeatNumber().containsAll(List.of("B1", "A2"));

        Book book2 = new Book(showNumber, phoneNumberB, List.of("B0", "B1"));
        Exception e = assertThrows(RuntimeException.class, () -> book2.execute(store));
        System.out.println(e.getMessage());
        assert e.getMessage().equals("One or more seats are unavailable.");

        Show show = store.get(showNumber);
        List<String> availableSeating = show.viewSeating();
        System.out.println(availableSeating);
        List<String> expectedSeating = List.of("A0", "A1", "B0", "B2", "C0", "C1", "C2");
        assert availableSeating.containsAll(expectedSeating);
    }

}
