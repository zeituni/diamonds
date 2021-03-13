package com.ttc.diamonds.repository;

import com.ttc.diamonds.dto.UserDTO;
import com.ttc.diamonds.dto.UserStatistics;
import com.ttc.diamonds.model.Store;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UserStatisticsRowMapper implements RowMapper {
    @Override
    public UserStatistics mapRow(ResultSet row, int i) throws SQLException {
        UserStatistics toReturn = new UserStatistics();
        Timestamp rowDate = row.getTimestamp("creation_date");
        if (rowDate != null) {
            toReturn.setDay(formatDate(rowDate));
        }
        UserDTO user = new UserDTO();
        user.setFirstName(row.getString("first_name"));
        user.setId(row.getLong("id"));
        user.setLastName(row.getString("last_name"));
        user.setStore(row.getString("store_name"));
        toReturn.setUser(user);
        toReturn.setTotal(row.getInt("total"));
        return toReturn;
    }

    private String formatDate(Timestamp timestamp) {
        DateFormat simpleDateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        return simpleDateFormat.format(timestamp);
    }
}
