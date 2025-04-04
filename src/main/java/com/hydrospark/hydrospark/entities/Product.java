//package com.hydrospark.hydrospark.entities;
//
//import jakarta.persistence.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//public class Product {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int proId;
//
//    private String productName;
//
//    @Lob
//    private byte[] prodImg;
//
//    public List<SubProducts> getSubProducts() {
//        return subProducts;
//    }
//
//    public void setSubProducts(List<SubProducts> subProducts) {
//        this.subProducts = subProducts;
//    }
//
//
//    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product",orphanRemoval = true)
//    private List<SubProducts> subProducts;
//
//    public Product(String productName, byte[] prodImg) {
//        this.productName = productName;
//        this.prodImg = prodImg;
//    }
//
//    public Product() {
//    }
//
//
//
//
//    public String getProductName() {
//        return productName;
//    }
//
//    public void setProductName(String productName) {
//        this.productName = productName;
//    }
//
//
//
//    public byte[] getProdImg() {
//        return prodImg;
//    }
//
//    public void setProdImg(byte[] prodImg) {
//        this.prodImg = prodImg;
//    }
//
//
//
//    public int getProId() {
//        return proId;
//    }
//
//    public void setProId(int proId) {
//        this.proId = proId;
//    }
//
//    public void addSubProd(SubProducts subProducts){
//        if(this.subProducts==null){
//            this.subProducts=new ArrayList<>();
//        }
//        this.subProducts.add(subProducts);
//    }
//
//
//}


package com.hydrospark.hydrospark.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_id")
    private int proId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    @Basic(fetch = FetchType.LAZY)
    private byte[] prodImg;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<SubProducts> subProducts = new ArrayList<>();

    // Constructors
    public Product() {
    }

    public Product(String productName, byte[] prodImg) {
        this.productName = productName;
        this.prodImg = prodImg;
    }

    // Getters and Setters
    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public byte[] getProdImg() {
        return prodImg;
    }

    public void setProdImg(byte[] prodImg) {
        this.prodImg = prodImg;
    }

    public List<SubProducts> getSubProducts() {
        return subProducts;
    }

    public void setSubProducts(List<SubProducts> subProducts) {
        this.subProducts = subProducts;
    }

    public void addSubProd(SubProducts subProducts) {
        this.subProducts.add(subProducts);
        subProducts.setProduct(this); // Ensure bidirectional relationship
    }
}
