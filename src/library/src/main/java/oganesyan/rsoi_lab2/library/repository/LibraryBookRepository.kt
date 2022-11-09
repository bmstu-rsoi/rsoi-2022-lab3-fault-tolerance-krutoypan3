package oganesyan.rsoi_lab2.library.repository

import oganesyan.rsoi_lab2.library.database.LibraryBooksEntites
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface LibraryBookRepository: JpaRepository<LibraryBooksEntites, Int>