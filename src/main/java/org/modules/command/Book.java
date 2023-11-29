package org.modules.command;

import org.modules.Booking;
import org.modules.Show;
import org.modules.Store;

import java.util.List;
import java.util.UUID;

public class Book implements Command<Booking, Show> {
    private Integer showNumber;
    private Integer phoneNumber;
    private List<String> seatNumberList;

    public Book(Integer showNumber, Integer phoneNumber, List<String> seatNumberList) {
        this.showNumber = showNumber;
        this.phoneNumber = phoneNumber;
        this.seatNumberList = seatNumberList;
    }

    @Override
    public Booking execute(Store<Show> showStore) {
        Show show = showStore.get(showNumber);
        if (show == null) {
            throw new RuntimeException("Show does not exist");
        }
        if (show.getBooking(phoneNumber) != null) {
            throw new RuntimeException("Booking already exists for this phone number");
        }
        return show.putBooking(phoneNumber, seatNumberList, show);
    }
}
