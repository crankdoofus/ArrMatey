package com.dnfapps.arrmatey.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dnfapps.arrmatey.model.InstanceType
import com.dnfapps.arrmatey.ui.viewmodel.InstanceViewModel
import com.dnfapps.arrmatey.ui.viewmodel.rememberHasMultipleInstances
import com.dnfapps.arrmatey.ui.viewmodel.rememberInstanceFor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstancePicker(
    type: InstanceType,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    val instanceViewModel = viewModel<InstanceViewModel>()
    val instance = rememberInstanceFor(type)
    val hasMultipleInstances = rememberHasMultipleInstances(type)

    val allInstances by instanceViewModel.allInstances.collectAsStateWithLifecycle()
    val typeInstances = allInstances.filter { it.type == type }

    var textWidth by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .onGloballyPositioned {
                    textWidth = it.size
                }
                .clickable(
                    enabled = hasMultipleInstances,
                    onClick = {
                        isExpanded = !isExpanded
                    }
                )
        ) {
            Text(
                text = instance?.label ?: ""
            )
            if (hasMultipleInstances) {
                ExposedDropdownMenuDefaults.TrailingIcon(isExpanded)
            }
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.width(
                with(LocalDensity.current) {
                    (textWidth.width * 1.5f).toDp()
                }
            )
        ) {
            typeInstances.forEach { instance ->
                DropdownMenuItem(
                    text = { Text(instance.label) },
                    onClick = {
                        isExpanded = false
                        instanceViewModel.setSelected(instance)
                    }
                )
            }
        }
    }
}