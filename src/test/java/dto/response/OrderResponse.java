package dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class OrderResponse {

    private boolean success;
    private String name;
    private Order order;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {
        private int number;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
