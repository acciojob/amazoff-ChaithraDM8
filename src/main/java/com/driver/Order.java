package com.driver;

import java.util.StringTokenizer;

public class Order {

    private String id;
    private int deliveryTime;

//    public Order(String id, int deliveryTime) {
//        this.id = id;
//        this.deliveryTime = deliveryTime;
//    }


    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        StringTokenizer st=new StringTokenizer(deliveryTime,":");
        String hr=st.nextToken();
        String min=st.nextToken();

        int time= Integer.parseInt(hr)*60 + Integer.parseInt(min);

        this.deliveryTime= time;
        this.id=id;
                //String.valueOf(time);
    }
    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}

    public void setId(String id) {
        this.id = id;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
