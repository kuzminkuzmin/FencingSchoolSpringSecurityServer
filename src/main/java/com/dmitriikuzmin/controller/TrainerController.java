package com.dmitriikuzmin.controller;

import com.dmitriikuzmin.dto.ResponseResult;
import com.dmitriikuzmin.model.Apprentice;
import com.dmitriikuzmin.model.Trainer;
import com.dmitriikuzmin.model.User;
import com.dmitriikuzmin.model.UserDetailsImpl;
import com.dmitriikuzmin.service.TrainerService;
import com.dmitriikuzmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.StringJoiner;

@RestController
@RequestMapping("/trainer")
public class TrainerController {
    private UserService userService;
    private TrainerService trainerService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    public ResponseEntity<ResponseResult<Trainer>> add(@Valid @RequestBody Trainer trainer) {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerService.add(trainer)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseResult<List<Trainer>>> getAll() {
        return new ResponseEntity<>(new ResponseResult<>(null, this.trainerService.getAll()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseResult<Trainer>> getById(@PathVariable long id, Authentication authentication) {
        User user = this.userService.get(((UserDetailsImpl) authentication.getPrincipal()).getId());
        if (user.getClass() == Trainer.class && id != user.getId()) {
            return new ResponseEntity<>(new ResponseResult<>("Ошибка доступа", null), HttpStatus.FORBIDDEN);
        }
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerService.get(id)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<ResponseResult<Trainer>> update(@Valid @RequestBody Trainer trainer, Authentication authentication) {
        User user = this.userService.get(((UserDetailsImpl) authentication.getPrincipal()).getId());
        if (user.getClass() == Trainer.class && trainer.getId() != user.getId()) {
            return new ResponseEntity<>(new ResponseResult<>("Ошибка доступа", null), HttpStatus.FORBIDDEN);
        }
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerService.update(trainer)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseResult<Trainer>> delete(@PathVariable long id, Authentication authentication) {
        User user = this.userService.get(((UserDetailsImpl) authentication.getPrincipal()).getId());
        if (user.getClass() == Trainer.class && id != user.getId()) {
            return new ResponseEntity<>(new ResponseResult<>("Ошибка доступа", null), HttpStatus.FORBIDDEN);
        }
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerService.delete(id)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseResult<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        StringJoiner errors = new StringJoiner(", ");
        ex.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        return new ResponseEntity<>(new ResponseResult<>(errors.toString(), null), HttpStatus.BAD_REQUEST);
    }
}
