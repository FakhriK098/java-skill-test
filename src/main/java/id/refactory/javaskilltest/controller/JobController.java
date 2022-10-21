package id.refactory.javaskilltest.controller;

import id.refactory.javaskilltest.util.TokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("api")
public class JobController {

    private final RestTemplate restTemplate = new RestTemplate();

    private final TokenUtils tokenUtils;

    @Value("${dans.baseUrl}")
    private String dansBaseUrl;

    public JobController(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @GetMapping(value = "/job", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> getJobList(
            @RequestHeader("Authorization") String tokenHeader
    ) {
        String tokenUser = tokenHeader.replace("Bearer ","");

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Invalid Token", HttpStatus.UNAUTHORIZED);
        }

        try {
            Boolean isExpiredToken = tokenUtils.isExpiredToken(tokenUser);

            if (isExpiredToken) {
                return new ResponseEntity<>("Expired Token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();

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
        String tokenUser = tokenHeader.replace("Bearer ","");

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Invalid Token", HttpStatus.UNAUTHORIZED);
        }

        try {
            Boolean isExpiredToken = tokenUtils.isExpiredToken(tokenUser);

            if (isExpiredToken) {
                return new ResponseEntity<>("Expired Token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>("Invalid Token", HttpStatus.UNAUTHORIZED);
        }

        try {
            String url = dansBaseUrl + "/recruitment/positions/" + id;

            return new ResponseEntity<>(restTemplate.getForObject(url, String.class), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
