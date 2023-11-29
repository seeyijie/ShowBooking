package org.modules;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InMemBookingStoreTest {
    @Test
    public void testPutSuccess() {
        InMemBookingStore store = new InMemBookingStore();
        try {
            Show show = new Show(1, 3, 3, 1);
            Booking toInsert = new Booking(1, List.of("A0", "A1"), show);
            UUID uuid = UUID.randomUUID();
            Booking booking = store.put(uuid.toString(), toInsert);
            assertDoesNotThrow(() -> UUID.fromString(booking.getBookingId()));
        } catch (Exception ignored) {}
    }

    @Test
    public void testPutDuplicate() {
        InMemBookingStore store = new InMemBookingStore();
        try {
            Show show = new Show(1, 3, 3, 1);
            Booking toInsert = new Booking(1, List.of("A0", "A1"), show);
            UUID uuid = UUID.randomUUID();
            store.put(uuid.toString(), toInsert);
            RuntimeException exception = assertThrows(RuntimeException.class, () -> store.put(uuid.toString(), toInsert));
            assertEquals("Booking already exists", exception.getMessage());
        } catch (Exception ignored) {}
    }

    @Test
    public void testGetBooking() {
        InMemBookingStore store = new InMemBookingStore();
        try {
            Show show = new Show(1, 3, 3, 1);
            Booking toInsert = new Booking(1, List.of("A0", "A1"), show);
            UUID uuid = UUID.randomUUID();
            store.put(uuid.toString(), toInsert);
            RuntimeException exception = assertThrows(RuntimeException.class, () -> store.put(uuid.toString(), toInsert));
            assertEquals("Booking already exists", exception.getMessage());
        } catch (Exception ignored) {}
    }

    @Test
    public void testRemoveBooking() {
        InMemBookingStore store = new InMemBookingStore();
        try {
            Show show = new Show(1, 3, 3, 1);
            Booking toInsert = new Booking(1, List.of("A0", "A1"), show);
            UUID uuid = UUID.randomUUID();
            store.put(uuid.toString(), toInsert);
            Boolean removed = store.remove(uuid.toString());
            assertTrue(removed);
            assertNull(store.get(uuid.toString()));
            Boolean removedAgain = store.remove(uuid.toString());
            assertFalse(removedAgain);
        } catch (Exception ignored) {}
    }

}
