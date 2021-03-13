package com.ttc.diamonds.service;

import com.ttc.diamonds.dto.StatisticsRow;
import com.ttc.diamonds.dto.UserStatistics;
import com.ttc.diamonds.dto.StoreStatistics;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StatisticsService {

    List<StatisticsRow> getJewelryVideosByDate(Long jewelryId, String from, String to);

    List<StatisticsRow> getJewelryVideosByBarcodeAndDate(String barcode, String from, String to);

    List<StatisticsRow> getSalesPersonAllVideosSent(Long userId);

    List<StatisticsRow> getSalesPersonDailyVideosSent(Long userId);

    List<StatisticsRow> getSalesPersonVideosSentByDate(Long userId, String from, String to);

    List<StatisticsRow> getStoreVideosSentByDate(Long manufacturerId, Long storeId, String from, String to);

    List<StoreStatistics> getAllStoresVideosSent(Long manufacturerId, String from, String to);

    List<StoreStatistics> getAllStoresPerManufacturer(Long manufacturerId);

    List<StoreStatistics> getStoreVideoSentGroupedByJewelry(Long manufacturerId, Long storeId, String from, String to);

    List<StatisticsRow> getSalesPersonAllVideosSentByDateGroupedByJewelry(Long userId, String from, String to);

    List<StatisticsRow> getTopJewelry(Long manufacturerId, String from, String to);

    List<StoreStatistics> getJewelryPerStore(Long manufacturerId, Long jewelryId, String from, String to);

    List<UserStatistics> getTopSalesPerson(Long manufacturerId, String from, String to);
}
