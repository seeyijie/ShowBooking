package org.modules.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modules.Booking;
import org.modules.Show;
import org.modules.store.InMemBookingStore;
import org.modules.store.InMemShowStore;

/**
 * Very simple test cases as it simulates the user input.
 * More detailed test case within the <ClassTest> itself.
 */
public class CommandHandlerTest {
    private InMemShowStore showStore = new InMemShowStore();
    private InMemBookingStore bookingStore = new InMemBookingStore();

    @BeforeEach
    public void setup() {
        String[] params = new String[] {"1", "3", "3", "1"};
        try {
            CommandHandler.execute("SETUP", params, showStore, bookingStore);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testSetup() {
        String[] params = new String[] {"100", "3", "3", "1"};
        try {
            CommandHandler.execute("SETUP", params, showStore, bookingStore);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testView() {
        String[] params = new String[] {"1"};
        try {
            CommandHandler.execute("VIEW", params, showStore, bookingStore);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testAvailable() {
        String[] params = new String[] {"1"};
        try {
            CommandHandler.execute("AVAILABLE", params, showStore, bookingStore);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testBook() {
        String[] params = new String[] {"1", "912341234", "A0,A1"};
        try {
            CommandHandler.execute("BOOK", params, showStore, bookingStore);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testCancel() {
        {
            String[] params = new String[] {"1", "912341234", "A0,A1"};
            try {
                CommandHandler.execute("BOOK", params, showStore, bookingStore);
            } catch (Exception ignored) {
            }
        }
        {
            Show show = showStore.get(1);
            Booking booking = show.getBooking(912341234);
            String[] params = new String[] {booking.getBookingId(), "912341234"};
            try {
                CommandHandler.execute("CANCEL", params, showStore, bookingStore);
            } catch (Exception ignored) {
            }
        }
    }
}
