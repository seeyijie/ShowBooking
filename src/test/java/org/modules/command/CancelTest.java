package org.modules.command;

import org.junit.jupiter.api.Test;
import org.modules.InMemShowStore;
import org.modules.Show;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CancelTest {
    private static final InMemShowStore showStore = new InMemShowStore();

    @Test
    public void testExecuteGivenNoShowNumber() {
        Cancel cancel = new Cancel(1, 1);
        Exception e = assertThrows(RuntimeException.class, () -> cancel.execute(showStore));
        assert e.getMessage().equals("Show does not exist");
    }

    @Test
    public void testExecuteGivenShowNumberButNoBooking() {
        int showNumber = 2;
        Show show = new Show(showNumber, 10, 10, 1);
        showStore.put(showNumber, show);
        Cancel cancel = new Cancel(showNumber, 1);
        Exception e = assertThrows(Exception.class, () -> cancel.execute(showStore));
        assert e.getMessage().equals("Invalid phone number. Please key in the correct phone number.");
    }

    @Test
    public void testExecuteGivenCancellationWindowExceeded() throws InterruptedException {
        int showNumber = 3;
        int phoneNumber = 912341234;
        Show savedShow = showStore.put(showNumber, new Show(showNumber, 10, 10, 1));
        savedShow.putBooking(phoneNumber, List.of("A1", "B1"), savedShow);
        Cancel cancel = new Cancel(showNumber, 912341234);
        Thread.sleep(61000);
        Exception e = assertThrows(Exception.class, () -> cancel.execute(showStore));
        assert e.getMessage().equals("Cancellation window has passed. Cannot cancel booking.");
    }

    @Test
    public void testExecuteSuccessfully() throws Exception {
        int showNumber = 4;
        int phoneNumber = 912341234;
        Show savedShow = showStore.put(showNumber, new Show(showNumber, 4, 4, 1));
        savedShow.putBooking(phoneNumber, List.of("A1", "B1"), savedShow);
        Cancel cancel = new Cancel(showNumber, 912341234);
        cancel.execute(showStore);
        assert savedShow.getBooking(phoneNumber) == null;
        boolean[][] seats = savedShow.getSeats();
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                assert seats[i][j];
            }
        }
        assertTrue(
                savedShow.viewSeating().containsAll(List.of("A0", "A2", "A3", "B0", "B2", "B3", "C0", "C1", "C2", "C3", "D0", "D1", "D2", "D3"))
        );
    }
}
