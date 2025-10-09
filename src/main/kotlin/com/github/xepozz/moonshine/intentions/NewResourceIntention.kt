package com.github.xepozz.moonshine.intentions

import com.github.xepozz.moonshine.actions.NewActionsUtil
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.elements.PhpClass

class NewResourceIntention : AbstractNewIntention() {
    override fun getText() = "Create Moonshine Resource"
    override fun getFamilyName() = "Create Moonshine Resource"

    override fun invoke(
        project: Project,
        editor: Editor,
        element: PsiElement
    ) {
        val element = element.parent as? PhpClass ?: return

        NewActionsUtil.invokeCommand(
            project,
            listOf(
                "artisan",
                "moonshine:resource",
                element.name,
                "--model",
                element.fqn,
                "--type",
                "2",
                "-n",
            ),
        )
    }
}