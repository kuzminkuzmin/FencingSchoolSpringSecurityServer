package com.dmitriikuzmin.controller;

import com.dmitriikuzmin.dto.ResponseResult;
import com.dmitriikuzmin.model.*;
import com.dmitriikuzmin.service.TrainerScheduleService;
import com.dmitriikuzmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/trainer_schedule")
public class TrainerScheduleController {
    private UserService userService;
    private TrainerScheduleService trainerScheduleService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTrainerScheduleService(TrainerScheduleService trainerScheduleService) {
        this.trainerScheduleService = trainerScheduleService;
    }

    @PostMapping("/{trainerId}")
    public ResponseEntity<ResponseResult<TrainerSchedule>> add(@PathVariable long trainerId,
                                                               @RequestParam String day,
                                                               @RequestParam @DateTimeFormat(pattern = "HH:mm")LocalTime start,
                                                               @RequestParam @DateTimeFormat(pattern = "HH:mm")LocalTime end,
                                                               Authentication authentication) {
        User user = this.userService.get(((UserDetailsImpl) authentication.getPrincipal()).getId());
        if (user.getClass() == Trainer.class && trainerId != user.getId()) {
            return new ResponseEntity<>(new ResponseResult<>("Ошибка доступа", null), HttpStatus.FORBIDDEN);
        }
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerScheduleService.add(trainerId, day, start, end)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<ResponseResult<TrainerSchedule>> get(@PathVariable long trainerId, Authentication authentication) {
        User user = this.userService.get(((UserDetailsImpl) authentication.getPrincipal()).getId());
        if (user.getClass() == Trainer.class && trainerId != user.getId()) {
            return new ResponseEntity<>(new ResponseResult<>("Ошибка доступа", null), HttpStatus.FORBIDDEN);
        }
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerScheduleService.get(trainerId)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{trainerId}")
    public ResponseEntity<ResponseResult<TrainerSchedule>> delete(@PathVariable long trainerId, @RequestParam String day, Authentication authentication) {
        User user = this.userService.get(((UserDetailsImpl) authentication.getPrincipal()).getId());
        if (user.getClass() == Trainer.class && trainerId != user.getId()) {
            return new ResponseEntity<>(new ResponseResult<>("Ошибка доступа", null), HttpStatus.FORBIDDEN);
        }
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerScheduleService.delete(trainerId, day)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
