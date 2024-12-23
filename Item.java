package org.project;
public class Item {

    private  String name;
    private int quantity;
    private  double price;

    public Item(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Item() {
        
    }


    public void setName(String name){
        this.name = name;
    }


    public void setPrice(double  price){
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
