package model.dao;


import lombok.Getter;
import lombok.Setter;
import model.service.UserService;

@Setter
@Getter
public class UserDao {

    private UserService userService;

    public void sayDao() {
        System.out.println("UserDao");
    }
}
