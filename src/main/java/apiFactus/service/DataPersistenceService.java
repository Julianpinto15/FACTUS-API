package apiFactus.service;

import apiFactus.dto.ApiResponseDTO;
import apiFactus.dto.MunicipalityDTO;
import apiFactus.dto.NumberingRangeDTO;
import apiFactus.dto.TributeDTO;
import apiFactus.dto.UnitMeasureDTO;
import apiFactus.model.Municipality;
import apiFactus.model.NumberingRange;
import apiFactus.model.Tribute;
import apiFactus.model.UnitMeasure;
import apiFactus.repository.MunicipalityRepository;
import apiFactus.repository.NumberingRangeRepository;
import apiFactus.repository.TributeRepository;
import apiFactus.repository.UnitMeasureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DataPersistenceService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataPersistenceService.class);

    private final RestTemplate apiRestTemplate;
    private final AuthService authService;
    private final NumberingRangeRepository numberingRangeRepository;
    private final TributeRepository tributeRepository;
    private final MunicipalityRepository municipalityRepository;
    private final UnitMeasureRepository unitMeasureRepository;
    private final String apiUrl;

    public DataPersistenceService(
            @Qualifier("apiRestTemplate") RestTemplate apiRestTemplate,
            AuthService authService,
            NumberingRangeRepository numberingRangeRepository,
            TributeRepository tributeRepository,
            MunicipalityRepository municipalityRepository,
            UnitMeasureRepository unitMeasureRepository,
            @Value("${factus.api.url}") String apiUrl) {
        this.apiRestTemplate = apiRestTemplate;
        this.authService = authService;
        this.numberingRangeRepository = numberingRangeRepository;
        this.tributeRepository = tributeRepository;
        this.municipalityRepository = municipalityRepository;
        this.unitMeasureRepository = unitMeasureRepository;
        this.apiUrl = apiUrl;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            authService.refreshToken();
        } catch (Exception e) {
            logger.error("Failed to authenticate with Factus API: {}", e.getMessage(), e);
            throw e;
        }

        fetchAndStoreNumberingRanges();
        fetchAndStoreTributes();
        fetchAndStoreMunicipalities();
        fetchAndStoreUnitMeasures();
    }

    private void fetchAndStoreNumberingRanges() {
        try {
            ResponseEntity<ApiResponseDTO<NumberingRangeDTO>> response = apiRestTemplate.exchange(
                    apiUrl + "/v1/numbering-ranges",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponseDTO<NumberingRangeDTO>>() {});

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getData() != null) {
                for (NumberingRangeDTO rangeDTO : response.getBody().getData()) {
                    try {
                        // Eliminar la entidad existente (si existe)
                        numberingRangeRepository.deleteById(rangeDTO.getId());
                        numberingRangeRepository.flush(); // Asegurarse de que la eliminación se aplique inmediatamente

                        // Crear una nueva entidad
                        logger.debug("Inserting NumberingRange#{}", rangeDTO.getId());
                        NumberingRange range = new NumberingRange();
                        range.setId(rangeDTO.getId());
                        range.setPrefix(rangeDTO.getPrefix());
                        range.setStart_number(rangeDTO.getStart_number());
                        range.setEnd_number(rangeDTO.getEnd_number());
                        range.setStatus(rangeDTO.getStatus());
                        range.setVersion(0L); // Inicializar el campo version

                        NumberingRange savedRange = numberingRangeRepository.saveAndFlush(range);
                        logger.debug("Saved NumberingRange#{} with version {}", savedRange.getId(), savedRange.getVersion());
                    } catch (Exception e) {
                        logger.error("Error saving NumberingRange#{}: {}", rangeDTO.getId(), e.getMessage(), e);
                    }
                }
                logger.info("Numbering ranges saved successfully: {}", response.getBody().getData().size());
            } else {
                logger.warn("No numbering ranges found in the response");
            }
        } catch (Exception e) {
            logger.error("Error fetching numbering ranges: {}", e.getMessage(), e);
        }
    }

    private void fetchAndStoreTributes() {
        try {
            ResponseEntity<ApiResponseDTO<TributeDTO>> response = apiRestTemplate.exchange(
                    apiUrl + "/v1/tributes/products?name=",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponseDTO<TributeDTO>>() {});

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getData() != null) {
                for (TributeDTO tributeDTO : response.getBody().getData()) {
                    try {
                        // Eliminar la entidad existente (si existe)
                        tributeRepository.deleteById(tributeDTO.getId());
                        tributeRepository.flush(); // Asegurarse de que la eliminación se aplique inmediatamente

                        // Crear una nueva entidad
                        logger.debug("Inserting Tribute#{}", tributeDTO.getId());
                        Tribute tribute = new Tribute();
                        tribute.setId(tributeDTO.getId());
                        tribute.setCode(tributeDTO.getCode());
                        tribute.setName(tributeDTO.getName());
                        tribute.setVersion(0L); // Inicializar el campo version

                        Tribute savedTribute = tributeRepository.saveAndFlush(tribute);
                        logger.debug("Saved Tribute#{} with version {}", savedTribute.getId(), savedTribute.getVersion());
                    } catch (Exception e) {
                        logger.error("Error saving Tribute#{}: {}", tributeDTO.getId(), e.getMessage(), e);
                    }
                }
                logger.info("Tributes saved successfully: {}", response.getBody().getData().size());
            } else {
                logger.warn("No tributes found in the response");
            }
        } catch (Exception e) {
            logger.error("Error fetching tributes: {}", e.getMessage(), e);
        }
    }

    private void fetchAndStoreMunicipalities() {
        try {
            ResponseEntity<ApiResponseDTO<MunicipalityDTO>> response = apiRestTemplate.exchange(
                    apiUrl + "/v1/municipalities",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponseDTO<MunicipalityDTO>>() {});

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getData() != null) {
                for (MunicipalityDTO municipalityDTO : response.getBody().getData()) {
                    try {
                        // Eliminar la entidad existente (si existe)
                        municipalityRepository.deleteById(municipalityDTO.getId());
                        municipalityRepository.flush(); // Asegurarse de que la eliminación se aplique inmediatamente

                        // Crear una nueva entidad
                        logger.debug("Inserting Municipality#{}", municipalityDTO.getId());
                        Municipality municipality = new Municipality();
                        municipality.setId(municipalityDTO.getId());
                        municipality.setName(municipalityDTO.getName());
                        municipality.setDepartment(municipalityDTO.getDepartment());
                        municipality.setVersion(0L); // Inicializar el campo version

                        Municipality savedMunicipality = municipalityRepository.saveAndFlush(municipality);
                        logger.debug("Saved Municipality#{} with version {}", savedMunicipality.getId(), savedMunicipality.getVersion());
                    } catch (Exception e) {
                        logger.error("Error saving Municipality#{}: {}", municipalityDTO.getId(), e.getMessage(), e);
                    }
                }
                logger.info("Municipalities saved successfully: {}", response.getBody().getData().size());
            } else {
                logger.warn("No municipalities found in the response");
            }
        } catch (Exception e) {
            logger.error("Error fetching municipalities: {}", e.getMessage(), e);
        }
    }

    private void fetchAndStoreUnitMeasures() {
        try {
            ResponseEntity<ApiResponseDTO<UnitMeasureDTO>> response = apiRestTemplate.exchange(
                    apiUrl + "/v1/measurement-units",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponseDTO<UnitMeasureDTO>>() {});

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getData() != null) {
                for (UnitMeasureDTO unitMeasureDTO : response.getBody().getData()) {
                    try {
                        // Eliminar la entidad existente (si existe)
                        unitMeasureRepository.deleteById(unitMeasureDTO.getId());
                        unitMeasureRepository.flush(); // Asegurarse de que la eliminación se aplique inmediatamente

                        // Crear una nueva entidad
                        logger.debug("Inserting UnitMeasure#{}", unitMeasureDTO.getId());
                        UnitMeasure unitMeasure = new UnitMeasure();
                        unitMeasure.setId(unitMeasureDTO.getId());
                        unitMeasure.setName(unitMeasureDTO.getName());
                        unitMeasure.setSymbol(unitMeasureDTO.getSymbol());
                        unitMeasure.setVersion(0L); // Inicializar el campo version

                        UnitMeasure savedUnitMeasure = unitMeasureRepository.saveAndFlush(unitMeasure);
                        logger.debug("Saved UnitMeasure#{} with version {}", savedUnitMeasure.getId(), savedUnitMeasure.getVersion());
                    } catch (Exception e) {
                        logger.error("Error saving UnitMeasure#{}: {}", unitMeasureDTO.getId(), e.getMessage(), e);
                    }
                }
                logger.info("Unit measures saved successfully: {}", response.getBody().getData().size());
            } else {
                logger.warn("No unit measures found in the response");
            }
        } catch (Exception e) {
            logger.error("Error fetching unit measures: {}", e.getMessage(), e);
        }
    }

    public List<NumberingRange> getNumberingRanges() {
        return numberingRangeRepository.findAll();
    }

    public List<Tribute> getTributes() {
        return tributeRepository.findAll();
    }

    public List<Municipality> getMunicipalities() {
        return municipalityRepository.findAll();
    }

    public List<UnitMeasure> getUnitMeasures() {
        return unitMeasureRepository.findAll();
    }
}