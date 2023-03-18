package ru.practicum.mainservice.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.user.dto.UserDtoIn;
import ru.practicum.mainservice.user.dto.UserDtoOut;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/admin")
public class UserAdminController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserDtoOut> add(@Valid @RequestBody UserDtoIn userDtoIn) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.add(userDtoIn));
    }

    @GetMapping("/users")
    public List<UserDtoOut> getAll(@RequestParam(name = "ids") List<Long> ids,
                                   @RequestParam (name = "from", defaultValue = "0") Integer from,
                                   @RequestParam (name = "size", defaultValue = "10") Integer size) {
        return userService.getAll(ids, from, size);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
