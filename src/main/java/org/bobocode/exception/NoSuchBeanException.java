package org.bobocode.exception;

public class NoSuchBeanException extends RuntimeException {
    public NoSuchBeanException(Class<?> beanType) {
        super("Can not find bean with type " + beanType.getName());
    }

    public NoSuchBeanException(String beanName) {
        super("Can not find bean with name " + beanName);
    }
}
