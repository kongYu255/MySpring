package IOC;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PropertyValues {

    private final List<PropertyValue> propertyValues = new ArrayList<>();

    public void addPropertyValue(PropertyValue propertyValue) {
        // 这里可以对PropertyValue做一些操作

        // 插入进list
        propertyValues.add(propertyValue);
    }

}
