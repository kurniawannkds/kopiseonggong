package com.androiddevnkds.kopiseong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryModel {

    @SerializedName("category_list")
    private List<CategorySatuan> categorySatuanList;

    @SerializedName("error_msg")
    private String errorMessage;

    public CategoryModel(List<CategorySatuan> categorySatuanList, String errorMessage) {
        this.categorySatuanList = categorySatuanList;
        this.errorMessage = errorMessage;
    }

    public CategoryModel() {
    }

    public List<CategorySatuan> getCategorySatuanList() {
        return categorySatuanList;
    }

    public void setCategorySatuanList(List<CategorySatuan> categorySatuanList) {
        this.categorySatuanList = categorySatuanList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public class CategorySatuan{

        @SerializedName("category_id")
        private String categoryID;

        @SerializedName("category_name")
        private String categoryName;

        @SerializedName("category_general")
        private String categoryGeneral;

        public CategorySatuan() {
        }

        public CategorySatuan(String categoryID, String categoryName, String categoryGeneral) {
            this.categoryID = categoryID;
            this.categoryName = categoryName;
            this.categoryGeneral = categoryGeneral;
        }

        public String getCategoryID() {
            return categoryID;
        }

        public void setCategoryID(String categoryID) {
            this.categoryID = categoryID;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryGeneral() {
            return categoryGeneral;
        }

        public void setCategoryGeneral(String categoryGeneral) {
            this.categoryGeneral = categoryGeneral;
        }
    }
}
