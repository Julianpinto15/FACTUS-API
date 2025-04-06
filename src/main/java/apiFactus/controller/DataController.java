package apiFactus.controller;

import apiFactus.model.Municipality;
import apiFactus.model.NumberingRange;
import apiFactus.model.Tribute;
import apiFactus.model.UnitMeasure;
import apiFactus.service.DataPersistenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataController {

    private final DataPersistenceService dataPersistenceService;

    public DataController(DataPersistenceService dataPersistenceService) {
        this.dataPersistenceService = dataPersistenceService;
    }

    @GetMapping("/numbering-ranges")
    public ResponseEntity<List<NumberingRange>> getNumberingRanges() {
        return ResponseEntity.ok(dataPersistenceService.getNumberingRanges());
    }

    @GetMapping("/tributes")
    public ResponseEntity<List<Tribute>> getTributes() {
        return ResponseEntity.ok(dataPersistenceService.getTributes());
    }

    @GetMapping("/municipalities")
    public ResponseEntity<List<Municipality>> getMunicipalities() {
        return ResponseEntity.ok(dataPersistenceService.getMunicipalities());
    }

    @GetMapping("/unit-measures")
    public ResponseEntity<List<UnitMeasure>> getUnitMeasures() {
        return ResponseEntity.ok(dataPersistenceService.getUnitMeasures());
    }
}