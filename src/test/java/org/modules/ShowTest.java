package org.modules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ShowTest {
    @Test
    public void testCreateShow() {
        Show show = new Show(1, 3, 3, 1);
        assert show.getShowNumber() == 1;
        assert show.getNumOfRows() == 3;
        assert show.getSeatsPerRow() == 3;
        assert show.getCancellationWindow() == 1;
    }

    @Test
    public void testCreateShowWithInvalidShowNumber() {
        Exception e = assertThrows(Exception.class, () -> new Show(-1, 3, 3, 1));
        assertEquals("Show number must be a positive integer", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 27})
    public void testCreateShowWithInvalidNumOfRows(Integer rows) {
        Exception e = assertThrows(Exception.class, () -> new Show(1, rows, 3, 1));
        assertEquals("Number of rows must be in the range 1 to 26", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 11})
    public void testCreateShowWithInvalidSeatsPerRow(Integer seatsPerRow) {
        Exception e = assertThrows(Exception.class, () -> new Show(1, 3, seatsPerRow, 1));
        assertEquals("Number of seats per row must be in the range 1 to 10", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 11})
    public void testInvalidCancellationWindow(Integer cancellationWindow) {
        Exception e = assertThrows(Exception.class, () -> new Show(1, 3, 3, cancellationWindow));
        assertEquals("Cancellation window must be in the range 1 to 10", e.getMessage());
    }

    @Test
    public void testIsValidSeatNumber() {
        Show show = new Show(1, 3, 3, 1);
        List<String> availableSeats = show.viewSeating();
        assert availableSeats.size() == 9;
        List<String> expectedAvailableSeats = List.of("A0", "A1", "A2", "B0", "B1", "B2", "C0", "C1", "C2");
        assert availableSeats.containsAll(expectedAvailableSeats);
    }

    @Test
    public void testPutBooking() {
        Long startTime = System.currentTimeMillis();
        Show show = new Show(1, 3, 3, 1);
        try {
            Booking booked = show.putBooking(91231234, List.of("A0", "A1"), show);
            assert booked.getSeatNumber().size() == 2;
            assert booked.getSeatNumber().containsAll(List.of("A0", "A1"));
            assert booked.getShowNumber() == 1;
            assert booked.getTimestamp() >= startTime;
            assert booked.getTimestamp() <= System.currentTimeMillis();
            assertDoesNotThrow(() -> UUID.fromString(booked.getBookingId()));
            assertNotNull(show.getBooking(91231234));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"A3", "D0", "-1", "123", "!@#$", "Z1", "", " ", "\t"})
    public void testPutBookingWithInvalidSeatNumber() {
        Show show = new Show(1, 3, 3, 1);
        Exception e = assertThrows(Exception.class, () -> show.putBooking(91231234, List.of("A3"), show));
        assertEquals("Invalid seat number", e.getMessage());
        assertNull(show.getBooking(91231234));
    }

    @Test
    public void testRemoveBooking() {
        Show show = new Show(1, 3, 3, 1);
        try {
            show.putBooking(91231234, List.of("A2"), show);
            assert show.viewSeating().size() == 8;
            assert show.viewSeating().containsAll(List.of("A0", "A1", "B0", "B1", "B2", "C0", "C1", "C2"));
            assert show.removeBooking(91231234);
            assertNull(show.getBooking(91231234));
            assert show.viewSeating().size() == 9;
            assert show.viewSeating().containsAll(List.of("A0", "A1", "A2", "B0", "B1", "B2", "C0", "C1", "C2"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRemoveBookingWithInvalidPhoneNumber() {
        Show show = new Show(1, 3, 3, 1);
        try {
            Exception e = assertThrows(Exception.class, () -> show.removeBooking(91231234));
            assertEquals("Invalid phone number. Please key in the correct phone number.", e.getMessage());
            assertNull(show.getBooking(91231234));
            List<String> availableSeats = show.viewSeating();
            assert availableSeats.size() == 9;
            List<String> expectedAvailableSeats = List.of("A0", "A1", "A2", "B0", "B1", "B2", "C0", "C1", "C2");
            assert availableSeats.containsAll(expectedAvailableSeats);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRemoveBookingWithCancellationWindowPassed() {
        Show show = new Show(1, 3, 3, 1);
        try {
            show.putBooking(91231234, List.of("A2"), show);
            assert show.viewSeating().size() == 8;
            assert show.viewSeating().containsAll(List.of("A0", "A1", "B0", "B1", "B2", "C0", "C1", "C2"));
            Thread.sleep(61000);
            Exception e = assertThrows(Exception.class, () -> show.removeBooking(91231234));
            assertEquals("Cancellation window has passed. Cannot cancel booking.", e.getMessage());
            assertNotNull(show.getBooking(91231234));
            assert show.viewSeating().size() == 8;
            assert show.viewSeating().containsAll(List.of("A0", "A1", "B0", "B1", "B2", "C0", "C1", "C2"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
