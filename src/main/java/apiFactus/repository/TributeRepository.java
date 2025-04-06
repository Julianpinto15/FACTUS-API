package apiFactus.repository;

import apiFactus.model.Tribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TributeRepository extends JpaRepository<Tribute, Integer> {
}