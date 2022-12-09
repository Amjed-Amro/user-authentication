package com.eCommerce.demo.intities.AppUser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity (name = "app_user_update_history")
public class AppUserUpdateHistory {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(nullable = false)
    private Long id;
    @Column (name = "action")
    private String action;

    @Column (name = "changedAt")
    private LocalDateTime changedAt;
    @Column(name = "changerIpAddress")
    private String changerIpAddress;
    @Column (name = "port")
    private Integer port;
    @Column(name = "status")
    private String status;


    public AppUserUpdateHistory(String action, LocalDateTime changedAt, String changerIpAddress, Integer port,String status) {
        this.action = action;
        this.changedAt = changedAt;
        this.changerIpAddress = changerIpAddress;
        this.port = port;
        this.status=status;
    }
}
