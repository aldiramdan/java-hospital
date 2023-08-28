package com.aldiramdan.hospital.validator;

import com.aldiramdan.hospital.exception.custom.BadRequestException;
import com.aldiramdan.hospital.exception.custom.ConflictException;
import com.aldiramdan.hospital.exception.custom.NotFoundException;
import com.aldiramdan.hospital.exception.custom.NotProcessException;
import com.aldiramdan.hospital.model.dto.request.DiseaseRequest;
import com.aldiramdan.hospital.model.entity.Disease;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiseaseValidator {
    public void validateDiseaseNotFound(Optional<Disease> findDisease) throws Exception {
        if (findDisease.isEmpty()) {
            throw new NotFoundException("Disease is not found!");
        }
    }

    public void validateDiseaseIsExists(Optional<Disease> findDisease) throws Exception {
        if (findDisease.isPresent()) {
            throw new ConflictException("Disease name has been exists");
        }
    }

    public void validateDiseaseIsAlreadyDeleted(Disease disease) throws Exception {
        if (Objects.nonNull(disease.getIsDeleted()) && disease.getIsDeleted()) {
            throw new NotProcessException("Disease is already deleted!");
        }
    }

    public void validateDiseaseIsAlreadyRecovered(Disease disease) throws Exception {
        if (!disease.getIsDeleted()) {
            throw new NotProcessException("Disease is already recovered!");
        }
    }

    public void validateDiseaseDuplicateName(List<DiseaseRequest> listDisease) throws Exception {
        Map<String, Boolean> diseaseMap = new HashMap<>();
        for (DiseaseRequest v : listDisease) {
            String name = v.getName();
            if (diseaseMap.containsKey(name)) {
                throw new BadRequestException("Disease has duplicate name");
            }
            diseaseMap.put(name, true);
        }
    }
}
