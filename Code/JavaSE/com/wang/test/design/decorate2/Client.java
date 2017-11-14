package com.wang.test.design.decorate2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        //修饰后执行
        List<String> list =  component.operate( new ArrayList<String>());

        list.forEach(str ->{
            System.out.println(str);
        });

    }
}
