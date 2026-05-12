package com.orderbooking.stockportfolio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private Float price;
    @ManyToOne
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;
    private Date createdAt;
    private Date updatedAt;


}
