package com.eCommerce.demo.intities.AppUser;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity (name = "app_user_roles")
@Table(
        name = "app_user_roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_id",columnNames = "app_user_role_id")}
)

public class AppUserRoles {

    @Id
    @SequenceGenerator(
            name = "app_user_roles_sequence",
            sequenceName = "app_user_roles_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            ,generator = "app_user_roles_sequence"
    )
    @Column(name = "app_user_role_id", nullable = false)
    private Long id;
    @Column (name = "rule")
    private String rule;

    public AppUserRoles(String rule){
        this.rule = rule;
    }

}
