package org.modules.command;

import org.modules.Booking;
import org.modules.Show;
import org.modules.Store;

public class Cancel implements Command<Boolean, Show> {
    private Integer showNumber;
    private Integer phoneNumber;

    public Cancel(Integer showNumber, Integer phoneNumber) {
        this.showNumber = showNumber;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Boolean execute(Store<Show> showStore) throws Exception {
        Show show = showStore.get(showNumber);
        if (show == null) {
            throw new RuntimeException("Show does not exist");
        }
        return show.removeBooking(phoneNumber);
    }
}
