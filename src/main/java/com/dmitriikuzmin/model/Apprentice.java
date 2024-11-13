package com.dmitriikuzmin.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "appretices")
public class Apprentice extends User{
    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{11}$", message = "Телефон должен состоять из 11 цифр: 8 (ХХХ) ХХХ ХХ ХХ")
    @NotBlank(message = "Введите номер телефона")
    private String phoneNumber;
}
