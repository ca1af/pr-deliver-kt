package com.example.prdeliver.message.application

import com.example.prdeliver.global.exception.BadRequestException
import com.example.prdeliver.global.exception.ExceptionMessage
import com.example.prdeliver.message.domain.Message
import com.example.prdeliver.message.domain.MessageRepository
import org.springframework.stereotype.Service

@Service
class MessageService(private val messageRepository: MessageRepository) {
    companion object {
        const val NO_COLLABORATOR: String = "PR 할당자가 없어요. 깃허브 Collaborator 를 추가 한 후 다시 시도 해 주세요"
    }

    fun updateMessage(messengerId: Long?, template: String?) {
        val message: Message = messageRepository.findByMessengerId(messengerId) ?: throw BadRequestException(
            ExceptionMessage.MESSENGER_NOT_FOUND.message
        )

        // 응답이 필요할수도?
        message.updateMessage(template)
    }

    fun deleteAllMessagesByMessengerId(messengerId: Long?) {
        messageRepository.deleteAllByMessengerId(messengerId)
    }

    fun save(messengerId: Long): Message {
        return messageRepository.save(Message(messengerId))
    }

    fun getMessage(messengerId: Long, githubPRResponse: GithubPRResponse, assigneeLogins: List<String>): String {
        if (assigneeLogins.isEmpty()) {
            return NO_COLLABORATOR
        }

        val message = messageRepository.findByMessengerId(messengerId) ?: save(messengerId)
        return message.mergeMessage(
            githubPRResponse.prTitle, githubPRResponse.prLink, githubPRResponse.prAuthor, assigneeLogins
        )
    }
}
