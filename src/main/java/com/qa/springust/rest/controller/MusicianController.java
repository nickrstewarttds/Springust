package com.qa.springust.rest.controller;

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

import com.qa.springust.exception.MusicianNotFoundException;
import com.qa.springust.persistence.domain.Musician;
import com.qa.springust.rest.dto.MusicianDTO;
import com.qa.springust.service.MusicianService;

@RestController
@RequestMapping("/musician")
public class MusicianController {

    private MusicianService service;

    @Autowired
    public MusicianController(MusicianService service) {
        super();
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<MusicianDTO> create(@RequestBody Musician musician) {
        MusicianDTO created = this.service.create(musician);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<MusicianDTO> read(@PathVariable Long id) throws MusicianNotFoundException {
        return ResponseEntity.ok(this.service.read(id));
    }

    @GetMapping("/read")
    public ResponseEntity<List<MusicianDTO>> read() {
        return ResponseEntity.ok(this.service.read());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MusicianDTO> update(@PathVariable Long id, @RequestBody MusicianDTO musicianDTO) {
        return new ResponseEntity<>(this.service.update(musicianDTO, id), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MusicianDTO> delete(@PathVariable Long id) {
        return this.service.delete(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) // 204
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }

    @GetMapping("/readBy/{name}")
    public ResponseEntity<List<MusicianDTO>> findByName(@PathVariable String name) throws MusicianNotFoundException {
        return ResponseEntity.ok(this.service.findByName(name));
    }

    @GetMapping("/readBy/{strings}")
    public ResponseEntity<List<MusicianDTO>> findByStrings(@PathVariable Integer strings)
            throws MusicianNotFoundException {
        return ResponseEntity.ok(this.service.findByStrings(strings));
    }

    @GetMapping("/readBy/{type}")
    public ResponseEntity<List<MusicianDTO>> findByType(@PathVariable String type) throws MusicianNotFoundException {
        return ResponseEntity.ok(this.service.findByType(type));
    }

    @GetMapping("/read/names")
    public ResponseEntity<List<MusicianDTO>> orderByName() {
        return ResponseEntity.ok(this.service.orderByNameAZ());
    }

    @GetMapping("/read/strings")
    public ResponseEntity<List<MusicianDTO>> orderByStrings() {
        return ResponseEntity.ok(this.service.orderByStringsAsc());
    }

    @GetMapping("/read/types")
    public ResponseEntity<List<MusicianDTO>> orderByType() {
        return ResponseEntity.ok(this.service.orderByTypeAZ());
    }

}
