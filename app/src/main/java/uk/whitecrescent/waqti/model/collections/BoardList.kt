package uk.whitecrescent.waqti.model.collections

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Transient
import uk.whitecrescent.waqti.model.persistence.Cache
import uk.whitecrescent.waqti.model.persistence.Caches
import uk.whitecrescent.waqti.model.task.ID

// Just a container of all the Boards, not a collection because we don't want to persist it, it's
// just a wrapper for Caches.boards

// TODO: 29-Dec-18 I think the only way this can truly work is if it's an AbstractWaqtiList
// there can only exist one BoardList ever but this helps us to have the dragging and moving
// functionality
@Entity
class BoardList(name: String = "") : AbstractWaqtiList<Board>() {

    @Convert(converter = IDArrayListConverter::class, dbType = String::class)
    override var idList: ArrayList<ID> = ArrayList()

    @Transient
    override val cache: Cache<Board> = Caches.boards

    @Id
    override var id: ID = 0L

    var name: String = name
        set(value) {
            field = value
            update()
        }

    init {
        if (this.notDefault()) {
            this.update()
            this.initialize()
        }
    }

    override fun removeAt(index: Int): AbstractWaqtiList<Board> {
        val boardToRemove = this[index]
        boardToRemove.clear().update()
        Caches.boards.remove(boardToRemove)
        return super.removeAt(index) as BoardList
    }

    override fun move(fromIndex: Int, toIndex: Int): AbstractWaqtiList<Board> {
        when {
            !inRange(toIndex, fromIndex) -> {
                throw  IndexOutOfBoundsException("Cannot move $fromIndex  to $toIndex, limits are 0 and $nextIndex")
            }
            else -> {
                val found = this[fromIndex]
                super.removeAt(fromIndex)
                super.addAt(toIndex, found)
                return this
            }
        }
    }

    override fun notDefault(): Boolean {
        return id != 0L || name != ""
    }

    override fun update() {
        Caches.boardLists.put(this)
    }

    override fun initialize() {

    }

}