package com.example.fengcheng.main.dataBean;

import java.util.List;

/**
 * @Package com.example.fengcheng.main.dataBean
 * @FileName Products
 * @Date 4/13/18, 2:29 PM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class Products {

    private List<ProductBean> productList;

    public List<ProductBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductBean> productList) {
        this.productList = productList;
    }

    public static class ProductBean {
        /**
         * pid : 308
         * pname : i5-Laptop
         * quantity : 1
         * prize : 60000
         * discription : Online directory of electrical goods manufacturers, electronic goodssuppliers and electronic productList manufacturers. Get details of electronic products
         * imageurl : https://rjtmobile.com/ansari/shopingcart/admin/uploads/product_t_images/images.jpg
         */

        private String pid;
        private String pname;
        private String quantity;
        private String prize;
        private String discription;
        private String imageurl;

        public ProductBean(String pid, String pname, String quantity, String prize, String discription, String imageurl) {
            this.pid = pid;
            this.pname = pname;
            this.quantity = quantity;
            this.prize = prize;
            this.discription = discription;
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

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getPrize() {
            return prize;
        }

        public void setPrize(String prize) {
            this.prize = prize;
        }

        public String getDiscription() {
            return discription;
        }

        public void setDiscription(String discription) {
            this.discription = discription;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }
    }
}
