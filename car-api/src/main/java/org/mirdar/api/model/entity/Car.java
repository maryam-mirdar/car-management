package org.mirdar.api.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "car")
@Data
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "model")
    private String model;

    @Column(name = "license_plate", unique = true)
    private String licensePlate;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
