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

import com.qa.springust.dto.GuitaristDTO;
import com.qa.springust.service.GuitaristService;

@RestController
@RequestMapping("/guitarist")
public class GuitaristController {

    // Re - Representational
    // S - State
    // T - Transfer

    // @Autowired
    // Field Autowiring:
    //
    // Spring reflects in a setter method which we can't see (e.g. setService() )
    // this is run AFTER our Controller gets created
    // if the setter method fails, we end up with a Controller that isn't wired up
    // to the Service
    // which will cause exceptions later on!
    private GuitaristService service;

    // Constructor Autowiring:
    //
    // Spring wires the Controller up to the Service at the moment the Controller is
    // created,
    // so, if the autowiring fails, then our Controller object never gets created!
    // This causes fewer exceptions - if we want to make sure our autowiring has
    // worked,
    // all we need to do is check if the Controller exists!
    @Autowired
    public GuitaristController(GuitaristService service) {
        super();
        this.service = service;
    }

    // create
    @PostMapping("/create")
    public ResponseEntity<GuitaristDTO> create(@RequestBody GuitaristDTO guitaristDTO) {
        return new ResponseEntity<>(this.service.create(guitaristDTO), HttpStatus.CREATED);
    }

    // readAll
    @GetMapping("/read")
    public ResponseEntity<List<GuitaristDTO>> getAllGuitarists() {
        return ResponseEntity.ok(this.service.read());
    }

    // readById
    @GetMapping("/read/{id}")
    public ResponseEntity<GuitaristDTO> getGuitaristById(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.read(id));
    }

    // update
    @PutMapping("/update/{id}")
    public ResponseEntity<GuitaristDTO> updateGuitaristById(@PathVariable Long id,
            @RequestBody GuitaristDTO guitaristDTO) {
        return new ResponseEntity<>(this.service.update(guitaristDTO, id), HttpStatus.ACCEPTED);
    }

    // delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GuitaristDTO> deleteGuitaristById(@PathVariable Long id) {
        // Ternary Statements (If/Else):
        //
        // return the boolean result of the delete function
        // UNLESS the HTTP status returned is 204, in which case throw HTTP status 500
        return this.service.delete(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) // 204
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }

}
