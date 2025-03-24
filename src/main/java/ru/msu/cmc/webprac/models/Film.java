package ru.msu.cmc.webprac.models;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "films")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Film implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "film_id")
    private Long id;

    @Column(nullable = false, name = "title")
    @NonNull
    private String title;

    @Column(nullable = false, name = "genre")
    @NonNull
    private String genre;

    @Column(nullable = false, name = "company")
    @NonNull
    private String company;

    @Column(nullable = false, name = "director")
    @NonNull
    private String director;

    @Column(nullable = false, name = "year_of_release")
    @NonNull
    private Long year_of_release;

    @Column(nullable = false, name = "description")
    @NonNull
    private String description;
}