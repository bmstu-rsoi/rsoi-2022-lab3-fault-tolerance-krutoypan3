package oganesyan.rsoi_lab2.library.database

import javax.persistence.*

@Entity
@Table(name = "library")
class LibraryEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column
    var library_uid: String? = null

    @Column
    var name: String? = null

    @Column
    var city: String? = null

    @Column
    var address: String? = null


}