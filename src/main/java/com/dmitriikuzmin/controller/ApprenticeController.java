package com.dmitriikuzmin.controller;

import com.dmitriikuzmin.dto.ResponseResult;
import com.dmitriikuzmin.model.Apprentice;
import com.dmitriikuzmin.model.User;
import com.dmitriikuzmin.model.UserDetailsImpl;
import com.dmitriikuzmin.service.ApprenticeService;
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
@RequestMapping("/apprentice")
public class ApprenticeController {
    private ApprenticeService apprenticeService;
    private UserService userService;

    @Autowired
    public void setApprenticeService(ApprenticeService apprenticeService) {
        this.apprenticeService = apprenticeService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ResponseResult<Apprentice>> add(@Valid @RequestBody Apprentice apprentice) {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.apprenticeService.add(apprentice)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseResult<List<Apprentice>>> getAll() {
        return new ResponseEntity<>(new ResponseResult<>(null, this.apprenticeService.getAll()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseResult<Apprentice>> get(@PathVariable long id) {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.apprenticeService.get(id)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<ResponseResult<Apprentice>> update(@Valid @RequestBody Apprentice apprentice, Authentication authentication) {
        User user = this.userService.get(((UserDetailsImpl) authentication.getPrincipal()).getId());
        if (user.getClass() == Apprentice.class && apprentice.getId() != user.getId()) {
            return new ResponseEntity<>(new ResponseResult<>("Ошибка доступа", null), HttpStatus.FORBIDDEN);
        }
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.apprenticeService.update(apprentice)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseResult<Apprentice>> delete(@PathVariable long id, Authentication authentication) {
        User user = this.userService.get(((UserDetailsImpl) authentication.getPrincipal()).getId());
        if (user.getClass() == Apprentice.class && id != user.getId()) {
            return new ResponseEntity<>(new ResponseResult<>("Ошибка доступа", null), HttpStatus.FORBIDDEN);
        }
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.apprenticeService.delete(id)), HttpStatus.OK);
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
