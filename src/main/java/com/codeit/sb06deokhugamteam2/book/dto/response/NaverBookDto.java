package com.codeit.sb06deokhugamteam2.book.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NaverBookDto {
    private String title;
    private String author;
    private String publisher;
    private String description;
    private LocalDate publishedDate;
    private String isbn;
    private String thumbnailImage;

    @JsonProperty(value = "pubdate", access = JsonProperty.Access.WRITE_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    public void setPublishedDate(LocalDate pubdate) {
        this.publishedDate = pubdate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    @JsonProperty(value = "image", access = JsonProperty.Access.WRITE_ONLY)
    public void setImage(String imageUrl) {
        this.thumbnailImage = imageUrl;
    }
}
