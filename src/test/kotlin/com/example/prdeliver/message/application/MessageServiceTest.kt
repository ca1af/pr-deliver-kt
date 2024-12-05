package com.example.prdeliver.message.application

import com.example.prdeliver.message.domain.Message
import com.example.prdeliver.message.domain.MessageRepository
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class MessageServiceTest(
    @Autowired private val messageRepository: MessageRepository,
    @Autowired private val messageService: MessageService,
) {
    private val messengerId = 1L
    private val githubPRResponse = GithubPRResponse("prTitle", "prLink", "prAuthor")
    private val assigneeLogins = listOf("assignee1", "assignee2")

    @Test
    @DisplayName("PR 할당자가 없을 때, 적절한 메시지를 반환한다.")
    fun testGetMessage_NoCollaborators() {
        // When
        val message = messageService.getMessage(messengerId, githubPRResponse, emptyList())

        // Then
        assertThat(message).isEqualTo("PR 할당자가 없어요. 깃허브 Collaborator 를 추가 한 후 다시 시도 해 주세요")
    }

    @Test
    @DisplayName("기존에 저장된 템플릿과 할당자가 있을 때 올바른 메시지를 생성한다.")
    fun testGetMessage_WithCollaboratorsAndExistingTemplate() {
        // Given
        val existingTemplate = Message("{author} {title} {assignee} {link}", messengerId)
        messageRepository.save(existingTemplate)

        // When
        val message = messageService.getMessage(messengerId, githubPRResponse, assigneeLogins)

        // Then
        assertThat(message).isEqualTo("prAuthor prTitle assignee1, assignee2 prLink")
    }

    @Test
    @DisplayName("기존 템플릿이 없을 때, 새로운 기본 템플릿을 저장하고 메시지를 생성한다.")
    fun testGetMessage_WithCollaboratorsAndNewTemplate() {
        // When
        val message = messageService.getMessage(messengerId, githubPRResponse, assigneeLogins)

        // Then
        assertThat(message.trimIndent()).isEqualTo(
            """
            안녕하세요 여러분! 
            prAuthor님이 새로운 PR을 제출했어요: prTitle. 
            리뷰는 assignee1, assignee2님께 할당되었습니다. 
            여기서 확인할 수 있어요: [PR 링크](prLink) 
            꼼꼼하게 리뷰하고 피드백 부탁드려요. 감사합니다!
            """.trimIndent()
        )

        val savedMessage = messageRepository.findByMessengerId(messengerId)
        assertThat(savedMessage).isNotNull
        assertThat(savedMessage!!.template).contains("{author}", "{title}", "{assignee}", "{link}")
    }

    @Test
    @DisplayName("사용자 정의 템플릿을 사용하여 올바른 메시지를 생성한다.")
    fun testGetMessage_WithCustomTemplate() {
        // Given
        val customTemplate = Message("{author} {title} {assignee} {link}", messengerId)
        messageRepository.save(customTemplate)

        // When
        val message = messageService.getMessage(messengerId, githubPRResponse, assigneeLogins)

        // Then
        assertThat(message).isEqualTo("prAuthor prTitle assignee1, assignee2 prLink")
    }

    @Test
    @DisplayName("메신저 ID로 메시지를 업데이트하면 템플릿이 변경된다.")
    fun testUpdateMessage_Success() {
        // Given
        val existingMessage = Message("{author} {title} {assignee} {link}", messengerId)
        messageRepository.save(existingMessage)
        val newTemplate = "새로운 템플릿: {author}, {title}, {assignee}, {link}"

        // When
        messageService.updateMessage(messengerId, newTemplate)

        // Then
        val updatedMessage = messageRepository.findByMessengerId(messengerId)
        assertThat(updatedMessage).isNotNull
        assertThat(updatedMessage!!.template).isEqualTo(newTemplate)
    }
}
