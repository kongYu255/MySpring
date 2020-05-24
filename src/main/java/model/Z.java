package model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Z {

    private X x;

    public void say() {
        System.out.println("this is Z");
    }
}
