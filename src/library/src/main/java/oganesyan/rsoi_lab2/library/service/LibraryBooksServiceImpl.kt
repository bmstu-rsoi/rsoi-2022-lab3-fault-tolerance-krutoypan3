package oganesyan.rsoi_lab2.library.service

import oganesyan.rsoi_lab2.library.model.library_book.*
import oganesyan.rsoi_lab2.library.repository.LibraryBookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Transactional
@Service
open class LibraryBooksServiceImpl @Autowired constructor(
    private val entityManager: EntityManager, private val libraryBookRepository: LibraryBookRepository, restTemplateBuilder: RestTemplateBuilder,
): LibraryBookService {
    private val restTemplate = restTemplateBuilder.build()

    override fun getBooksIdByLibraryId(library_id: Int?): LibraryBookInfoResponse {
        entityManager.joinTransaction()
        val entities = entityManager.createNativeQuery("SELECT book_id, library_id, available_count FROM library_books WHERE library_id = '$library_id'").resultList
        val list = LibraryBookInfoResponse(arrayListOf())
        entities.forEach {
            val objectArray: Array<Any?>? = it as Array<Any?>? // ПРОСТО АДСКИЕ КОСТЫЛИ,ЧТОБЫ ПРЕОБРАЗОВАТЬ ПОЛУЧЕННЫЙ
            list.books.add(LibraryBookInfo(                    // МАССИВ ОБЪЕКТОВ В ЧЕЛОВЕЧЕСКИЙ ВИД
                objectArray?.get(0)?.toString()?.toInt(),
                objectArray?.get(1)?.toString()?.toInt(),
                objectArray?.get(2)?.toString()?.toInt(),
            ))
        }
        return list
    }

    override fun getAvailableCountByBookUidAndLibraryUid(book_uid: String?, library_uid: String?): LibraryBookInfo {

        val url = "http://localhost:8060/library-system/getLibraryIDByUID?library_uid=${library_uid}"
        val libraryId = restTemplate.getForObject(url, LibraryIdUidResponse::class.java)?.library_id

        val url2 = "http://localhost:8060/library-system/books/getBookIDByUID?book_uid=${book_uid}"
        val bookId = restTemplate.getForObject(url2, BookIdUidResponse::class.java)?.book_id

        return if (bookId != null && libraryId != null) {
            entityManager.joinTransaction()
            val entity = entityManager.createNativeQuery("SELECT book_id, library_id, available_count FROM library_books WHERE library_id = '$libraryId' AND book_id = '$bookId'").resultList

            println("\n$entity\n")

            val item: Array<Any?>? = entity[0] as Array<Any?>?

            LibraryBookInfo(
                book_id = bookId,
                library_id = libraryId,
                available_count = item?.get(2)?.toString()?.toInt() ?: 0
            )
        } else
            LibraryBookInfo(bookId, libraryId, available_count = null)
    }

    override fun putLibraryBook(createLibraryBookRequest: CreateLibraryBookRequest) {
        entityManager.createNativeQuery("INSERT INTO library_books (book_id, library_id, available_count) VALUES (?,?,?)")
            .setParameter(1, createLibraryBookRequest.book_id)
            .setParameter(2, createLibraryBookRequest.library_id)
            .setParameter(3, createLibraryBookRequest.available_count)
            .executeUpdate()
    }

    override fun changeAvailableCountByBookUidAndLibraryUid(
        book_uid: String?,
        library_uid: String?,
        available_count: Int?,
    ) {
        val libraryBookInfo = getAvailableCountByBookUidAndLibraryUid(book_uid, library_uid)

        val url = "http://localhost:8060/library-system/getLibraryIDByUID?library_uid=${library_uid}"
        val libraryId = restTemplate.getForObject(url, LibraryIdUidResponse::class.java)?.library_id

        val url2 = "http://localhost:8060/library-system/books/getBookIDByUID?book_uid=${book_uid}"
        val bookId = restTemplate.getForObject(url2, BookIdUidResponse::class.java)?.book_id


        entityManager.joinTransaction()

        entityManager.createNativeQuery("UPDATE library_books SET available_count = '${libraryBookInfo.available_count!! + available_count!!}' WHERE book_id = '$bookId' AND library_id = '$libraryId'").executeUpdate()
    }
}