package com.example.intermediate.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter

@AllArgsConstructor
@NoArgsConstructor
public class Error {

    private String field;

    private String message;

}
