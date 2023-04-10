package com.github.qky666.selenidepom.test.kotlin.tiddlywiki.pom.storyriver

import com.codeborne.selenide.SelenideElement
import com.github.qky666.selenidepom.pom.Required
import com.github.qky666.selenidepom.pom.Widget
import com.github.qky666.selenidepom.pom.WidgetsCollection

class StoryRiverWidget(self: SelenideElement) : Widget(self) {
    val tiddlerViews = WidgetsCollection(findAll("div.tc-tiddler-view-frame"), ::TiddlerViewWidget)
    val tiddlerEdits = WidgetsCollection(findAll("div.tc-tiddler-edit-frame"), ::TiddlerEditWidget)
}

class TiddlerViewWidget(self: SelenideElement) : Widget(self) {
    @Required val title = find("h2.tc-title")

    @Required val moreActions = find("button[class$=more-tiddler-actions]")
    @Required val edit = find("button[class$=edit]")
    @Required val close = find("button[class$=close]")

    @Required val body = find ("div.tc-tiddler-body")
}

class TiddlerEditWidget(self: SelenideElement) : Widget(self) {
    @Required val title = find("div.tc-tiddler-title")

    @Required val delete = find("button[class$=delete]")
    @Required val cancel = find("button[class$=cancel]")
    @Required val save = find("button[class$=save]")

    @Required val titleInput = find("input.tc-titlebar")

    @Required val canvas = find("canvas")
}
