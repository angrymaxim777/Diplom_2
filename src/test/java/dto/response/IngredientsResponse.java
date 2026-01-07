package dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IngredientsResponse {

    private boolean success;
    private List<Ingredient> data;

    public static class Ingredient {

        @JsonProperty("_id")
        private String id;

        private String name;
        private String type;
        private int proteins;
        private int fat;
        private int carbohydrates;
        private int calories;
        private int price;
        private String image;

        @JsonProperty("image_mobile")
        private String imageMobile;

        @JsonProperty("image_large")
        private String imageLarge;

        @JsonProperty("__v")
        private int version;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getProteins() {
            return proteins;
        }

        public void setProteins(int proteins) {
            this.proteins = proteins;
        }

        public int getFat() {
            return fat;
        }

        public void setFat(int fat) {
            this.fat = fat;
        }

        public int getCarbohydrates() {
            return carbohydrates;
        }

        public void setCarbohydrates(int carbohydrates) {
            this.carbohydrates = carbohydrates;
        }

        public int getCalories() {
            return calories;
        }

        public void setCalories(int calories) {
            this.calories = calories;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImageMobile() {
            return imageMobile;
        }

        public void setImageMobile(String imageMobile) {
            this.imageMobile = imageMobile;
        }

        public String getImageLarge() {
            return imageLarge;
        }

        public void setImageLarge(String imageLarge) {
            this.imageLarge = imageLarge;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Ingredient> getData() {
        return data;
    }

    public void setData(List<Ingredient> data) {
        this.data = data;
    }
}
