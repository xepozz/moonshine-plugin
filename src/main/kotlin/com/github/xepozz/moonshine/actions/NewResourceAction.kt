package com.github.xepozz.moonshine.actions

import com.github.xepozz.moonshine.dialogs.NewResourceDialog
import com.intellij.openapi.actionSystem.AnActionEvent

class NewResourceAction : AbstractNewAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        println("project: $project")

        NewResourceDialog({
            println(it)

            NewActionsUtil.invokeCommand(
                project,
                listOf(
                    "artisan",
                    "moonshine:resource",
                    it.modelClass,
//                    "element.name",
//                    "--model",
//                element.fqn,
//                    "element.fqn",
                    "--type",
                    "${it.type}",
                    "-n",
                ),
            )
        }).show()

    }
}