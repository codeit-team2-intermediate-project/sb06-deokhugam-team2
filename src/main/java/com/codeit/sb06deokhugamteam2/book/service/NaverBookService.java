package com.codeit.sb06deokhugamteam2.book.service;

import com.codeit.sb06deokhugamteam2.book.dto.response.NaverBookDto;
import com.codeit.sb06deokhugamteam2.common.exception.ErrorCode;
import com.codeit.sb06deokhugamteam2.common.exception.exceptions.NaverSearchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class NaverBookService {
    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;

    public NaverBookService(RestTemplate restTemplate,
                            @Value("${spring.naver.client-id}") String clientId,
                            @Value("${spring.naver.client-secret}") String clientSecret) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public NaverBookDto info(String isbn) {
        String requestUrl = "https://openapi.naver.com/v1/search/book.json?query=" + isbn;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<NaverBookDto> response = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, NaverBookDto.class);
        NaverBookDto naverBookDto = response.getBody();

        if (naverBookDto == null) {
            throw new NaverSearchException(
                    ErrorCode.NAVER_SEARCH_EXCEPTION
                    , Map.of("isbn", isbn)
                    , HttpStatus.NOT_FOUND);
        }

        if (naverBookDto != null && naverBookDto.getThumbnailImage() != null) {
            byte[] imageBytes = downloadImage(naverBookDto.getThumbnailImage());
            String encodingImageData = Base64.getEncoder().encodeToString(imageBytes);
            naverBookDto.setThumbnailImage(encodingImageData);
        }

        return naverBookDto;
    }

    private byte[] downloadImage(String imageUrl) {
        return restTemplate.getForObject(imageUrl, byte[].class);
    }
}
