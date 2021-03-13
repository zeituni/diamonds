package com.ttc.diamonds.service;


import com.ttc.diamonds.dto.StatisticsRow;
import com.ttc.diamonds.dto.UserStatistics;
import com.ttc.diamonds.dto.StoreStatistics;
import com.ttc.diamonds.repository.StatisticsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsDao dao;

    @Override
    public List<StatisticsRow> getJewelryVideosByDate(Long jewelryId, String from, String to) {
        return dao.getJewelryVideosByDate(jewelryId, from, to);
    }

    @Override
    public List<StatisticsRow> getJewelryVideosByBarcodeAndDate(String barcode, String from, String to) {
        List<StatisticsRow> toReturn = dao.getJewelryVideosByBarcodeAndDate(barcode, from, to);
        return (List<StatisticsRow>)addMissingDates(toReturn, from, to);

    }

    @Override
    public List<StatisticsRow> getSalesPersonAllVideosSent(Long userId) {
        return dao.getSalesPersonAllVideosSent(userId);
    }

    @Override
    public List<StatisticsRow> getSalesPersonDailyVideosSent(Long userId) {
        return null;
    }

    @Override
    public List<StatisticsRow> getSalesPersonVideosSentByDate(Long userId, String from, String to) {
        List<StatisticsRow> toReturn = dao.getSalesPersonVideosSentByDate(userId, from, to);
        return (List<StatisticsRow>)addMissingDates(toReturn, from, to);
    }

    @Override
    public List<StatisticsRow> getStoreVideosSentByDate(Long manufacturerId, Long storeId, String from, String to) {
        List<StoreStatistics> toReturn = dao.getStoreVideosSentByDate(manufacturerId, storeId, from, to);
        return (List<StatisticsRow>) addMissingDates(toReturn, from, to);
    }

    @Override
    public List<StoreStatistics> getAllStoresVideosSent(Long manufacturerId, String from, String to) {
        return dao.getAllStoresVideosSent(manufacturerId, from, to);
    }

    @Override
    public List<StoreStatistics> getAllStoresPerManufacturer(Long manufacturerId) {
        return dao.getAllStoresPerManufacturer(manufacturerId);
    }


    @Override
    public List<StoreStatistics> getStoreVideoSentGroupedByJewelry(Long manufacturerId, Long storeId, String from, String to) {
        return dao.getStoreVideosSentByDateGroupedByJewelry(manufacturerId, storeId, from, to);
    }

    @Override
    public List<StatisticsRow> getSalesPersonAllVideosSentByDateGroupedByJewelry(Long userId, String from, String to) {
        return dao.getSalesPersonVideosSentByDateGroupedByJewelry(userId, from, to);
    }

    @Override
    public List<StatisticsRow> getTopJewelry(Long manufacturerId, String from, String to) {
        return dao.getTopJewelry(manufacturerId, from, to);
    }

    @Override
    public List<StoreStatistics> getJewelryPerStore(Long manufacturerId, Long jewelryId, String from, String to) {
        return dao.getBarcodePerStore(manufacturerId, jewelryId, from, to);
    }

    @Override
    public List<UserStatistics> getTopSalesPerson(Long manufacturerId, String from, String to) {
        return dao.getTopSalesPerson(manufacturerId, from, to);
    }


    private List<? extends StatisticsRow> addMissingDates(List<? extends StatisticsRow> results, String from, String to) {

        try {
            List<StatisticsRow> toReturn = new ArrayList<>();
            DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startParameter = inputDateFormat.parse(from);
            Date endParameter = inputDateFormat.parse(to);
            DateFormat dbDateFormat = new SimpleDateFormat("MM/dd/yy");
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(startParameter);
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(endParameter);
            endDate.add(Calendar.DATE, 1);
            int index = 0;
            while (startDate.getTime().before(endDate.getTime())) {
                if (index < results.size()) {
                    StatisticsRow current = results.get(index);
                    Date statsDate = dbDateFormat.parse(current.getDay());
                    // check if this is the same date
                    if (statsDate.equals(startDate.getTime())) {
                        toReturn.add(current);
                        index++;
                    } else {
                        // add empty statistics row
                        StatisticsRow statisticsRow = new StatisticsRow();
                        statisticsRow.setDay(dbDateFormat.format(startDate.getTime()));
                        statisticsRow.setTotal(0);
                        toReturn.add(statisticsRow);
                    }
                } else {
                    StatisticsRow statisticsRow = new StatisticsRow();
                    statisticsRow.setDay(dbDateFormat.format(startDate.getTime()));
                    statisticsRow.setTotal(0);
                    toReturn.add(statisticsRow);
                }
                startDate.add(Calendar.DATE, 1);
            }
            return toReturn;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
