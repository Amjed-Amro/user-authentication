package com.eCommerce.demo.intities.AppUser;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity (name = "app_user_old_passwords")
@Table(
        name = "app_user_old_passwords",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_id",columnNames = "app_user_old_passwords_id")}

)
public class OldPasswords {

    @Id
    @SequenceGenerator(
            name = "app_user_old_passwords_sequence",
            sequenceName = "app_user_old_passwords_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            ,generator = "app_user_old_passwords_sequence"
    )
    @Column(name = "app_user_old_passwords_id", nullable = false)
    private Long id;
    @Column (name = "password")
    private String password;

    @Column (name = "changedAt")
    private LocalDateTime changedAt;
    @Column(name = "changerIpAddress")
    private String changerIpAddress;
    @Column (name = "port")
    private Integer port;


    public OldPasswords(String password, LocalDateTime changedAt, String changerIpAddress, Integer port) {
        this.password = password;
        this.changedAt = changedAt;
        this.changerIpAddress = changerIpAddress;
        this.port = port;
    }
}
