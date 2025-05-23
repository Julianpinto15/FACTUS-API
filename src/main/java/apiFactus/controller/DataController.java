package apiFactus.controller;

import apiFactus.model.*;
import apiFactus.repository.LegalOrganizationRepository;
import apiFactus.repository.MunicipalityRepository;
import apiFactus.repository.TributeRepository;
import apiFactus.service.DataPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {

    private final DataPersistenceService dataPersistenceService;

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    private LegalOrganizationRepository legalOrganizationRepository;

    @Autowired
    private TributeRepository tributeRepository;

    public DataController(DataPersistenceService dataPersistenceService) {
        this.dataPersistenceService = dataPersistenceService;
    }

    @GetMapping("/numbering-ranges")
    public ResponseEntity<List<NumberingRange>> getNumberingRanges() {
        return ResponseEntity.ok(dataPersistenceService.getNumberingRanges());
    }

    @GetMapping("/municipalities")
    public ResponseEntity<List<Municipality>> getMunicipalities() {
        return ResponseEntity.ok(municipalityRepository.findAll());
    }

    @GetMapping("/legal-organizations")
    public ResponseEntity<List<LegalOrganization>> getLegalOrganizations() {
        return ResponseEntity.ok(legalOrganizationRepository.findAll());
    }

    @GetMapping("/tributes")
    public ResponseEntity<List<Tribute>> getTributes() {
        return ResponseEntity.ok(tributeRepository.findAll());
    }
}