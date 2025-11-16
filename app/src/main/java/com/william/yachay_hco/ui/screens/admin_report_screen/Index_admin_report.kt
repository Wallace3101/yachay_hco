package com.william.yachay_hco.ui.screens.admin_report_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.william.yachay_hco.R
import com.william.yachay_hco.ui.screens.admin_report_screen.partes.*
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.viewmodel.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportScreen(
    viewModel: ReportViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Pendientes", "Aprobados", "Rechazados", "Todos")

    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(selectedTab) {
        val status = when (selectedTab) {
            0 -> "PENDIENTE"
            1 -> "APROBADO"
            2 -> "RECHAZADO"
            3 -> null
            else -> null
        }
        viewModel.getAllReports(status)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Administración de Reportes",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Revisar reportes de usuarios",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BlueYachay,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .paint(
                    painter = painterResource(id = R.drawable.cultural_bg_2),
                    contentScale = ContentScale.Crop
                )
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = BlueYachay,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = BlueYachay,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index)
                                    FontWeight.Bold
                                else
                                    FontWeight.Normal
                            )
                        }
                    )
                }
            }

            AdminReportsContent(viewModel)
        }
    }
}