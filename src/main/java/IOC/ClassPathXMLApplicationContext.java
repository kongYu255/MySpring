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

    // bean工厂，生产bean并返回
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
