package com.github.xepozz.moonshine.utils

import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder

object PhpCommandUtil {
    fun invokeCommand(
        project: Project,
        arguments: List<String>
    ) {
        val outputBuffer = StringBuilder()
        PhpCommandSettingsBuilder
            .create(project, false)
            .apply {
                setWorkingDir(project.basePath)
                addArguments(arguments)
            }

            .createGeneralCommandLine()
            .run { OSProcessHandler(this) }
            .apply {
                addProcessListener(object : ProcessListener {
                    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                        println("onTextAvailable: ${event.text}")

                        outputBuffer.append(event.text)
                    }

                    override fun processTerminated(event: ProcessEvent) {
                        println("processTerminated: ${event.exitCode}")

                        val notificationType = when (event.exitCode) {
                            0 -> NotificationType.INFORMATION
                            else -> NotificationType.ERROR
                        }

                        ApplicationManager.getApplication().invokeLater {
                            NotificationGroupManager.getInstance()
                                .getNotificationGroup("MoonShine Debug")
                                .createNotification("$outputBuffer", notificationType)
                                .notify(project);
                        }
                    }
                })
            }
            .startNotify()
    }
}