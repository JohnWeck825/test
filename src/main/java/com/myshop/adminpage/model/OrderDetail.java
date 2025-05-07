//package com.myshop.adminpage.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Data
//public class OrderDetail {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    private Integer quantity;
//    private Float price;
//    private Float subtotal;
//
//    private Float shippingCost;
//
//    private Float extraFee;
//
//    private Float discount;
//
//    @Column(columnDefinition = "TEXT", length = 200)
//    private String note;
//
//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private Order order;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;
//}
