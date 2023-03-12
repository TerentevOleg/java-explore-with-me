package ru.practicum.mainservice.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.compilation.dto.CompilationDtoIn;
import ru.practicum.mainservice.compilation.dto.CompilationDtoOut;
import ru.practicum.mainservice.compilation.dto.CompilationPatchDtoIn;
import ru.practicum.mainservice.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/admin")
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping("/compilations")
    public ResponseEntity<CompilationDtoOut> add(@RequestBody @Valid CompilationDtoIn compilationDtoIn) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(compilationService.add(compilationDtoIn));
    }

    @PatchMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDtoOut> update(@PathVariable Long compId,
                                                    @RequestBody @Valid CompilationPatchDtoIn compilationPatchDtoIn) {
        return ResponseEntity.ok(compilationService.update(compId, compilationPatchDtoIn));
    }

    @DeleteMapping("/compilations/{compId}")
    public ResponseEntity<Void> delete(@PathVariable Long compId) {
        compilationService.delete(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
