package edu.illinois.cs.cs124.ay2023.mp.tests.helpers

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Fork of the RecyclerViewMatcher from https://github.com/dannyroa/espresso-samples.
 *
 * <p>Used to test the summary list UI component.
 */

class RecyclerViewMatcher(private val recyclerViewId: Int) {
    fun atPosition(position: Int, targetViewId: Int = -1): Matcher<View> = object : TypeSafeMatcher<View>() {
        var resources: Resources? = null
        var childView: View? = null

        override fun describeTo(description: Description) {
            val idDescription = try {
                resources?.getResourceName(recyclerViewId)
            } catch (_: Resources.NotFoundException) {
                "$recyclerViewId (resource name not found)"
            } ?: recyclerViewId.toString()
            description.appendText("RecyclerView with id: $idDescription at position: $position")
        }

        override fun matchesSafely(view: View): Boolean {
            resources = view.resources
            if (childView == null) {
                childView =
                    (view.rootView.findViewById<View>(recyclerViewId) as? RecyclerView)
                        ?.findViewHolderForAdapterPosition(position)
                        ?.itemView
                        ?: return false
            }
            return when (targetViewId) {
                -1 -> childView === view
                else -> childView!!.findViewById<View>(targetViewId) === view
            }
        }
    }
}

fun withRecyclerView(recyclerViewId: Int) = RecyclerViewMatcher(recyclerViewId)

// md5: 63969ee45961470a139ff2e5cf93db40 // DO NOT REMOVE THIS LINE
