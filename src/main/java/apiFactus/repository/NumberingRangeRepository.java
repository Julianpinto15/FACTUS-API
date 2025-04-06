package apiFactus.repository;

import apiFactus.model.NumberingRange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NumberingRangeRepository extends JpaRepository<NumberingRange, Integer> {
}