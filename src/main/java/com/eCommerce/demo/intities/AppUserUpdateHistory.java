package com.eCommerce.demo.intities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "AppUserUpdateHistory")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AppUserUpdateHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String action;
    private LocalDateTime changedAt;
    private String changerIpAddress;
    private String changerEmail;

}
