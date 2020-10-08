package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.springust.dto.GuitaristDTO;
import com.qa.springust.persistence.domain.Guitarist;
import com.qa.springust.persistence.repository.GuitaristRepository;

@SpringBootTest
class GuitaristServiceUnitTest {

    // autowiring -> is using something you've already got lying around
    @Autowired
    private GuitaristService service;

    // mockbean -> get mockito to simulate responses from whatever class
    @MockBean
    private GuitaristRepository repo;

    // we've mocked the modelMapper here, which means we need to set its values
    // for every test we use it with using the when().theReturn() method-chain
    @MockBean
    private ModelMapper modelMapper;

    // we don't need a mapToDTO() function if we're spinning up a mock modelMapper
    // because mockito inverts the control of that object - as in, manages it
    // for us instead :) less code woo

    // items we're spinning up to <expect> from our unit tests
    private final List<Guitarist> guitaristList = new ArrayList<>();
    private Guitarist testGuitarist;
    private Guitarist testGuitaristWithId;
    private GuitaristDTO guitaristDTO;

    // final values to assign to those <expected> objects
    final Long id = 1L;

    final String testName = "John Darnielle";
    final Integer testStrings = 6;
    final String testType = "Guitar";

    final String updatedName = "PPH";
    final Integer updatedStrings = 4;
    final String updatedType = "Bass";

    @BeforeEach
    void init() {
        this.testGuitarist = new Guitarist(testName, testStrings, testType);
        this.guitaristList.add(testGuitarist);
        this.testGuitaristWithId = new Guitarist(testGuitarist.getName(), testGuitarist.getStrings(),
                testGuitarist.getType());
        this.testGuitaristWithId.setId(id);
        this.guitaristDTO = modelMapper.map(testGuitaristWithId, GuitaristDTO.class);
    }

    @Test
    void createTest() {
        // a when() to set up our mocked repo
        when(this.repo.save(this.testGuitarist)).thenReturn(this.testGuitaristWithId);

        // and a when() to set up our mocked modelMapper
        when(this.modelMapper.map(this.testGuitaristWithId, GuitaristDTO.class)).thenReturn(this.guitaristDTO);

        // check that the guitaristDTO we set up as our <expected> value
        // is the same as the <actual> result we get when running the service.create()
        // method

        assertThat(this.guitaristDTO).isEqualTo(this.service.create(this.testGuitarist));

        // we check that our mocked repository was hit - if it was, that means our
        // service works
        verify(this.repo, atLeastOnce()).save(this.testGuitarist);
    }

    @Test
    void readOneTest() {
        // the repo we spin up extends a Spring type of repo that uses Optionals
        // thus, when we run a method in the repo (e.g. findById() ) we have to
        // return the object we want as an Optional (using Optional.of(our object) )
        // (just think of an Optional as a wrapper-class for any object)
        when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testGuitaristWithId));

        when(this.modelMapper.map(this.testGuitaristWithId, GuitaristDTO.class)).thenReturn(this.guitaristDTO);

        assertThat(this.guitaristDTO).isEqualTo(this.service.read(this.id));

        verify(this.repo, atLeastOnce()).findById(this.id);
    }

    @Test
    void readAllTest() {
        // findAll() returns a list, which is handy, since we've got one spun up :D
        when(this.repo.findAll()).thenReturn(this.guitaristList);

        when(this.modelMapper.map(this.testGuitaristWithId, GuitaristDTO.class)).thenReturn(this.guitaristDTO);

        assertThat(this.service.read().isEmpty()).isFalse();

        verify(this.repo, atLeastOnce()).findAll();
    }

    @Test
    void updateTest() {
        Guitarist oldEntity = new Guitarist(this.updatedName, this.updatedStrings, this.updatedType);
        oldEntity.setId(this.id);
        GuitaristDTO oldDTO = new GuitaristDTO(null, this.updatedName, this.updatedStrings, this.updatedType);

        Guitarist newEntity = new Guitarist(oldDTO.getName(), oldDTO.getStrings(), oldDTO.getType());
        newEntity.setId(this.id);
        GuitaristDTO newDTO = new GuitaristDTO(this.id, newEntity.getName(), newEntity.getStrings(),
                newEntity.getType());

        // finById() grabs a specific guitarist out of the repo
        when(this.repo.findById(this.id)).thenReturn(Optional.of(oldEntity));

        // we then save() an updated guitarist back to the repo
        // we'd normally save() it once we've done stuff to it, but instead we're just
        // feeding in an <expected>
        // that we set up at the top of this method ^
        when(this.repo.save(oldEntity)).thenReturn(newEntity);

        when(this.modelMapper.map(newEntity, GuitaristDTO.class)).thenReturn(newDTO);

        assertThat(newDTO).isEqualTo(this.service.update(oldDTO, this.id));

        // since we've ran two when().thenReturn() methods, we need to run a verify() on
        // each:
        verify(this.repo, atLeastOnce()).findById(this.id);
        verify(this.repo, atLeastOnce()).save(newEntity);
    }

    @Test
    void deleteTest() {
        when(this.repo.existsById(this.id)).thenReturn(true, false);
        assertThat(this.service.delete(this.id)).isTrue();
        verify(this.repo, atLeastOnce()).deleteById(this.id);
        verify(this.repo, atLeastOnce()).existsById(this.id);
    }

}