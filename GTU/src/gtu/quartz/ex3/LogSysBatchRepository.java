package gtu.quartz.ex3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LogSysBatchRepository extends JpaRepository<LogSysBatch, Long> {

}
