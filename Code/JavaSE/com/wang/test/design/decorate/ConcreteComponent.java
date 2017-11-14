package com.wang.test.design.decorate;

/**
 * 具体构件
 * @Author HeJiawang
 * @Date 2017/11/14 20:40
 */
public class ConcreteComponent extends Component {

    /**
     * 具体实现
     */
    @Override
    public void operate() {
        System.out.println("do somthing");
    }
}
