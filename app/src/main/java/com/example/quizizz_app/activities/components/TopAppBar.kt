package com.example.quizizz_app.activities.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun TopAppBarItem(
    modifier: Modifier = Modifier,
    contentLogo: @Composable (modifier: Modifier) -> Unit,
    iconLeft: Painter? = null,
    iconRight: @Composable (modifier: Modifier) -> Unit,
    backgroundColor: Brush,
    onClickLeftIcon: () -> Unit,
    onClickRightIcon: () -> Unit,
    onClick: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .background(brush = backgroundColor)
            .padding(15.dp)
            .clickable { onClick() }

    ) {
        val (layoutIconLeft, layoutImageLogo, layoutIconRight) = createRefs()
        iconLeft?.let {
            Image(
                painter = iconLeft,
                contentDescription = "Icon Left",
                modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .clickable {
                        onClickLeftIcon()
                    }
                    .constrainAs(layoutIconLeft) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        top.linkTo(parent.top)
                    })
        }
        contentLogo(
            modifier.constrainAs(layoutImageLogo) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }
        )

        iconRight(
            modifier
                .clickable { onClickRightIcon() }
                .constrainAs(layoutIconRight) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(parent.top)
                }
        )

    }

}