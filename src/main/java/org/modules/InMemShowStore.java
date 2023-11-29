package org.modules;

import java.util.HashMap;
import java.util.Map;

public class InMemShowStore implements Store<Show> {
    private Map<Integer, Show> showMap;

    public InMemShowStore() {
        this.showMap = new HashMap<>();
    }

    @Override
    public Show put(int showNumber, Show show) {
        if (showMap.containsKey(show.getShowNumber())) {
            throw new RuntimeException("Show already exists");
        }
        showMap.put(showNumber, show);
        return show;
    }

    @Override
    public Show put(String key, Show value) {
        throw new Error("Not required");
    }

    @Override
    public Show get(int showNumber) {
        return showMap.get(showNumber);
    }

    @Override
    public Show get(String key) {
        throw new Error("Not required");
    }
}