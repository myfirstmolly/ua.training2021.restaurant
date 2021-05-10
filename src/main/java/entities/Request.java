package entities;

import java.sql.Date;
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

    private List<Bill> bill;

    public Request() {
    }

    public Request(User customer, Status status, String deliveryAddress) {
        this.customer = customer;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
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
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", bill=" + bill +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return id == request.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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

    public List<Bill> getBill() {
        return bill;
    }

    public void setBill(List<Bill> bill) {
        this.bill = bill;
    }

}
