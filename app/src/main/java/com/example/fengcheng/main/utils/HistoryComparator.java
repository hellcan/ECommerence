package com.example.fengcheng.main.utils;

import com.example.fengcheng.main.dataBean.OrderHistory;

import java.util.Comparator;

/**
 * @Package com.example.fengcheng.main.utils
 * @FileName HistoryComparator
 * @Date 4/18/18, 10:51 AM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class HistoryComparator implements Comparator {


    @Override
    public int compare(Object o1, Object o2) {
        OrderHistory.Order order1 = (OrderHistory.Order)o1;
        OrderHistory.Order order2 = (OrderHistory.Order)o2;

        if (order1.getPlacedon().compareTo(order2.getPlacedon()) > 0){
            return -1;
        }

        return 1;
    }
}
