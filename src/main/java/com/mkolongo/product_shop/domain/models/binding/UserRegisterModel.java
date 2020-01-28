package com.mkolongo.product_shop.domain.models.binding;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserRegisterModel {

    @NotBlank(message = "Invalid username!")
    private String username;

    @NotBlank(message = "Invalid password!")
    private String password;

    @NotBlank(message = "Invalid confirm password!")
    private String confirmPassword;

    @Email(regexp = "^\\w+[.\\w]*@\\w+[.\\w]*", message = "Invalid email address!")
    private String email;
}
