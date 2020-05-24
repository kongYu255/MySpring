package IOC.Factory;

import IOC.BeanDefinition;
import IOC.BeanPostProcessor;
import IOC.BeanReference;
import IOC.PropertyValue;
import IOC.XML.XMLBeanDefinitionReader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XMLBeanFactory implements BeanFactory {

    // 存放类信息BeanDefinition的map
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    // bean的二级缓存,存放提前曝光的bean
    private Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();
    // 存放单例对象的Map
    private Map<String, Object> beanMap = new ConcurrentHashMap<>();

    // 解析XML文件的类
    private XMLBeanDefinitionReader beanDefinitionReader;

    // 存放实现了BeanPostProcessor接口的类
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public XMLBeanFactory(String XMLPath) throws Exception {
        beanDefinitionReader = new XMLBeanDefinitionReader();
        loadBeanDefinitions(XMLPath);
    }

    // 加载Bean
    public void loadBeanDefinitions(String XMLPath) throws Exception {
        beanDefinitionReader.loadBeanDefinitions(XMLPath);
        // 注册bean,将BeanDefinition从XMLReader类中放入本类map中
        registerBeanDefinition();
        registerBeanPostProcessor();
    }

    private void registerBeanDefinition() {
        for(Map.Entry<String, BeanDefinition> entry : beanDefinitionReader.getBeanDefinitions().entrySet()) {
            String name = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            beanDefinitionMap.put(name,beanDefinition);
        }
    }

    public void registerBeanPostProcessor() throws Exception {
        // 找到所有实现了BeanPostProcessor的类
        List beans = getBeanForType(BeanPostProcessor.class);
        for(Object bean : beans) {
            addBeanPostProcessors(bean);
        }
    }

    public void addBeanPostProcessors(Object bean) {
        beanPostProcessors.add((BeanPostProcessor) bean);
    }

    public List getBeanForType(Class type) throws Exception {
        List beans = new ArrayList();
        for(Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()) {
            String beanDefinitionName = beanDefinitionEntry.getKey();
            // 查找bean是否有实现BeanPostProcessor接口
            if(type.isAssignableFrom(beanDefinitionEntry.getValue().getBeanClass())) {
                beans.add(getBean(beanDefinitionName));
            }
        }
        return beans;
    }

    @Override
    public Object getBean(String beanId) throws Exception {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanId);
        if(beanDefinition == null) {
            throw new Exception("no is bean named " + beanId);
        }
        Object bean = beanMap.get(beanId);
        if(bean == null) {
            bean = createBean(beanId,beanDefinition);
            bean = initializeBean(bean,beanId);
        }
        // 存入beanMap
        beanMap.put(beanId,bean);
        return bean;
    }

    // 反射创建bean，并且进行属性注入
    private Object createBean(String beanId,BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException {
        Object object = beanDefinition.getBeanClass().newInstance();
        setPropertyValue(beanId,object,beanDefinition);

        return object;
    }

    // 属性注入
    private void setPropertyValue(String beanId,Object bean,BeanDefinition beanDefinition) {

        for(PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
            Object value = propertyValue.getValue();
            try {
                // 如果是对象注入
                if(value instanceof BeanReference) {
                    BeanReference beanReference = (BeanReference) value;
                    value = earlySingletonObjects.get(beanReference.getName());
                    // 循环依赖检查，如果有循环依赖则提前暴露
                    if(isDependent()) {
                        earlySingletonObjects.put(beanId, bean);
                    }
                    if(value == null) {
                        value = getBean(beanReference.getName());
                    }
                }
                // setter注入
                Method setMethod = bean.getClass().getDeclaredMethod("set" +
                        propertyValue.getPropertyName().substring(0,1).toUpperCase() +
                        propertyValue.getPropertyName().substring(1),value.getClass()
                );
                setMethod.setAccessible(true);
                setMethod.invoke(bean,value);

                // set注入结束后把提前暴露的bean缓存删掉
                earlySingletonObjects.remove(beanId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 判断类是否有循环依赖
    public boolean isDependent() {
        return true;
    }

    // 调用bean的初始化方法，并且插入执行实现了BeanPostProcessor接口的类的postProcessBeforeInitialization方法
    public Object initializeBean(Object bean, String beanName) throws Exception {
        for(BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessBeforeInitialization(bean,beanName);
        }
        // 执行初始化方法
        initMethod(bean,beanName);
        for(BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean,beanName);
        }
        return bean;
    }

    public void initMethod(Object bean, String beanName) throws Exception {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        String methodName = beanDefinition.getInitMethodName();
        if(methodName == null) {
            return;
        }
        Method init = bean.getClass().getDeclaredMethod(beanDefinition.getInitMethodName());
        init.setAccessible(true);
        init.invoke(bean);
    }
}
