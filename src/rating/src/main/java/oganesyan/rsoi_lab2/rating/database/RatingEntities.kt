package oganesyan.rsoi_lab2.rating.database

import javax.persistence.*

@Entity
@Table(name = "rating")
class RatingEntities {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int? = null
    @Column var username: String? = null
    @Column var stars: Int? = null
}