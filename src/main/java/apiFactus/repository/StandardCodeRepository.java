package apiFactus.repository;

import apiFactus.model.StandardCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StandardCodeRepository extends JpaRepository<StandardCode, Integer> {
}
