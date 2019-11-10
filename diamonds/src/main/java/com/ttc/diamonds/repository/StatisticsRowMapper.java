package com.ttc.diamonds.repository;

import com.ttc.diamonds.dto.StatisticsRow;
import com.ttc.diamonds.dto.UserStatistics;
import com.ttc.diamonds.model.Jewelry;
import com.ttc.diamonds.model.User;
import com.ttc.diamonds.service.converter.JewelryConverter;
import com.ttc.diamonds.service.converter.UserConverter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class StatisticsRowMapper implements RowMapper<StatisticsRow> {

    private UserRepository userRepository;
    private JewelryRepository jewelryRepository;

    public StatisticsRowMapper(UserRepository userRepository, JewelryRepository jewelryRepository) {
        this.jewelryRepository = jewelryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserStatistics mapRow(ResultSet row, int i) throws SQLException {
        UserStatistics userStatistics = new UserStatistics();
        Timestamp rowDate = row.getTimestamp("creation_date");
        if (rowDate != null) {
            userStatistics.setDay(formatDate(rowDate));
            userStatistics.setHour(String.valueOf(extractFromDate(rowDate, Calendar.HOUR_OF_DAY)));
        }
        Long userId = row.getLong("user");
        if (userId != null) {
            User user = userRepository.getOne(userId);
            userStatistics.setUser(UserConverter.convertEntityToDto(user));
        }
        Long jewelryId = row.getLong("jewelry");
        if (jewelryId != null && jewelryId > 0) {
            Jewelry jewelry = jewelryRepository.getOne(jewelryId);
            userStatistics.setJewelryDTO(JewelryConverter.convertEntityToDTO(jewelry));
        }
        userStatistics.setTotal(row.getInt("total"));
        return userStatistics;
    }

    private String formatDate(Timestamp timestamp) {
        DateFormat simpleDateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        return simpleDateFormat.format(timestamp);
    }

    private int extractFromDate(Timestamp date, int field) {
        Calendar calendar = new Calendar.Builder().build();
        calendar.setTimeInMillis(date.getTime());
        return calendar.get(field);
    }
}
