package IOC.XML;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

public class Dom4j_XML {

    private String configPath;

    public Dom4j_XML(String configPath) {
        this.configPath = configPath;
    }

    public InputStream getResourceInputStream() {
        return this.getClass().getClassLoader().getResourceAsStream(configPath);
    }

    public Object getBean(String beanName) throws Exception {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(getResourceInputStream());
        if(document == null) {
            return null;
        }
        // 获取根节点
        Element rootElement = document.getRootElement();

        // 遍历根节点
        List<Element> elements = rootElement.elements();
        Object object = null;
        for (Element element : elements ) {
            // 获取xml中的名字是否与传进来一致
            String name = element.attributeValue("name");
            if(!name.equals(beanName)) {
                continue;
            }
            // 获取类路径反射创建对象
            String classString = element.attributeValue("class");
            Class<?> clazz = Class.forName(classString);
            object = clazz.newInstance();
            // 获取属性注入
            List<Element> elements1 = element.elements();
            for(Element element1 : elements1) {
                String propertyName = element1.attributeValue("name");
                String propertyValue = element1.attributeValue("value");
                Field field = clazz.getDeclaredField(propertyName);
                field.setAccessible(true);
                field.set(object,propertyValue);
            }
        }
        return object;
    }
}
