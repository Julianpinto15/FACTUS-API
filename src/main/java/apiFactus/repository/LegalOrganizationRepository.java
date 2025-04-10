package apiFactus.repository;

import apiFactus.model.LegalOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LegalOrganizationRepository extends JpaRepository<LegalOrganization, Integer> {
}
