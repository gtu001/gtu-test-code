package gtu.spring.jdbc.crud;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public abstract class SpringJdbcCrudRepository implements CrudRepository<DicEntity, DicEntityPk> {
    
    abstract List<DicEntity> findByTypeOrderByIndexNo(String type); 

    abstract List<DicEntity> findByTypeAndKeyOrderByIndexNo(String type, String key);

    @Query("select d from DicEntity d where d.key in (:key) and d.type = :type order by d.indexNo")
    abstract List<DicEntity> findByTypeAndKeys(@Param("type") String type , @Param("key") List<String> key);

    @Query("SELECT d from DicEntity d where d.type like :type order by d.type, d.indexNo")
    abstract List<DicEntity> findByLikeType(@Param("type") String type);
    
    abstract List<DicEntity> findByTypeAndKeyNotOrderByIndexNo(String type, String key); 
 
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SpringJdbcCrudRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> List<T> simpleQuery(String sql, Map<String, Object> paramMap, Class<T> clz) {
        BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<>(clz);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValues(paramMap);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
}
