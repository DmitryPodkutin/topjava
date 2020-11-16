package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import ru.javawebinar.topjava.repository.JpaUtil;

import java.util.Objects;

public abstract class AbstractJpaUserServiceTest extends AbstractUserServiceTest {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    protected JpaUtil jpaUtil;

    @Before
    public void setUp() {
        Objects.requireNonNull(cacheManager.getCache("users")).clear();
        jpaUtil.clear2ndLevelHibernateCache();
    }
}
