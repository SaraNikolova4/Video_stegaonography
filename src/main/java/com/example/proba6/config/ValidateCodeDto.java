package com.example.proba6.config;

import lombok.Data;
import org.springframework.ui.Model;

@Data
public class ValidateCodeDto {

    private String username;
    private String code;
}
