package com.wang.test.design.decorate;

/**
 * @Author HeJiawang
 * @Date 2017/11/14 20:46
 */
public class ConcreteDecotator2 extends Decorator {

    /**
     * 定义被修饰者
     *
     * @param component
     */
    public ConcreteDecotator2(Component component) {
        super(component);
    }

    /**
     * 定义自己的修饰方法
     */
    private void method1(){
        System.out.println("method2 修饰");
    }

    /**
     * 重写父类的方法
     */
    @Override
    public void operate() {
        super.operate();
        this.method1();
    }
}
