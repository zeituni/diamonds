package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.StatisticsRow;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface StatisticsService {

    List<StatisticsRow> getJewelryVideosByDate(Long jewelryId, String from, String to);

    List<StatisticsRow> getSalesPersonAllVideosSent(Long userId);

    List<StatisticsRow> getSalesPersonDailyVideosSent(Long userId);

    List<StatisticsRow> getSalesPersonVideosSentByDay(Long userId, Date from, Date to);


    Object getSalesPersonVideosSentByDate(Long userId, String from, String to);
}
