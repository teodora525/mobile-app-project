package com.example.mobileapp.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ingredient implements Serializable {
    private String id;
    private String name;
    private String category;
    private String defaultUnit;
    private boolean branded;
    private List<String> allergens;

    public Ingredient() {
        this.allergens = new ArrayList<>();
    }

    public Ingredient(String id, String name, String category, String defaultUnit, boolean branded, List<String> allergens) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.defaultUnit = defaultUnit;
        this.branded = branded;
        this.allergens = allergens != null ? allergens : new ArrayList<>();
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDefaultUnit() {
        return defaultUnit;
    }

    public void setDefaultUnit(String defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public boolean isBranded() {
        return branded;
    }

    public void setBranded(boolean branded) {
        this.branded = branded;
    }

    public List<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }
}
