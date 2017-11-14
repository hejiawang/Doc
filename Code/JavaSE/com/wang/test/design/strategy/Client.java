package com.wang.test.design.strategy;

/**
 * 使用策略模式的高层模块
 * @Author HeJiawang
 * @Date 2017/11/14 19:50
 */
public class Client {

    public static void main(String[] args) {
        Context context = null;

        Strategy strategy1 = new ConcreteStrategy1();
        context = new Context(strategy1);
        context.doAnything();

        Strategy strategy2 = new ConcreteStrategy2();
        context = new Context(strategy2);
        context.doAnything();
    }
}
