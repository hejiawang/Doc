package com.wang.test.design.strategy;

/**
 * 封装角色
 * @Author HeJiawang
 * @Date 2017/11/14 19:48
 */
public class Context {

    /**
     * 抽象策略
     */
    private Strategy strategy = null;

    /**
     * 构造函数设置具体策略
     * @param _strategy
     */
    public Context( Strategy _strategy ){
        this.strategy = _strategy;
    }

    /**
     * 封装后的策略方法
     */
    public void doAnything(){
        this.strategy.doSomething();
    }

}
