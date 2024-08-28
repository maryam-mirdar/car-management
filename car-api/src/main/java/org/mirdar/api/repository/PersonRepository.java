package org.mirdar.api.repository;

import org.mirdar.api.model.entity.PersonEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    boolean existsByNationalCode(String nationalCode);

    @Query("select p from PersonEntity p where p.firstName like concat('%', :firstNameFilter,'%')" +
            "and p.lastName like concat('%', :lastNameFilter,'%' ) ")
    List<PersonEntity> findByFirstNameLikeAndLastNameLike(String firstNameFilter, String lastNameFilter, Sort sort);
}