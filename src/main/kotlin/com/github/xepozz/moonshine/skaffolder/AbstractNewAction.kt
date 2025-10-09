package com.github.xepozz.moonshine.skaffolder

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

open class AbstractNewAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        NotificationGroupManager.getInstance()
            .getNotificationGroup("MoonShine Debug")
            .createNotification("Not implemented yet", NotificationType.INFORMATION)
            .notify(project);
    }
}