package com.bookexchange.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListedBooksResponse {
    long id;
    String title;
    BigDecimal priceNew;
    BigDecimal price;
    int conditionNumber;
    String description;
    String thumbnail;
    String publisher;
    String schoolName;
    String fullName;
    String author;
    
    // Constructor for JPQL projection
    public ListedBooksResponse(Long id, String title, BigDecimal priceNew, BigDecimal price, 
                              Integer conditionNumber, String description, String thumbnail, 
                              String publisher, String schoolName, String fullName, String author) {
        this.id = id;
        this.title = title;
        this.priceNew = priceNew;
        this.price = price;
        this.conditionNumber = conditionNumber != null ? conditionNumber : 0;
        this.description = description;
        this.thumbnail = thumbnail;
        this.publisher = publisher;
        this.schoolName = schoolName;
        this.fullName = fullName;
        this.author = author;
    }
}
