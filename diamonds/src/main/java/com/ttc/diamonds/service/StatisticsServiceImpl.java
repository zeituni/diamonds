package com.ttc.diamonds.service;

import com.sun.javafx.tools.packager.PackagerException;
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
    public List<UserStatistics> getJewelryVideosByDate(Long jewelryId, String from, String to) {
        return dao.getJewelryVideosByDate(jewelryId, from, to);
    }

    @Override
    public List<UserStatistics> getJewelryVideosByBarcodeAndDate(String barcode, String from, String to) {
        return dao.getJewelryVideosByBarcodeAndDate(barcode, from, to);    }

    @Override
    public List<UserStatistics> getSalesPersonAllVideosSent(Long userId) {
        return dao.getSalesPersonAllVideosSent(userId);
    }

    @Override
    public List<UserStatistics> getSalesPersonDailyVideosSent(Long userId) {
        return null;
    }

    @Override
    public List<UserStatistics> getSalesPersonVideosSentByDate(Long userId, String from, String to) {
        List<UserStatistics> toReturn = dao.getSalesPersonVideosSentByDate(userId, from, to);
        return (List<UserStatistics>)addMissingDates(toReturn, from, to);
    }

    @Override
    public List<StoreStatistics> getStoreVideosSentByDate(Long manufacturerId, Long storeId, String from, String to) {
        List<StoreStatistics> toReturn = dao.getStoreVideosSentByDate(manufacturerId, storeId, from, to);
        return (List<StoreStatistics>) addMissingDates(toReturn, from, to);
    }

    @Override
    public List<StoreStatistics> getAllStoresVideosSent(Long manufacturerId, String from, String to) {
        return dao.getAllStoresVideosSent(manufacturerId, from, to);
    }

    @Override
    public List<StoreStatistics> getStoreVideoSentGroupedByJewelry(Long manufacturerId, Long storeId, String from, String to) {
        return dao.getStoreVideosSentByDateGroupedByJewelry(manufacturerId, storeId, from, to);
    }

    @Override
    public List<UserStatistics> getSalesPersonAllVideosSentByDateGroupedByJewelry(Long userId, String from, String to) {
        return dao.getSalesPersonVideosSentByDateGroupedByJewelry(userId, from, to);
    }


    private List<? extends StatisticsRow> addMissingDates(List<? extends StatisticsRow> results, String from, String to) {

        try {
            List<StatisticsRow> toReturn = new ArrayList<>();
            DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date start = inputDateFormat.parse(from);
            Date end = inputDateFormat.parse(to);
            DateFormat dbDateFormat = new SimpleDateFormat("MM/dd/yy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            int index = 0;
            while (calendar.getTime().before(end)) {
                if (index < results.size()) {
                    StatisticsRow current = results.get(index);
                    Date statsDate = dbDateFormat.parse(current.getDay());
                    // check if this is the same date
                    if (statsDate.equals(calendar.getTime())) {
                        toReturn.add(current);
                        index++;
                    } else {
                        // add empty statistics row
                        StatisticsRow statisticsRow = new StatisticsRow();
                        statisticsRow.setDay(dbDateFormat.format(calendar.getTime()));
                        statisticsRow.setTotal(0);
                        toReturn.add(statisticsRow);
                    }
                } else {
                    StatisticsRow statisticsRow = new StatisticsRow();
                    statisticsRow.setDay(dbDateFormat.format(calendar.getTime()));
                    statisticsRow.setTotal(0);
                    toReturn.add(statisticsRow);
                }
                calendar.add(Calendar.DATE, 1);
            }
            return toReturn;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
