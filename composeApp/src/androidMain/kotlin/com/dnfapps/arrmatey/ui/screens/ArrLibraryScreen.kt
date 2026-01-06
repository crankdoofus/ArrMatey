package com.dnfapps.arrmatey.ui.screens

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dnfapps.arrmatey.PreferencesStore
import com.dnfapps.arrmatey.R
import com.dnfapps.arrmatey.api.arr.model.AnyArrMedia
import com.dnfapps.arrmatey.api.arr.viewmodel.LibraryUiState
import com.dnfapps.arrmatey.api.arr.viewmodel.UiErrorType
import com.dnfapps.arrmatey.compose.components.MediaList
import com.dnfapps.arrmatey.compose.components.PosterGrid
import com.dnfapps.arrmatey.compose.utils.FilterBy
import com.dnfapps.arrmatey.compose.utils.SortBy
import com.dnfapps.arrmatey.compose.utils.SortOrder
import com.dnfapps.arrmatey.compose.utils.applyFiltering
import com.dnfapps.arrmatey.compose.utils.applySorting
import com.dnfapps.arrmatey.entensions.copy
import com.dnfapps.arrmatey.entensions.getString
import com.dnfapps.arrmatey.entensions.showSnackbarImmediately
import com.dnfapps.arrmatey.model.InstanceType
import com.dnfapps.arrmatey.navigation.ArrScreen
import com.dnfapps.arrmatey.navigation.ArrTabNavigation
import com.dnfapps.arrmatey.ui.components.DropdownPicker
import com.dnfapps.arrmatey.ui.components.FilterMenuButton
import com.dnfapps.arrmatey.ui.components.InstancePicker
import com.dnfapps.arrmatey.ui.components.SortMenuButton
import com.dnfapps.arrmatey.ui.components.ViewTypeMenuButton
import com.dnfapps.arrmatey.ui.tabs.LocalArrTabNavigation
import com.dnfapps.arrmatey.ui.tabs.LocalArrViewModel
import com.dnfapps.arrmatey.ui.tabs.LocalInstance
import com.dnfapps.arrmatey.ui.theme.ViewType
import com.dnfapps.arrmatey.ui.viewmodel.NetworkConnectivityViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.w3c.dom.Text

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ArrLibraryTab(
    type: InstanceType,
    navigation: ArrTabNavigation = LocalArrTabNavigation.current
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val networkViewModel = viewModel<NetworkConnectivityViewModel>()
    val instance = LocalInstance.current

    val preferenceStore: PreferencesStore = koinInject()
    val selectedSortOrder by preferenceStore.sortOrder.collectAsState(SortOrder.Asc)
    val selectedSortOption by preferenceStore.sortBy.collectAsState(SortBy.Title)
    val selectedFilter by preferenceStore.filterBy.collectAsState(FilterBy.All)
    val viewTypeMap by preferenceStore.viewType.collectAsState(emptyMap())
    val selectedViewType = viewTypeMap[type] ?: ViewType.Grid

    val snackbarHostState = remember { SnackbarHostState() }
    var hasServerConnetivityError by remember { mutableStateOf(false) }
    val hasNetworkConnection by networkViewModel.isConnected.collectAsStateWithLifecycle()

    var showFilterSheet by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        },
        floatingActionButton = {
//            instance?.let {
//                FloatingActionButton(
//                    onClick = {
//                        navigation.navigateTo(ArrScreen.Search())
//                    }
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = null
//                    )
//                }
//            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InstancePicker(type)

                        if (hasServerConnetivityError) {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.clickable {
                                    scope.launch {
                                        val message = context.getString(R.string.instance_connect_error, instance?.url ?: "")
                                        snackbarHostState.showSnackbarImmediately(message = message)
                                    }
                                }
                            )
                        }
                        if (!hasNetworkConnection) {
                            Icon(
                                imageVector = Icons.Default.SignalWifiOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.clickable {
                                    scope.launch {
                                        snackbarHostState.showSnackbarImmediately(message = context.getString(R.string.no_network))
                                    }
                                }
                            )
                        }
                    }
                },
                actions = {
                    instance?.let {
                        IconButton(
                            onClick = { showSearchBar = !showSearchBar }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.search)
                            )
                        }
                        IconButton(
                            onClick = {
                                navigation.navigateTo(ArrScreen.Search())
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                        IconButton(
                            onClick = { showFilterSheet = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = stringResource(R.string.filter)
                            )
                        }

//                        ViewTypeMenuButton(
//                            viewType = selectedViewType,
//                            onViewTypeChanged = { preferenceStore.saveViewType(type, it) }
//                        )
//                        FilterMenuButton(
//                            instanceType = type,
//                            selectedFilter = selectedFilter,
//                            onFilterChange = {
//                                preferenceStore.saveFilterBy(it)
//                            }
//                        )
//                        SortMenuButton(
//                            instanceType = type,
//                            onSortChanged = {
//                                preferenceStore.saveSortBy(it)
//                            },
//                            onOrderChanged = {
//                                preferenceStore.saveSortOrder(it)
//                            },
//                            sortBy = selectedSortOption,
//                            sortOrder = selectedSortOrder
//                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues.copy(bottom = 0.dp))
                .fillMaxSize()
        ) {
            val arrViewModel = LocalArrViewModel.current
            arrViewModel?.let { arrViewModel ->

                val uiState by arrViewModel.uiState.collectAsStateWithLifecycle()
                when (val state = uiState) {
                    is LibraryUiState.Initial,
                    is LibraryUiState.Loading -> {
                        LoadingIndicator(
                            modifier = Modifier
                                .size(96.dp)
                                .align(Alignment.Center)
                        )
                    }

                    is LibraryUiState.Success -> {
                        PullToRefreshBox(
                            isRefreshing = state.isRefreshing,
                            onRefresh = {
                                scope.launch {
                                    arrViewModel.refreshLibrary()
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val items = state.items
                                .applyFiltering(type, selectedFilter)
                                .applySorting(type, selectedSortOption, selectedSortOrder)
                                .let { lst ->
                                    if (searchQuery.isNotEmpty()) {
                                        lst.filter { it.title.contains(searchQuery, ignoreCase = true) }
                                    } else {
                                        lst
                                    }
                                }

                            if (items.isEmpty()) {
                                EmptyLibraryView(modifier = Modifier.align(Alignment.Center))
                            } else {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    AnimatedVisibility(
                                        visible = showSearchBar,
                                        enter = expandVertically(),
                                        exit = shrinkVertically()
                                    ) {
                                        OutlinedTextField(
                                            value = searchQuery,
                                            onValueChange = { searchQuery = it },
                                            modifier = Modifier
                                                .padding(horizontal = 18.dp, vertical = 12.dp)
                                                .fillMaxWidth(),
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = null,
                                                    modifier = Modifier.clickable {
                                                        searchQuery = ""
                                                        showSearchBar = false
                                                    }
                                                )
                                            },
                                            placeholder = { Text(stringResource(R.string.search)) },
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                    }

                                    MediaView(
                                        items = items,
                                        onItemClick = {
                                            it.id?.let { id ->
                                                navigation.navigateTo(
                                                    ArrScreen.Details(id = id)
                                                )
                                            }
                                        },
                                        viewType = selectedViewType
                                    )
                                }
                            }
                        }
                    }

                    is LibraryUiState.Error -> {
                        LaunchedEffect(state.error) {
                            snackbarHostState.showSnackbarImmediately(state.error.message)
                        }

                        hasServerConnetivityError = state.type == UiErrorType.Network

                        var isRefreshing by remember { mutableStateOf(false) }

                        LaunchedEffect(state) {
                            isRefreshing = false
                        }

                        LaunchedEffect(isRefreshing) {
                            if (isRefreshing) {
                                scope.launch { arrViewModel.refreshLibrary() }
                            }
                        }

                        PullToRefreshBox(
                            isRefreshing = isRefreshing,
                            onRefresh = {
                                isRefreshing = true
                            },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            InstanceErrorView(
                                onRefresh = { isRefreshing = true },
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            } ?: run {
                NoInstanceView(
                    type = type,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (showFilterSheet) {
                FilterSheet(
                    type = type,
                    onDismiss = { showFilterSheet = false },
                    selectedViewType = selectedViewType,
                    onViewTypeChanged = { preferenceStore.saveViewType(type, it) },
                    selectedFilter = selectedFilter,
                    onFilterChanged = { preferenceStore.saveFilterBy(it) },
                    selectedSortOrder = selectedSortOrder,
                    onSortOrderChanged = { preferenceStore.saveSortOrder(it) },
                    selectedSortBy = selectedSortOption,
                    onSortByChanged = { preferenceStore.saveSortBy(it) }
                )
            }
        }
    }
}

@Composable
private fun NoInstanceView(
    type: InstanceType,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.CloudQueue,
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
        Text(
            text = stringResource(R.string.no_type_instances, type.name),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Text(text = stringResource(R.string.no_type_instances_message, type.name))
    }
}

@Composable
private fun EmptyLibraryView(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.VideoLibrary,
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
        Text(
            text = stringResource(R.string.empty_library),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = stringResource(R.string.empty_library_message)
        )
    }
}

@Composable
private fun InstanceErrorView(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.CloudOff,
            contentDescription = null,
            modifier = Modifier.size(128.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = stringResource(R.string.couldnt_connect),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Text(text = stringResource(R.string.couldnt_connect_message))
        Button(onClick = onRefresh) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
fun MediaView(
    items: List<AnyArrMedia>,
    onItemClick: (AnyArrMedia) -> Unit,
    viewType: ViewType
) {
    when (viewType) {
        ViewType.List -> MediaList(
            items = items,
            onItemClick = onItemClick,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxSize()
        )
        ViewType.Grid -> PosterGrid(
            items = items,
            onItemClick = onItemClick,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    type: InstanceType,
    onDismiss: () -> Unit,
    selectedViewType: ViewType,
    onViewTypeChanged: (ViewType) -> Unit,
    selectedFilter: FilterBy,
    onFilterChanged: (FilterBy) -> Unit,
    selectedSortOrder: SortOrder,
    onSortOrderChanged: (SortOrder) -> Unit,
    selectedSortBy: SortBy,
    onSortByChanged: (SortBy) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            ViewTypePicker(
                viewType = selectedViewType,
                onViewTypeChanged = onViewTypeChanged
            )

            DropdownPicker(
                options = FilterBy.typeEntries(type),
                selectedOption = selectedFilter,
                onOptionSelected = onFilterChanged,
                label = { Text(stringResource(R.string.filter_by)) },
                getOptionLabel = { getString(it.iosText) }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DropdownPicker(
                    options = SortBy.typeEntries(type),
                    selectedOption = selectedSortBy,
                    onOptionSelected = onSortByChanged,
                    label = { Text(stringResource(R.string.sort_by)) },
                    getOptionLabel = { getString(it.textKey) },
                    getOptionIcon = { it.androidIcon },
                    modifier = Modifier.weight(1f)
                )
                DropdownPicker(
                    options = SortOrder.entries,
                    selectedOption = selectedSortOrder,
                    onOptionSelected = onSortOrderChanged,
                    label = { Text(stringResource(R.string.sort_order)) },
                    getOptionLabel = { getString(it.iosText) },
                    getOptionIcon = { it.androidIcon },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ViewTypePicker(
    viewType: ViewType,
    onViewTypeChanged: (ViewType) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ViewTypeButton(
            selected = viewType == ViewType.Grid,
            modifier = Modifier.weight(1f),
            onClick = { onViewTypeChanged(ViewType.Grid) },
            icon = Icons.Default.GridView,
            label = "Grid"
        )
        ViewTypeButton(
            selected = viewType == ViewType.List,
            modifier = Modifier.weight(1f),
            onClick = { onViewTypeChanged(ViewType.List) },
            icon = Icons.AutoMirrored.Default.List,
            label = "List"
        )
    }
}

@Composable
fun ViewTypeButton(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val defaultButtonColors = ButtonDefaults.buttonColors()
    val borderStroke = BorderStroke(1.dp, defaultButtonColors.containerColor)

    val colors = if (selected) defaultButtonColors else {
        ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        )
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        border = borderStroke,
        colors = colors
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Text(text = label)
    }

}