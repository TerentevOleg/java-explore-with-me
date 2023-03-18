package ru.practicum.mainservice.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.category.dto.CategoryDtoOut;
import ru.practicum.mainservice.category.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDtoOut>> getAll(@RequestParam(name = "from", defaultValue = "0")
                                                              Integer from,
                                                              @RequestParam (name = "size", defaultValue = "10")
                                                              Integer size) {
        return ResponseEntity.ok(categoryService.getAll(from, size));
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoryDtoOut> getById(@PathVariable Long catId) {
        return ResponseEntity.ok(categoryService.getById(catId));
    }
}
