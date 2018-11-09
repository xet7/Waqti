package uk.whitecrescent.waqti.model.task

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import uk.whitecrescent.waqti.model.Cacheable
import uk.whitecrescent.waqti.model.hash
import uk.whitecrescent.waqti.model.persistence.Caches

/**
 * The user defined level of importance of a Task represented as a String with a number representing importance
 * level.
 *
 * Priority is particularly useful in solving or mediating Task collisions within collections. A Task collision
 * occurs when two or more Tasks in a collection share the same time, if they have different priority levels then
 * the Task with the higher priority level will be shown and a collision warning will be displayed to the user,
 * this is called a weak collision. If the tasks have equal priority levels then the user must mediate or solve
 * the collision themselves, this is called a strong collision.
 *
 * Priority can not be a Constraint.
 *
 * @see Task
 * @author Bassam Helal
 */
@Entity
class Priority(name: String = "", importanceLevel: Int = 0) : Cacheable {

    @Id
    override var id = 0L //Caches.priorities.newID()

    init {
        update()
    }

    companion object {
        fun fromString(string: String): Priority {
            val name = string.substringAfter("name:")
            val importanceLevel = string.substringAfter("importanceLevel:").toInt()
            return Priority(name, importanceLevel)
        }
    }

    override fun notDefault(): Boolean {
        return false
    }

    var name = name
        set(value) {
            field = value
            update()
        }

    var importanceLevel = importanceLevel
        set(value) {
            field = value
            update()
        }

    override fun update() = Caches.priorities.put(this)

    operator fun component1() = name

    operator fun component2() = importanceLevel

    override fun hashCode() = hash(name, importanceLevel)

    override fun equals(other: Any?) =
            other is Priority &&
                    other.name == this.name &&
                    other.importanceLevel == this.importanceLevel

    override fun toString() = "name:$name importanceLevel:$importanceLevel"

}