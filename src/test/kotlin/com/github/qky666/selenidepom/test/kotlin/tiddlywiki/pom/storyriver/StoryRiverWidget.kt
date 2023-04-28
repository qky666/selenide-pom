package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.storyriver

import com.codeborne.selenide.Condition
import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.common.Required
import com.github.qky666.selenidepom.pom.web.LangConditionedElement
import com.github.qky666.selenidepom.pom.web.Widget
import com.github.qky666.selenidepom.pom.web.WidgetsCollection

class StoryRiverWidget(self: SelenideElement) : Widget(self) {
    val tiddlerViews = WidgetsCollection(findAll("div.tc-tiddler-view-frame"), ::TiddlerViewWidget)
    val tiddlerEdits = WidgetsCollection(findAll("div.tc-tiddler-edit-frame"), ::TiddlerEditWidget)
}

open class TiddlerViewWidget(self: SelenideElement) : Widget(self) {
    @Required open val title = find("h2.tc-title")

    @Required val moreActions = find("button[class$=more-tiddler-actions]")

    @Required val edit = find("button[class$=edit]")

    @Required val close = find("button[class$=close]")

    @Required open val body = find("div.tc-tiddler-body")
}

class TiddlerEditWidget(self: SelenideElement) : Widget(self) {
    @Required val title = find("div.tc-tiddler-title")

    @Required val delete = find("button[class$=delete]")

    @Required val cancel = find("button[class$=cancel]")

    @Required val save = find("button[class$=save]")

    @Required val titleInput = find("input.tc-titlebar")

    @Required val bodyEditorIframe = find("iframe.tc-edit-texteditor-body")
    // shadowCss or shadowDeepCss not working here, so we have to make it the hard way
    // @Required val bodyInput = find(shadowCss("textarea", "iframe.tc-edit-texteditor-body"))
}

class GettingStartedTiddlerViewWidget(self: SelenideElement) : TiddlerViewWidget(self) {
    @Required override val title = LangConditionedElement(super.title, "GettingStarted")

    @Required val titleAgain = LangConditionedElement(super.title, Condition.exactText("GettingStarted"))

    @Required override val body = LangConditionedElement(
        super.body,
        mapOf(
            "en" to Condition.text("Welcome to TiddlyWiki and the TiddlyWiki community"),
            "es" to Condition.text("Bienvenido a TiddlyWiki y a su comunidad de usuarios")
        )
    )
}
