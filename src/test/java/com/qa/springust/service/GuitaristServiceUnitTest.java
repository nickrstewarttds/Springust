package com.qa.springust.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
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
    private List<Guitarist> guitaristList;
    private Guitarist testGuitarist;
    private Guitarist testGuitaristWithId;
    private GuitaristDTO guitaristDTO;

    // final values to assign to those <expected> objects
    final Long id = 1L;
    final String testName = "John Darnielle";
    final Integer testStrings = 6;
    final String testType = "Ibanez Talman";

    @BeforeEach
    void init() {
        this.guitaristList = new ArrayList<>();
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

        GuitaristDTO expected = this.guitaristDTO;
        GuitaristDTO actual = this.service.create(this.testGuitarist);
        assertThat(expected).isEqualTo(actual);

        // we check that our mocked repository was hit - if it was, that means our
        // service works
        verify(this.repo, times(1)).save(this.testGuitarist);
    }

    @Test
    void readTest() {
        // the repo we spin up extends a Spring type of repo that uses Optionals
        // thus, when we run a method in the repo (e.g. findById() ) we have to
        // return the object we want as an Optional (using Optional.of(our object) )
        // (just think of an Optional as a wrapper-class for any object)
        when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testGuitaristWithId));

        when(this.modelMapper.map(testGuitaristWithId, GuitaristDTO.class)).thenReturn(guitaristDTO);

        assertThat(this.guitaristDTO).isEqualTo(this.service.read(this.id));

        verify(this.repo, times(1)).findById(this.id);
    }

    @Test
    void readAllTest() {
        // findAll() returns a list, which is handy, since we've got one spun up :D
        when(this.repo.findAll()).thenReturn(this.guitaristList);

        when(this.modelMapper.map(this.testGuitaristWithId, GuitaristDTO.class)).thenReturn(guitaristDTO);

        assertThat(this.service.read().isEmpty()).isFalse();

        verify(this.repo, times(1)).findAll();
    }

    @Test
    void updateTest() {
        Guitarist guitarist = new Guitarist("Peter Peter Hughes", 4, "Fender American");
        guitarist.setId(this.id);

        GuitaristDTO guitaristDTO = new GuitaristDTO(null, "Peter Peter Hughes", 4, "Fender American");

        Guitarist updatedGuitarist = new Guitarist(guitaristDTO.getName(), guitaristDTO.getStrings(),
                guitaristDTO.getType());
        updatedGuitarist.setId(this.id);

        GuitaristDTO updatedGuitaristDTO = new GuitaristDTO(this.id, updatedGuitarist.getName(),
                updatedGuitarist.getStrings(), updatedGuitarist.getType());

        // finById() grabs a specific guitarist out of the repo
        when(this.repo.findById(this.id)).thenReturn(Optional.of(guitarist));

        // we then save() an updated guitarist back to the repo
        // we'd normally save() it once we've done stuff to it, but instead we're just
        // feeding in an <expected>
        // that we set up at the top of this method ^
        when(this.repo.save(guitarist)).thenReturn(updatedGuitarist);

        when(this.modelMapper.map(updatedGuitarist, GuitaristDTO.class)).thenReturn(updatedGuitaristDTO);

        assertThat(updatedGuitaristDTO).isEqualTo(this.service.update(guitaristDTO, this.id));

        // since we've ran two when().thenReturn() methods, we need to run a verify() on
        // each:
        verify(this.repo, times(1)).findById(1L);
        verify(this.repo, times(1)).save(updatedGuitarist);
    }

    @Test
    void deleteTest() {
        // we're running this.repo.existsById(id) twice, hence two returns (true &
        // false)
        // the <true> and <false> get plugged in once each to our two verify() methods
        when(this.repo.existsById(id)).thenReturn(true, false);

        assertThat(this.service.delete(id)).isTrue();

        // this plugs in the <true> from our when().thenReturn()
        verify(this.repo, times(1)).deleteById(id);

        // this plugs in the <false> from our when().thenReturn()
        verify(this.repo, times(2)).existsById(id);
    }

}