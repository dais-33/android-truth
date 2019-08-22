package com.example.myapplication.truth

import com.example.myapplication.ExampleUnitTest
import com.example.myapplication.truth.subject.BallTeamSubject
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Truth

object TruthUtil {

    /**
     * BallTeamのsubjectを生成する
     */
    fun assertThat(ballTeam: ExampleUnitTest.BallTeam): BallTeamSubject = Truth.assertAbout { metadata: FailureMetadata, actual: ExampleUnitTest.BallTeam ->
        BallTeamSubject(metadata, actual)
    }.that(ballTeam)
}