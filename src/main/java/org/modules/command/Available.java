package org.modules.command;

import org.modules.Show;
import org.modules.Store;

import java.util.List;

public class Available implements Command<List<String>, Show> {
    private Integer showNumber;
    public Available(Integer showNumber) {
        this.showNumber = showNumber;
    }

    @Override
    public List<String> execute(Store<Show> store) {
        Show show = store.get(showNumber);
        if (show == null) {
            throw new RuntimeException("Show does not exist");
        }
        return show.viewSeating();
    }
}
