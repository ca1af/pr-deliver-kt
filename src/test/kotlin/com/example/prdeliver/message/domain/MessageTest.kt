package com.example.prdeliver.message.domain

import com.example.prdeliver.global.exception.ExceptionMessage
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters")
class MessageTest {
    @Test
    fun `필수 placeHolder 가 없을 시 예외이다`() {
        val userInput = "TEST"
        val repoId = 1L

        Assertions.assertThatThrownBy { Message(userInput, repoId) }
            .isInstanceOf(MessageTemplateException.MissingPlaceholderTemplateException::class.java)
            .hasMessage(ExceptionMessage.MESSAGE_IS_NEEDED.message("AUTHOR_NAME")) // author name 부터 판별한다
    }

    @Test
    fun `ReviewMessage 객체 생성에 성공한다`() {
        val template = ("안녕하세요 여러분! {author}님이 새로운 PR을 제출했어요: {title}. 리뷰는 {assignee}님께 할당되었습니다."
                + "여기서 확인할 수 있어요: [PR 링크]({link}) 꼼꼼하게 리뷰하고 피드백 부탁드려요. 감사합니다!")
        val repoId = 1L

        val message = Message(template, repoId)
        Assertions.assertThat(message).isNotNull()
    }

    @Test
    fun `필수 메시지를 조합해서 하나의 메시지로 리턴 할 수 있다`() {
        val prTitle = "pr 제목"
        val prLink = "pr 링크"
        val prAuthor = "pr 작성자"
        val reviewAssignee = "리뷰 할당자"
        val template = ("안녕하세요 여러분! {author}님이 새로운 PR을 제출했어요: {title}. 리뷰는 {assignee}님께 할당되었습니다."
                + "여기서 확인할 수 있어요: [PR 링크]({link}) 꼼꼼하게 리뷰하고 피드백 부탁드려요. 감사합니다!")
        val message = Message(template, 1L)

        // when
        val mergedMessage = message.mergeMessage(prTitle, prLink, prAuthor, listOf(reviewAssignee))

        // then
        val expected = "안녕하세요 여러분! pr 작성자님이 새로운 PR을 제출했어요: pr 제목. 리뷰는 리뷰 할당자님께 할당되었습니다." +
                "여기서 확인할 수 있어요: [PR 링크](pr 링크) 꼼꼼하게 리뷰하고 피드백 부탁드려요. 감사합니다!"
        SoftAssertions.assertSoftly { softly: SoftAssertions ->
            softly.assertThat(mergedMessage).contains(prTitle)
            softly.assertThat(mergedMessage).contains(prLink)
            softly.assertThat(mergedMessage).contains(prAuthor)
            softly.assertThat(mergedMessage).contains(reviewAssignee)
            softly.assertThat(mergedMessage).isEqualTo(expected)
        }
    }
}
