package com.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.store.dto.Label;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(length = 200)
    private String name;

    private Double price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDate addedAt;

    @ElementCollection(targetClass = Label.class)
    @Enumerated(EnumType.STRING)
    private Set<Label> labels;

}
