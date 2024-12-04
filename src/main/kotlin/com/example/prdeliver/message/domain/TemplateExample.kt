package com.example.prdeliver.message.domain

enum class TemplateExample(val message: String) {
    PR_TEMPLATE(
        """
            안녕하세요 여러분! 
            {author}님이 새로운 PR을 제출했어요: {title}. 
            리뷰는 {assignee}님께 할당되었습니다. 
            여기서 확인할 수 있어요: [PR 링크]({link}) 
            꼼꼼하게 리뷰하고 피드백 부탁드려요. 감사합니다!
            
            """.trimIndent()
    )
}
