package com.github.xepozz.moonshine.index

import com.github.xepozz.moonshine.MoonshineClasses
import com.github.xepozz.spiral.index.AbstractIndex
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.EnumeratorStringDescriptor
import com.jetbrains.php.lang.PhpFileType
import com.jetbrains.php.lang.psi.elements.ArrayCreationExpression
import com.jetbrains.php.lang.psi.elements.ClassConstantReference
import com.jetbrains.php.lang.psi.elements.ClassReference
import com.jetbrains.php.lang.psi.elements.PhpAttribute
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import com.jetbrains.php.lang.psi.elements.impl.ClassConstImpl

private typealias ResourcesIndexType = String

class ResourcesIndex : AbstractIndex<ResourcesIndexType>() {
    companion object {
        val key = ID.create<String, ResourcesIndexType>("Moonshine.Resources")
    }

    override fun getVersion() = 1

    override fun getName() = key

    override fun getValueExternalizer() = EnumeratorStringDescriptor.INSTANCE

    override fun getInputFilter() = FileBasedIndex.InputFilter { it.fileType == PhpFileType.INSTANCE }

    override fun getIndexer() = DataIndexer<String, ResourcesIndexType, FileContent> { inputData ->
        inputData
            .psiFile
            .let { PsiTreeUtil.findChildrenOfType(it, PhpClass::class.java) }
            .filter { it.fqn == MoonshineClasses.RESOURCE }
            .mapNotNull { it.fqn }
            .associateWith { it }
    }

}