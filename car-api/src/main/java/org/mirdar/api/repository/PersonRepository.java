package org.mirdar.api.repository;

import org.mirdar.api.model.entity.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByNationalCode(String nationalCode);

    @Query("select p from Person p where p.firstName like concat('%',?1,'%')and p.lastName like concat('%',?2,'%' ) ")
    List<Person> findByFirstNameLikeAndLastNameLike(String firstNameFilter, String lastNameFilter, Sort sort);
}