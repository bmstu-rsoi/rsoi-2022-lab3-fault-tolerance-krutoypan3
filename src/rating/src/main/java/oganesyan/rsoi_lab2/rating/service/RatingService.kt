package oganesyan.rsoi_lab2.rating.service

import oganesyan.rsoi_lab2.rating.model.RatingResponse
import oganesyan.rsoi_lab2.rating.model.SetRatingRequest
import org.springframework.transaction.annotation.Transactional

interface RatingService {
    @Transactional(readOnly = true)
    fun getRatingByUsername(username: String): RatingResponse

    fun setRatingByUsername(setRatingRequest: SetRatingRequest)
}