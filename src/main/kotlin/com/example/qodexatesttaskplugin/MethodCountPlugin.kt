package com.example.qodexatesttaskplugin

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.openapi.ui.Messages
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction

class MethodCountPlugin : IntentionAction {
    override fun startInWriteAction(): Boolean {
        return false
    }

    override fun getText() = "Show number of methods"

    override fun getFamilyName() = "MethodCount"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        if (file is KtFile) {
            val element = file.findElementAt(editor?.caretModel?.offset ?: -1)
            return element != null  && element.parent is KtClass
        }
        return false
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (file is KtFile && editor != null) {
            val element = file.findElementAt(editor.caretModel.offset)
            if (element != null && element.parent is KtClass) {
                val ktClass = element.parent as KtClass
                val methodCount = ktClass.declarations.count { it is KtNamedFunction }
                showMethodCountDialog(ktClass.name!!, methodCount)
            }
        }
    }

    private fun showMethodCountDialog(className: String, methodCount: Int) {
        Messages.showMessageDialog(
            "Class: $className\nNumber of Methods: $methodCount",
            "Method Count",
            Messages.getInformationIcon()
        )
    }
}