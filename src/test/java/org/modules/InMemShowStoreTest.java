package org.modules;

import org.junit.jupiter.api.Test;
import org.modules.InMemShowStore;
import org.modules.Show;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemShowStoreTest {
    @Test
    public void testPutSuccess() {
        InMemShowStore store = new InMemShowStore();
        Show toInsert = new Show(1, 3, 3, 1);
        Show show = store.put(1, toInsert);
        assert show.getShowNumber() == 1;
        assert show.getNumOfRows() == 3;
        assert show.getSeatsPerRow() == 3;
        assert show.getCancellationWindow() == 1;
    }

    @Test
    public void testPutUnsuccessfulGivenShowExists() {
        InMemShowStore store = new InMemShowStore();
        Show toInsert = new Show(1, 3, 3, 1);
        store.put(1, toInsert);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> store.put(1, toInsert));
        assertEquals("Show already exists", exception.getMessage());
    }

    @Test
    public void testGetSuccess() {
        InMemShowStore store = new InMemShowStore();
        Show toInsert = new Show(1, 3, 3, 1);
        store.put(1, toInsert);
        Show show = store.get(1);
        assert show.getShowNumber() == 1;
        assert show.getNumOfRows() == 3;
        assert show.getSeatsPerRow() == 3;
        assert show.getCancellationWindow() == 1;
    }

}
