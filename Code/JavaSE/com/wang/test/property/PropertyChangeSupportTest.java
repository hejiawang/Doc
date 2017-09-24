package com.wang.test.property;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PropertyChangeSupportTest {

    public static void main(String[] args) {  
    	
        TestBean test = new TestBean();
        test.setStr("ddddd");
        test.addPropertyChangeListener(new PropertyChangeListener1());
        test.setStr("yyyy");
        
        //test.addPropertyChangeListener(new PropertyChangeListener2());
    	//test.setStr("hhhhhhf");
    }  
      
    public static class PropertyChangeListener1 implements PropertyChangeListener{

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			System.out.println( "1:  " + evt.getPropertyName() + "   " + evt.getNewValue() + "   " + evt.getOldValue());
			
		}
    }
    
    public static class PropertyChangeListener2 implements PropertyChangeListener{

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			System.out.println( "2:  " + evt.getPropertyName() + "   " + evt.getNewValue() + "   " + evt.getOldValue());
			
		}
    }
}
