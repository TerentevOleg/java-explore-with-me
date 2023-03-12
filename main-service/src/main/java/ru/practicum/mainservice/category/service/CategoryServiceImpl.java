package ru.practicum.mainservice.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.category.dto.CategoryDtoIn;
import ru.practicum.mainservice.category.dto.CategoryDtoOut;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.AlreadyExistsException;
import ru.practicum.mainservice.exception.ContentDetectedException;
import ru.practicum.mainservice.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CategoryDtoOut add(CategoryDtoIn categoryDtoIn) {
        log.debug("CategoryServiceImpl: add category with name= {}", categoryDtoIn.getName());
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(categoryDtoIn)));
    }

    @Override
    public List<CategoryDtoOut> getAll(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(from, size))
                .stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDtoOut getById(Long id) {
        Category category = findCategoryById(id);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDtoOut update(Long id, CategoryDtoOut categoryDtoOut) {
        Category category = findCategoryById(id);
        if (category.getName().equals(categoryDtoOut.getName())) {
            throw new AlreadyExistsException("CategoryServiceImpl: there is already a category with name=" +
                    categoryDtoOut.getName() + ".");
        }
        category.setName(categoryDtoOut.getName());
        log.debug("CategoryServiceImpl: update category with id= {}", id);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long id) {
        Category category = findCategoryById(id);
        List<Event> events = eventRepository.findEventsByCategory(category);
        if (!events.isEmpty()) {
            throw new ContentDetectedException("CategoryServiceImpl: the category is not empty.");
        } else {
            log.debug("CategoryServiceImpl: delete category with id= {}", id);
            categoryRepository.deleteById(id);
        }
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("CategoryServiceImpl: category  with id=" +
                                id + " was not found."));
    }
}
