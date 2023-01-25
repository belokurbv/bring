package org.bobocode.exception;

public class NoUniqueBeanException extends RuntimeException {
    public NoUniqueBeanException(Class<?> beanType) {
        super("Defined more than one bean with type " + beanType.getName());
    }
}
