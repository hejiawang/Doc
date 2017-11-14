package com.wang.test.design.decorate2;

import java.util.List;

/**
 * 抽象装饰者
 * @Author HeJiawang
 * @Date 2017/11/14 20:42
 */
public abstract class Decorator extends Component {

    private Component component = null;

    /**
     * 通过构造函数传递被修饰者
     * @param component
     */
    public Decorator(Component component) {
        this.component = component;
    }

    /**
     * 委托给被修饰者执行
     */
    @Override
    public List<String> operate(List<String> list) {
        return this.component.operate(list);
    }
}
