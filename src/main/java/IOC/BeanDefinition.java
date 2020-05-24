package IOC;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BeanDefinition {

    private Object object;

    private Class beanClass;

    private String beanClassName;

    private String initMethodName;

    private PropertyValues propertyValues = new PropertyValues();

    public BeanDefinition() {

    }
}
