package oganesyan.rsoi_lab2.reservation.service

import oganesyan.rsoi_lab2.reservation.model.*
import org.springframework.transaction.annotation.Transactional

interface ReservationService {
    @Transactional(readOnly = true)
    fun getReservationsByUsername(username: String): ReservationByUsernameItemResponse

    fun putReservation(request: CreateReservationRequest): CreateReservationResponse

    fun removeReservation(request: RemoveReservationRequest): ReservationByUsernameItem
}