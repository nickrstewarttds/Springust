package com.qa.springust.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.springust.dto.BandDTO;
import com.qa.springust.service.BandService;

@RestController
@RequestMapping("/band")
public class BandController {

    private BandService service;

    @Autowired
    public BandController(BandService service) {
        super();
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<BandDTO> create(@RequestBody BandDTO bandDTO) {
        BandDTO created = this.service.createBand(bandDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/readAll")
    public ResponseEntity<List<BandDTO>> getAllBands() {
        return ResponseEntity.ok(this.service.getAllBands());
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<BandDTO> getBandById(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.getBandById(id));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<BandDTO> updateBandById(@PathVariable Long id, @RequestBody BandDTO bandDTO) {
        BandDTO updated = this.service.updateBandById(bandDTO, id);
        return new ResponseEntity<>(updated, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BandDTO> deleteBandById(@PathVariable Long id) {
        return this.service.deleteBandById(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
