package org.mirdar.api.model.entity;

import com.mwga.common.database.common.BaseMasterEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NamedEntityGraph(name = "Car.person",
        attributeNodes = @NamedAttributeNode("person")
)
@Table(name = "car")
@Getter
@Setter
public class CarEntity extends BaseMasterEntity {
    @Column(nullable = false)
    private String model;

    @Column(unique = true, nullable = false)
    private String licensePlate;

    @Column(name = "person_id", nullable = false)
    private String personId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", insertable = false, updatable = false)
    private PersonEntity person;
}