package model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class X {

    private Y y;

    public void say() {
        System.out.println("this is X");
        y.say();
    }
}
