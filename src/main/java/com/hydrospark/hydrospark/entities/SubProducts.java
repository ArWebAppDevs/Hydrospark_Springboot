//package com.hydrospark.hydrospark.entities;
//
//
//import jakarta.persistence.*;
//
//@Entity
//public class SubProducts {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int subProdId;
//    private String subTypeName;
//    private double subTypePrice;
//    private String description;
//    @Lob
//    private byte[] subProdImg;
//    @ManyToOne
//    private Product product;
//
//    public SubProducts(String subTypeName, double subTypePrice, String description, Product product,byte[] subProdImg) {
//        this.subTypeName = subTypeName;
//        this.subTypePrice = subTypePrice;
//        this.description = description;
//        this.product = product;
//        this.subProdImg=subProdImg;
//    }
//
//    public SubProducts() {
//    }
//
//    public int getSubProdId() {
//        return subProdId;
//    }
//
//    public void setSubProdId(int subProdId) {
//        this.subProdId = subProdId;
//    }
//
//    public String getSubTypeName() {
//        return subTypeName;
//    }
//
//    public void setSubTypeName(String subTypeName) {
//        this.subTypeName = subTypeName;
//    }
//
//    public double getSubTypePrice() {
//        return subTypePrice;
//    }
//
//    public void setSubTypePrice(double subTypePrice) {
//        this.subTypePrice = subTypePrice;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }
//
//    public byte[] getSubProdImg() {
//        return subProdImg;
//    }
//
//    public void setSubProdImg(byte[] subProdImg) {
//        this.subProdImg = subProdImg;
//    }
//}

//package com.hydrospark.hydrospark.entities;
//
//import jakarta.persistence.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "sub_products") // Optional: Explicit table name
//public class SubProducts {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "sub_prod_id")
//    private int subProdId;
//
//    @Column(name = "sub_type_name", nullable = false)
//    private String subTypeName;
//
//    @Column(name = "sub_type_price", nullable = false)
//    private double subTypePrice;
//
//    @Column(name = "description")
//    private String description;
//
//    @Lob
//    @Column(columnDefinition = "MEDIUMBLOB")
//    @Basic(fetch = FetchType.LAZY)
//    private byte[] subProdImg;
//
////    @Lob
////    @Basic(fetch = FetchType.LAZY)
////    @Column(columnDefinition = "MEDIUMBLOB")
////    private byte[] detailsImage;
//
//    @Column(name = "dash_size")
//    private Integer dashSize; // Integer to allow null values
//
//    @Column(name = "termination")
//    private String termination;
//
//    @Column(name = "thread_size")
//    private String threadSize;
//
//    @Column(name = "group_name") // Renamed to avoid SQL keyword conflict
//    private String group;
//
//    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//    @JoinColumn(name = "pro_id", nullable = false)
//    private Product product;
//
//    @OneToMany(mappedBy = "subProducts",cascade = CascadeType.ALL)
//    private List<ComponentEntry> componentEntry;
//
//    // Constructors
//    public SubProducts() {
//    }
//
//    public SubProducts(String subTypeName, double subTypePrice, String description, byte[] subProdImg,
//                       Integer dashSize, String termination, String threadSize, String group, Product product) {
//        this.subTypeName = subTypeName;
//        this.subTypePrice = subTypePrice;
//        this.description = description;
//        this.subProdImg = subProdImg;
//        this.dashSize = dashSize;
//        this.termination = termination;
//        this.threadSize = threadSize;
//        this.group = group;
//        this.product = product;
//    }
//
//    // Getters and Setters
//    public int getSubProdId() {
//        return subProdId;
//    }
//
//    public void setSubProdId(int subProdId) {
//        this.subProdId = subProdId;
//    }
//
//    public String getSubTypeName() {
//        return subTypeName;
//    }
//
//    public void setSubTypeName(String subTypeName) {
//        this.subTypeName = subTypeName;
//    }
//
//    public double getSubTypePrice() {
//        return subTypePrice;
//    }
//
//    public void setSubTypePrice(double subTypePrice) {
//        this.subTypePrice = subTypePrice;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public byte[] getSubProdImg() {
//        return subProdImg;
//    }
//
//    public void setSubProdImg(byte[] subProdImg) {
//        this.subProdImg = subProdImg;
//    }
//
//
//    public Integer getDashSize() {
//        return dashSize;
//    }
//
//    public void setDashSize(Integer dashSize) {
//        this.dashSize = dashSize;
//    }
//
//    public String getTermination() {
//        return termination;
//    }
//
//    public void setTermination(String termination) {
//        this.termination = termination;
//    }
//
//    public String getThreadSize() {
//        return threadSize;
//    }
//
//    public void setThreadSize(String threadSize) {
//        this.threadSize = threadSize;
//    }
//
//    public String getGroup() {
//        return group;
//    }
//
//    public void setGroup(String group) {
//        this.group = group;
//    }
//
//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }
//
//
//    public List<ComponentEntry> getComponentEntry() {
//        return componentEntry;
//    }
//
//    public void addComponent(ComponentEntry componentEntry){
//        if(this.componentEntry==null){
//            this.componentEntry=new ArrayList<>();
//        }
//        this.componentEntry.add(componentEntry);
//    }
//
//
//    // Optional: toString for debugging
//    @Override
//    public String toString() {
//        return "SubProducts{" +
//                "subProdId=" + subProdId +
//                ", subTypeName='" + subTypeName + '\'' +
//                ", subTypePrice=" + subTypePrice +
//                ", description='" + description + '\'' +
//                ", dashSize=" + dashSize +
//                ", termination='" + termination + '\'' +
//                ", threadSize='" + threadSize + '\'' +
//                ", group='" + group + '\'' +
//                '}';
//    }
//}
package com.hydrospark.hydrospark.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sub_products")
public class SubProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_prod_id")
    private int subProdId;

    @Column(name = "sub_type_name", nullable = false)
    private String subTypeName;

    @Column(name = "sub_type_price", nullable = false)
    private double subTypePrice;

    @Column(name = "description",length = 5000)
    private String description;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    @Basic(fetch = FetchType.LAZY)
    private byte[] subProdImg;


    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    @Basic(fetch = FetchType.LAZY)
    private byte[] detailedImg;

    @Column(name = "dash_size")
    private String dashSize;

    @Column(name = "termination")
    private String termination;

    @Column(name = "thread_size")
    private String threadSize;

    @Column(name = "group_name")
    private String group;

    @ManyToOne(fetch = FetchType.LAZY) // No cascade here; Product should be independent
    @JoinColumn(name = "pro_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "subProducts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComponentEntry> components = new ArrayList<>(); // Renamed for clarity

    // Constructors
    public SubProducts() {}

    public SubProducts(String subTypeName, double subTypePrice, String description, byte[] subProdImg,byte[] detailedImg,
                       String dashSize, String termination, String threadSize, String group, Product product) {
        this.subTypeName = subTypeName;
        this.subTypePrice = subTypePrice;
        this.description = description;
        this.subProdImg = subProdImg;
        this.dashSize = dashSize;
        this.termination = termination;
        this.threadSize = threadSize;
        this.group = group;
        this.product = product;
        this.detailedImg=detailedImg;
    }

    // Getters and Setters
    public int getSubProdId() { return subProdId; }
    public void setSubProdId(int subProdId) { this.subProdId = subProdId; }
    public String getSubTypeName() { return subTypeName; }
    public void setSubTypeName(String subTypeName) { this.subTypeName = subTypeName; }
    public double getSubTypePrice() { return subTypePrice; }
    public void setSubTypePrice(double subTypePrice) { this.subTypePrice = subTypePrice; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public byte[] getSubProdImg() { return subProdImg; }
    public void setSubProdImg(byte[] subProdImg) { this.subProdImg = subProdImg; }
    public String getDashSize() { return dashSize; }
    public void setDashSize(String dashSize) { this.dashSize = dashSize; }
    public String getTermination() { return termination; }
    public void setTermination(String termination) { this.termination = termination; }
    public String getThreadSize() { return threadSize; }
    public void setThreadSize(String threadSize) { this.threadSize = threadSize; }
    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public List<ComponentEntry> getComponents() {
        if (components == null) {
            components = new ArrayList<>();
        }
        return components;
    }

    public void setComponents(List<ComponentEntry> components) {
        this.components = components;
    }

    public byte[] getDetailedImg() {
        return detailedImg;
    }

    public void setDetailedImg(byte[] detailedImg) {
        this.detailedImg = detailedImg;
    }

    // Improved method to add components
    public void addComponent(ComponentEntry component) {
        if (components == null) {
            components = new ArrayList<>();
        }
        if (!components.contains(component)) { // Avoid duplicates
            components.add(component);
            component.setSubProducts(this); // Sync bidirectional relationship
        }
    }

    // Optional: Remove component
    public void removeComponent(ComponentEntry component) {
        if (components != null && components.contains(component)) {
            components.remove(component);
            component.setSubProducts(null); // Clear the relationship
        }
    }

    @Override
    public String toString() {
        return "SubProducts{" +
                "subProdId=" + subProdId +
                ", subTypeName='" + subTypeName + '\'' +
                ", subTypePrice=" + subTypePrice +
                ", description='" + description + '\'' +
                ", dashSize=" + dashSize +
                ", termination='" + termination + '\'' +
                ", threadSize='" + threadSize + '\'' +
                ", group='" + group + '\'' +
                ", componentsCount=" + (components != null ? components.size() : 0) +
                '}';
    }
}