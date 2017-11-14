package com.wang.test.design.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author HeJiawang
 * @Date 2017/11/14 20:09
 */
public class Client {



    public static void main(String[] args) {

        List<String> list = new ArrayList<String>();
        list.add("adf");
        list.add("f");
        list.add("j");
        list.add("bf");
        list.add("l");

        List<IHandlerData> handlerList= new ArrayList<IHandlerData>(){
            {
                add(new HandlerData1());
                add(new HandlerData2());
            }
        };

        for( IHandlerData handler : handlerList ){
            list = handler.handler(list);
        }

        list.forEach(str ->{
            System.out.println(str);
        });
    }


}
