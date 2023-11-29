package org.modules;

import java.util.UUID;

public interface Store<T> {
    T put(int key, T value);

    T put(String key, T value);

    T get(int key);

    T get(String key);

    Boolean remove(String key);

}
