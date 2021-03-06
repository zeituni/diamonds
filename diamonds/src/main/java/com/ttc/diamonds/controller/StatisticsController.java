package com.ttc.diamonds.controller;

import com.ttc.diamonds.dto.UserStatistics;
import com.ttc.diamonds.dto.StoreStatistics;
import com.ttc.diamonds.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(method = RequestMethod.GET, path = "/getJewelryByDate")
    public ResponseEntity<List<UserStatistics>> getJewelryByDate(@RequestParam("jewelry") Long jewelry,
                                                                 @RequestParam("from") String from,
                                                                 @RequestParam("to") String to) {
        return new ResponseEntity(statisticsService.getJewelryVideosByDate(jewelry, from, to), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getJewelryByBarcodeAndDate")
    public ResponseEntity<List<UserStatistics>> getJewelryByBarcodeAndDate(@RequestParam("barcode") String barcode,
                                                                           @RequestParam("from") String from,
                                                                           @RequestParam("to") String to) {
        return new ResponseEntity(statisticsService.getJewelryVideosByBarcodeAndDate(barcode, from, to), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getSalesPersonAllVideos")
    public ResponseEntity<List<UserStatistics>> getSalesPersonAllVideos(@RequestParam("userId") Long userId) {
        return new ResponseEntity(statisticsService.getSalesPersonAllVideosSent(userId), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getSalesPersonAllVideosGroupedByJewelry")
    public ResponseEntity<List<UserStatistics>> getSalesPersonAllVideosByDate(@RequestParam("userId") Long userId,
                                                                        @RequestParam("from") String from,
                                                                        @RequestParam("to") String to) {
        return new ResponseEntity(statisticsService.getSalesPersonAllVideosSentByDateGroupedByJewelry(userId, from, to), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getSalesPersonVideosByDateRange")
    public ResponseEntity<List<UserStatistics>> getSalesPersonAllVideos(@RequestParam("userId") Long userId,
                                                                        @RequestParam("from") String from,
                                                                        @RequestParam("to") String to) {
        return new ResponseEntity(statisticsService.getSalesPersonVideosSentByDate(userId, from, to), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getTopSalesPerson")
    public ResponseEntity<List<UserStatistics>> getTopSalesPerson(@RequestParam("manufacturerId") Long manufacturerId,
                                                                  @RequestParam("from") String from,
                                                                        @RequestParam("to") String to) {
        return new ResponseEntity(statisticsService.getTopSalesPerson(manufacturerId, from, to), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getAllStoresVideos")
    public ResponseEntity<List<StoreStatistics>> getAllStoresVideos(@RequestParam("manufacturerId") Long manufacturerId,
                                                                    @RequestParam("from") String from,
                                                                    @RequestParam("to") String to) {
        List<StoreStatistics> stores = statisticsService.getAllStoresVideosSent(manufacturerId, from, to);
        return new ResponseEntity(stores, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getAllStoresPerManufacturer")
    public ResponseEntity<List<StoreStatistics>> getAllStoresVideos(@RequestParam("manufacturerId") Long manufacturerId) {
        List<StoreStatistics> stores = statisticsService.getAllStoresPerManufacturer(manufacturerId);
        return new ResponseEntity(stores, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/getStoreVideosByDate")
    public ResponseEntity<List<StoreStatistics>> getStoresVideosByDate(@RequestParam("manufacturerId") Long manufacturerId,
                                                                    @RequestParam("storeId") Long storeId,
                                                                    @RequestParam("from") String from,
                                                                    @RequestParam("to") String to) {
        return new ResponseEntity(statisticsService.getStoreVideosSentByDate(manufacturerId, storeId, from, to), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getStoreVideosGroupByJewelry")
    public ResponseEntity<List<StoreStatistics>> getStoresVideosGroupByJewelry(@RequestParam("manufacturerId") Long manufacturerId,
                                                                    @RequestParam("storeId") Long storeId,
                                                                    @RequestParam("from") String from,
                                                                    @RequestParam("to") String to) {
        return new ResponseEntity(statisticsService.getStoreVideoSentGroupedByJewelry(manufacturerId, storeId, from, to), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getTopJewelry")
    public ResponseEntity<List<StoreStatistics>> getStoresVideosGroupByJewelry(@RequestParam("manufacturerId") Long manufacturerId,
                                                                               @RequestParam("from") String from,
                                                                               @RequestParam("to") String to) {
        return new ResponseEntity(statisticsService.getTopJewelry(manufacturerId, from, to), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getJewelryCountPerStore")
    public ResponseEntity<List<StoreStatistics>> getJewelryCountPerStore(@RequestParam("manufacturerId") Long manufacturerId,
                                                                               @RequestParam("jewelryId") Long jewelryId,
                                                                               @RequestParam("from") String from,
                                                                               @RequestParam("to") String to) {
        return new ResponseEntity(statisticsService.getJewelryPerStore(manufacturerId, jewelryId, from, to), HttpStatus.OK);
    }
}
