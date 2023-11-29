package org.modules;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.UUID.randomUUID;

@Getter
public class Booking {
    private String bookingId;
    private Integer showNumber;
    private List<String> seatNumber;
    private Long timestamp; // in ms

    public Booking(Integer showNumber, List<String> seatNumber, Show show) throws Exception {
        for (String s : seatNumber) {
            if (!isValidSeatNumber(s, show)) {
                throw new Exception("Invalid seat number");
            }
        }
        this.bookingId = randomUUID().toString();
        this.showNumber = showNumber;
        this.seatNumber = seatNumber;
        this.timestamp = System.currentTimeMillis();
    }

    private boolean isValidSeatNumber(String seatNumber, Show show) {
        int seatsPerRow = show.getSeatsPerRow();
        int rows = show.getNumOfRows();
        Pattern pattern = Pattern.compile("^[A-Z][0-9]$");
        Matcher matcher = pattern.matcher(seatNumber);
        if (!matcher.matches()) {
            return false;
        }
        if (seatNumber.charAt(0) - 'A' >= rows) {
            return false;
        }
        return Integer.parseInt(String.valueOf(seatNumber.charAt(1))) < seatsPerRow;
    }
}