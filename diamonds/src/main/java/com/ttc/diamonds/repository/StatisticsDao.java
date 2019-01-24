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
}
