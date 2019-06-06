package uk.whitecrescent.waqti.frontend.customview

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.property_card.view.*
import uk.whitecrescent.waqti.R

class PropertyCard
@JvmOverloads constructor(context: Context,
                          attributeSet: AttributeSet? = null,
                          defStyle: Int = 0) : CardView(context, attributeSet, defStyle) {

    init {

        View.inflate(context, R.layout.property_card, this)

        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.PropertyCard)

        title_textView.apply {
            attributes.getString(R.styleable.PropertyCard_title).apply {
                if (this == null) text = SpannableStringBuilder("")
                else text = SpannableStringBuilder(this)
            }
        }

        attributes.recycle()
    }

    inline fun onClick(crossinline onClick: () -> Unit) {
        root_cardView.setOnClickListener {
            onClick()
        }
    }

}