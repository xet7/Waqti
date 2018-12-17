package uk.whitecrescent.waqti.android.customview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_list.view.*
import uk.whitecrescent.waqti.R
import uk.whitecrescent.waqti.model.collections.TaskList
import uk.whitecrescent.waqti.model.logE
import uk.whitecrescent.waqti.model.now
import uk.whitecrescent.waqti.model.task.ID
import uk.whitecrescent.waqti.model.task.Task

//needs to have the BoardID in constructor
class BoardAdapter(val boardID: ID = 0) : RecyclerView.Adapter<BoardViewHolder>() {

    val itemList: MutableList<TaskList> = Array(10, { TaskList("Header $it") }).toMutableList()

    lateinit var boardView: BoardView

    init {
        this.setHasStableIds(true)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        boardView = recyclerView as BoardView
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemId(position: Int): Long {
        return itemList[position].id
    }

    // TODO: 14-Dec-18 Here we will programmatically create what's in R.layout.task_list so that
    // we can avoid having to do it in XML
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        /*val linearLayout = LinearLayout(parent.context)
        // Header and footer can actually be in xml list cant tho
        val header = LayoutInflater.from(linearLayout.context).inflate(R.layout.task_list_header, linearLayout)
        val list = BoardView.TaskListView(linearLayout.context)
        val footer = LayoutInflater.from(linearLayout.context).inflate(R.layout.task_list_footer, linearLayout)
        linearLayout.addView(header)
        linearLayout.addView(list)
        linearLayout.addView(footer)*/

        return BoardViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.task_list, parent, false)
        )
    }

/*    override fun onViewAttachedToWindow(holder: BoardViewHolder) {
        val list = holder.itemView.taskList_recyclerView
        if (list !in boardView.views) {
            boardView.views.add(list)
            logE("Attached ${holder.itemView.taskListHeader_textView.text}")
            logE("Size: ${boardView.views.size}")
        }
    }

    override fun onViewDetachedFromWindow(holder: BoardViewHolder) {
        val list = holder.itemView.taskList_recyclerView
        if (list in boardView.views) {
            boardView.views.remove(list)
            logE("Detached ${holder.itemView.taskListHeader_textView.text}")
            logE("Size: ${boardView.views.size}")
        }
    }*/


    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {

        holder.itemView.taskListHeader_textView.text = itemList[position].name
        holder.itemView.taskListFooter_textView.text = "Footer ${itemList[position].name.last()}"

        holder.itemView.taskListFooter_textView.setOnClickListener {
            (holder.itemView.taskList_recyclerView.adapter as TaskListAdapter).itemList.add(Task("New Boi!!! @ $now"))
            (holder.itemView.taskList_recyclerView.adapter as TaskListAdapter).notifyDataSetChanged()
            (holder.itemView.taskList_recyclerView.smoothScrollToPosition(
                    (holder.itemView.taskList_recyclerView.adapter as TaskListAdapter).itemCount - 1))
        }

        val list = holder.itemView.taskList_recyclerView
        if (list !in boardView.views) {
            boardView.views.add(list)
            logE("Added ${holder.itemView.taskListHeader_textView.text}")
            logE("Size: ${boardView.views.size}")
        }
    }

    fun add(taskList: TaskList) {
        itemList.add(taskList)
        notifyDataSetChanged()
    }
}