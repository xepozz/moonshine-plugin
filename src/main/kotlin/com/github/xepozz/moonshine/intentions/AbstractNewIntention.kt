package com.github.xepozz.moonshine.intentions

import com.intellij.codeInsight.intention.BaseElementAtCaretIntentionAction
import com.intellij.codeInsight.intention.preview.IntentionPreviewInfo
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.jetbrains.php.lang.lexer.PhpTokenTypes
import com.jetbrains.php.lang.psi.elements.PhpClass

abstract class AbstractNewIntention : BaseElementAtCaretIntentionAction() {
    override fun isAvailable(
        project: Project,
        editor: Editor,
        element: PsiElement
    ) = element.elementType == PhpTokenTypes.IDENTIFIER && element.parent is PhpClass

    override fun generatePreview(project: Project, editor: Editor, file: PsiFile) = IntentionPreviewInfo.EMPTY
}