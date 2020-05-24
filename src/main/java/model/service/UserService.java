package model.service;


import lombok.Getter;
import lombok.Setter;
import model.dao.UserDao;

@Setter
@Getter
public class UserService {

    private UserDao userDao;

    public void sayService() {

        userDao.sayDao();
    }
}
