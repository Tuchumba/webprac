package ru.msu.cmc.webprac.models;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "user_id")
    private Long id;

    @Column(nullable = false, name = "username")
    @NonNull
    private String username;

    @Column(name = "phone")
    @NonNull
    private String phone;

    @Column(nullable = false, name = "email")
    @NonNull
    private String email;
}