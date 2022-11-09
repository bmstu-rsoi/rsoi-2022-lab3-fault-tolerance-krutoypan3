package oganesyan.rsoi_lab2.library.database

import javax.persistence.*

@Entity
@Table(name = "library_books")
class LibraryBooksEntites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column
    var book_id: Int? = null

    @Column
    var library_id: Int? = null

    @Column
    var available_count: Int? = null // Count in library
}