package com.wang.test.design.decorate2;

import java.util.List;

/**
 * 抽象构件
 * @Author HeJiawang
 * @Date 2017/11/14 20:39
 */
public abstract class Component {

    /**
     * 抽象的方法
     */
    public abstract List<String> operate(List<String> list);

}
