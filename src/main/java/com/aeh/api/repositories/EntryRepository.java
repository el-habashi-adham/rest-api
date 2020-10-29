package com.aeh.api.repositories;

import com.aeh.api.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "entries", path = "entries")
public interface EntryRepository extends JpaRepository<Entry, Long> {
    Entry findByDenomination(Integer denomination);

    @Query(value = "select sum(assets.amount) from assets", nativeQuery = true)
    Optional<Integer> getCount();
}
