//package com.myshop.adminpage.model;
//
//import com.myshop.adminpage.constant.OrderStatus;
//import com.myshop.adminpage.constant.PaymentMethod;
//import jakarta.persistence.*;
//import lombok.Data;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//public class Order {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
////    @Column(length = 100, nullable = false)
//    private String firstName;
//
////    @Column(length = 100, nullable = false)
//    private String lastName;
//
//    private String phone;
//
//    @CreationTimestamp
//    private LocalDateTime createdTime;
//
//    private Double total;
//
//    @Enumerated(EnumType.STRING)
//    private PaymentMethod paymentMethod;
//
//    @Enumerated(EnumType.STRING)
//    private OrderStatus status;
//
//    @ManyToOne
//    @JoinColumn(name = "customer_id")
//    private Customer customer;
//
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
//    private java.util.Set<OrderDetail> orderDetails = new java.util.LinkedHashSet<>();
//
//}
