package IOC;


import lombok.Data;
import lombok.Getter;

/**
 *  该类用于存储Bean之中的属性名和属性值
 */
@Getter
public class PropertyValue {
    private String propertyName;
    private Object value;

    public PropertyValue(String name,Object value) {
        this.propertyName = name;
        this.value = value;
    }

}
