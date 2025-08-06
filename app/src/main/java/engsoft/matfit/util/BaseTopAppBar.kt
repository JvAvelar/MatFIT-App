package engsoft.matfit.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(
    title: String,
    leftIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack,
    onLeftIconClick: (() -> Unit)? = null,
    rightIcon: ImageVector? = null,
    onRightIconClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            },
            navigationIcon = {
                if (leftIcon != null) {
                    IconButton(onClick = { onLeftIconClick?.invoke() }) {
                        Icon(
                            imageVector = leftIcon,
                            contentDescription = "Left Icon",
                            tint = Color.White
                        )
                    }
                }
            },
            actions = {
                if (rightIcon != null) {
                    IconButton(onClick = { onRightIconClick?.invoke() }) {
                        Icon(
                            imageVector = rightIcon,
                            contentDescription = "Right Icon",
                            tint = Color.White
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
@Preview
fun TopAppBarPreview(){
    BaseTopAppBar(
        title = "Payment type"
    )
}