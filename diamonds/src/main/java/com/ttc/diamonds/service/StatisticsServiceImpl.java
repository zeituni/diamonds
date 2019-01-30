package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.StatisticsRow;
import com.ttc.diamonds.repository.StatisticsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<StatisticsRow> getSalesPersonAllVideosSent(Long userId) {
        return dao.getSalesPersonAllVideosSent(userId);
    }

    @Override
    public List<StatisticsRow> getSalesPersonDailyVideosSent(Long userId) {
        return null;
    }

    @Override
    public List<StatisticsRow> getSalesPersonVideosSentByDate(Long userId, String from, String to) {
        return dao.getSalesPersonVideosSentByDate(userId, from, to);
    }
}