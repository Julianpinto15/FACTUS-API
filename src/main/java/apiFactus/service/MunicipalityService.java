package apiFactus.service;

import apiFactus.dto.MunicipalityDTO;
import apiFactus.repository.MunicipalityRepository;
import apiFactus.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.List;

public class MunicipalityService {
/*
    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    @Qualifier("apiRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private TokenUtil tokenUtil;

    private final String FACTUS_API_URL = "https://api-sandbox.factus.com.co/municipalities";

    public List<MunicipalityDTO> getAllMunicipalities() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenUtil.getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<MunicipalityDTO[]> response = restTemplate.exchange(
                FACTUS_API_URL, HttpMethod.GET, entity, MunicipalityDTO[].class
        );

        return List.of(response.getBody());
    }
*/
}
