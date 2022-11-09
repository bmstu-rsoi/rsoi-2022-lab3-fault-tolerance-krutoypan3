package oganesyan.rsoi_lab2.library.repository

import oganesyan.rsoi_lab2.library.database.BookEntities
import oganesyan.rsoi_lab2.library.database.LibraryEntities
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface BookRepository: JpaRepository<BookEntities, Int>