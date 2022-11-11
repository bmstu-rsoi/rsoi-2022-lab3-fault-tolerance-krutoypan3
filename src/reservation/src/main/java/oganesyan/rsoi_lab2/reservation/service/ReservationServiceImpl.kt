package oganesyan.rsoi_lab2.reservation.service

import oganesyan.rsoi_lab2.reservation.database.ReservationEntities
import oganesyan.rsoi_lab2.reservation.model.*
import oganesyan.rsoi_lab2.reservation.repository.ReservationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.UriComponentsBuilder
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.EntityManager
import kotlin.collections.ArrayList

@Transactional
@Service
open class ReservationServiceImpl@Autowired constructor(
    private val entityManager: EntityManager,
    private val reservationRepository: ReservationRepository,
    restTemplateBuilder: RestTemplateBuilder,): ReservationService {
    private val restTemplate = restTemplateBuilder.build()
    override fun getReservationsByUsername(username: String): ReservationByUsernameItemResponse {
        return entitiesToResponse(reservationRepository.findAllByUsername(username))
    }

    override fun putReservation(request: CreateReservationRequest): CreateReservationResponse {
        println("\nTESTO : POINT-5\n")
        // TODO Тут нужно проверять кол-во уже взятых книг
        val reservations = getReservationsByUsername(request.username).reservations
        println("\nTESTO : POINT-6\n")
        // TODO Тут нужно рассчитывать максимальное кол-во книг, которое может взять пользователь
        // Здесь отправляем запрос на получение рейтинга пользователя по username

        println("\nTESTO : POINT-6.5: stars:${request.stars}\n")


        val maxBooksCount = request.stars!! / 10
        println("\nTESTO : POINT-7\n")
        println("\nreservation.size:${reservations.size}\n")
        if (reservations.size < maxBooksCount) {
            println("\nTESTO : POINT-7.5: availableCount:${request.available_count}\n")
            if (request.available_count != null) {
                if (request.available_count!! > 0) {
                    val entity = ReservationEntities()
                    entity.book_uid = UUID.fromString(request.bookUid)
                    entity.library_uid = UUID.fromString(request.libraryUid)
                    entity.username = request.username
                    entity.reservation_uid = UUID.randomUUID()

                    entity.start_date = Timestamp(Date().time + 10800000)


                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val timeMs = sdf.parse(request.tillDate).time

                    entity.till_date = Timestamp(timeMs)

                    // TODO Тут еще нужно получить статус книги по ее UID \\ Upd. Зачем?

                    entity.status = "RENTED"

                    reservationRepository.save(entity)

                    val startDate = sdf.format(entity.start_date)

                    println("\nTESTO : POINT-8 NOT NULL\n")

                    println(entity.status)
                    println(entity)
                    return CreateReservationResponse(
                        status = entity.status,
                        startDate = startDate,
                        tillDate = request.tillDate,
                        reservation_uid = entity.reservation_uid!!.toString()
                    )
                }
            }
        }
        println("\nTESTO : POINT-8 NULL\n")

        return CreateReservationResponse(
            status = null,
            startDate = null,
            tillDate = null,
            reservation_uid = null
        )
    }

    override fun removeReservation(request: RemoveReservationRequest): ReservationByUsernameItem {
        entityManager.joinTransaction()

        val reservation = getReservationByUid(request.reservationUid)


        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val current_date = Date().time + 10800000

        println("\n${reservation.till_date}\n")

        val till_date = sdf.parse(reservation.till_date).time

        val status = if (current_date > till_date)
            "EXPIRED"
        else
            "RETURNED"

        println("\nPOINT 12 $status\n")

        reservation.status?.let {
            entityManager.createNativeQuery("UPDATE reservation SET status = '$status' WHERE reservation_uid = '${request.reservationUid}'").executeUpdate()
        }
        return reservation
    }

    private fun getReservationByUid(reservation_uid: String): ReservationByUsernameItem {
        entityManager.joinTransaction()

        println("\n$reservation_uid\n")

        val entities = entityManager.createNativeQuery("SELECT CAST(reservation_uid AS VARCHAR), username, CAST(book_uid AS VARCHAR), CAST(library_uid AS VARCHAR), status, CAST(start_date AS VARCHAR), CAST(till_date AS VARCHAR) FROM reservation WHERE reservation_uid = '$reservation_uid'").resultList

        println("\n$entities\n")

        if (entities?.isNotEmpty() == true) {
            val objectArray: Array<Any?>? = entities[0] as Array<Any?>?

            println("\nPOINT 10\n")
            println("\n${objectArray?.size}\n")
            return ReservationByUsernameItem(
                reservation_uid = objectArray?.get(0).toString(),
                username = objectArray?.get(1).toString(),
                book_uid = objectArray?.get(2).toString(),
                library_uid = objectArray?.get(3).toString(),
                status = objectArray?.get(4).toString(),
                start_date = objectArray?.get(5).toString(),
                till_date = objectArray?.get(6).toString(),
            )
        }
        println("\nPOINT 11\n")
        return ReservationByUsernameItem(
            reservation_uid = reservation_uid,
            username = null,
            book_uid = null,
            library_uid = null,
            status = null,
            start_date = null,
            till_date = null,
        )
    }

    private fun entitiesToResponse(entities: List<ReservationEntities>): ReservationByUsernameItemResponse {
        val list = ReservationByUsernameItemResponse(arrayListOf())
        entities.forEach {
            list.reservations.add(
                ReservationByUsernameItem(
                it.reservation_uid.toString(),
                it.username,
                it.book_uid.toString(),
                it.library_uid.toString(),
                it.status,
                it.start_date.toString(),
                it.till_date.toString()
            )
            )
        }
        return list
    }
}