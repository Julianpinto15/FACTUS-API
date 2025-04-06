package apiFactus.repository;

import apiFactus.model.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MunicipalityRepository extends JpaRepository<Municipality, Integer> {
}