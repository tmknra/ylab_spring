package com.edu.ulab.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookEntity extends Entity {
    private Long id;
    private Long userId;
    private String title;
    private String author;
    private Long pageCount;
}
