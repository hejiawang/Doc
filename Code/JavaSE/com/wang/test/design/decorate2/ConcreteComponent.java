package com.wang.test.design.decorate2;

import java.util.List;

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
    public List<String> operate(List<String> list) {
        System.out.println("do somthing");
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        return list;
    }
}
