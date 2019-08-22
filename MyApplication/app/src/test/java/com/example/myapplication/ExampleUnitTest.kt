package com.example.myapplication

import com.google.common.truth.Correspondence
import com.google.common.truth.Truth
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.tuple
import org.assertj.core.api.Assertions.within
import org.assertj.core.api.SoftAssertions
import org.assertj.core.groups.Tuple

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@Suppress("NonAsciiCharacters", "TestFunctionName")
class ExampleUnitTest {

    @Test
    fun 文字列のアサーション_AssertJ() {
        Assertions.assertThat("TOKYO")
            .`as`("TEXT CHECK TOKYO")
            .isEqualTo("TOKYO")
            .isEqualToIgnoringCase("tokyo")
            .isNotEqualTo("KYOTO")
            .isNotBlank()
            .startsWith("TO")
            .endsWith("YO")
            .contains("OKY")
            .matches("[A-Z]{5}")
            .isInstanceOf(String::class.java)
    }

    @Test
    fun 文字列のアサーション_truth() {
        Truth.assertWithMessage("TEXT CHECK TOKYO").that("TOKYO").apply {
            isEqualTo("TOKYO")
//            isEqualToIgnoringCase("tokyo")
            isNotEqualTo("KYOTO")
            isNotEmpty()
            startsWith("TO")
            endsWith("YO")
            contains("OKY")
            matches("[A-Z]{5}")
            isInstanceOf(String::class.java)
        }
    }

    @Test
    fun 間違えてもテストが続く_assertJ() {
        SoftAssertions().apply {
            assertThat("TOKYO")
                .`as`("TEXT CHECK TOKYO")
                .isEqualTo("HOKKAIDO")
                .isEqualToIgnoringCase("tokyo")
                .isNotEqualTo("KYOTO")
                .isNotBlank()
                .startsWith("TO")
                .endsWith("YO")
                .contains("OKY")
                .matches("[A-Z]{7}")
                .isInstanceOf(String::class.java)
        }.assertAll()
    }

    @Test
    fun 間違えてもテストが続く_truth() {
        Truth.assertWithMessage("truthはそもそもメソッドチェーンではないので無理").that(false).apply {
            isTrue()
        }
    }

    @Test
    fun 数値のアサーション_assertJ() {
        Assertions.assertThat(3.14159)
            .isNotZero()
            .isNotNegative()
            .isGreaterThan(3.0)
            .isLessThanOrEqualTo(4.0)
            .isBetween(3.0, 3.2)
            .isCloseTo(Math.PI, within(0.001))
    }

    @Test
    fun 数値のアサーション_truth() {
        Truth.assertThat(3.14159).apply {
            isNonZero()
            isAtLeast(0.0)
            isGreaterThan(3.0)
            isAtMost(4.0)
//            isBetween(3.0, 3.2)
//            isCloseTo(Math.PI, within(0.001))
        }
    }

    @Test
    fun コレクションのアサーション_assertJ() {
        val target = listOf("Giants", "Dodgers", "Athletics")

        Assertions.assertThat(target)
            .hasSize(3)
            .contains("Dodgers")
            .containsOnly("Athletics", "Dodgers", "Giants")
            .containsExactly("Giants", "Dodgers", "Athletics")
            .doesNotContain("Padres")
    }

    @Test
    fun コレクションのアサーション_truth() {
        val target = listOf("Giants", "Dodgers", "Athletics")

        Truth.assertThat(target).apply {
            hasSize(3)
            contains("Dodgers")
            containsAtLeast("Athletics", "Dodgers", "Giants")
            containsExactly("Giants", "Dodgers", "Athletics").inOrder()
            doesNotContain("Padres")
        }
    }

    @Test
    fun フィルタリング_assertJ() {
        val target = listOf(
            BallTeam("Giants", "San Francisco", "AT&T Park"),
            BallTeam("Dodgers", "Los Angels", "Dodger Stadium"),
            BallTeam("Angels", "Los Angels", "Angel Stadium"),
            BallTeam("Athletics", "Oakland", "Oakland Coliseum"),
            BallTeam("Padres", "San Diego", "Petco Park")
        )

        Assertions.assertThat(target)
            .filteredOn { team -> team.city.startsWith("San") }
            .filteredOn { team -> team.city.endsWith("Francisco") }
            .extracting("name", String::class.java)
            .containsExactly("Giants")
        Assertions.assertThat(target)
            .filteredOn { team -> team.city == "Los Angels" }
            .extracting("name", "stadium")
            .containsExactly(
                tuple("Dodgers", "Dodger Stadium"),
                tuple("Angels", "Angel Stadium")
            )
    }

    @Test
    fun フィルタリング_truth() {
        val target = listOf(
            BallTeam("Giants", "San Francisco", "AT&T Park"),
            BallTeam("Dodgers", "Los Angels", "Dodger Stadium"),
            BallTeam("Angels", "Los Angels", "Angel Stadium"),
            BallTeam("Athletics", "Oakland", "Oakland Coliseum"),
            BallTeam("Padres", "San Diego", "Petco Park")
        )

        // Truthにfilter機能がない？ので自分でfilterする
        // 一部の要素のみをみて比較する場合、Correspondenceを定義して行う
        // 比較する要素ごとにCorrespondenceの定義が必要になる
        val filterTarget = target.filter { team -> team.city.startsWith("San") }
            .filter { team -> team.city.endsWith("Francisco") }
        Truth.assertThat(filterTarget)
            .comparingElementsUsing(HAS_NAME)
            .containsExactly("Giants")
        val filterTarget2 = target.filter { team -> team.city == "Los Angels" }
        Truth.assertThat(filterTarget2)
            .comparingElementsUsing(BALLTEAM_NAME_PARSES_TO_TUPLE)
            .containsExactly(
                tuple("Dodgers", "Dodger Stadium"),
                tuple("Angels", "Angel Stadium")
            )
    }

    @Test
    fun 例外の検証_assertJ() {
        Assertions.assertThatExceptionOfType(RuntimeException::class.java)
            .isThrownBy { functionMayThrow() }
            .withMessage("Aborted!")
            .withNoCause()
    }

    @Test
    fun 例外の検証_truth() {
        try {
            functionMayThrow()
        } catch (e: Throwable) {
            Truth.assertThat(e).apply {
                isInstanceOf(RuntimeException::class.java)
                hasMessageThat().contains("Aborted!")
//                withNoCause()
            }
        }
    }

    data class BallTeam(val name: String, val city: String, val stadium: String)

    private val HAS_NAME = Correspondence.transforming<BallTeam, String>({ it?.name }, "Has an Name of")

    private val BALLTEAM_NAME_PARSES_TO_TUPLE = Correspondence.from(::ballteamParsesToTuple, "parses to")

    // StringとBallteamのnameとstadiumの比較
    private fun ballteamParsesToTuple(actual: BallTeam?, expected: Tuple?): Boolean {
        if (actual == null) {
            return expected == null
        }

        return expected?.toList()?.let { list ->
            if (list.isEmpty() || list.size != 2) return false
            actual.name == list[0] && actual.stadium == list[1]
        } ?: false
    }

    @Throws(RuntimeException::class)
    private fun functionMayThrow() {
        throw RuntimeException("Aborted!")
    }
}
