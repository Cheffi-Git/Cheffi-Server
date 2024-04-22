package com.cheffi.notification.service

import com.cheffi.notification.domain.RequestFailure
import com.cheffi.notification.dto.MessageResponse
import com.cheffi.notification.repository.RequestFailureRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class RequestFailureService(
    private val requestFailureRepository: RequestFailureRepository
) {

    @Transactional
    fun logAll(messageResponses: List<MessageResponse>) {
        requestFailureRepository.saveAll(messageResponses.map(RequestFailure::from))
    }

}
