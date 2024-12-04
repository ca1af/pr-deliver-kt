package com.example.prdeliver.global.exception

enum class ExceptionMessage(val message: String) {
    MESSAGE_IS_NEEDED("필수 메시지 입력값이 누락되었습니다. 누락된 값 : "),
    ;

    fun message(detail: String): String {
        return this.message + detail
    }
}
