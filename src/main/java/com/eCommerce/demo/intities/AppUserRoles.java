package com.eCommerce.demo.intities;


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
    private String role;


}
