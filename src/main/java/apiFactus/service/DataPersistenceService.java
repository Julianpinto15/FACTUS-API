package apiFactus.service;

import apiFactus.dto.*;
import apiFactus.model.*;
import apiFactus.model.Customer;
import apiFactus.repository.*;
import jakarta.transaction.Transactional;
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
    private final LegalOrganizationRepository legalOrganizationRepository;
    private final String apiUrl;
    private final CustomerRepository customerRepository;

    public DataPersistenceService(
            @Qualifier("apiRestTemplate") RestTemplate apiRestTemplate,
            AuthService authService,
            NumberingRangeRepository numberingRangeRepository,
            TributeRepository tributeRepository,
            MunicipalityRepository municipalityRepository,
            UnitMeasureRepository unitMeasureRepository,
            LegalOrganizationRepository legalOrganizationRepository,
            @Value("${factus.api.url}") String apiUrl,
            CustomerRepository customerRepository) {
        this.apiRestTemplate = apiRestTemplate;
        this.authService = authService;
        this.numberingRangeRepository = numberingRangeRepository;
        this.tributeRepository = tributeRepository;
        this.municipalityRepository = municipalityRepository;
        this.unitMeasureRepository = unitMeasureRepository;
        this.legalOrganizationRepository = legalOrganizationRepository;
        this.apiUrl = apiUrl;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
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
                    new ParameterizedTypeReference<>() {});

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getData() != null) {
                for (NumberingRangeDTO rangeDTO : response.getBody().getData()) {
                    try {
                        NumberingRange existingRange = numberingRangeRepository.findById(rangeDTO.getId())
                                .orElse(new NumberingRange());

                        logger.debug("Processing NumberingRange#{}", rangeDTO.getId());
                        existingRange.setId(rangeDTO.getId());
                        existingRange.setPrefix(rangeDTO.getPrefix());
                        existingRange.setStart_number(rangeDTO.getFrom());
                        existingRange.setEnd_number(rangeDTO.getTo());

                        NumberingRange savedRange = numberingRangeRepository.save(existingRange);
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
                    new ParameterizedTypeReference<>() {});

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getData() != null) {
                for (TributeDTO tributeDTO : response.getBody().getData()) {
                    try {
                        Tribute existingTribute = tributeRepository.findById(tributeDTO.getId())
                                .orElse(new Tribute());

                        logger.debug("Processing Tribute#{}", tributeDTO.getId());
                        existingTribute.setId(tributeDTO.getId());
                        existingTribute.setCode(tributeDTO.getCode());
                        existingTribute.setName(tributeDTO.getName());

                        Tribute savedTribute = tributeRepository.save(existingTribute);
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
                    new ParameterizedTypeReference<>() {});

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getData() != null) {
                for (MunicipalityDTO municipalityDTO : response.getBody().getData()) {
                    try {
                        Municipality existingMunicipality = municipalityRepository.findById(municipalityDTO.getId())
                                .orElse(new Municipality());

                        logger.debug("Processing Municipality#{}", municipalityDTO.getId());
                        existingMunicipality.setId(municipalityDTO.getId());
                        existingMunicipality.setName(municipalityDTO.getName());

                        Municipality savedMunicipality = municipalityRepository.save(existingMunicipality);
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
                    new ParameterizedTypeReference<>() {});

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().getData() != null) {
                for (UnitMeasureDTO unitMeasureDTO : response.getBody().getData()) {
                    try {
                        UnitMeasure existingUnitMeasure = unitMeasureRepository.findById(unitMeasureDTO.getId())
                                .orElse(new UnitMeasure());

                        logger.debug("Processing UnitMeasure#{}", unitMeasureDTO.getId());
                        existingUnitMeasure.setId(unitMeasureDTO.getId());
                        existingUnitMeasure.setName(unitMeasureDTO.getName());
                        existingUnitMeasure.setCode(unitMeasureDTO.getCode());

                        UnitMeasure savedUnitMeasure = unitMeasureRepository.save(existingUnitMeasure);
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

    private void saveCustomerFromInvoice(CustomerDTO customerDTO) {
        if (customerDTO.getId() == null) {
            logger.warn("Skipping customer with null identification");
            return;
        }

        Customer existingCustomer = customerRepository.findById(customerDTO.getId())
                .orElse(new Customer());

        existingCustomer.setIdentification(customerDTO.getIdentification());
        existingCustomer.setIdentificationDocumentId(customerDTO.getIdentification_document_id());
        existingCustomer.setDv(customerDTO.getDv());
        existingCustomer.setGraphic_representation_name(customerDTO.getGraphic_representation_name());
        existingCustomer.setCompany(customerDTO.getCompany());
        existingCustomer.setTradeName(customerDTO.getTrade_name());
        existingCustomer.setNames(customerDTO.getNames());
        existingCustomer.setAddress(customerDTO.getAddress());
        existingCustomer.setEmail(customerDTO.getEmail());
        existingCustomer.setPhone(customerDTO.getPhone());

        if (customerDTO.getLegalOrganizationId() != null) {
            try {
                LegalOrganization legalOrg = legalOrganizationRepository.findById(Integer.parseInt(customerDTO.getLegalOrganizationId()))
                        .orElse(null);
                existingCustomer.setLegal_organization(legalOrg);
            } catch (NumberFormatException e) {
                logger.warn("Invalid legal_organization_id: {}", customerDTO.getLegalOrganizationId());
            }
        }

        if (customerDTO.getTributeId() != null) {
            try {
                Tribute tribute = tributeRepository.findById(Integer.parseInt(customerDTO.getTributeId()))
                        .orElse(null);
                existingCustomer.setTribute(tribute);
            } catch (NumberFormatException e) {
                logger.warn("Invalid tribute_id: {}", customerDTO.getTributeId());
            }
        }

        if (customerDTO.getMunicipalityId() != null) {
            try {
                Municipality municipality = municipalityRepository.findById(Integer.parseInt(customerDTO.getMunicipalityId()))
                        .orElse(null);
                existingCustomer.setMunicipality(municipality);
            } catch (NumberFormatException e) {
                logger.warn("Invalid municipality_id: {}", customerDTO.getMunicipalityId());
            }
        }

        Customer savedCustomer = customerRepository.save(existingCustomer);
        logger.debug("Saved Customer#{} with version {}", savedCustomer.getId(), savedCustomer.getVersion());
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

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }
}