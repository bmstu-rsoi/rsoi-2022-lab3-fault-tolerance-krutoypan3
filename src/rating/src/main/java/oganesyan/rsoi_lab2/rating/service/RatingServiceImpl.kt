package oganesyan.rsoi_lab2.rating.service

import oganesyan.rsoi_lab2.rating.database.RatingEntities
import oganesyan.rsoi_lab2.rating.model.RatingResponse
import oganesyan.rsoi_lab2.rating.model.SetRatingRequest
import oganesyan.rsoi_lab2.rating.repository.RatingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
open class RatingServiceImpl @Autowired constructor(private val ratingRepository: RatingRepository) : RatingService {
    override fun getRatingByUsername(username: String): RatingResponse {
        return entitiesToResponse(ratingRepository.getFirstByUsername(username))
    }

    private fun entitiesToResponse(entities: RatingEntities?): RatingResponse {
        return RatingResponse(entities?.username, entities?.stars ?: 50)
    }

    override fun setRatingByUsername(setRatingRequest: SetRatingRequest) {
        val entities = getRatingByUsername(setRatingRequest.username)
        entities.username?.let {
            ratingRepository.deleteByUsername(it)
        }
        val saveEntity = RatingEntities()
        saveEntity.username = setRatingRequest.username
        saveEntity.stars = (entities.stars?: 50) + setRatingRequest.stars

        if (saveEntity.stars!! > 100)
            saveEntity.stars = 100
        else if (saveEntity.stars!! < 1)
            saveEntity.stars = 1

        ratingRepository.save(saveEntity)
    }
}