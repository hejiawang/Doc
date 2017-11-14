package com.wang.test.design.decorate;

/**
 * @Author HeJiawang
 * @Date 2017/11/14 20:49
 */
public class Client {

    public static void main(String[] args) {

        Component component = new ConcreteComponent();
        //第一次修饰
        component = new ConcreteDecotator1(component);
        //第二次修饰
        component = new ConcreteDecotator2(component);

        component = new ConcreteDecotator3(component);
        //修饰后执行
        component.operate();
    }
}
