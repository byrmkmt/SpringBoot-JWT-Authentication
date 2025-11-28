package com.security.session.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Kullanıcı adı boş olamaz")
        @Size(min = 3, max = 50, message = "Kullanıcı adı 3-20 karakter arasında olmalı")
        String username,

        @NotBlank(message = "Şifre boş olamaz")
        @Size(min = 8, message = "Şifre en az 8 karakter olmalı")
        String password
) {}
