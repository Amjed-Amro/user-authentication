package com.eCommerce.demo.intities.AppUser;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AppUserRoles")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AppUserRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String rule;

    public AppUserRoles(String rule) {
        this.rule = rule;
    }

}
