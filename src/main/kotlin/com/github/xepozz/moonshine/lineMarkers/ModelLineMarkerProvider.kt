package com.github.xepozz.moonshine.lineMarkers

import com.github.xepozz.moonshine.MoonshineClasses
import com.github.xepozz.moonshine.MoonshineIcons
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.util.NotNullLazyValue
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.PhpClassHierarchyUtils
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.ClassConstantReference
import com.jetbrains.php.lang.psi.elements.ClassReference
import com.jetbrains.php.lang.psi.elements.PhpClass

class ModelLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun getLineMarkerInfo(element: PsiElement): RelatedItemLineMarkerInfo<*>? {
        val element = element as? PhpClass ?: return null
        val nameIdentifier = element.nameIdentifier ?: return null

        val project = element.project
        val phpIndex = PhpIndex.getInstance(project)

        val modelClass = phpIndex.getClassesByFQN(MoonshineClasses.MODEL).firstOrNull() ?: return null

        if (!PhpClassHierarchyUtils.isSuperClass(modelClass, element, true)) return null

        val resources = findResourceClasses(phpIndex, element)

        println("resources: $resources")

        // todo: replace with more suitable icon
        return NavigationGutterIconBuilder.create(MoonshineIcons.MOONSHINE)
            .setTargets(NotNullLazyValue.createValue {
                resources
                    .plus(
                        resources.flatMap { findPagesInResource(it) }
                    )
            })
            .setTooltipText("Navigate to pages")
            .createLineMarkerInfo(nameIdentifier)

    }

    private fun findResourceClasses(
        phpIndex: PhpIndex,
        element: PhpClass
    ): MutableList<PhpClass> {
        val resources = mutableListOf<PhpClass>()

        phpIndex.processAllSubclasses(MoonshineClasses.MODEL_RESOURCE, { phpClass ->
            phpClass
                .findFieldByName("model", false)
                ?.run { defaultValue as? ClassConstantReference }
                ?.run { classReference as? ClassReference }
                ?.takeIf { it.fqn == element.fqn }
                ?.apply { resources.add(phpClass) }

            true
        })

        return resources
    }

    private fun findPagesInResource(
        element: PhpClass
    ): Collection<PhpClass> {
        return element.findMethodByName("pages")
            ?.takeIf { it.containingClass?.fqn != MoonshineClasses.CRUD_RESOURCE }
            ?.run { PsiTreeUtil.findChildrenOfType(this, ClassReference::class.java) }
            ?.mapNotNull { it.resolve() as? PhpClass }
            ?: emptyList()
    }
}