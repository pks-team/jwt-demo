package com.pks.demo.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Martin Varga
 */
@RestController
public class JWTController {
    private TokenService tokenService;

    @Autowired
    public JWTController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "/auth/{userId}",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    public HttpEntity<String> auth(@PathVariable("userId") String userId,
                                   HttpServletRequest request) {
        String token = tokenService.generateToken(userId, request);
        return new HttpEntity<>(token);
    }

    @RequestMapping(value = "/token",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    public HttpEntity<Map<String, Object>> token(@RequestHeader(name = "X-JWT-Token") String token) {
        Map<String, Object> infoFromToken = tokenService.getInfoFromToken(token);
        return new HttpEntity<>(infoFromToken);
    }
}
