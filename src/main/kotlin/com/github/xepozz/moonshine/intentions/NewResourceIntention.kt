package com.github.xepozz.moonshine.intentions

import com.intellij.codeInsight.intention.BaseElementAtCaretIntentionAction
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.codeInsight.intention.preview.IntentionPreviewInfo
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.codeInspection.util.IntentionName
import com.intellij.execution.CommandLineUtil
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.modcommand.ModCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.newvfs.VfsImplUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.jetbrains.php.config.commandLine.PhpCommandLineCommand
import com.jetbrains.php.config.commandLine.PhpCommandLinePathProcessor
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.lang.lexer.PhpTokenTypes
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.macro.PhpExecutableMacro
import com.jetbrains.php.phpunit.PhpUnitExecutionUtil
import org.jetbrains.php.performanceTesting.PhpCommandProvider
import org.jetbrains.php.performanceTesting.RunPhpRunConfigurationCommand

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
                    }

                    override fun processTerminated(event: ProcessEvent) {
                        if (event.exitCode == 0) {
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