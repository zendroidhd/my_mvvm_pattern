package com.technologies.zenlight.earncredits.utils

import com.technologies.zenlight.earncredits.data.model.api.UserProfile
import io.reactivex.subjects.BehaviorSubject


val userProfileObservable = BehaviorSubject.create<UserProfile>()

fun pushUserProfileToObservers(userProfile: UserProfile) {
    userProfileObservable.onNext(userProfile)
}