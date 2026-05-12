package com.orderbooking.stockportfolio.entity;

import com.orderbooking.stockportfolio.enums.BasketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private BasketStatus basketStatus;
    private Date createdAt;
    private Date updatedAt;
}

