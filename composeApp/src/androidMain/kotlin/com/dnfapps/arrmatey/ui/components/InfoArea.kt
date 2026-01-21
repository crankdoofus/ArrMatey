package com.dnfapps.arrmatey.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dnfapps.arrmatey.R
import com.dnfapps.arrmatey.arr.api.model.ArrMedia

@Composable
fun InfoArea(item: ArrMedia) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.information),
            fontWeight = FontWeight.Medium,
            fontSize = 26.sp
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column (
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
//                val infoItems by item.infoItems.collectAsStateWithLifecycle(emptyList())
//                if (infoItems.isEmpty()) {
//                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//                }
//                infoItems.forEachIndexed { index, info ->
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(text = info.label, fontSize = 14.sp)
//                        Text(
//                            text = info.value,
//                            color = MaterialTheme.colorScheme.primary,
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis,
//                            textAlign = TextAlign.End,
//                            fontSize = 14.sp
//                        )
//                    }
//                    if (index < infoItems.size - 1) {
//                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
//                    }
//                }
            }
        }
    }
}