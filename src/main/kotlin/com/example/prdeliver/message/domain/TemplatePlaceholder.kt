package com.example.prdeliver.message.domain

enum class TemplatePlaceholder(val placeholder: String) {
    AUTHOR_NAME("{author}"),
    PR_TITLE("{title}"),
    ASSIGNEE_NAME("{assignee}"),
    PR_LINK("{link}")
}
