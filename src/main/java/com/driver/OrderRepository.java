package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderDB = new HashMap<>();//unassigned
    HashMap<String, DeliveryPartner> partnerDB = new HashMap<>();
    HashMap<String, List<String>> orderPartnerDB = new HashMap<>();
    HashMap<String, String> allOrders = new HashMap<>();
    HashMap<String, String> assignedOrderDB = new HashMap<>();

    public void addOrder(Order order) {
        if (!orderDB.containsKey(order.getId())) {
            orderDB.put(order.getId(), order);
        }
    }

    public void addPartner(String partnerId) {

        partnerDB.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        List<String> orders = new ArrayList<>();
        assignedOrderDB.put(orderId, partnerId);
        if (orderPartnerDB.containsKey(partnerId)) {
            orders = orderPartnerDB.get(partnerId);
            orders.add(orderId);
            orderPartnerDB.put(partnerId, orders);
        } else {
            orders.add(orderId);
            orderPartnerDB.put(partnerId, orders);
        }

    }


    public Order getOrderById(String orderId) {
        return orderDB.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerDB.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId) {
        List<String> orders = orderPartnerDB.get(partnerId);
        return orders.size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orders = orderPartnerDB.get(partnerId);

        return orders;
    }

    public List<String> getAllOrders() {

        List<String> orderList = new ArrayList<>();
        for (String key : orderDB.keySet()) {
            allOrders.put(key, "unassigned");
        }


//        for(String key:orderPartnerDB.keySet()){
//            List<String> orders = orderPartnerDB.get(key);
//            for(int i=0;i<orders.size();i++){
//                assignedOrderDB.put(orders.get(i),key);
//            }
//        }
        for (String key : assignedOrderDB.keySet()) {
            allOrders.put(key, "assigned");
        }
        for (String key : allOrders.keySet()) {
            orderList.add(key);
        }
        return orderList;
    }

    public Integer getCountOfUnassignedOrders() {
        int count = 0;

        for (String key : orderDB.keySet()) {
            if (!assignedOrderDB.containsKey(key)) {
                count++;
                System.out.print(key + " ");
            }
        }
        return count;
    }

    public void deletePartnerById(String partnerId) {
        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.
        partnerDB.remove(partnerId);
        List<String> orders = orderPartnerDB.get(partnerId);
        orderPartnerDB.remove(partnerId);

        for (int i = 0; i < orders.size(); i++) {
            assignedOrderDB.remove(orders.get(i));
            //addOrder(new Order(orders.get(i),"00:00"));
        }
    }


    public void deleteOrderById(String orderId) {
        //Delete an order and also
        // remove it from the assigned order of that partnerId

        orderDB.remove(orderId);
        allOrders.remove(orderId);

        String partnerKey = "";
        for (int i = 0; i < assignedOrderDB.size(); i++) {
            if (assignedOrderDB.containsKey(orderId)) {
                partnerKey = assignedOrderDB.get(orderId);
                assignedOrderDB.remove(orderId);
            }
        }
        for (int i = 0; i < orderPartnerDB.size(); i++) {
            if (orderPartnerDB.containsKey(partnerKey)) {
                List<String> orderlist = orderPartnerDB.get(partnerKey);
                orderlist.remove(orderId);
                orderPartnerDB.put(partnerKey, orderlist);
            }
        }
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {

        String hour = time.substring(0, 2);
        String min = time.substring(3, 5);
        int tim = Integer.parseInt(hour) * 60 + Integer.parseInt(min);
        List<String> orderlist = new ArrayList<>();
        orderlist = orderPartnerDB.get(partnerId);
        int count = 0;
        for (int i = 0; i < orderlist.size(); i++) {
            int Time = orderDB.get(orderlist.get(i)).getDeliveryTime();
            if (Time > tim) {
                count++;
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orderlist = new ArrayList<>();
        orderlist = orderPartnerDB.get(partnerId);
        List<Integer> time = new ArrayList<>();
        for (int i = 0; i < orderlist.size(); i++) {
            int Time = orderDB.get(orderlist.get(i)).getDeliveryTime();
            time.add(Time);
        }

        int maxTime = 0;
        for (int i = 0; i < time.size(); i++) {
            maxTime = Math.max(maxTime, time.get(i));

        }
        int hr = maxTime / 60;
        int min = maxTime % 60;
        String hour = String.valueOf(hr);
        String minute = String.valueOf(min);

        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1)
            minute = "0" + minute;

        return hour + ":" + minute;
    }
}
