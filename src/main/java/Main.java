import IOC.ClassPathXMLApplicationContext;
import model.User;
import model.X;
import model.service.UserService;

public class Main {
    public static void main(String[] args) throws Exception {
        ClassPathXMLApplicationContext context = new ClassPathXMLApplicationContext("applicationContext.xml");

        User user = (User) context.getBean("user");
//        System.out.println(user.getName());
    }
}
