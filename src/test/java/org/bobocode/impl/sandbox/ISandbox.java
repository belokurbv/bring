package org.bobocode.impl.sandbox;

import org.bobocode.annotation.Bean;

public interface ISandbox {
    default void hello() {
        System.out.println("hello");
    }
}
