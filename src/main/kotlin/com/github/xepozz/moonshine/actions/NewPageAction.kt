package com.github.xepozz.moonshine.actions

import com.github.xepozz.moonshine.dialogs.NewPageDialog
import com.intellij.openapi.actionSystem.AnActionEvent

class NewPageAction : AbstractNewAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        NewPageDialog({ state ->
            NewActionsUtil.invokeCommand(
                project,
                listOfNotNull(
                    "artisan",
                    "moonshine:page",
                    state.modelClass,
                    "--force",
                    "--crud".takeIf { state.crud },
                    "--skip-menu".takeIf { state.skipMenu },
                    "-n",
                ),
            )
        }).show()

    }
}