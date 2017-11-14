package com.wang.test.design.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author HeJiawang
 * @Date 2017/11/14 20:03
 */
public class HandlerData2 implements IHandlerData {

    @Override
    public List<String> handler(List<String> list) {
        List<String>  list2 = new ArrayList<String>();

        list.forEach(str->{
            if ( !str.contains("b") ) {
                list2.add(str);
            }
        });

        return list2;
    }
}
