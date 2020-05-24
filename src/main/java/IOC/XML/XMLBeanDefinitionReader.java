package IOC.XML;

import IOC.BeanDefinition;
import IOC.BeanReference;
import IOC.PropertyValue;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XMLBeanDefinitionReader {

    // 存放BeanDefinition的map
    private Map<String, BeanDefinition> beanDefinitions;

    public XMLBeanDefinitionReader() {
        beanDefinitions = new ConcurrentHashMap<>(16);
    }

    public Map<String,BeanDefinition> getBeanDefinitions() { return beanDefinitions ;}

    public void loadBeanDefinitions(String XMLPath) {
        try {
            InputStream inputStream = getResourceInputStream(XMLPath);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);

            // 获取根节点
            Element rootElement = document.getRootElement();
            // 遍历根节点
            parseBeanDefinitions(rootElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseBeanDefinitions(Element rootElement) throws ClassNotFoundException {
        List<Element> elementList = rootElement.elements();
        if(elementList.size() <= 0) {
            return;
        }
        for(Element element : elementList) {
            parseBeanDefinition(element);
        }
    }

    public void parseBeanDefinition(Element element) throws ClassNotFoundException {
        // 获取类id
        String name = element.attributeValue("id");
        // 获取类路径
        String className = element.attributeValue("class");
        String initMethodName = element.attributeValue("init-method");
        // 创建BeanDefinition，存入信息
        BeanDefinition beanDefinition = new BeanDefinition();
        Class<?> clazz = Class.forName(className);
        beanDefinition.setBeanClass(clazz);
        beanDefinition.setBeanClassName(className);
        if(initMethodName != null) {
            beanDefinition.setInitMethodName(initMethodName);
        }
        // 设置属性
        processProperty(element,beanDefinition);
        // 存入map中
        beanDefinitions.put(name,beanDefinition);
    }

    public void processProperty(Element element, BeanDefinition beanDefinition) {
        List<Element> elementList = element.elements();
        for(Element element1 : elementList) {
            String name = element1.attributeValue("name");
            String value = element1.attributeValue("value");
            if(value != null) {
                // 如果是属性注入
                PropertyValue propertyValue = new PropertyValue(name, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            } else {
                // 如果是对象注入
                String ref = element1.attributeValue("ref");
                BeanReference beanReference = new BeanReference();
                beanReference.setName(ref);
                PropertyValue propertyValue = new PropertyValue(name,beanReference);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
        }
    }

    public InputStream getResourceInputStream(String XMLPath) {
        return this.getClass().getClassLoader().getResourceAsStream(XMLPath);
    }
}
