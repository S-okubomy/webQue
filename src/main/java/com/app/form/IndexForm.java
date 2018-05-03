package com.app.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Data
public class IndexForm {
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 500)
    private String inputQueText;
//    @NotNull
//    @Size(min = 1, max = 127)
//    private String lastName;
}
