package com.dmitriikuzmin.controller;

import com.dmitriikuzmin.dto.ResponseResult;
import com.dmitriikuzmin.model.TrainerSchedule;
import com.dmitriikuzmin.service.TrainerScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/trainer_schedule")
public class TrainerScheduleController {
    private TrainerScheduleService trainerScheduleService;

    @Autowired
    public void setTrainerScheduleService(TrainerScheduleService trainerScheduleService) {
        this.trainerScheduleService = trainerScheduleService;
    }

    @PostMapping("/{trainerId}")
    public ResponseEntity<ResponseResult<TrainerSchedule>> add(@PathVariable long trainerId,
                                                               @RequestParam String day,
                                                               @RequestParam @DateTimeFormat(pattern = "HH:mm")LocalTime start,
                                                               @RequestParam @DateTimeFormat(pattern = "HH:mm")LocalTime end) {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerScheduleService.add(trainerId, day, start, end)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<ResponseResult<TrainerSchedule>> get(@PathVariable long trainerId) {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerScheduleService.get(trainerId)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{trainerId}")
    public ResponseEntity<ResponseResult<TrainerSchedule>> delete(@PathVariable long trainerId, @RequestParam String day) {
        try {
            return new ResponseEntity<>(new ResponseResult<>(null, this.trainerScheduleService.delete(trainerId, day)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseResult<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
