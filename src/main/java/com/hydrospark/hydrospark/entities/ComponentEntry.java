
package com.hydrospark.hydrospark.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "component_entry")
public class ComponentEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_name", nullable = false)
    private String partName;

    @Column(name = "g", nullable = false)
    private String g;

    @Column(name = "g1", nullable = false)
    private String g1;

    @Column(name = "a", nullable = false)
    private String a;

    @Column(name = "b", nullable = false)
    private String b;

    @Column(name = "c", nullable = false)
    private String c;

    @Column(name = "d", nullable = false)
    private String d;

    @Column(name = "e", nullable = false)
    private String e;

    @Column(name = "l", nullable = false)
    private String l;

    @Column(name = "dn", nullable = false)
    private String dn;

    @Column(name = "af_hex", nullable = false)
    private String afHex;

    @ManyToOne(fetch = FetchType.LAZY) // Remove cascade unless needed
    @JoinColumn(name = "sub_prod_id") // Ensure this matches your DB schema
    private SubProducts subProducts;

    // Constructors
    public ComponentEntry() {}

    public ComponentEntry(String partName, String g, String g1, String a, String b, String c, String d, String e, String l, String dn, String afHex) {
        this.partName = partName;
        this.g = g;
        this.g1 = g1;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.l = l;
        this.dn = dn;
        this.afHex = afHex;
    }

    // Getters and Setters


    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getG1() {
        return g1;
    }

    public void setG1(String g1) {
        this.g1 = g1;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getA() { return a; }
    public void setA(String a) { this.a = a; }
    public String getB() { return b; }
    public void setB(String b) { this.b = b; }
    public String getC() { return c; }
    public void setC(String c) { this.c = c; }
    public String getE() { return e; }
    public void setE(String e) { this.e = e; }
 public String getAfHex() { return afHex; }
    public void setAfHex(String afHex) { this.afHex = afHex; }
    public SubProducts getSubProducts() { return subProducts; }
    public void setSubProducts(SubProducts subProducts) { this.subProducts = subProducts; }
}

//package com.hydrospark.hydrospark.entities;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "component_entry")
//public class ComponentEntry {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "part_no", nullable = false)
//    private String partNo;
//
//    @Column(name = "hose_size_d", nullable = false)
//    private String hoseSizeD;
//
//    @Column(name = "bsp_thread_size_d", nullable = false)
//    private String bspThreadSizeD;
//
//    @Column(name = "a", nullable = false)
//    private String a;
//
//    @Column(name = "b", nullable = false)
//    private String b;
//
//    @Column(name = "c", nullable = false)
//    private String c;
//
//    @Column(name = "e", nullable = false)
//    private String e;
//
//    @Column(name = "f", nullable = false)
//    private String f;
//
//    @Column(name = "af_hex", nullable = false)
//    private String afHex;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    private SubProducts subProducts;
//
//    // Constructors
//    public ComponentEntry() {}
//
//    public ComponentEntry(String partNo, String hoseSizeD, String bspThreadSizeD, String a, String b, String c, String e, String f, String afHex) {
//        this.partNo = partNo;
//        this.hoseSizeD = hoseSizeD;
//        this.bspThreadSizeD = bspThreadSizeD;
//        this.a = a;
//        this.b = b;
//        this.c = c;
//        this.e = e;
//        this.f = f;
//        this.afHex = afHex;
//    }
//
//    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getPartNo() {
//        return partNo;
//    }
//
//    public void setPartNo(String partNo) {
//        this.partNo = partNo;
//    }
//
//    public String getHoseSizeD() {
//        return hoseSizeD;
//    }
//
//    public void setHoseSizeD(String hoseSizeD) {
//        this.hoseSizeD = hoseSizeD;
//    }
//
//    public String getBspThreadSizeD() {
//        return bspThreadSizeD;
//    }
//
//    public void setBspThreadSizeD(String bspThreadSizeD) {
//        this.bspThreadSizeD = bspThreadSizeD;
//    }
//
//    public String getA() {
//        return a;
//    }
//
//    public void setA(String a) {
//        this.a = a;
//    }
//
//    public String getB() {
//        return b;
//    }
//
//    public void setB(String b) {
//        this.b = b;
//    }
//
//    public String getC() {
//        return c;
//    }
//
//    public void setC(String c) {
//        this.c = c;
//    }
//
//    public String getE() {
//        return e;
//    }
//
//    public void setE(String e) {
//        this.e = e;
//    }
//
//    public String getF() {
//        return f;
//    }
//
//    public void setF(String f) {
//        this.f = f;
//    }
//
//    public String getAfHex() {
//        return afHex;
//    }
//
//    public void setAfHex(String afHex) {
//        this.afHex = afHex;
//    }
//
//    public SubProducts getSubProducts() {
//        return subProducts;
//    }
//
//    public void setSubProducts(SubProducts subProducts) {
//        this.subProducts = subProducts;
//    }
//}
