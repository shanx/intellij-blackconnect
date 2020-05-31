package me.lensvol.blackconnect

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile


class FormatUsingBlackdAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            FileEditorManagerEx.getInstance(project).selectedTextEditor?.let { editor ->
                val configuration = BlackConnectSettingsConfiguration.getInstance(project)
                BlackdReformatter(project, configuration).process(editor.document)
            }
        }
    }


    override fun update(event: AnActionEvent) {
        val project: Project? = event.project
        event.presentation.isEnabled = false

        if (project == null)
            return

        val configuration = BlackConnectSettingsConfiguration.getInstance(project)
        val vFile: VirtualFile? = event.getData(PlatformDataKeys.VIRTUAL_FILE)
        vFile?.let {
            event.presentation.isEnabled = BlackdReformatter(project, configuration).isFileSupported(vFile)
        }
    }
}