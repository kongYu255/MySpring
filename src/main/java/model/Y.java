package model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Y {

    private Z z;

    public void say() {
        System.out.println("this is Y");
        z.say();
    }
}
