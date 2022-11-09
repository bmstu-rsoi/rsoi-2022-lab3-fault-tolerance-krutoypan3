package oganesyan.rsoi_lab2.reservation.database

import java.sql.Timestamp
import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "reservation")
class ReservationEntities {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int? = null
    @Column var reservation_uid: UUID? = null
    @Column var username: String? = null
    @Column var book_uid: UUID? = null
    @Column var library_uid: UUID? = null
    @Column var status: String? = null
    @Column var start_date: Timestamp? = null
    @Column var till_date: Timestamp? = null
}