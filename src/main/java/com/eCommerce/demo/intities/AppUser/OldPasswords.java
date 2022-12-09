package com.eCommerce.demo.intities.AppUser;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "OldPasswords")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OldPasswords {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String password;
    private LocalDateTime changedAt;
    private String changerIpAddress;
    private Integer port;


}
