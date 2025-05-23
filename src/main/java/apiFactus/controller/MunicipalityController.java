package apiFactus.controller;

import apiFactus.dto.MunicipalityDTO;
import apiFactus.service.MunicipalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/municipalities")
public class MunicipalityController {
/*
    @Autowired
    private MunicipalityService municipalityService;

    @GetMapping
    public ResponseEntity<List<MunicipalityDTO>> getAllMunicipalities() {
        return ResponseEntity.ok(municipalityService.getAllMunicipalities());
    }*/
}