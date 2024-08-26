package org.mirdar.api.repository;

import org.mirdar.api.model.entity.Car;
import org.mirdar.api.model.entity.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByPerson(Person person);

    boolean existsByLicensePlate(String licensePlate);

    @Query("select c from Car c where c.model like concat('%',?1,'%') ")
    List<Car> findByModelLike(String modelFilter, Sort sort);
}
