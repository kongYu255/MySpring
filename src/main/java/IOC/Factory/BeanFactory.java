package IOC.Factory;

public interface BeanFactory {

    public Object getBean(String beanId) throws Exception;
}
