package oganesyan.rsoi_lab2.library.database

import javax.persistence.*

@Entity
@Table(name = "books")
class BookEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column
    var book_uid: String? = null

    @Column
    var name: String? = null

    @Column
    var author: String? = null

    @Column
    var genre: String? = null

    @Column
    var condition: String? = null
}