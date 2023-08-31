package com.aldiramdan.hospital.validator;

import com.aldiramdan.hospital.exception.custom.BadRequestException;
import com.aldiramdan.hospital.exception.custom.ConflictException;
import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.dto.request.DiseaseRequest;
import com.aldiramdan.hospital.model.entity.Disease;
import com.aldiramdan.hospital.repository.DiseaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiseaseValidatorTest {
    @InjectMocks
    private DiseaseValidator diseaseValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateDisease_thenNotFound() {
        DiseaseRepository diseaseRepository = mock(DiseaseRepository.class);
        Optional<Disease> findDisease = Optional.empty();
        lenient().when(diseaseRepository.findById(UUID.randomUUID().toString())).thenReturn(findDisease);

        assertThrows(NotFoundException.class, () -> {
            diseaseValidator.validateDiseaseNotFound(findDisease);
        });
    }

    @Test
    public void testValidateDisease_thenIsExists() {
        DiseaseRepository diseaseRepository = mock(DiseaseRepository.class);
        Optional<Disease> findDisease = Optional.of(new Disease());
        lenient().when(diseaseRepository.findById(UUID.randomUUID().toString())).thenReturn(findDisease);

        assertThrows(ConflictException.class, () -> {
            diseaseValidator.validateDiseaseIsExists(findDisease);
        });
    }

    @Test
    public void testValidateDisease_thenIsAlreadyDeleted() {
        Disease disease = new Disease();
        disease.setIsDeleted(true);

        assertThrows(NotProcessException.class, () -> {
            diseaseValidator.validateDiseaseIsAlreadyDeleted(disease);
        });
    }

    @Test
    public void testValidateDisease_thenIsAlreadyRecovered() {
        Disease disease = new Disease();
        disease.setIsDeleted(false);

        assertThrows(NotProcessException.class, () -> {
            diseaseValidator.validateDiseaseIsAlreadyRecovered(disease);
        });
    }

    @Test
    public void testValidateDisease_DuplicateName() {
        DiseaseRequest diseaseRequest = new DiseaseRequest();
        diseaseRequest.setName("Flu");

        List<DiseaseRequest> listDisease = new ArrayList<>();
        listDisease.add(diseaseRequest);
        listDisease.add(diseaseRequest);

        assertThrows(BadRequestException.class, () -> {
            diseaseValidator.validateDiseaseDuplicateName(listDisease);
        });
    }
}
