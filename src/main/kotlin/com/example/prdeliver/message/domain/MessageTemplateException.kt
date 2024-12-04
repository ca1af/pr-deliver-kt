package com.example.prdeliver.message.domain

import com.example.prdeliver.global.exception.BadRequestException

class MessageTemplateException(message: String?) : RuntimeException(message) {
    class MissingPlaceholderTemplateException(message: String) : BadRequestException(DEFAULT_MESSAGE + message) {
        companion object {
            private const val DEFAULT_MESSAGE = "필수 메시지 입력값이 누락되었습니다. 누락된 값 : "
        }
    }
}
