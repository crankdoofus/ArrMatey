package com.dnfapps.arrmatey.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import arrmatey.shared.generated.resources.Res
import arrmatey.shared.generated.resources.api_key
import arrmatey.shared.generated.resources.api_key_placeholder
import arrmatey.shared.generated.resources.cache_on_disk
import arrmatey.shared.generated.resources.custom_timeout_seconds
import arrmatey.shared.generated.resources.failure
import arrmatey.shared.generated.resources.host
import arrmatey.shared.generated.resources.host_description
import arrmatey.shared.generated.resources.host_placeholder
import arrmatey.shared.generated.resources.label
import arrmatey.shared.generated.resources.slow_instance
import arrmatey.shared.generated.resources.success
import arrmatey.shared.generated.resources.test
import com.dnfapps.arrmatey.compose.components.AMOutlinedTextField
import com.dnfapps.arrmatey.compose.screens.viewmodel.AddInstanceScreenViewModel
import com.dnfapps.arrmatey.compose.utils.requiredStringResource
import com.dnfapps.arrmatey.model.InstanceType
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrConfigurationScreen(
    instanceType: InstanceType
) {
    val viewModel = viewModel<AddInstanceScreenViewModel>()

    val apiEndpoint by viewModel.apiEndpoint.collectAsStateWithLifecycle()
    val apiKey by viewModel.apiKey.collectAsStateWithLifecycle()
    var instanceLabel by remember { viewModel.instanceLabel }

    val endpointError by viewModel.endpointError.collectAsStateWithLifecycle()
    val isTesting by viewModel.testing.collectAsStateWithLifecycle()
    val testResult by viewModel.result.collectAsStateWithLifecycle()

    val isSlowInstance by viewModel.isSlowInstance.collectAsStateWithLifecycle()
    val customTimeout by viewModel.customTimeout.collectAsStateWithLifecycle()
    val cacheOnDisk by viewModel.cacheOnDisk.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AMOutlinedTextField(
            label = stringResource(Res.string.label),
            value = instanceLabel,
            onValueChange = { instanceLabel = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = instanceType.toString(),
            singleLine = true
        )

        AMOutlinedTextField(
            label = stringResource(Res.string.host),
            required = true,
            value = apiEndpoint,
            onValueChange = {
                viewModel.setApiEndpoint(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(Res.string.host_placeholder, instanceType.defaultPort),
            description = stringResource(Res.string.host_description, instanceType.toString()),
            singleLine = true,
            isError = endpointError,
            errorMessage = if (endpointError) "InvalidHost" else null
        )

        AMOutlinedTextField(
            label = stringResource(Res.string.api_key),
            required = true,
            value = apiKey,
            onValueChange = {
                viewModel.setApiKey(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = stringResource(Res.string.api_key_placeholder),
            singleLine = true
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    viewModel.testConnection()
                },
                enabled = !isTesting && apiEndpoint.isNotBlank() && apiKey.isNotBlank()
            ) {
                if (isTesting) CircularProgressIndicator() else Text(text = stringResource(Res.string.test))
            }

            testResult?.let { result ->
                if (result) {
                    Text(text = "✅ ${stringResource(Res.string.success)}", color = Color.Green)
                } else {
                    Text(text = "❌ ${stringResource(Res.string.failure)}", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = isSlowInstance,
                    onValueChange = { viewModel.setIsSlowInstance(it) }
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.slow_instance)
            )
            Switch(
                checked = isSlowInstance,
                onCheckedChange = null
            )
        }
        AMOutlinedTextField(
            value = customTimeout?.toString() ?: "",
            onValueChange = {
                viewModel.setCustomTimeout(it.toLongOrNull())
            },
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.custom_timeout_seconds),
            enabled = isSlowInstance,
            placeholder = "300",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .toggleable(
                    value = cacheOnDisk,
                    onValueChange = { viewModel.setCacheOnDisk(it) }
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.cache_on_disk)
            )
            Switch(
                checked = cacheOnDisk,
                onCheckedChange = null
            )
        }
    }
}