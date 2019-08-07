package uk.whitecrescent.waqti.frontend.customview.recyclerviews

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.board_card.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.colorAttr
import org.jetbrains.anko.textColor
import uk.whitecrescent.waqti.R
import uk.whitecrescent.waqti.backend.persistence.Caches
import uk.whitecrescent.waqti.backend.task.ID
import uk.whitecrescent.waqti.frontend.SimpleItemTouchHelperCallback
import uk.whitecrescent.waqti.frontend.appearance.BackgroundType
import uk.whitecrescent.waqti.frontend.appearance.toColor
import uk.whitecrescent.waqti.frontend.fragments.view.ViewBoardFragment
import uk.whitecrescent.waqti.frontend.fragments.view.ViewMode
import uk.whitecrescent.waqti.hideKeyboard
import uk.whitecrescent.waqti.invoke
import uk.whitecrescent.waqti.mainActivity
import uk.whitecrescent.waqti.mainActivityViewModel

class BoardListView
@JvmOverloads
constructor(context: Context,
            attributeSet: AttributeSet? = null,
            defStyle: Int = 0
) : WaqtiRecyclerView(context, attributeSet, defStyle) {

    val boardListAdapter: BoardListAdapter?
        get() = this.adapter as? BoardListAdapter

    init {
        scrollBarColor = mainActivity.colorAttr(R.attr.colorOnSurface).toColor
        layoutManager = GridLayoutManager(context, 1, VERTICAL, false)
    }

    fun changeViewMode(viewMode: ViewMode) {
        boardListAdapter?.viewMode = viewMode
        when (viewMode) {
            ViewMode.LIST_VERTICAL -> when (mainActivity.resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> (layoutManager as? GridLayoutManager)?.spanCount = 1
                Configuration.ORIENTATION_LANDSCAPE -> (layoutManager as? GridLayoutManager)?.spanCount = 2
            }
            ViewMode.GRID_VERTICAL -> when (mainActivity.resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> (layoutManager as? GridLayoutManager)?.spanCount = 2
                Configuration.ORIENTATION_LANDSCAPE -> (layoutManager as? GridLayoutManager)?.spanCount = 3
            }
        }
        adapter?.notifyDataSetChanged()
    }
}

class BoardListAdapter(boardListID: ID) : RecyclerView.Adapter<BoardListViewHolder>() {

    val boardList = Caches.boardLists[boardListID]
    var viewMode: ViewMode = ViewMode.LIST_VERTICAL
    lateinit var boardListView: BoardListView

    init {
        this.setHasStableIds(true)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        require(recyclerView is BoardListView) {
            "Recycler View attached to a BoardListAdapter must be a BoardListView," +
                    " passed in ${recyclerView::class}"
        }
        boardListView = recyclerView
        boardListView.changeViewMode(viewMode)

        ItemTouchHelper(object : SimpleItemTouchHelperCallback() {

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                if (viewHolder is BoardListViewHolder) {
                    viewHolder.itemView.alpha = 1F
                }
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (viewHolder != null && viewHolder is BoardListViewHolder) {
                    viewHolder.itemView.alpha = 0.7F
                }
            }

            override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, fromPos: Int,
                                 target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)

                boardList.move(fromPos, toPos).update()
                notifyItemMoved(fromPos, toPos)

            }

        }).attachToRecyclerView(boardListView)
    }

    override fun getItemId(position: Int): Long {
        return boardList[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardListViewHolder {
        return BoardListViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.board_card, parent, false))
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    override fun onBindViewHolder(holder: BoardListViewHolder, position: Int) {
        val board = boardList[position]
        holder.itemView {
            boardImage_imageView {
                when (board.backgroundType) {
                    BackgroundType.COLOR -> {
                        setImageDrawable(board.backgroundColor.toColorDrawable)
                    }
                    BackgroundType.UNSPLASH_PHOTO -> {
                        Glide.with(this)
                                .load(Uri.parse(board.backgroundPhoto.urls.regular))
                                .centerCrop()
                                .into(boardImage_imageView)
                    }
                }
            }
            boardName_textView {
                text = board.name
                when (viewMode) {
                    ViewMode.GRID_VERTICAL -> {
                        setTextAppearance(R.style.TextAppearance_MaterialComponents_Headline5)
                        textSize = 24F
                    }
                    ViewMode.LIST_VERTICAL -> {
                        setTextAppearance(R.style.TextAppearance_MaterialComponents_Headline3)
                        textSize = 40F
                    }
                }
                backgroundColor = board.barColor.toAndroidColor
                textColor = board.barColor.colorScheme.text.toAndroidColor
            }
            boardCard_cardView {
                setOnClickListener {
                    hideKeyboard()

                    mainActivityViewModel.boardID = board.id

                    ViewBoardFragment.show(mainActivity)
                }
            }
        }
    }

}

class BoardListViewHolder(view: View) : RecyclerView.ViewHolder(view)