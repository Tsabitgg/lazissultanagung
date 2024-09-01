package com.lazis.lazissultanagung.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Infak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String infakCategory;

    private double amount;

    private double distribution;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coa_id", referencedColumnName = "id")
    private Coa coa;

    @Column(columnDefinition = "BOOLEAN")
    private boolean emergency;
}
