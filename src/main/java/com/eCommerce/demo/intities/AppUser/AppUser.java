package com.eCommerce.demo.intities.AppUser;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

import static jakarta.persistence.FetchType.EAGER;


@Entity
@Table(name = "AppUser")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private Integer age;
    private String userName;
    private String password;
    private Boolean isEnabled;
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private String createdIpAddress;
    private Integer createdPort;
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = EAGER)
    private Set<AppUserRoles> appUserRole;
    @OneToMany(cascade = CascadeType.ALL, fetch = EAGER)
    private Set<OldPasswords> oldPasswords;
    @OneToMany(cascade = CascadeType.ALL, fetch = EAGER)
    private Set<AppUserUpdateHistory> appUserUpdateHistories;


}
