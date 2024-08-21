package com.lazis.lazissultanagung.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wakaf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String wakafCategory;
    private double amount;
    private double distribution;
}
