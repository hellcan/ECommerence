package com.example.fengcheng.main.dataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.example.fengcheng.main.dataBean
 * @FileName CartInfo
 * @Date 4/13/18, 11:48 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class CartInfo {

    private String userMobile;
    private List<OrderBean> orderBeanList;

    private static CartInfo instance;

    private CartInfo(){
        orderBeanList = new ArrayList<>();
    }

    public static CartInfo getInstance(){
        if (instance == null) {
            synchronized (CartInfo.class){
                if (instance == null) {
                    instance = new CartInfo() ;
                }
            }
        }
        return instance ;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public List<OrderBean> getOrderBeanList() {
        return orderBeanList;
    }

    public void setOrderBeanList(List<OrderBean> orderBeanList) {
        this.orderBeanList = orderBeanList;
    }

    public static class OrderBean {
        /**
         * pid : 308
         * pname : i5-Laptop
         * quantity : 1
         * prize : 60000
         * imageurl : https://rjtmobile.com/ansari/shopingcart/admin/uploads/product_t_images/images.jpg
         */

        private String pid;
        private String pname;
        private int quantity;
        private String prize;
        private String imageurl;

        public OrderBean(String pid, String pname, int quantity, String prize, String imageurl) {
            this.pid = pid;
            this.pname = pname;
            this.quantity = quantity;
            this.prize = prize;
            this.imageurl = imageurl;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getPrize() {
            return prize;
        }

        public void setPrize(String prize) {
            this.prize = prize;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }
    }
}
