package com.edu.ulab.app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends Entity{
    private Long id;
    private String fullName;
    private String title;
    private Integer age;
    private List<Long> booksIdList;

    public UserEntity() {
    }

}
