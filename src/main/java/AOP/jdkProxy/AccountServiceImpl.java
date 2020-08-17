package AOP.jdkProxy;

public class AccountServiceImpl implements IAccountService {
    @Override
    public void transfer() {
        System.out.println("正在转账........");
    }
}
