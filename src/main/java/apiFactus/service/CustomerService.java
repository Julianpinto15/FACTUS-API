package apiFactus.service;

import apiFactus.dto.CustomerDTO;
import apiFactus.model.Customer;
import apiFactus.model.LegalOrganization;
import apiFactus.model.Municipality;
import apiFactus.model.Tribute;
import apiFactus.repository.CustomerRepository;
import apiFactus.repository.LegalOrganizationRepository;
import apiFactus.repository.MunicipalityRepository;
import apiFactus.repository.TributeRepository;
import apiFactus.utils.TokenUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LegalOrganizationRepository legalOrganizationRepository;

    @Autowired
    private TributeRepository tributeRepository;

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Transactional
    public CustomerDTO createCustomer(@Valid CustomerDTO customerDTO) {
        // Buscar entidades relacionadas
        LegalOrganization legalOrganization = legalOrganizationRepository.findById(Integer.parseInt(customerDTO.getLegalOrganizationId()))
                .orElseThrow(() -> new IllegalArgumentException("LegalOrganization con ID " + customerDTO.getLegalOrganizationId() + " no encontrada"));

        Tribute tribute = tributeRepository.findById(Integer.parseInt(customerDTO.getTributeId()))
                .orElseThrow(() -> new IllegalArgumentException("Tribute con ID " + customerDTO.getTributeId() + " no encontrado"));

        Municipality municipality = municipalityRepository.findById(Integer.parseInt(customerDTO.getMunicipalityId()))
                .orElseThrow(() -> new IllegalArgumentException("Municipality con ID " + customerDTO.getMunicipalityId() + " no encontrado"));

        // Mapear DTO a entidad
        Customer customer = new Customer();
        customer.setIdentificationDocumentId(customerDTO.getIdentification_document_id());
        customer.setIdentification(customerDTO.getIdentification());
        customer.setDv(customerDTO.getDv());
        customer.setGraphic_representation_name(customerDTO.getGraphic_representation_name());
        customer.setCompany(customerDTO.getCompany());
        customer.setTradeName(customerDTO.getTrade_name());
        customer.setNames(customerDTO.getNames());
        customer.setAddress(customerDTO.getAddress());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setLegal_organization(legalOrganization);
        customer.setTribute(tribute);
        customer.setMunicipality(municipality);

        // Guardar en la base de datos
        customer = customerRepository.save(customer);

        // Actualizar ID en el DTO
        customerDTO.setId(customer.getIdentification());
        return customerDTO;
    }

    public CustomerDTO getCustomerByIdentification(String identification) {
        Customer customer = customerRepository.findByIdentification(identification);
        if (customer == null) {
            throw new IllegalArgumentException("Cliente con identificación " + identification + " no encontrado");
        }

        // Mapear entidad a DTO
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setIdentification_document_id(customer.getIdentificationDocumentId());
        customerDTO.setIdentification(customer.getIdentification());
        customerDTO.setDv(customer.getDv());
        customerDTO.setGraphic_representation_name(customer.getGraphic_representation_name());
        customerDTO.setCompany(customer.getCompany());
        customerDTO.setTrade_name(customer.getTradeName());
        customerDTO.setNames(customer.getNames());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setLegalOrganizationId(customer.getLegal_organization().getId().toString());
        customerDTO.setTributeId(customer.getTribute().getId().toString());
        customerDTO.setMunicipalityId(customer.getMunicipality().getId().toString());

        return customerDTO;
    }

    public Map<String, Object> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findAll(pageable);

        List<CustomerDTO> customerDTOs = customerPage.getContent().stream()
                .map(customer -> {
                    CustomerDTO dto = new CustomerDTO();
                    dto.setIdentification_document_id(customer.getIdentificationDocumentId());
                    dto.setIdentification(customer.getIdentification());
                    dto.setDv(customer.getDv());
                    dto.setGraphic_representation_name(customer.getGraphic_representation_name());
                    dto.setCompany(customer.getCompany());
                    dto.setTrade_name(customer.getTradeName());
                    dto.setNames(customer.getNames());
                    dto.setAddress(customer.getAddress());
                    dto.setEmail(customer.getEmail());
                    dto.setPhone(customer.getPhone());
                    dto.setLegalOrganizationId(customer.getLegal_organization().getId().toString());
                    dto.setTributeId(customer.getTribute().getId().toString());
                    dto.setMunicipalityId(customer.getMunicipality().getId().toString());
                    return dto;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("data", customerDTOs);
        response.put("total", customerPage.getTotalElements());
        return response;
    }
}