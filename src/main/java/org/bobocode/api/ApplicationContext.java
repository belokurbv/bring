package org.bobocode.api;

import org.bobocode.exception.NoSuchBeanException;
import org.bobocode.exception.NoUniqueBeanException;

import java.util.Map;

public interface ApplicationContext {
    <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException;

    <T> T getBean(String name, Class<T> beanType);

    <T> Map<String, T> getAllBeans(Class<T> beanType);
}
