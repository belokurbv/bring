package org.bobocode.impl;

import org.bobocode.api.ApplicationContext;
import org.bobocode.exception.NoSuchBeanException;
import org.bobocode.exception.NoUniqueBeanException;
import org.bobocode.impl.sandbox.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeClass;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextImplTest {
    static ApplicationContext applicationContext;

    @BeforeAll
    static void init() {
        applicationContext = new ApplicationContextImpl("org.bobocode.impl.sandbox");
    }

    @Test
    @DisplayName("Checks bean instance class")
    void getBeanByBeanName() {
        var bean = applicationContext.getBean(Sandbox.class);
        assertEquals(bean.getClass().getName(), Sandbox.class.getName());
    }

    @Test
    @DisplayName("Non-existent bean should threw an exception")
    void getNonExistsBean() {
        assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean(NotABean.class));
    }

    @Test
    @DisplayName("Throwing exception when beans are duplicated")
    void getDuplicateBean() {
        assertThrows(NoUniqueBeanException.class, () -> applicationContext.getBean(ISandbox.class));
    }

    @Test
    @DisplayName("Getting unique bean by it's name")
    void testGetBean() {
        assertEquals(applicationContext.getBean("sampleSandbox", ISandbox.class).getClass().getName(),
                Sandbox2.class.getName());
    }

    @Test
    @DisplayName("Getting size of beans map")
    void getAllBeans() {
        assertEquals(applicationContext.getAllBeans(ISandbox.class).size(), 2);
        assertEquals(applicationContext.getAllBeans(BennyHill.class).size(), 1);
    }
}