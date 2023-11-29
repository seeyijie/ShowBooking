package org.modules.command;

import org.junit.jupiter.api.Test;
import org.modules.store.InMemShowStore;
import org.modules.Show;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ViewTest {
    private static final InMemShowStore store = new InMemShowStore();

    @Test
    public void testViewGivenShowNumberDoesNotExist() {
        View view = new View(1);
        Exception e = assertThrows(RuntimeException.class, () -> view.execute(store));
        assert e.getMessage().equals("Show does not exist");
    }

    @Test
    public void testViewGivenShowNumberExists() {
        int showNumber = 2;
        store.put(showNumber, new Show(showNumber, 10, 10, 10));
        View view = new View(showNumber);
        Show show = view.execute(store);
        assert show.getShowNumber() == showNumber;
    }
}
