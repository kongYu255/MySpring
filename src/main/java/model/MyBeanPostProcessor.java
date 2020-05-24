package model;

import IOC.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        if(bean instanceof User) {
            System.out.println("init方法执行前");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        if(bean instanceof User) {
            System.out.println("init方法执行后");
        }
        return bean;
    }
}
