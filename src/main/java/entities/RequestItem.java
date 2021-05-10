package entities;

import java.sql.Date;
import java.util.Objects;

public final class RequestItem implements Entity {

    private int id;

    private int requestId;

    private Dish dish;

    private int quantity;

    private int price;

    private Date createdAt;

    public RequestItem() {
    }

    public RequestItem(int requestId, Dish dish, int quantity) {
        this.requestId = requestId;
        this.dish = dish;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "BillItem{" +
                "request=" + requestId +
                ", dish=" + dish +
                ", quantity=" + quantity +
                ", price=" + price +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestItem requestItem = (RequestItem) o;
        return requestId == requestItem.requestId &&
                dish.equals(requestItem.dish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, dish);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
