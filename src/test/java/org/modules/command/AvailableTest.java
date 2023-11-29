package org.modules.command;

import org.junit.jupiter.api.Test;
import org.modules.InMemShowStore;
import org.modules.Show;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AvailableTest {
    private static final InMemShowStore store = new InMemShowStore();

    @Test
    public void testAvailableGivenShowNumberDoesNotExist() {
        Available available = new Available(1);
        Exception e = assertThrows(RuntimeException.class, () -> available.execute(store));
        assert e.getMessage().equals("Show does not exist");
    }

    @Test
    public void testAvailableGivenShowNumberExists() {
        int showNumber = 2;
        store.put(showNumber, new Show(showNumber, 4, 4, 1));
        Available available = new Available(showNumber);
        List<String> availableSeats = available.execute(store);
        assert availableSeats.size() == 16;
        List<String> expectedSeats = List.of("A0", "A1", "A2", "A3", "B0", "B1", "B2", "B3", "C0", "C1", "C2", "C3", "D0", "D1", "D2", "D3");
        assert availableSeats.containsAll(expectedSeats);
    }
}
