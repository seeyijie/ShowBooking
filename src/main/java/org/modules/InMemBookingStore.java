package org.modules;

import java.util.HashMap;
import java.util.Map;

public class InMemBookingStore implements Store<Booking> {
    // bookingId to Booking
    private Map<String, Booking> bookingMap = new HashMap<>();

    @Override
    public Booking put(int key, Booking value) {
        throw new Error("Not required");
    }

    @Override
    public Booking put(String bookingId, Booking booking) {
        if (bookingMap.containsKey(bookingId)) {
            throw new RuntimeException("Booking already exists");
        }
        bookingMap.put(bookingId, booking);
        return booking;
    }

    public Boolean remove(String bookingId) {
        if (bookingMap.containsKey(bookingId)) {
            bookingMap.remove(bookingId);
            return true;
        }
        return false;
    }

    @Override
    public Booking get(int bookingId) {
        throw new Error("Not required");
    }

    @Override
    public Booking get(String id) {
        return bookingMap.get(id);
    }
}
