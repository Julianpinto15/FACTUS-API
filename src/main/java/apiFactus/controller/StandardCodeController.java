package apiFactus.controller;

import apiFactus.model.StandardCode;
import apiFactus.repository.StandardCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/standard-codes")
public class StandardCodeController {

    @Autowired
    private StandardCodeRepository standardCodeRepository;

    @GetMapping
    public ResponseEntity<List<StandardCode>> getStandardCodes() {
        List<StandardCode> codes = standardCodeRepository.findAll();
        if (codes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(codes);
    }
}