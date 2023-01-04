package com.eCommerce.demo.intities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Login_Ips")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginIp {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String email;
    private LocalDateTime loginAt;
    private String ipAddress;

}
