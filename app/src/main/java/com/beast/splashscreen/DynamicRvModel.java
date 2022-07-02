package com.beast.splashscreen;

public class DynamicRvModel {
    String name,info;
    int img;

    public String getPrice() {
        return price;
    }

    String price;

    public String getInfo() {
        return info;
    }

    public int getImg() {
        return img;
    }

    public DynamicRvModel(String name,String info,String price, int img) {
        this.name = name;
        this.info = info;
        this.price = price;
        this.img = img;
    }

    public String getName() {
        return name;
    }
}
