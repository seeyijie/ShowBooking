package org.modules.command;

import org.modules.Show;
import org.modules.Store;

public class Setup implements Command<Show, Show> {
    private Integer showNumber;
    private Integer numOfRows;
    private Integer seatsPerRow;
    private Integer cancellationWindow; // in minutes

    public Setup(Show show) {
        this.showNumber = show.getShowNumber();
        this.numOfRows = show.getNumOfRows();
        this.seatsPerRow = show.getSeatsPerRow();
        this.cancellationWindow = show.getCancellationWindow();
    }
    @Override
    public Show execute(Store<Show> store) {
        Show show = new Show(showNumber, numOfRows, seatsPerRow, cancellationWindow);
        if (store.get(show.getShowNumber()) != null) {
            throw new RuntimeException("Show already exists");
        }
        store.put(show.getShowNumber(), show);
        return show;
    }
}
