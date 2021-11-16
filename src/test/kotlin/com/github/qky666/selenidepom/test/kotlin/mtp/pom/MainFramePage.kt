package com.github.qky666.selenidepom.test.kotlin.mtp.pom

import com.github.qky666.selenidepom.kotlin.Required
import com.github.qky666.selenidepom.kotlin.RequiredContainer

open class MainFramePage : RequiredContainer {
    @Required val mainMenu = MainMenuWidget()
    val cookiesBanner = CookiesBannerWidget()
}
