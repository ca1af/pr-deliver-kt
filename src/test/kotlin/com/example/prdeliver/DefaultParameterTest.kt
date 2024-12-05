package com.example.prdeliver

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

@Suppress("NonAsciiCharacters")
class DefaultParameterTest {
    data class TestData(val str: String? = "디폴트")

    @Test
    fun `디폴트 파라미터로 Null 을 넣어주면 값은 null 이 된다`() {
        val testData = TestData(null)
        Assertions.assertThat(testData.str).isNull()
    }

    @Test
    fun `디폴트 파라미터로 아무것도 넣지 않으면 디폴트값이 할당된다`() {
        val testData = TestData()
        Assertions.assertThat(testData.str).isEqualTo("디폴트")
    }

    @Test
    fun `디폴트 파라미터의 이름을 명시하고 null 을 넣어주면 값은 null 이다`() {
        val testData = TestData(str = null)
        Assertions.assertThat(testData.str).isNull()
    }
}
