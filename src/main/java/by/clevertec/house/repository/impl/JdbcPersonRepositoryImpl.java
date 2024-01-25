package by.clevertec.house.repository.impl;

import by.clevertec.house.domain.Person;
import by.clevertec.house.repository.JdbcPersonRepository;
import by.clevertec.house.repository.RawMapper.PersonRowMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JdbcPersonRepositoryImpl implements JdbcPersonRepository {

    private static final String GET_PERSON_BY_NAME = "select * from persons where name=?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Person> getByName(String name) {
        return jdbcTemplate.query(GET_PERSON_BY_NAME, new PersonRowMapper(), name).stream()
                .findAny();
    }
}
