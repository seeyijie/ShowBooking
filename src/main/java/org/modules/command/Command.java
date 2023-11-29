package org.modules.command;

import org.modules.Store;

public interface Command<T, S> {
    T execute(Store<S> store) throws Exception;
}
