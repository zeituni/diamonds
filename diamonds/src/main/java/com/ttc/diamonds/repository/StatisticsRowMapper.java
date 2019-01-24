package com.ttc.diamonds.repository;

import com.ttc.diamonds.dto.StatisticsRow;
import com.ttc.diamonds.model.Jewelry;
import com.ttc.diamonds.model.User;
import com.ttc.diamonds.service.converter.JewelryConverter;
import com.ttc.diamonds.service.converter.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;


public class StatisticsRowMapper implements RowMapper<StatisticsRow> {

    private UserRepository userRepository;
    private JewelryRepository jewelryRepository;

    public StatisticsRowMapper(UserRepository userRepository, JewelryRepository jewelryRepository) {
        this.jewelryRepository = jewelryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public StatisticsRow mapRow(ResultSet row, int i) throws SQLException {
        StatisticsRow statisticsRow = new StatisticsRow();
        Timestamp rowDate = row.getTimestamp("creation_date");
        statisticsRow.setDay(String.valueOf(extractFromDate(rowDate, Calendar.DAY_OF_MONTH)));
        statisticsRow.setHour(String.valueOf(extractFromDate(rowDate, Calendar.HOUR_OF_DAY)));
        Long userId = row.getLong("user");
        if (userId != null) {
            User user = userRepository.getOne(userId);
            statisticsRow.setUser(UserConverter.convertEntityToDto(user));
        }
        Long jewelryId = row.getLong("jewelry");
        if (jewelryId != null) {
            Jewelry jewelry = jewelryRepository.getOne(jewelryId);
            statisticsRow.setJewelryDTO(JewelryConverter.convertEntityToDTO(jewelry));
        }
        statisticsRow.setTotal(row.getInt("total"));
        return statisticsRow;
    }

    private int extractFromDate(Timestamp date, int field) {
        Calendar calendar = new Calendar.Builder().build();
        calendar.setTimeInMillis(date.getTime());
        return calendar.get(field);
    }
}
