package com.ttc.diamonds.repository;

import com.ttc.diamonds.dto.JewelryDTO;
import com.ttc.diamonds.dto.StoreDTO;
import com.ttc.diamonds.dto.StoreStatistics;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class StoreStatisticsRowMapper implements RowMapper<StoreStatistics> {
    @Override
    public StoreStatistics mapRow(ResultSet row, int i) throws SQLException {
        StoreStatistics toReturn = new StoreStatistics();
        Timestamp rowDate = row.getTimestamp("creation_date");
        if (rowDate != null) {
            toReturn.setDay(formatDate(rowDate));
        }
        StoreDTO dto = new StoreDTO();
        dto.setId(row.getLong("storeId"));
        dto.setCity(row.getString("city"));
        dto.setState(row.getString("state"));
        dto.setName(row.getString("name"));
        dto.setLongitude(row.getDouble("longitude"));
        dto.setLatitude(row.getDouble("latitude"));
        toReturn.setStore(dto);
        Long jewelryId = row.getLong("jewelryId");
        if (jewelryId != null && jewelryId > 0) {
            JewelryDTO jewelryDTO = new JewelryDTO();
            jewelryDTO.setId(jewelryId);
            jewelryDTO.setBarcode(row.getString("barcode"));
            toReturn.setJewelryDTO(jewelryDTO);
        }
        toReturn.setTotal(row.getInt("total"));
        return toReturn;
    }


    private String formatDate(Timestamp timestamp) {
        DateFormat simpleDateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        return simpleDateFormat.format(timestamp);
    }
}
