package com.ttc.diamonds.repository;


import com.ttc.diamonds.dto.StatisticsRow;
import com.ttc.diamonds.dto.UserStatistics;
import com.ttc.diamonds.dto.StoreStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class StatisticsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JewelryRepository jewelryRepository;

    public List<StatisticsRow> getJewelryVideosByDate(Long jewelryId, String from, String to) {

        String sql = "select c.jewelry, c.sales_person as user, c.creation_date, count(jewelry) as total " +
                "from customer c " +
                "where c.jewelry = ? and creation_date between ? and ? " +
                "group by date(creation_date) " +
                "order by date(creation_date)";
        return jdbcTemplate.query(sql, new Object[] {jewelryId, from, to}, new StatisticsRowMapper(userRepository, jewelryRepository));
    }

    public List<StatisticsRow> getJewelryVideosByBarcodeAndDate(String barcode, String from, String to) {

        String sql = "select c.jewelry, c.sales_person as user, c.creation_date, count(jewelry) as total " +
                "from customer c " +
                "inner join jewelry j on c.jewelry = j.id " +
                "where j.barcode = ? and c.creation_date between ? and date_add(?, INTERVAL 1 DAY) " +
                "group by date(c.creation_date) " +
                "order by date(c.creation_date)";
        return jdbcTemplate.query(sql, new Object[] {barcode, from, to}, new StatisticsRowMapper(userRepository, jewelryRepository));
    }

    public List<StatisticsRow> getSalesPersonAllVideosSent(Long userId) {
        String sql = "select c.jewelry, c.sales_person as user, c.creation_date, count(c.jewelry) as total " +
                "from customer c " +
                "where c.sales_person = ? " +
                "group by c.jewelry";
        return jdbcTemplate.query(sql, new Object[] {userId}, new StatisticsRowMapper(userRepository, jewelryRepository));
    }

    public List<StatisticsRow> getSalesPersonVideosSentByDate(Long userId, String from, String to) {
        String sql = "select null as jewelry, c.sales_person as user, c.creation_date, count(c.jewelry) as total " +
                "from customer c " +
                "where c.sales_person = ? and creation_date between ? and ? " +
                "group by date(creation_date) " +
                "order by date(c.creation_date)";
        return jdbcTemplate.query(sql, new Object[] {userId, from, to}, new StatisticsRowMapper(userRepository, jewelryRepository));
    }

    public List<StoreStatistics> getStoreVideosSentByDate(Long manufacturerId, Long storeId, String from, String to) {
        String sql = "select s.id as storeId, state, city, s.name, longitude, latitude, null as jewelryId, null as barcode, c.creation_date, count(c.jewelry) as total " +
                "from customer c " +
                "inner join user u on c.sales_person = u.id " +
                "inner join store s on s.id = u.store " +
                "where s.id = ? and c.creation_date between ? and ? " +
                "and s.manufacturer = ? " +
                "group by date(c.creation_date)" +
                "order by date(c.creation_date)";
        return jdbcTemplate.query(sql, new Object[] {storeId, from, to, manufacturerId}, new StoreStatisticsRowMapper());
    }

    public List<StoreStatistics> getStoreVideosSentByDateGroupedByJewelry(Long manufacturerId, Long storeId, String from, String to) {
        String sql = "select s.id as storeId, state, city, s.name, longitude, latitude, j.id as jewelryId, j.barcode, c.creation_date, count(c.jewelry) as total " +
                "from customer c " +
                "inner join user u on c.sales_person = u.id " +
                "inner join store s on s.id = u.store " +
                "inner join jewelry j on c.jewelry = j.id " +
                "where s.id = ? and c.creation_date between ? and ? " +
                "and s.manufacturer = ? " +
                "group by c.jewelry " +
                "order by date(c.creation_date)";
        return jdbcTemplate.query(sql, new Object[] {storeId, from, to, manufacturerId}, new StoreStatisticsRowMapper());
    }

    public List<StoreStatistics> getAllStoresVideosSent(Long manufacturerId, String from, String to) {
        String sql = "select s.id as storeId, state, city, s.name, longitude, latitude,  null as jewelryId, null as barcode, c.creation_date, count(c.jewelry) as total\n" +
                "from store s\n" +
                "inner join user u on s.id = u.store\n" +
                "inner join customer c on c.sales_person = u.id\n" +
                "where s.manufacturer = ?\n" +
                "and c.creation_date between ? and ?\n" +
                "group by s.name\n " +
                "order by total desc;";
        return jdbcTemplate.query(sql, new Object[] {manufacturerId, from, to}, new StoreStatisticsRowMapper());
    }

    public List<StoreStatistics> getAllStoresPerManufacturer(Long manufacturerId) {
        String sql = "select s.id as storeId, state, city, s.name, longitude, latitude,  null as jewelryId, null as barcode, c.creation_date, count(c.jewelry) as total\n" +
                "from store s\n" +
                "left join user u on s.id = u.store\n" +
                "left join customer c on c.sales_person = u.id\n" +
                "where s.manufacturer = ?\n" +
                "group by s.name\n " +
                "order by total desc;";
        return jdbcTemplate.query(sql, new Object[] {manufacturerId}, new StoreStatisticsRowMapper());
    }

    public List<StatisticsRow> getSalesPersonVideosSentByDateGroupedByJewelry(Long userId, String from, String to) {
        String sql = "select jewelry, c.sales_person as user, c.creation_date, count(c.jewelry) as total " +
                "from customer c " +
                "where c.sales_person = ? and creation_date between ? and ? " +
                "group by jewelry ";
        return jdbcTemplate.query(sql, new Object[] {userId, from, to}, new StatisticsRowMapper(userRepository, jewelryRepository));
    }

    public List<StatisticsRow> getTopJewelry(Long manufacturerId, String from, String to) {
        String sql = "select jewelry, c.sales_person as user, c.creation_date, count(c.jewelry) as total " +
                "from customer c " +
                "where c.manufacturer = ? and creation_date between ? and ? " +
                "group by jewelry ";
        return jdbcTemplate.query(sql, new Object[] {manufacturerId, from, to}, new StatisticsRowMapper(userRepository, jewelryRepository));
    }

    public List<StoreStatistics> getBarcodePerStore(long manufacturerId, Long jewelryId, String from, String to) {
        String sql = "select s.id as storeId, s.state, s.city, s.name, longitude, latitude,  c.jewelry as jewelryId, null as barcode, c.creation_date, count(s.id) as total\n" +
                "from store s\n" +
                "inner join user u on u.store = s.id\n" +
                "inner JOIN customer c on c.sales_person = u.id\n" +
                "where c.jewelry = ?\n" +
                "and c.manufacturer = ? \n" +
                "and c.creation_date between ? and ? \n" +
                "group by s.id";
        return jdbcTemplate.query(sql, new Object[] {jewelryId, manufacturerId,  from, to}, new StoreStatisticsRowMapper());

    }
}
