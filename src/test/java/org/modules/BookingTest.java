package org.modules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {
    @Test
    public void createdSuccessfully() {
        Long startTime = System.currentTimeMillis();
        Show show = new Show(1, 3, 3, 1);
        try {
            Booking booking = new Booking(1, List.of("A0", "A1"), show);
            assertDoesNotThrow(() -> UUID.fromString(booking.getBookingId()));
            assert booking.getSeatNumber().size() == 2;
            assert booking.getShowNumber() == 1;
            assert booking.getTimestamp() >= startTime;
            assert booking.getTimestamp() <= System.currentTimeMillis();
        } catch (Exception ignored) {
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"A3", "D0", "-1", "123", "!@#$", "Z1", "", " ", "\t"})
    public void testBook(String input) {
        Show show = new Show(1, 3, 3, 1);
        Exception e = assertThrows(Exception.class, () -> new Booking(1, List.of(input), show));
        assertEquals("Invalid seat number", e.getMessage());
    }
}
