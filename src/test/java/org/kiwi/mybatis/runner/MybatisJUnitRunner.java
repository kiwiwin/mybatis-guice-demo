package org.kiwi.mybatis.runner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import org.apache.ibatis.session.SqlSessionManager;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mybatis.guice.XMLMyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import org.mybatis.guice.transactional.Transactional;

import javax.inject.Inject;

public class MybatisJUnitRunner extends BlockJUnit4ClassRunner {

    private Injector injector;

    @Inject
    private SqlSessionManager sqlSessionManager;

    public MybatisJUnitRunner(final Class<?> clazz) throws InitializationError {
        super(clazz);
        injector = Guice.createInjector(
                new XMLMyBatisModule() {
                    @Override
                    protected void initialize() {
                        install(JdbcHelper.MySQL);

                        setEnvironmentId("dev");
                        setClassPathResource("mybatis/mybatis-config.xml");

                        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), invocation -> {
                            if (sqlSessionManager != null && sqlSessionManager.isManagedSessionStarted()) {
                                sqlSessionManager.clearCache();
                            }
                            return invocation.proceed();
                        });
                    }
                });
        injector.injectMembers(this);
    }

    @Override
    protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
        final Statement beforeStatement = super.withBefores(method, target, statement);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                sqlSessionManager.startManagedSession();
                beforeStatement.evaluate();
            }
        };
    }

    @Override
    protected Statement withAfters(FrameworkMethod method, Object target, Statement statement) {
        final Statement afterStatement = super.withAfters(method, target, statement);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                afterStatement.evaluate();
                try {
                    sqlSessionManager.rollback();
                } finally {
                    sqlSessionManager.close();
                }
            }
        };
    }

    @Override
    protected Object createTest() throws Exception {
        Object testClass = super.createTest();
        injector.injectMembers(testClass);
        return testClass;
    }
}
