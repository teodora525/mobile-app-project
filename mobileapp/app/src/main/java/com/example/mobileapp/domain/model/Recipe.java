package com.example.mobileapp.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {
    private String id;
    private String title;
    private String authorId;
    private String authorUsername;
    private String authorRegion;
    private String story;

    // Kategorizacija
    private String mealType;
    private String dishType;
    private String dietType;

    // Poreklo
    private String originCountry;
    private String originRegion;
    private String originCity;

    // Detalji pripreme
    private int servings;
    private int totalPrepTimeMinutes;
    private String mainImageUrl;
    private List<String> additionalImages;
    private String videoUrl;

    // Ocene i alergeni
    private double averageRating;
    private int ratingCount;
    private List<String> allergens;

    // Sastojci i koraci (ugnjezdeni modeli)
    private List<IngredientItem> ingredients;
    private List<Step> steps;

    // Modifikacija recepta (Sekcija 5)
    private String originalRecipeId;
    private String originalRecipeTitle;
    private String modificationExplanation;

    private long createdAt;

    public Recipe() {
        this.additionalImages = new ArrayList<>();
        this.allergens = new ArrayList<>();
        this.ingredients = new ArrayList<>();
        this.steps = new ArrayList<>();
    }

    // Getters and Setters za Recipe
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

    public String getAuthorRegion() { return authorRegion; }
    public void setAuthorRegion(String authorRegion) { this.authorRegion = authorRegion; }

    public String getStory() { return story; }
    public void setStory(String story) { this.story = story; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public String getDishType() { return dishType; }
    public void setDishType(String dishType) { this.dishType = dishType; }

    public String getDietType() { return dietType; }
    public void setDietType(String dietType) { this.dietType = dietType; }

    public String getOriginCountry() { return originCountry; }
    public void setOriginCountry(String originCountry) { this.originCountry = originCountry; }

    public String getOriginRegion() { return originRegion; }
    public void setOriginRegion(String originRegion) { this.originRegion = originRegion; }

    public String getOriginCity() { return originCity; }
    public void setOriginCity(String originCity) { this.originCity = originCity; }

    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }

    public int getTotalPrepTimeMinutes() { return totalPrepTimeMinutes; }
    public void setTotalPrepTimeMinutes(int totalPrepTimeMinutes) { this.totalPrepTimeMinutes = totalPrepTimeMinutes; }

    public String getMainImageUrl() { return mainImageUrl; }
    public void setMainImageUrl(String mainImageUrl) { this.mainImageUrl = mainImageUrl; }

    public List<String> getAdditionalImages() { return additionalImages; }
    public void setAdditionalImages(List<String> additionalImages) { this.additionalImages = additionalImages; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public int getRatingCount() { return ratingCount; }
    public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }

    public List<String> getAllergens() { return allergens; }
    public void setAllergens(List<String> allergens) { this.allergens = allergens; }

    public List<IngredientItem> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientItem> ingredients) { this.ingredients = ingredients; }

    public List<Step> getSteps() { return steps; }
    public void setSteps(List<Step> steps) { this.steps = steps; }

    public String getOriginalRecipeId() { return originalRecipeId; }
    public void setOriginalRecipeId(String originalRecipeId) { this.originalRecipeId = originalRecipeId; }

    public String getOriginalRecipeTitle() { return originalRecipeTitle; }
    public void setOriginalRecipeTitle(String originalRecipeTitle) { this.originalRecipeTitle = originalRecipeTitle; }

    public String getModificationExplanation() { return modificationExplanation; }
    public void setModificationExplanation(String modificationExplanation) { this.modificationExplanation = modificationExplanation; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    // ==========================================
    // Ugnjezdena klasa: IngredientItem
    // ==========================================
    public static class IngredientItem implements Serializable {
        private String ingredientId;
        private String name;
        private Double amount;
        private String unit;
        private boolean branded;
        private List<String> allergens;

        public IngredientItem() {
            this.allergens = new ArrayList<>();
        }

        public IngredientItem(String ingredientId, String name, Double amount, String unit, boolean branded, List<String> allergens) {
            this.ingredientId = ingredientId;
            this.name = name;
            this.amount = amount;
            this.unit = unit;
            this.branded = branded;
            this.allergens = allergens != null ? allergens : new ArrayList<>();
        }

        public String getIngredientId() { return ingredientId; }
        public void setIngredientId(String ingredientId) { this.ingredientId = ingredientId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }

        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }

        public boolean isBranded() { return branded; }
        public void setBranded(boolean branded) { this.branded = branded; }

        public List<String> getAllergens() { return allergens; }
        public void setAllergens(List<String> allergens) { this.allergens = allergens; }
    }

    // ==========================================
    // Ugnjezdena klasa: Step
    // ==========================================
    public static class Step implements Serializable {
        private int order;
        private String title;
        private String description;
        private String preparationType;
        private int durationMinutes;
        private List<String> imageUrls;

        public Step() {
            this.imageUrls = new ArrayList<>();
        }

        public Step(int order, String title, String description, String preparationType, int durationMinutes) {
            this.order = order;
            this.title = title;
            this.description = description;
            this.preparationType = preparationType;
            this.durationMinutes = durationMinutes;
            this.imageUrls = new ArrayList<>();
        }

        public int getOrder() { return order; }
        public void setOrder(int order) { this.order = order; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getPreparationType() { return preparationType; }
        public void setPreparationType(String preparationType) { this.preparationType = preparationType; }

        public int getDurationMinutes() { return durationMinutes; }
        public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

        public List<String> getImageUrls() { return imageUrls; }
        public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    }
}
