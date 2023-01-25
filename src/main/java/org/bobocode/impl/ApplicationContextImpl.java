package org.bobocode.impl;

import lombok.SneakyThrows;
import org.bobocode.annotation.Bean;
import org.bobocode.annotation.Inject;
import org.bobocode.api.ApplicationContext;
import org.bobocode.exception.NoSuchBeanException;
import org.bobocode.exception.NoUniqueBeanException;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.*;

import static java.util.stream.Collectors.toMap;

public class ApplicationContextImpl implements ApplicationContext {
    private final String basePackage;

    public final Map<String, Object> beans;

    public ApplicationContextImpl(String basePackage) {
        this.basePackage = basePackage;
        this.beans = new HashMap<>();
        init();
    }

    @SneakyThrows
    private void init() {
        var reflections = new Reflections(basePackage);
        var classes = reflections.getTypesAnnotatedWith(Bean.class);
        var postProcessingBeans = new HashMap<Object, List<Field>>();
        for (var clazz : classes) {
            var bean = Arrays.stream(clazz.getConstructors()).findAny()
                    .orElseThrow(RuntimeException::new).newInstance();
            var beanName = generateBeanName(clazz);
            beans.put(beanName, bean);
            postProcessingBeans.put(bean, Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .toList()
            );
        }
        initPostProcess(postProcessingBeans);
    }

    @SneakyThrows
    private void initPostProcess(HashMap<Object, List<Field>> postProcessingBeans) {
        for (var entry : postProcessingBeans.entrySet()) {
            for (var field : entry.getValue()) {
                field.setAccessible(true);
                var value = field.getAnnotation(Inject.class).value();
                if (value.isEmpty()) {
                    field.set(entry.getKey(), getBean(field.getType()));
                } else {
                    field.set(entry.getKey(), beans.get(value));
                }
            }
        }
    }

    private static String generateBeanName(Class<?> clazz) {
        var value = clazz.getAnnotation(Bean.class).value();
        return value.isEmpty() ? getClassName(clazz) : value;
    }

    private static String getClassName(Class<?> clazz) {
        var className = clazz.getSimpleName();
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        var beanTypeBeans = this.getAllBeans(beanType);

        if (beanTypeBeans.size() > 1) {
            throw new NoUniqueBeanException(beanType);
        }

        return beanTypeBeans.entrySet()
                .stream()
                .findFirst()
                .map(Map.Entry::getValue)
                .map(beanType::cast)
                .orElseThrow(() -> new NoSuchBeanException(beanType));
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) {
        return Optional.ofNullable(beans.get(name))
                .map(beanType::cast)
                .orElseThrow(() -> new NoSuchBeanException(beanType));
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return beans.entrySet()
                .stream()
                .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClass()))
                .collect(toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }
}
