package com.villejuif.eartheventtraker.events

import android.net.Uri
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.villejuif.eartheventtraker.R
import com.villejuif.eartheventtraker.network.EonetCategory
import com.villejuif.eartheventtraker.network.EonetEvent
import com.villejuif.eartheventtraker.network.EonetGeometry
import com.villejuif.eartheventtraker.network.EonetSource
import java.lang.StringBuilder


@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<EonetEvent>?) {
    items?.let {
        (listView.adapter as MyEarthEventRecyclerViewAdapter).submitList(items)
    }
}

@BindingAdapter("android:src")
fun setImageViewResource(imageView: ImageView, categories: List<EonetCategory>) {
    imageView.setImageResource(src(categories))
}

@BindingAdapter("android:text")
fun setText(textView: TextView, sources: List<EonetSource>) {
    textView.text = text(sources)
}

@BindingAdapter("android:text")
fun setTextDate(textView: TextView, geometries: List<EonetGeometry>){
    if(geometries.isEmpty()){
        textView.text = textView.context.getText(R.string.earth_event_date_no_reported)
    }else{
        textView.text = geometries[0].date
    }
}

@BindingAdapter("earthEventsApiStatus")
fun bindStatus(statusImageView: ImageView, status: EarthEventsStatus?) {
    when (status) {
        EarthEventsStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_loading)
        }
        EarthEventsStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        EarthEventsStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

private fun text(sources: List<EonetSource>): String {
    return if (sources.isEmpty()) {
        "Unknown source"
    } else {
        val stringBuilder = StringBuilder()

        sources.forEach { source ->
            run {
                if (stringBuilder.isNotEmpty()) stringBuilder.append(", ")
                stringBuilder.append(Uri.parse(source.url).host)
            }
        }

        stringBuilder.toString()
    }
}

@DrawableRes
private fun src(categories: List<EonetCategory>): Int {
    return when {
        categories.isEmpty() -> {
            R.drawable.ic_unknown
            //TODO Fiberbase analytics
        }
        categories.size == 1 -> {
            val category = categories[0].title
            when {
                category.contains("wildfire", ignoreCase = true) -> {
                    //TODO Fiberbase analytics
                    R.drawable.ic_flame
                }
                category.contains("volcano", ignoreCase = true) -> {
                    //TODO Fiberbase analytics
                    R.drawable.ic_volcano
                }
                category.contains("ice", ignoreCase = true) -> {
                    //TODO Fiberbase analytics
                    R.drawable.ic_sea_ice
                }
                category.contains("storms", ignoreCase = true) -> {
                    //TODO Fiberbase analytics
                    R.drawable.ic_cyclone
                }
                else -> {
                    //TODO Fiberbase analytics
                    R.drawable.ic_unknown
                }
            }
        }
        else -> {
            //TODO Fiberbase analytics
            R.drawable.ic_caution
        }
    }
}































