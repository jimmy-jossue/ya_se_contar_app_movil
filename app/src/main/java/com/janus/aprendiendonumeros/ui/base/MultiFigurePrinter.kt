package com.janus.aprendiendonumeros.ui.base

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.data.model.ResourceImage
import com.janus.aprendiendonumeros.ui.utilities.loadImageFromUrl
import com.janus.aprendiendonumeros.ui.utilities.positionIsRepeated

interface MultiFigurePrinter {

    var listImages: List<ResourceImage>
    var randomResourceImage: ResourceImage
    var activity: Activity

    fun addImages(
        container: ViewGroup,
        totalImages: Int,
        eventClick: View.OnClickListener? = null,
    ) {
        val randomNumber: Int = (listImages.indices).random()
        randomResourceImage = listImages[randomNumber]

        val imageViewSize = when {
            (totalImages < 3) -> activity.resources.getDimension(R.dimen.img_size_large)
            (totalImages < 5) -> activity.resources.getDimension(R.dimen.img_size_medium)
            else -> activity.resources.getDimension(R.dimen.img_size_small)
        }.toInt()

        val containerWidth: Int = container.measuredWidth.minus(imageViewSize)
        val containerHeight = container.measuredHeight.minus(imageViewSize)

        for (i: Int in 1..totalImages) {
            var x: Int = (0..containerWidth).random()
            var y: Int = (0..containerHeight).random()

            while (container.positionIsRepeated(x, y, imageViewSize)) {
                x = (0..containerWidth).random()
                y = (0..containerHeight).random()
            }

            val view: ImageView = createImage(x, y, imageViewSize)

            if (eventClick != null) {
                view.setOnClickListener(eventClick)
            }
            container.addView(view)
        }
    }

    private fun createImage(x: Int, y: Int, size: Int): ImageView {
        val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(size, size)
        val image = ImageView(activity)
        image.layoutParams = layoutParams
        image.adjustViewBounds = true
        image.x = x.toFloat()
        image.y = y.toFloat()
        image.loadImageFromUrl(randomResourceImage.icon)
        return image
    }
}