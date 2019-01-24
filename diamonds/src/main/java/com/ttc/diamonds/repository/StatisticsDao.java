package com.ttc.diamonds.repository;


import com.ttc.diamonds.dto.StatisticsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
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
                "group by date(creation_date)";
        return jdbcTemplate.query(sql, new Object[] {jewelryId, from, to}, new StatisticsRowMapper(userRepository, jewelryRepository));
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
                "group by date(creation_date)";
        return jdbcTemplate.query(sql, new Object[] {userId, from, to}, new StatisticsRowMapper(userRepository, jewelryRepository));
    }
}
