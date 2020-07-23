package study.proxy.example.jdk.dynamic;

import study.proxy.example.TextUpperCaseMethodMatcher;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DynamicInvocationHandler implements InvocationHandler {

    private Object target;
    private final Map<String, Method> methods = new HashMap<>();
    public DynamicInvocationHandler(Object target) {
        this.target = target;
        for(Method method : target.getClass().getDeclaredMethods()){
            this.methods.put(method.getName(), method);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = methods.get(method.getName()).invoke(target, args);

        TextUpperCaseMethodMatcher matcher = new TextUpperCaseMethodMatcher("say");
        if (matcher.matches(method, proxy.getClass(), args)) {
            result = ((String) result).toUpperCase();
        }
        return result;
    }
}
