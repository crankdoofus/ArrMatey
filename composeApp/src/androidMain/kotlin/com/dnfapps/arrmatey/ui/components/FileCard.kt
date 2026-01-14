package com.dnfapps.arrmatey.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dnfapps.arrmatey.R
import com.dnfapps.arrmatey.api.arr.model.MediaFile
import com.dnfapps.arrmatey.api.arr.model.MovieFile
import com.dnfapps.arrmatey.compose.utils.breakable
import com.dnfapps.arrmatey.compose.utils.bytesAsFileSizeString
import com.dnfapps.arrmatey.entensions.Bullet
import com.dnfapps.arrmatey.utils.format
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun FileCard(file: MediaFile) {
    ContainerCard {
        Text(
            text = file.relativePath.breakable(),
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = listOfNotNull(
                file.quality?.qualityLabel,
                file.languages.first().name,
                file.size.bytesAsFileSizeString()
            ).joinToString(Bullet),
            fontSize = 12.sp
        )
        file.dateAdded?.format("MMM d, yyyy")?.let { formattedDate ->
            Text(
                text = stringResource(R.string.added_on, formattedDate),
                fontSize = 12.sp
            )
        }
    }
}