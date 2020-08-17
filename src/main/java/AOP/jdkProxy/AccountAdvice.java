package AOP.jdkProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AccountAdvice implements InvocationHandler {
    public IAccountService target;

    public AccountAdvice(IAccountService target) {
        this.target = target;
    }

    // 增强
    public void before() {
        System.out.println("正在核实身份信息.......");
    }
    public void after() {
        System.out.println("转账成功.......");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        before();
        method.invoke(target,args);
        after();
        return null;
    }
}
