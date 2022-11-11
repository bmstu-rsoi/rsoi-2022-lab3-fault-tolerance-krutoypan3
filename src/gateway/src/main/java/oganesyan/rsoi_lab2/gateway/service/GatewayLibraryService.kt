package oganesyan.rsoi_lab2.gateway.service

import oganesyan.rsoi_lab2.gateway.model.*
import org.springframework.transaction.annotation.Transactional
import java.util.ArrayList

interface GatewayLibraryService {
    fun getLibraryByCity(libraryRequest: GatewayLibraryRequest): GatewayLibraryResponse

    fun getBooksByLibrary(gatewayBooksByLibraryRequest: GatewayBooksByLibraryRequest): GatewayBookResponse

    fun getRating(username: String): GatewayRatingResponse

    fun setReservation(username: String, gatewayReservationRequest: GatewayReservationRequest): GatewayReservationResponse

    fun getReservation(username: String): ArrayList<GatewayReservationResponse>

    fun returnReservation(username: String, gatewayReservationReturnRequest: GatewayReservationReturnRequest, reservationUid: String): GatewayBaseResponse
}