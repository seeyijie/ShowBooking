package org.modules.command;

import org.modules.Show;
import org.modules.Store;

import java.util.List;

public class View implements Command<Show, Show> {
    private Integer showNumber;

    public View(Integer showNumber) {
        this.showNumber = showNumber;
    }

    @Override
    public Show execute(Store<Show> store) {
        if (store.get(showNumber) == null) {
            throw new RuntimeException("Show does not exist");
        }
        return store.get(showNumber);
    }
}
