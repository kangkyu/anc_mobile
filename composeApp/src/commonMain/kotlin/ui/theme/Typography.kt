package ui.theme

import ancmobile.composeapp.generated.resources.Res
import ancmobile.composeapp.generated.resources.noto_sans_kr_regular
import ancmobile.composeapp.generated.resources.noto_serif_kr_regular
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font

val customTypography: Typography
    @Composable
    get() = Typography(

        //val h1: TextStyle,
        //val h2: TextStyle,
        //val h3: TextStyle,
        //val h4: TextStyle,
        //val h5: TextStyle,
        //val h6: TextStyle,
        //val subtitle1: TextStyle,
        //val subtitle2: TextStyle,
        //val body1: TextStyle,
        //val body2: TextStyle,
        //val button: TextStyle,
        //val caption: TextStyle,
        //val overline: TextStyle

        body1 = TextStyle(
            fontFamily = FontFamily(
                Font(resource = Res.font.noto_sans_kr_regular,
                    weight = FontWeight.Normal, style = FontStyle.Normal)
            ),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
        /* Other default text styles to override
        button = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp
        ),
        caption = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )
        */
    )
