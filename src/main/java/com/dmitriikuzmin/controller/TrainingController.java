package com.dmitriikuzmin.controller;

import com.dmitriikuzmin.dto.ResponseResult;
import com.dmitriikuzmin.model.*;
import com.dmitriikuzmin.service.TrainingService;
import com.dmitriikuzmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/training")
public class TrainingController {
    private UserService userService;
    private TrainingService trainingService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTrainingService(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<ResponseResult<Training>> add(@RequestParam long apprenticeId,
                                                        @RequestParam long trainerId,
                                                        @RequestParam int numberGym,
                                                        @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate date,
                                                        @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime timeStart,
                                                        Authentication authentication) {

        User user = this.userService.get(((UserDetailsImpl) authentication.getPrincipal()).getId());
        if (user.getClass() == Apprentice.class && apprenticeId != user.getId()
                || user.getClass() == Trainer.class && trainerId != user.getId()) {
            return new ResponseEntity<>(new ResponseResult<>("Ошибка доступа", null), HttpStatus.FORBIDDEN);
        }

        try {
            Training training = this.trainingService.add(apprenticeId, trainerId, numberGym, date, timeStart);
            return new ResponseEntity<>(new ResponseResult<>(null, training), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseResult<Training>> get(@PathVariable long id, Authentication authentication) {
        User user = this.userService.get(((UserDetailsImpl) authentication.getPrincipal()).getId());
        Training training = this.trainingService.get(id);
        if (user.getClass() == Apprentice.class && training.getApprentice().getId() != user.getId()) {
            return new ResponseEntity<>(new ResponseResult<>("Ошибка доступа", null), HttpStatus.FORBIDDEN);
        }
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainingService.get(id)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/apprentice")
    public ResponseEntity<ResponseResult<List<Training>>> getByApprentice(@RequestParam long apprenticeId, Authentication authentication) {
        User user = this.userService.get(((UserDetailsImpl) authentication.getPrincipal()).getId());
        if (user.getClass() == Apprentice.class && apprenticeId != user.getId()) {
            return new ResponseEntity<>(new ResponseResult<>("Ошибка доступа", null), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new ResponseResult<>(null, this.trainingService.getByApprentice(apprenticeId)), HttpStatus.OK);
    }

    @GetMapping("/trainer")
    public ResponseEntity<ResponseResult<List<Training>>> getByTrainer(@RequestParam long trainerId) {
        return new ResponseEntity<>(new ResponseResult<>(null, this.trainingService.getByTrainer(trainerId)), HttpStatus.OK);
    }

    @GetMapping("/availableTime")
    public ResponseEntity<ResponseResult<List<LocalTime>>> getAvailableTime(@RequestParam long trainerId,
                                                                            @RequestParam int numberGym,
                                                                            @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate date) {
        List<LocalTime> timeList = this.trainingService.getAvailableTime(trainerId, numberGym, date);
        return new ResponseEntity<>(new ResponseResult<>(null, timeList), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseResult<Training>> delete(@PathVariable long id, Authentication authentication) {
        User user = this.userService.get(((UserDetailsImpl) authentication.getPrincipal()).getId());
        Training training = this.trainingService.get(id);
        if (user.getClass() == Apprentice.class && training.getApprentice().getId() != user.getId()
                || user.getClass() == Trainer.class && training.getTrainer().getId() != user.getId()) {
            return new ResponseEntity<>(new ResponseResult<>("Ошибка доступа", null), HttpStatus.FORBIDDEN);
        }
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainingService.delete(id)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
