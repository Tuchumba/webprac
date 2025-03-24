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
public class Rent implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "rent_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "copy_id")
    @ToString.Exclude
    @NonNull
    private Copy copy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @NonNull
    private User user;

    @Column(nullable = false, name = "date_of_transfer")
    @NonNull
    private Timestamp date_of_transfer;

    @Column(nullable = false, name = "date_of_receipt")
    @NonNull
    private Timestamp date_of_receipt;

    @Column(name = "actual_date_of_receipt", nullable = true)
    private Timestamp actual_date_of_receipt;

    @Column(nullable = false, name = "transfer_amount")
    @NonNull
    private Long transfer_amount;
}