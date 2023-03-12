package ru.practicum.mainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.compilation.dto.CompilationDtoOut;
import ru.practicum.mainservice.compilation.dto.CompilationDtoIn;
import ru.practicum.mainservice.compilation.dto.CompilationPatchDtoIn;
import ru.practicum.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.compilation.repository.CompilationRepository;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CompilationDtoOut add(CompilationDtoIn compilationDtoIn) {
        Compilation compilation = compilationMapper.toCompilation(compilationDtoIn);
        compilation.setEvents(eventRepository.findAllByIdIn(compilationDtoIn.getEvents()));
        log.debug("CompilationServiceImpl: add compilation= {}", compilationDtoIn.getTitle());
        compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public CompilationDtoOut getById(Long id) {
        Compilation compilation = findCompilationById(id);
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDtoOut> getAll(Boolean pinned, Integer from, Integer size) {
        return compilationRepository.findAllByPinned(pinned, PageRequest.of(from, size))
                .stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompilationDtoOut update(Long id, CompilationPatchDtoIn compilationPatchDtoIn) {
        Compilation compilation = findCompilationById(id);
        compilation.setEvents(eventRepository.findAllByIdIn(compilationPatchDtoIn.getEvents()));
        compilation.setPinned(compilationPatchDtoIn.getPinned());
        compilation.setTitle(compilationPatchDtoIn.getTitle());
        log.debug("CompilationServiceImpl: update compilation with id= {}", id);
        Compilation compilationResult = compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(compilationResult);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findCompilationById(id);
        log.debug("CompilationServiceImpl: delete compilation with id= {}", id);
        compilationRepository.deleteById(id);
    }

    private Compilation findCompilationById(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("CompilationServiceImpl: compilation with id=" +
                                id + " was not found."));
    }
}
