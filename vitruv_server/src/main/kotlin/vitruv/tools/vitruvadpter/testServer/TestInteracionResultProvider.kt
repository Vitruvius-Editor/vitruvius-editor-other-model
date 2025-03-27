package vitruv.tools.vitruvadpter.testServer

import tools.vitruv.change.interaction.InteractionResultProvider
import tools.vitruv.change.interaction.UserInteractionOptions

class TestInteractionResultProvider : InteractionResultProvider {
    override fun getConfirmationInteractionResult(
        p0: UserInteractionOptions.WindowModality?,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: String?,
        p5: String?,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getNotificationInteractionResult(
        p0: UserInteractionOptions.WindowModality?,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: UserInteractionOptions.NotificationType?,
    ) {
        TODO("Not yet implemented")
    }

    override fun getTextInputInteractionResult(
        p0: UserInteractionOptions.WindowModality?,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: String?,
        p5: UserInteractionOptions.InputValidator?,
    ): String? {
        print("$p1 $p2 $p3 $p4 $p5")
        print("\n")
        val myInput = readln()
        return myInput
    }

    override fun getMultipleChoiceSingleSelectionInteractionResult(
        p0: UserInteractionOptions.WindowModality?,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: String?,
        p5: Iterable<String?>?,
    ): Int {
        //print p1, p2, p3, p4, p5 each on a new line
        print("$p1 /n $p2 /n $p3 /n $p4 /n $p5")
        print("\n")
        //read am integer from the console
        val myInput = 1;
        //return the input
        return myInput
    }

    override fun getMultipleChoiceMultipleSelectionInteractionResult(
        p0: UserInteractionOptions.WindowModality?,
        p1: String?,
        p2: String?,
        p3: String?,
        p4: String?,
        p5: Iterable<String?>?,
    ): Iterable<Int?>? {
        TODO("Not yet implemented")
    }
}
