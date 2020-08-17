package model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private String name;
    private String password;

    public User(){

    }

    public void init() {
        System.out.println("User初始化");
    }

}
