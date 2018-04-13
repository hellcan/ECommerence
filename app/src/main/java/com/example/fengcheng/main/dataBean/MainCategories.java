package com.example.fengcheng.main.dataBean;

import java.util.List;

/**
 * @Package com.example.fengcheng.main.dataBean
 * @FileName MainCategories
 * @Date 4/12/18, 11:14 AM
 * @Author Created by fengchengding
 * @Description ECommerence
 */

public class MainCategories {

    private List<CategoryBean> categoryList;

    public List<CategoryBean> getcategoryList() {
        return categoryList;
    }

    public void setcategoryList(List<CategoryBean> category) {
        this.categoryList = category;
    }

    public static class CategoryBean {
        /**
         * cid : 107
         * cname : Electronics
         * cdiscription : Online directory of electrical goods manufacturers, electronic goods suppliers and electronic product manufacturers. Get details of electronic products.
         * cimagerl : https://rjtmobile.com/ansari/shopingcart/admin/uploads/category_images/images.jpg
         */

        private String cid;
        private String cname;
        private String cdiscription;
        private String cimagerl;

        public CategoryBean(String cid, String cname, String cdiscription, String cimagerl) {
            this.cid = cid;
            this.cname = cname;
            this.cdiscription = cdiscription;
            this.cimagerl = cimagerl;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public String getCdiscription() {
            return cdiscription;
        }

        public void setCdiscription(String cdiscription) {
            this.cdiscription = cdiscription;
        }

        public String getCimagerl() {
            return cimagerl;
        }

        public void setCimagerl(String cimagerl) {
            this.cimagerl = cimagerl;
        }
    }
}
