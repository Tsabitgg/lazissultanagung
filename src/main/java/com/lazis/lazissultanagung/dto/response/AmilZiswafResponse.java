package com.lazis.lazissultanagung.dto.response;

import lombok.Data;

@Data
public class AmilZiswafResponse {
    private long id;
    private String category;
    private String categoryName;
    private double amount;
    private double amil;

    public AmilZiswafResponse(long id, String category, String categoryName, double amount, double amil) {
        this.id = id;
        this.category = category;
        this.categoryName = categoryName;
        this.amount = amount;
        this.amil = amil;
    }

}
