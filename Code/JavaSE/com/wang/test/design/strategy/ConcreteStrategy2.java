package com.wang.test.design.strategy;

/**
 * @Author HeJiawang
 * @Date 2017/11/14 19:47
 */
public class ConcreteStrategy2 implements Strategy {

    /**
     * 具体策略2的运算法则
     */
    @Override
    public void doSomething() {
        System.out.println("具体策略2的运算法则");
    }
}
