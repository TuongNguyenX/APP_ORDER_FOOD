package com.example.firebase.Model;

public class Favorites {
    private String FoodId;
    private String FoodName;
    private String FoodPrice;
    private String FoodMenuId;
    private String FoodImage;
    private String FoodDiscount;
    private String FoodIdDescription;
    private String UserPhone;

    public Favorites() {
    }

    public Favorites(String foodId, String foodName, String foodPrice, String foodMenuId, String foodImage, String foodDiscount, String foodIdDescription, String userPhone) {
        FoodId = foodId;
        FoodName = foodName;
        FoodPrice = foodPrice;
        FoodMenuId = foodMenuId;
        FoodImage = foodImage;
        FoodDiscount = foodDiscount;
        FoodIdDescription = foodIdDescription;
        UserPhone = userPhone;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        FoodPrice = foodPrice;
    }

    public String getFoodMenuId() {
        return FoodMenuId;
    }

    public void setFoodMenuId(String foodMenuId) {
        FoodMenuId = foodMenuId;
    }

    public String getFoodImage() {
        return FoodImage;
    }

    public void setFoodImage(String foodImage) {
        FoodImage = foodImage;
    }

    public String getFoodDiscount() {
        return FoodDiscount;
    }

    public void setFoodDiscount(String foodDiscount) {
        FoodDiscount = foodDiscount;
    }

    public String getFoodIdDescription() {
        return FoodIdDescription;
    }

    public void setFoodIdDescription(String foodIdDescription) {
        FoodIdDescription = foodIdDescription;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}
