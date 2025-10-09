package com.github.xepozz.moonshine.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

open class AbstractNewAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        println("Hello World!")
    }
}