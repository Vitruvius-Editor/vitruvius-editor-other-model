package vitruv.tools.vitruvadpter.testServer

import tools.vitruv.change.interaction.InteractionResultProvider
import tools.vitruv.change.interaction.UserInteractionOptions

class TestResultProvider: InteractionResultProvider {
    override fun getConfirmationInteractionResult(
        p0: UserInteractionOptions.WindowModality?,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: String?,
        p5: String?
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getNotificationInteractionResult(
        p0: UserInteractionOptions.WindowModality?,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: UserInteractionOptions.NotificationType?
    ) {
        TODO("Not yet implemented")
    }

    override fun getTextInputInteractionResult(
        p0: UserInteractionOptions.WindowModality?,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: String?,
        p5: UserInteractionOptions.InputValidator?
    ): String {
        TODO("Not yet implemented")
    }

    override fun getMultipleChoiceSingleSelectionInteractionResult(
        p0: UserInteractionOptions.WindowModality?,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: String?,
        p5: MutableIterable<String>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun getMultipleChoiceMultipleSelectionInteractionResult(
        p0: UserInteractionOptions.WindowModality?,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: String?,
        p5: MutableIterable<String>?
    ): MutableIterable<Int> {
        TODO("Not yet implemented")
    }
}