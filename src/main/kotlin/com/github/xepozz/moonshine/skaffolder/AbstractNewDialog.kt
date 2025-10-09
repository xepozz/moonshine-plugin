package com.github.xepozz.moonshine.skaffolder

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

abstract class AbstractNewDialog<T>(
    val callback: (T) -> Unit,
) : DialogWrapper(true) {
    abstract var state: T

    protected override fun createCenterPanel(): JComponent {
        return panel {
            row {
                label("Testing")
                    .gap(RightGap.COLUMNS)
                    .align(Align.FILL)
            }
            row {
                textField()
            }
        }
    }

    override fun doOKAction() {
        super.doOKAction()
        callback(state)
    }
}