package com.janus.aprendiendonumeros.ui.base

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.janus.aprendiendonumeros.ui.utilities.UIDesign
import com.janus.aprendiendonumeros.ui.utilities.loadImageFromUrl
import com.janus.aprendiendonumeros.ui.utilities.positionIsRepeated

interface MultiFigurePrinter {

    fun setUpFigures(container: ViewGroup, currentNumber: Int, figure: String) {
        val sizeFigures = UIDesign.getSize(container.context, currentNumber)
        val containerWidth = container.measuredWidth.minus(sizeFigures)
        val containerHeight = container.measuredHeight.minus(sizeFigures)

        for (i: Int in 1..currentNumber) {
            var x: Int = (0..containerWidth).random()
            var y: Int = (0..containerHeight).random()

            while (container.positionIsRepeated(x, y, sizeFigures)) {
                x = (0..containerWidth).random()
                y = (0..containerHeight).random()
            }

            val figureView: ImageView = createFigure(container.context, x, y, sizeFigures, figure)
            container.addView(figureView)
        }
    }

    private fun createFigure(
        context: Context,
        x: Int,
        y: Int,
        size: Int,
        image: String,
    ): ImageView {
        val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(size, size)
        val figureView = ImageView(context)
        figureView.layoutParams = layoutParams
        figureView.adjustViewBounds = true
        figureView.x = x.toFloat()
        figureView.y = y.toFloat()
        figureView.loadImageFromUrl(image)
        figureView.alpha = 0f
        return figureView
    }
}