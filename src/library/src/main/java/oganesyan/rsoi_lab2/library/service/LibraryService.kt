package oganesyan.rsoi_lab2.library.service

import oganesyan.rsoi_lab2.library.model.library.CreateLibraryRequest
import oganesyan.rsoi_lab2.library.model.library.LibraryInfo
import oganesyan.rsoi_lab2.library.model.library_book.LibraryIdUidResponse
import oganesyan.rsoi_lab2.library.model.library.LibraryRequest
import oganesyan.rsoi_lab2.library.model.library.LibraryResponse
import org.springframework.transaction.annotation.Transactional

interface LibraryService {
    @Transactional(readOnly = true)
    fun getLibraryByCity(libraryRequest: LibraryRequest): LibraryResponse

    fun putLibrary(createLibraryRequest: CreateLibraryRequest)

    @Transactional(readOnly = true)
    fun getAllLibrary(page: Int?, size: Int?): LibraryResponse

    @Transactional(readOnly = true)
    fun getLibraryIdByUid(library_uid: String?): LibraryIdUidResponse

    fun getLibraryByUid(library_uid: String?): LibraryInfo
}