package id.refactory.javaskilltest.controller;

import id.refactory.javaskilltest.util.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import static id.refactory.javaskilltest.helper.Constant.dansBaseUrl;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("api")
public class JobController {

    private final RestTemplate restTemplate = new RestTemplate();

    private final JwtUtils jwtUtils;

    public JobController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @GetMapping(value = "/job", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> getJobList(
            @RequestHeader("Authorization") String tokenHeader
    ) {
        if (tokenHeader == null && !tokenHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Invalid Token", HttpStatus.UNAUTHORIZED);
        }

        try {
            String url = dansBaseUrl + "/recruitment/positions.json";

            return new ResponseEntity<>(restTemplate.getForObject(url, String.class), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/job/{id}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> getJobDetail(
            @PathVariable String id,
            @RequestHeader("Authorization") String tokenHeader
    ) {

        if (tokenHeader == null && !tokenHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Invalid Token", HttpStatus.UNAUTHORIZED);
        }

        try {
            String url = dansBaseUrl + "/recruitment/positions.json/" + id;

            return new ResponseEntity<>(restTemplate.getForObject(url, String.class), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
