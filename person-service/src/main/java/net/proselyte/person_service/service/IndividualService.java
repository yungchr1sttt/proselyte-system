package net.proselyte.person_service.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.proselyte.person.dto.IndividualDto;
import net.proselyte.person.dto.IndividualPageDto;
import net.proselyte.person.dto.IndividualWriteDto;
import net.proselyte.person.dto.IndividualWriteResponseDto;
import net.proselyte.person_service.exception.PersonException;
import net.proselyte.person_service.mapper.IndividualMapper;
import net.proselyte.person_service.repository.IndividualRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Setter
@Service
@RequiredArgsConstructor
public class IndividualService {

    private final IndividualRepository individualRepository;

    private final IndividualMapper individualMapper;

    @Transactional
    public IndividualWriteResponseDto register(IndividualWriteDto writeDto) {
        var individual = individualMapper.to(writeDto);
        individualRepository.save(individual);
        log.info("IN - register: individual: [{}] successfully registered", individual.getUser().getEmail());
        return new IndividualWriteResponseDto(individual.getId().toString());
    }

    public IndividualPageDto findByEmails(List<String> emails) {
        var individuals = individualRepository.findAllByEmails(emails);
        var from = individualMapper.from(individuals);
        var individualPageDto = new IndividualPageDto();
        individualPageDto.setItems(from);
        return individualPageDto;
    }

    public IndividualDto findById(UUID id) {
        var individual = individualRepository.findById(id)
                .orElseThrow(() -> new PersonException("Individual not found by id = [%s]", id));
        log.info("IN - findById: individual with id: [{}] successfully found", id);
        return individualMapper.from(individual);
    }

    @Transactional
    public void softDelete(UUID id) {
        log.info("IN - softDelete: individual with id: [{}] successfully deleted", id);
        individualRepository.softDelete(id);
    }

    @Transactional
    public void hardDelete(UUID id) {
        var individual = individualRepository.findById(id)
                .orElseThrow(() -> new PersonException("Individual not found by id = [%s]", id));
        log.info("IN - hardDelete: individual with id: [{}] successfully deleted", id);
        individualRepository.delete(individual);
    }

    @Transactional
    public IndividualWriteResponseDto update(UUID id, IndividualWriteDto writeDto) {
        var individual = individualRepository.findById(id)
                .orElseThrow(() -> new PersonException("Individual not found by id = [%s]", id));
        log.info("IN - update: individual with id: [{}] successfully updated", id);
        individualMapper.update(individual, writeDto);
        individualRepository.save(individual);
        return new IndividualWriteResponseDto(individual.getId().toString());
    }


}
