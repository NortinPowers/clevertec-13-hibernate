package by.clevertec.house.repository.RawMapper;

import static by.clevertec.house.util.ResponseUtils.DATE_PATTERN;

import by.clevertec.house.domain.Gender;
import by.clevertec.house.domain.Passport;
import by.clevertec.house.domain.Person;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class PersonRowMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        UUID uuid = UUID.fromString(rs.getString("uuid"));
        person.setUuid(uuid);
        person.setName(rs.getString("name"));
        person.setSurname(rs.getString("surname"));
        Gender gender = Gender.values()[rs.getInt("gender")];
        person.setGender(gender);
        Passport passport = getPassport(rs);
        person.setPassport(passport);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDateTime createDate = LocalDateTime.parse(rs.getString("create_date"), formatter);
        person.setCreateDate(createDate);
        LocalDateTime updateDate = LocalDateTime.parse(rs.getString("update_date"), formatter);
        person.setUpdateDate(updateDate);
        return person;
    }

    private Passport getPassport(ResultSet rs) throws SQLException {
        String passportSeries = rs.getString("passport_series");
        String passportNumber = rs.getString("passport_number");
        Passport passport = new Passport();
        passport.setPassportSeries(passportSeries);
        passport.setPassportNumber(passportNumber);
        return passport;
    }
}
