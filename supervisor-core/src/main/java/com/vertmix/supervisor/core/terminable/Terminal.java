package com.vertmix.supervisor.core.terminable;

import java.io.Closeable;
import java.io.IOException;

public interface Terminal extends Closeable {

    default void closeSilently() {
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
