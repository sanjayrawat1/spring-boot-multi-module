package com.github.sanjayrawat1.web.rest;

import com.github.sanjayrawat1.errors.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  REST controller for sampling exception response.
 *
 * @author sanjayrawat1
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ErrorController {

    @GetMapping("/v1/errors/bad-request")
    public ResponseEntity<Void> throwBadRequestException() {
        throw new BadRequestException();
    }
}
