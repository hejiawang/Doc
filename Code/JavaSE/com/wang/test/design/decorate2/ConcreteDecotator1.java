package com.wang.test.design.decorate2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author HeJiawang
 * @Date 2017/11/14 20:46
 */
public class ConcreteDecotator1 extends Decorator {

    /**
     * 定义被修饰者
     *
     * @param component
     */
    public ConcreteDecotator1(Component component) {
        super(component);
    }

    /**
     * 定义自己的修饰方法
     */
    private List<String> method1(List<String> list){
        List<String>  list2 = new ArrayList<String>();

        list.forEach(str->{
            if ( !str.contains("a") ) {
                list2.add(str);
            }
        });
        System.out.println("do somthing 1");
        return list2;
    }

    /**
     * 重写父类的方法
     */
    @Override
    public List<String> operate(List<String> list) {
        List<String> list1 = super.operate(list);
        return this.method1(list1);
    }
}
