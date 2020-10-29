package com.aeh.api.test.repositories;

import com.aeh.api.models.Entry;
import com.aeh.api.repositories.EntryRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EntryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EntryRepository entryRepository;

    private Entry entry = new Entry(100, 3);

    @Test
    public void findAllShouldReturnEntries() {
        this.entityManager.persist(entry);
        List<Entry> entries = entryRepository.findAll();

        Assertions.assertThat(entries != null && entries.size() == 1).isTrue();
    }

    @Test
    public void findByDenominationShouldReturnEntry() {
        this.entityManager.persist(entry);

        Entry found = entryRepository.findByDenomination(entry.getDenomination());
        Assertions.assertThat(entry != null && found.getDenomination().equals(entry.getDenomination())).isTrue();
    }

    @Test
    public void getCountShouldReturnInteger() {
        this.entityManager.persist(entry);

        Optional<Integer> count = this.entryRepository.getCount();

        Assertions.assertThat(count.isPresent() && count.get().equals(3)).isTrue();
    }
}
