package com.example.myapplication.truth.subject

import com.example.myapplication.ExampleUnitTest
import com.google.common.truth.FailureMetadata
import com.google.common.truth.Subject

class BallTeamSubject(failureMetadata: FailureMetadata, private val subject: ExampleUnitTest.BallTeam) : Subject(failureMetadata, subject) {

    fun isNameEqualsTo(name: String) {
        check("getName()").that(subject.name).isEqualTo(name)
    }

    fun isStadiumEqualsTo(stadium: String) {
        check("getStadium()").that(subject.stadium).isEqualTo(stadium)
    }
}