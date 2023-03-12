package ru.practicum.mainservice.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.dto.CategoryDtoIn;
import ru.practicum.mainservice.category.dto.CategoryDtoOut;
import ru.practicum.mainservice.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/admin")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<CategoryDtoOut> add(@RequestBody @Valid CategoryDtoIn categoryDtoIn) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.add(categoryDtoIn));
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<CategoryDtoOut> update(@PathVariable Long catId,
                                                 @Valid @RequestBody CategoryDtoOut categoryDtoOut) {
        return ResponseEntity.ok(categoryService.update(catId, categoryDtoOut));
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Void> delete(@PathVariable Long catId) {
        categoryService.delete(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
