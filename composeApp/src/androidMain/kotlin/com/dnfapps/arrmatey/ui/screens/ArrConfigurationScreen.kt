package com.dnfapps.arrmatey.ui.screens

import com.dnfapps.arrmatey.shared.MR
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dnfapps.arrmatey.compose.components.AMOutlinedTextField
import com.dnfapps.arrmatey.database.dao.ConflictField
import com.dnfapps.arrmatey.database.dao.InsertResult
import com.dnfapps.arrmatey.instances.model.InstanceType
import com.dnfapps.arrmatey.instances.state.AddInstanceUiState
import com.dnfapps.arrmatey.utils.mokoString
import com.dnfapps.arrmatey.utils.thenGet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrConfigurationScreen(
    instanceType: InstanceType,
    uiState: AddInstanceUiState,
    onApiEndpointChanged: (String) -> Unit,
    onApiKeyChanged: (String) -> Unit,
    onInstanceLabelChanged: (String) -> Unit,
    onIsSlowInstanceChanged: (Boolean) -> Unit,
    onCustomTimeoutChanged: (Long?) -> Unit,
    onTestConnection: () -> Unit
) {
    val apiEndpoint = uiState.apiEndpoint
    val apiKey = uiState.apiKey
    val instanceLabel = uiState.instanceLabel

    val endpointError = uiState.endpointError
    val isTesting = uiState.testing
    val testResult = uiState.testResult

    val isSlowInstance = uiState.isSlowInstance
    val customTimeout = uiState.customTimeout

    val createResult = uiState.createResult

    val hasLabelConflict = remember(createResult) {
        (createResult as? InsertResult.Conflict)
            ?.fields
            ?.contains(ConflictField.InstanceLabel) == true
    }

    val hasUrlConflict = remember(createResult) {
        (createResult as? InsertResult.Conflict)
            ?.fields
            ?.contains(ConflictField.InstanceUrl) == true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AMOutlinedTextField(
            label = mokoString(MR.strings.label),
            value = instanceLabel,
            onValueChange = onInstanceLabelChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = instanceType.toString(),
            singleLine = true,
            isError = hasLabelConflict,
            errorMessage = hasLabelConflict thenGet mokoString(MR.strings.instance_label_exists)
        )

        AMOutlinedTextField(
            label = mokoString(MR.strings.host),
            required = true,
            value = apiEndpoint,
            onValueChange = onApiEndpointChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = mokoString(MR.strings.host_placeholder) + "${instanceType.defaultPort}",
            description = mokoString(MR.strings.host_description, instanceType.name),
            singleLine = true,
            isError = endpointError || hasUrlConflict,
            errorMessage = when {
                endpointError -> mokoString(MR.strings.invalid_host)
                hasUrlConflict -> mokoString(MR.strings.instance_url_exists)
                else -> null
            }
        )

        AMOutlinedTextField(
            label = mokoString(MR.strings.api_key),
            required = true,
            value = apiKey,
            onValueChange = onApiKeyChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = mokoString(MR.strings.api_key_placeholder),
            singleLine = true
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onTestConnection,
                enabled = !isTesting && apiEndpoint.isNotBlank() && apiKey.isNotBlank()
            ) {
                if (isTesting) {
                    CircularProgressIndicator()
                } else {
                    Text(text = mokoString(MR.strings.test))
                }
            }

            testResult?.let { result ->
                if (result) {
                    Text(
                        text = "✅ ${mokoString(MR.strings.success)}",
                        color = Color.Green
                    )
                } else {
                    Text(
                        text = "❌ ${mokoString(MR.strings.failure)}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = isSlowInstance,
                    onValueChange = onIsSlowInstanceChanged
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = mokoString(MR.strings.slow_instance))
            Switch(
                checked = isSlowInstance,
                onCheckedChange = null
            )
        }

        AMOutlinedTextField(
            value = customTimeout?.toString() ?: "",
            onValueChange = { onCustomTimeoutChanged(it.toLongOrNull()) },
            modifier = Modifier.fillMaxWidth(),
            label = mokoString(MR.strings.custom_timeout_seconds),
            enabled = isSlowInstance,
            placeholder = "300",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}
