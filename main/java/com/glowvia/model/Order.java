package com.glowvia.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Model class for an order placed by a user.
 */
public class Order {

    private int orderId;
    private int userId;
    private Timestamp orderDate;
    private BigDecimal totalAmount;
    private String status;
    private String customerName;
    private List<OrderItem> items;

    public Order() {
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
