package com.example.fengcheng.main.dataBean;

// FIXME generate failure  field _$OrderHistory123

import java.util.List;

/**
 * @Package com.example.fengcheng.main.dataBean
 * @FileName OrderHistory
 * @Date 4/17/18, 12:08 AM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class OrderHistory {
    List<Order> orderList;

    public OrderHistory(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public static class Order {
        /**
         * orderid : 2147483697
         * orderstatus : 1
         * name :
         * billingadd : Noida
         * deliveryadd : Noida
         * mobile : 123456789
         * email : 001@test.com
         * itemid : 319
         * itemname :
         * itemquantity : 6
         * totalprice : 5000
         * paidprice : 5000
         * placedon : 2018-04-16 19:54:12
         */

        private String orderid;
        private String orderstatus;
        private String name;
        private String billingadd;
        private String deliveryadd;
        private String mobile;
        private String email;
        private String itemid;
        private String itemname;
        private String itemquantity;
        private String totalprice;
        private String paidprice;
        private String placedon;

        public Order(String orderid, String orderstatus, String name, String billingadd, String deliveryadd, String mobile, String email, String itemid, String itemname, String itemquantity, String totalprice, String paidprice, String placedon) {
            this.orderid = orderid;
            this.orderstatus = orderstatus;
            this.name = name;
            this.billingadd = billingadd;
            this.deliveryadd = deliveryadd;
            this.mobile = mobile;
            this.email = email;
            this.itemid = itemid;
            this.itemname = itemname;
            this.itemquantity = itemquantity;
            this.totalprice = totalprice;
            this.paidprice = paidprice;
            this.placedon = placedon;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getOrderstatus() {
            return orderstatus;
        }

        public void setOrderstatus(String orderstatus) {
            this.orderstatus = orderstatus;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBillingadd() {
            return billingadd;
        }

        public void setBillingadd(String billingadd) {
            this.billingadd = billingadd;
        }

        public String getDeliveryadd() {
            return deliveryadd;
        }

        public void setDeliveryadd(String deliveryadd) {
            this.deliveryadd = deliveryadd;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getItemquantity() {
            return itemquantity;
        }

        public void setItemquantity(String itemquantity) {
            this.itemquantity = itemquantity;
        }

        public String getTotalprice() {
            return totalprice;
        }

        public void setTotalprice(String totalprice) {
            this.totalprice = totalprice;
        }

        public String getPaidprice() {
            return paidprice;
        }

        public void setPaidprice(String paidprice) {
            this.paidprice = paidprice;
        }

        public String getPlacedon() {
            return placedon;
        }

        public void setPlacedon(String placedon) {
            this.placedon = placedon;
        }
    }
}
