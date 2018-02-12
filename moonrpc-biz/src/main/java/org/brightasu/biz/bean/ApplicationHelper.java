package org.brightasu.biz.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 动态bean操作，持有 ApplicationContext
 */
public class ApplicationHelper implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static DefaultListableBeanFactory getBeanFactory() {
        AutowireCapableBeanFactory autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();
        return (DefaultListableBeanFactory) autowireCapableBeanFactory;
    }

    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    /**
     * 根据类获取bean，获取不到就动态生成一个注册到spring中
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T obtainBean(Class<T> clazz) {
        T bean = null;
        try {
            bean = context.getBean(clazz);
        } catch (BeansException e) {
            synchronized (ApplicationHelper.class) {
                try {
                    bean = context.getBean(clazz);
                } catch (BeansException e1) {
                    String beanName = clazz.getSimpleName();
                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
                    beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                    AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                    registerBean(beanName, beanDefinition);
                    bean = context.getBean(clazz);
                }
            }
        }
        return bean;
    }

    public static void registerBean(String beanName, BeanDefinition beanDefinition) {
        getBeanFactory().registerBeanDefinition(beanName, beanDefinition);
    }

    public static void removeBean(String beanName) {
        getBeanFactory().removeBeanDefinition(beanName);
    }
}
