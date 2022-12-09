package com.eCommerce.demo.intities.AppUser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity (name = "app_user")
// change the name of the constarin on a column  @UniqueConstraint(name = "unique_email",columnNames = "email"
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_email",columnNames = "email")
        }
)
public class AppUser{
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "user_sequence"
    )
    @Column(
            name = "user_id",
            updatable = false
    )
    private Long id;
    @Column (
            name = "first_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String firstName;
    @Column (
            name = "last_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String lastName;
    @Column (
            name = "email",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String email;
    @Column (
            name = "gender",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String gender;
    @Column (
            name = "age",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private Integer age;
    private String userName;
    private String password;
    private Boolean isEnabled = Boolean.FALSE;
    private Boolean isAccountNonExpired = Boolean.TRUE;
    private Boolean isAccountNonLocked = Boolean.FALSE;
    private Boolean isCredentialsNonExpired = Boolean.TRUE;

    private String createdIpAddress;
    private Integer createdPort;
    private LocalDateTime createdAt;

    @OneToMany(cascade = ALL, fetch = EAGER)
    @JoinTable(name = "app_user_rules"
            ,joinColumns =@JoinColumn(name = "user_id")
            ,inverseJoinColumns =@JoinColumn (name = "app_user_role_id")
    )
    @Column(
            name = "rules",
            nullable = false,
            columnDefinition = "app_user_roles"
    )
    private Set<AppUserRoles> appUserRole;

    @OneToMany(cascade = ALL, fetch = EAGER)
    @JoinTable(name = "app_user_oldPasswords"
            ,joinColumns =@JoinColumn(name = "user_id")
            ,inverseJoinColumns =@JoinColumn (name = "app_user_old_Passwords_id")
    )
    @Column(
            name = "oldPasswords",
            nullable = false,
            columnDefinition = "app_user_oldPasswords"
    )
    private Set<OldPasswords> oldPasswords;

    @OneToMany(cascade = ALL, fetch = EAGER)
    @JoinTable(name = "app_user_update_history"
            ,joinColumns =@JoinColumn(name = "user_id")
            ,inverseJoinColumns =@JoinColumn (name = "app_user_update_history_id")
    )
    @Column(
            name = "actions",
            nullable = false,
            columnDefinition = "app_user_Update_History"
    )
    private Set<AppUserUpdateHistory> appUserUpdateHistories;



    public AppUser(String firstName,
                   String lastName,
                   String email,
                   String gender,
                   Integer age,
                   String userName,
                   String password,
                   Set<AppUserRoles> appUserRole,
                   Boolean isEnabled,
                   Boolean isAccountNonExpired,
                   Boolean isAccountNonLocked,
                   Boolean isCredentialsNonExpired) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.userName = userName;
        this.password = password;
        this.appUserRole = appUserRole;
        this.isEnabled = isEnabled;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }




}
