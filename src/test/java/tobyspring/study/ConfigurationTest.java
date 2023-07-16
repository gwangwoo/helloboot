package tobyspring.study;

import static org.assertj.core.api.Assertions.assertThat;
import static tobyspring.study.ConfigurationTest.MyConfig.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class ConfigurationTest {

    @Test
    void configuration() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(MyConfig.class);
        applicationContext.refresh();
        Bean1 bean1 = applicationContext.getBean(Bean1.class);
        Bean2 bean2 = applicationContext.getBean(Bean2.class);
        assertThat(bean1.common).isSameAs(bean2.common);
        System.out.println(bean1.common);
        System.out.println(bean2.common);
    }

    @Test
    void proxyTest() {
        MyConfigProxy myConfigProxy = new MyConfigProxy();
        Bean1 bean1 = myConfigProxy.bean1();
        Bean2 bean2 = myConfigProxy.bean2();
        assertThat(bean1.common).isSameAs(bean2.common);
    }
    static class MyConfigProxy extends MyConfig {
        private Common common;

        @Override
        Common common() {
            if(this.common == null) this.common = super.common();
            return this.common;
        }
    }
    @Configuration(proxyBeanMethods = true)
    static class MyConfig {
        @Bean
        Common common() {
            return new Common();
        }

        @Bean
        Bean1 bean1() {
            return new Bean1(common());
        }
        @Bean
        Bean2 bean2() {
            return new Bean2(common());
        }

        static class Common {
        }

        static class Bean1 {
            private final Common common;
            public Bean1(Common common) {
                this.common = common;
            }
        }
        static class Bean2 {
            private final Common common;
            public Bean2(Common common) {
                this.common = common;
            }
        }
    }
}
