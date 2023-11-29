package org.modules.command;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modules.InMemShowStore;
import org.modules.Show;
import org.modules.command.Setup;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetupTest {
    private static InMemShowStore store;

    @BeforeAll
    public static void setup() {
        store = new InMemShowStore();
    }

    @Test
    public void testExecute() {
        Show show = new Show(1, 10, 10, 10);
        Setup setup = new Setup(show);
        setup.execute(store);
        Show savedShow = store.get(1);
        assert savedShow.getShowNumber() == 1;
        assert savedShow.getNumOfRows() == 10;
        assert savedShow.getSeatsPerRow() == 10;
        assert savedShow.getCancellationWindow() == 10;

        Setup setup2 = new Setup(show);
        Exception e = assertThrows(RuntimeException.class, () -> setup2.execute(store));
        assert e.getMessage().equals("Show already exists");
    }
}
