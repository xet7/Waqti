@file:Suppress("NOTHING_TO_INLINE")

package uk.whitecrescent.waqti.frontend

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import uk.whitecrescent.waqti.R
import uk.whitecrescent.waqti.frontend.customview.recyclerviews.ScrollSnapMode
import uk.whitecrescent.waqti.frontend.fragments.view.ViewMode

/**
 * A safe and efficient way to access the [SharedPreferences] of Waqti by using type-aware
 * variables.
 *
 * All [SharedPreferences] used in Waqti will be found here, it is recommended against accessing
 * [SharedPreferences] directly, you should instead use one of the inline variables here to set
 * or get a specific preference.
 *
 * You should only have one instance of this class at any given point and that instance will be
 * found in [MainActivity] called [MainActivity.preferences].
 *
 * @author Bassam Helal
 */
class WaqtiPreferences(val mainActivity: MainActivity) {

    // The preferences keys, they're long for a reason, there's no use for them outside this class
    private val WAQTI_SHARED_PREFERENCES = "WaqtiSharedPreferences"
    val BOARD_LIST_NAME_PREFERENCES_KEY = "BoardListName"
    val BOARD_LIST_VIEW_MODE_PREFERENCES_KEY = "BoardListViewMode"
    val LIST_WIDTH_PREFERENCES_KEY = "ListWidth"
    val CARD_TEXT_SIZE_PREFERENCES_KEY = "CardTextSize"
    val LIST_HEADER_TEXT_SIZE_PREFERENCES_KEY = "ListHeaderTextSize"
    val BOARD_SCROLL_SNAP_MODE_PREFERENCES_KEY = "BoardScrollSnapMode"

    val sharedPreferences: SharedPreferences =
            mainActivity.getSharedPreferences(WAQTI_SHARED_PREFERENCES, Context.MODE_PRIVATE)


    // TODO: 29-Jun-19 Remove this and let us use the BoardList.name property
    inline var boardListName: String
        set(value) = sharedPreferences.edit {
            putString(BOARD_LIST_NAME_PREFERENCES_KEY, value)
        }
        get() = sharedPreferences.getString(
                BOARD_LIST_NAME_PREFERENCES_KEY,
                mainActivity.getString(R.string.allBoards))!!

    // TODO: 15-Jul-19 We could make this be a property inside of BoardList instead
    inline var boardListViewMode: ViewMode
        set(value) = sharedPreferences.edit {
            putString(BOARD_LIST_VIEW_MODE_PREFERENCES_KEY, value.name)
        }
        get() = ViewMode.valueOf(sharedPreferences.getString(
                BOARD_LIST_VIEW_MODE_PREFERENCES_KEY,
                ViewMode.GRID_VERTICAL.name)!!)

    inline var listWidth: Int
        set(value) = sharedPreferences.edit {
            putInt(LIST_WIDTH_PREFERENCES_KEY, value)
            settingsChanged()
        }
        get() = sharedPreferences.getInt(
                LIST_WIDTH_PREFERENCES_KEY, 66)

    inline var cardTextSize: Int
        set(value) = sharedPreferences.edit {
            putInt(CARD_TEXT_SIZE_PREFERENCES_KEY, value)
            settingsChanged()
        }
        get() = sharedPreferences.getInt(
                CARD_TEXT_SIZE_PREFERENCES_KEY, 18)

    inline var listHeaderTextSize: Int
        set(value) = sharedPreferences.edit {
            putInt(LIST_HEADER_TEXT_SIZE_PREFERENCES_KEY, value)
            settingsChanged()
        }
        get() = sharedPreferences.getInt(
                LIST_HEADER_TEXT_SIZE_PREFERENCES_KEY, 28)

    inline var boardScrollSnapMode: ScrollSnapMode
        set(value) = sharedPreferences.edit {
            putString(BOARD_SCROLL_SNAP_MODE_PREFERENCES_KEY, value.name)
            settingsChanged()
        }
        get() = ScrollSnapMode.valueOf(sharedPreferences.getString(
                BOARD_SCROLL_SNAP_MODE_PREFERENCES_KEY,
                ScrollSnapMode.PAGED.name)!!)

    inline fun settingsChanged() {
        mainActivity.viewModel.settingsChanged = true
    }
}