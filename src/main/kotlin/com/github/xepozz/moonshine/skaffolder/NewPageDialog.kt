package com.github.xepozz.moonshine.skaffolder

import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class NewPageDialog(
    callback: (State) -> Unit,
    override var state: State = State(),
) : AbstractNewDialog<NewPageDialog.State>(callback) {
    init {
        title = "New Page"
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            row {
                textField()
                    .label("Model class")
                    .focused()
                    .bindText(state::modelClass)
            }
            row {
                checkBox("CRUD")
                    .bindSelected(state::crud)
            }
            row {
                checkBox("Skip menu")
                    .bindSelected(state::skipMenu)
            }
        }
    }

    data class State(
        var modelClass: String = "",
        var crud: Boolean = true,
        var skipMenu: Boolean = false,
    )
}