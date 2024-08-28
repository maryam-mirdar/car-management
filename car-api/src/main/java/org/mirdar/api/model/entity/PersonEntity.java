package org.mirdar.api.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String nationalCode;

    @OneToMany(mappedBy = "person" , cascade = CascadeType.PERSIST , fetch = FetchType.LAZY)
    private List<CarEntity> cars;
}