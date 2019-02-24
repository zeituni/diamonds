package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.UserStatistics;
import com.ttc.diamonds.dto.StoreStatistics;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StatisticsService {

    List<UserStatistics> getJewelryVideosByDate(Long jewelryId, String from, String to);

    List<UserStatistics> getJewelryVideosByBarcodeAndDate(String barcode, String from, String to);

    List<UserStatistics> getSalesPersonAllVideosSent(Long userId);

    List<UserStatistics> getSalesPersonDailyVideosSent(Long userId);

    List<UserStatistics> getSalesPersonVideosSentByDate(Long userId, String from, String to);

    List<StoreStatistics> getStoreVideosSentByDate(Long manufacturerId, Long storeId, String from, String to);

    List<StoreStatistics> getAllStoresVideosSent(Long manufacturerId, String from, String to);

    List<StoreStatistics> getStoreVideoSentGroupedByJewelry(Long manufacturerId, Long storeId, String from, String to);

    List<UserStatistics> getSalesPersonAllVideosSentByDateGroupedByJewelry(Long userId, String from, String to);
}
