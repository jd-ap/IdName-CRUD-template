package crud.tech.proof.service;

import crud.tech.proof.entity.IdName;
import crud.tech.proof.repository.IdNameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdNameServiceImplTest {

    @InjectMocks
    IdNameServiceImpl idNameService;

    @Spy
    IdNameRepository idNameRepository;

    @Test
    void givenFindOne_thenReturnsOne() {
        //given
        UUID aId = UUID.fromString("e2042d1d-87eb-4677-9d55-4a017bfa590e");
        var expected = IdName.builder().id(aId).name("tests").active(Boolean.TRUE).build();

        when(idNameRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(expected));

        //when
        Optional<IdName> result = idNameService.findOneById(aId);

        //then
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    void givenFindAll_thenReturnsList() {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        var itemOne = IdName.builder().id(UUID.fromString("e000001d-87eb-4677-9d55-4a017bfa590e")).build();
        var itemTwo = IdName.builder().id(UUID.fromString("e000002d-87eb-4677-9d55-4a017bfa590e")).build();
        var expected = new PageImpl<>(List.of(itemOne, itemTwo), pageable, 2);

        when(idNameRepository.findAll(any(Pageable.class))).thenReturn(expected);

        //when
        var result = idNameService.findAll(pageable);

        //then
        assertFalse(result.isEmpty());
        assertTrue(result.hasNext());
        assertFalse(result.hasPrevious());
        assertEquals(2, result.getTotalPages());
        assertTrue(result.getContent().stream().findFirst().isPresent());
        assertEquals(itemOne, result.stream().findFirst().orElseThrow());
    }

    @Test
    void givenFindAllByNameThatContain_thenReturnsList() {
        //given
        String name = "name";
        Pageable pageable = PageRequest.of(0, 1);
        var aItem = IdName.builder().id(UUID.fromString("e2042d1d-87eb-4677-9d55-4a017bfa590e")).name("a name test").build();
        var expected = new PageImpl<>(List.of(aItem), pageable, 1);

        when(idNameRepository.findAllByName(anyString(), any(Pageable.class)))
                .thenReturn(expected);

        //when
        var result = idNameService.findAllByNameThatContain(name, pageable);

        //then
        assertFalse(result.isEmpty());
        assertFalse(result.hasNext());
        assertFalse(result.hasPrevious());
        assertEquals(1, result.getTotalPages());
        assertEquals(aItem, result.stream().findFirst().orElseThrow());
    }

    @Test
    void givenCreateOne_thenReturnsOne() {
        //given
        var item = IdName.builder().name("tests").build();
        var expected = IdName.builder().id(UUID.fromString("e2042d1d-87eb-4677-9d55-4a017bfa590e")).name("tests").build();

        when(idNameRepository.save(any(IdName.class))).thenReturn(expected);

        //when
        var result = idNameService.createOne(item);

        //then
        assertEquals(expected, result);
        verify(idNameRepository, times(1)).save(any());
    }

    @Test
    void givenModifyOneById_thenReturnsOne() {
        //given
        var aId = UUID.fromString("e2042d1d-87eb-4677-9d55-4a017bfa590e");
        var item = IdName.builder().name("tests").build();

        var stored = IdName.builder().id(aId).name("a name test").active(Boolean.TRUE).build();
        var expected = IdName.builder().id(aId).name("tests").active(Boolean.TRUE).build();

        when(idNameRepository.findById(any(UUID.class))).thenReturn(Optional.of(stored));
        when(idNameRepository.save(any(IdName.class))).thenReturn(expected);

        //when
        var result = idNameService.modifyOneById(aId, item);

        //then
        assertEquals(expected, result);
        verify(idNameRepository, times(1)).findById(aId);
        verify(idNameRepository, times(1)).save(any());
    }

    @Test
    void givenDeleteOne_thenReturnsOK() {
        //given
        var aId = UUID.fromString("e2042d1d-87eb-4677-9d55-4a017bfa590e");

        doNothing().when(idNameRepository).deleteById(any(UUID.class));

        //when
        idNameService.deleteOneById(aId);

        //then
        verify(idNameRepository, times(1)).deleteById(aId);
    }
}