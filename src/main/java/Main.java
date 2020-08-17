import AOP.jdkProxy.AccountAdvice;
import AOP.jdkProxy.AccountServiceImpl;
import AOP.jdkProxy.IAccountService;
import IOC.ClassPathXMLApplicationContext;
import model.User;
import model.X;
import model.service.UserService;

import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) throws Exception {
        ClassPathXMLApplicationContext context = new ClassPathXMLApplicationContext("applicationContext.xml");
        X x = (X) context.getBean("x");
        x.say();
    }
}
