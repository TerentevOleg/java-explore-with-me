package ru.practicum.mainservice.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.compilation.dto.CompilationDtoOut;
import ru.practicum.mainservice.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompilationPublicController {

    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public ResponseEntity<List<CompilationDtoOut>> getAll(@RequestParam(defaultValue = "false") Boolean pinned,
                                                                   @RequestParam (name = "from", defaultValue = "0")
                                                                   Integer from,
                                                                   @RequestParam (name = "size", defaultValue = "10")
                                                                   Integer size) {
        return ResponseEntity.ok(compilationService.getAll(pinned, from, size));
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDtoOut> getById(@PathVariable Long compId) {
        return ResponseEntity.ok(compilationService.getById(compId));
    }
}
