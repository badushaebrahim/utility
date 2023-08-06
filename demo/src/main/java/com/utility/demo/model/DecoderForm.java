package com.utility.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DecoderForm {
    private String base64String;
    private String fileName;

    // Getters and setters
}
