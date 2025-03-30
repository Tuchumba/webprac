package ru.msu.cmc.webprac.models;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "copies")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Copy implements CommonEntity<Integer> {
    public enum CopyType {
        DVD,
        tape
    }

    public enum RentStatus {
        reserved,
        free
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "copy_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "film_id")
    @ToString.Exclude
    @NonNull
    private Film film_id;

    @Column(nullable = false, name = "type")
    @NonNull
    private CopyType type;

    @Column(nullable = false, name = "status")
    @NonNull
    private RentStatus status;

    @Column(nullable = false, name = "price")
    @NonNull
    private Long price;
}