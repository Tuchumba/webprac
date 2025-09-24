package ru.msu.cmc.webprac.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "rents")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Rent implements CommonEntity<Integer> {

    public enum RentMethod {
        RENT,
        PURCHASE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "rental_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "copy_id")
    @ToString.Exclude
    @NonNull
    private Copy copy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    @NonNull
    private User user;

    @Column(nullable = false, name = "rent_or_purchase")
    @NonNull
    private RentMethod rent_or_purchase;

    @Column(nullable = false, name = "start_time")
    @NonNull
    private Timestamp start_time;

    @Column(name = "end_time")
    private Timestamp end_time;

    @Column(nullable = false, name = "price")
    @NonNull
    private Long price;
}