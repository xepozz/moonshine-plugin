package com.github.xepozz.moonshine.intentions

import com.intellij.codeInsight.intention.BaseElementAtCaretIntentionAction
import com.intellij.codeInsight.intention.preview.IntentionPreviewInfo
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder
import com.jetbrains.php.lang.lexer.PhpTokenTypes
import com.jetbrains.php.lang.psi.elements.PhpClass

class NewResourceIntention : BaseElementAtCaretIntentionAction() {
    override fun getText() = "Create Moonshine Resource"
    override fun getFamilyName() = "Create Moonshine Resource"

    override fun isAvailable(
        project: Project,
        editor: Editor,
        element: PsiElement
    ) = element.elementType == PhpTokenTypes.IDENTIFIER && element.parent is PhpClass

    override fun generatePreview(project: Project, editor: Editor, file: PsiFile) = IntentionPreviewInfo.EMPTY

    override fun invoke(
        project: Project,
        editor: Editor,
        element: PsiElement
    ) {
        val element = element.parent as? PhpClass ?: return

        val outputBuffer = StringBuilder()
        PhpCommandSettingsBuilder
            .create(project, false)
            .apply {
                setWorkingDir(project.basePath)
                addArguments(
                    listOf(
                        "artisan",
                        "moonshine:resource",
                        element.name,
                        "--model",
                        element.fqn,
                        "--type",
                        "2",
                        "-n",
                    )
                )
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

                        val notificationType = when(event.exitCode) {
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

//        PhpInterpretersManagerImpl.getInstance(project).interpreters.firstOrNull()?.let { interpreter ->
//            println("interpreter: ${interpreter.name}")
//
//
//        }
    }
}