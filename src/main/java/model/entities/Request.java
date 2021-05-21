package model.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Request implements Entity {

    private int id;

    private User customer;

    private Status status;

    private String deliveryAddress;

    private long totalPrice;

    private User approvedBy;

    private Date createdAt;

    private Date updatedAt;

    private List<RequestItem> requestItems;

    public Request() {
    }

    public Request(User customer, Status status) {
        this.customer = customer;
        this.status = status;
        requestItems = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", customer=" + customer +
                ", status=" + status +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", totalPrice=" + totalPrice +
                ", approvedBy=" + approvedBy +
                ", bill=" + requestItems +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return id == request.id &&
                totalPrice == request.totalPrice &&
                customer.equals(request.customer) &&
                status == request.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, status, totalPrice);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<RequestItem> getRequestItems() {
        return requestItems;
    }

    public void setRequestItems(List<RequestItem> requestItems) {
        this.requestItems = requestItems;
    }

}
