package apiFactus.repository;

import apiFactus.model.UnitMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitMeasureRepository extends JpaRepository<UnitMeasure, Integer> {
}