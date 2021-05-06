package entities;

import java.sql.Date;
import java.util.Objects;

public final class BillItem {

    private Request request;

    private Dish dish;

    private int quantity;

    private Date createdAt;

    @Override
    public String toString() {
        return "BillItem{" +
                "request=" + request +
                ", dish=" + dish +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillItem billItem = (BillItem) o;
        return request.equals(billItem.request) &&
                dish.equals(billItem.dish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request, dish);
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
