package com.bashar.avalag.src.core.ui.widgets.items

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bashar.avalag.R

@Preview(showBackground = true, showSystemUi = false,)
@Composable fun ChevronItem(
    @DrawableRes icon: Int = R.drawable.ic_ring, title: String = "Notifications Setting", subtitle: String? = null, onClick: () -> Unit = {}
) {
    Row(Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)
        .padding(8.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        Box(Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 12.dp),) {
            Icon(painterResource(icon), null, modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center))
        }; Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f),verticalArrangement = Arrangement.Center) {

           Text(title, fontWeight = FontWeight.Medium, fontSize = 16.sp)

            if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.width(24.dp))
        Icon(Icons.AutoMirrored.Outlined.ArrowForwardIos, null, modifier = Modifier.size(20.dp))
    }
}