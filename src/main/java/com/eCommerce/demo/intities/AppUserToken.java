package com.eCommerce.demo.intities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Tokens")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class AppUserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String tokenType;
    private String token;
    private String tokenPath;
    private String userEmail;
    private String creatorIp;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime createdAt;


}
