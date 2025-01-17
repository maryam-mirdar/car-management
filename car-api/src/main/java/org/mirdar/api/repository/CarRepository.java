package org.mirdar.api.repository;

import org.mirdar.api.model.entity.CarEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<CarEntity, Long> {
    List<CarEntity> findByPersonId(Long PersonId);

    boolean existsByLicensePlate(String licensePlate);

    @Query("select c from CarEntity c where c.model like concat('%', :modelFilter,'%') ")
    List<CarEntity> findByModelLike(String modelFilter, Sort sort);

    @Query("select c from CarEntity c join fetch c.person where c.id = :id ")
    Optional<CarEntity> findByIdWithPerson(@Param("id") Long id);

    @EntityGraph(value = "Car.person")
    List<CarEntity> findAll();
}