package IOC;


import IOC.Factory.BeanFactory;
import IOC.Factory.XMLBeanFactory;

public class ClassPathXMLApplicationContext {

    // 配置文件名称
    private String configPath;

    public ClassPathXMLApplicationContext(String configPath) throws Exception {
        this.configPath = configPath;
        beanFactory = new XMLBeanFactory(configPath);
    }

    private BeanFactory beanFactory;

    public Object getBean(String beanId) {
        try {
            return beanFactory.getBean(beanId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
