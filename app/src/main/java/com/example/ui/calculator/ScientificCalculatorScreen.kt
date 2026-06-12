package com.example.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState

data class CalcButton(
    val mainText: String,
    val shiftText: String? = null,
    val alphaText: String? = null,
    val isAlphaTopRight: Boolean = true,
    val type: ButtonType = ButtonType.Normal
)

enum class ButtonType {
    Normal,
    Function,
    Action, // AC, DEL
    Equals
}

val LuminaSurface = Color(0xFFF8F9FA)
val KeyNormal = Color(0xFFFFFFFF)
val KeyFunction = Color(0xFFF1F3F4)
val KeyActionBg = Color(0xFFFDE8E8)
val KeyActionText = Color(0xFF9B1C1C)
val KeyEquals = Color(0xFF4F46E5)
val ShiftColor = Color(0xFFD97706) // Amber
val AlphaColor = Color(0xFF0D9488) // Teal

@Composable
fun ScientificCalculatorScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: CalculatorViewModel = viewModel()
) {
    var isScientific by remember { mutableStateOf(true) }

    val expression by viewModel.expression.collectAsState()
    val result by viewModel.result.collectAsState()
    val isShift by viewModel.isShift.collectAsState()
    val isAlpha by viewModel.isAlpha.collectAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(LuminaSurface)) {
        val isTablet = maxWidth > 600.dp
        
        if (isTablet) {
            TabletLayout(isScientific, { isScientific = !isScientific }, onNavigateBack, viewModel, expression, result, isShift, isAlpha)
        } else {
            MobileLayout(isScientific, { isScientific = !isScientific }, onNavigateBack, viewModel, expression, result, isShift, isAlpha)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletLayout(isScientific: Boolean, onToggleMode: () -> Unit, onNavigateBack: () -> Unit,
                 viewModel: CalculatorViewModel, expression: String, result: String, isShift: Boolean, isAlpha: Boolean) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Left Sidebar - 25% (History)
        Column(
            modifier = Modifier
                .weight(0.25f)
                .fillMaxHeight()
                .background(LuminaSurface)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("History", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
                Icon(Icons.Default.History, contentDescription = "History", tint = KeyActionText)
            }
            Spacer(modifier = Modifier.height(16.dp))
            HistoryList()
        }
        
        Spacer(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.LightGray))
        
        // Main Area - 75%
        Column(
            modifier = Modifier
                .weight(0.75f)
                .fillMaxHeight()
                .padding(24.dp)
        ) {
            TopHeader(isTablet = true, onNavigateBack = onNavigateBack)
            Spacer(modifier = Modifier.height(16.dp))
            CalculatorDisplay(isScientific = isScientific, expression = expression, result = result, isShift = isShift, isAlpha = isAlpha)
            Spacer(modifier = Modifier.height(24.dp))
            CalculatorKeypad(isTablet = true, isScientific = isScientific, onToggleMode = onToggleMode, onKeyPress = viewModel::onKeyPress, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun MobileLayout(isScientific: Boolean, onToggleMode: () -> Unit, onNavigateBack: () -> Unit,
                 viewModel: CalculatorViewModel, expression: String, result: String, isShift: Boolean, isAlpha: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            TopHeader(isTablet = false, onNavigateBack = onNavigateBack)
            Spacer(modifier = Modifier.height(16.dp))
            CalculatorDisplay(isScientific = isScientific, expression = expression, result = result, isShift = isShift, isAlpha = isAlpha)
            Spacer(modifier = Modifier.height(16.dp))
            CalculatorKeypad(isTablet = false, isScientific = isScientific, onToggleMode = onToggleMode, onKeyPress = viewModel::onKeyPress, modifier = Modifier.weight(1f))
        }
        
        CalculatorBottomNav()
    }
}

@Composable
fun CalculatorBottomNav() {
    NavigationBar(containerColor = LuminaSurface, tonalElevation = 0.dp) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Calculate, contentDescription = "Calculate") },
            label = { Text("Calculate") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = KeyEquals,
                indicatorColor = KeyEquals
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.History, contentDescription = "History") },
            label = { Text("History") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Functions, contentDescription = "Variables") },
            label = { Text("Variables") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Code, contentDescription = "Constants") },
            label = { Text("Constants") }
        )
    }
}

@Composable
fun TopHeader(isTablet: Boolean, onNavigateBack: () -> Unit) {
    if (isTablet) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = KeyEquals, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scientific Genius", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = KeyEquals)
            }
            Icon(Icons.Default.History, contentDescription = "History", tint = Color.Gray, modifier = Modifier.size(24.dp))
        }
    } else {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onNavigateBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = KeyEquals)
            }
            Text("Scientific Genius", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = KeyEquals, modifier = Modifier.align(Alignment.Center))
            IconButton(onClick = { }, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(Icons.Default.History, contentDescription = "History", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun CalculatorDisplay(isScientific: Boolean = true, expression: String, result: String, isShift: Boolean, isAlpha: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            if (isScientific) {
                Text("SHIFT", color = if (isShift) ShiftColor else Color.LightGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(12.dp))
                Text("ALPHA", color = if (isAlpha) AlphaColor else Color.LightGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(12.dp))
                Text("RAD", color = Color.DarkGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(12.dp))
                Text("MATH", color = Color.DarkGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = expression.ifEmpty { "0" }, 
            fontSize = 28.sp, 
            color = Color.DarkGray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = result.ifEmpty { " " }, 
            fontSize = 44.sp, 
            fontWeight = FontWeight.Bold, 
            color = KeyEquals,
            maxLines = 1
        )
    }
}

@Composable
fun CalculatorKeypad(isTablet: Boolean, isScientific: Boolean, onToggleMode: () -> Unit, onKeyPress: (CalcButton) -> Unit, modifier: Modifier = Modifier) {
    val spacing = if (isTablet) 8.dp else 4.dp
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        // Mode toggle
        Row(
            modifier = Modifier
                .background(Color(0xFFE5E7EB), RoundedCornerShape(20.dp))
                .padding(4.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .background(if (!isScientific) KeyEquals else Color.Transparent, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { if(isScientific) onToggleMode() }
                    .then(if (!isScientific) Modifier.shadow(2.dp, RoundedCornerShape(16.dp)) else Modifier)
            ) {
                Text("Standard", fontSize = 14.sp, color = if (!isScientific) Color.White else Color.Gray, fontWeight = if (!isScientific) FontWeight.Bold else FontWeight.Normal)
            }
            Box(
                modifier = Modifier
                    .background(if (isScientific) KeyEquals else Color.Transparent, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { if(!isScientific) onToggleMode() }
                    .then(if (isScientific) Modifier.shadow(2.dp, RoundedCornerShape(16.dp)) else Modifier)
            ) {
                Text("Scientific", fontSize = 14.sp, color = if (isScientific) Color.White else Color.Gray, fontWeight = if (isScientific) FontWeight.Bold else FontWeight.Normal)
            }
        }
        Spacer(modifier = Modifier.height(spacing))

        if (isScientific) {
            // Function Keys (5 rows)
            FunctionGrid(spacing, onKeyPress = onKeyPress, modifier = Modifier.weight(5f))
            
            // Numeric Keys (4 rows)
            NumericGrid(spacing, onKeyPress = onKeyPress, modifier = Modifier.weight(4.8f)) // ~20% taller relative weight
        } else {
            NumericGrid(spacing, onKeyPress = onKeyPress, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun FunctionGrid(spacing: androidx.compose.ui.unit.Dp, onKeyPress: (CalcButton) -> Unit, modifier: Modifier = Modifier) {
    val rows = listOf(
        listOf(
            CalcButton("SHIFT", type = ButtonType.Function),
            CalcButton("ALPHA", type = ButtonType.Function),
            CalcButton("◀", type = ButtonType.Function),
            CalcButton("▲", type = ButtonType.Function),
            CalcButton("▼", type = ButtonType.Function),
            CalcButton("▶", type = ButtonType.Function)
        ),
        listOf(
            CalcButton("CALC", shiftText = "SOLVE", alphaText = "=", isAlphaTopRight = false),
            CalcButton("∫", shiftText = "d/dx", alphaText = ":", isAlphaTopRight = false),
            CalcButton("x⁻¹", shiftText = "x!"),
            CalcButton("logₐb", shiftText = "Σ"),
            CalcButton("MODE", type = ButtonType.Function),
            CalcButton("ON", type = ButtonType.Function)
        ),
        listOf(
            CalcButton("a/b", shiftText = "d/c"),
            CalcButton("√", shiftText = "³√"),
            CalcButton("x²", shiftText = "x³"),
            CalcButton("xⁿ", shiftText = "x√"),
            CalcButton("log", shiftText = "10^x"),
            CalcButton("ln", shiftText = "e^x")
        ),
        listOf(
            CalcButton("(-)", shiftText = "∠", alphaText = "A"),
            CalcButton("° ' \"", shiftText = "←", alphaText = "B"),
            CalcButton("hyp", alphaText = "C"),
            CalcButton("sin", shiftText = "sin⁻¹", alphaText = "D"),
            CalcButton("cos", shiftText = "cos⁻¹", alphaText = "E"),
            CalcButton("tan", shiftText = "tan⁻¹", alphaText = "F")
        ),
        listOf(
            CalcButton("RCL", shiftText = "STO"),
            CalcButton("ENG", shiftText = "←", alphaText = "i"),
            CalcButton("(", shiftText = "%"),
            CalcButton(")", shiftText = ",", alphaText = "X"),
            CalcButton("S⇔D", shiftText = "a b/c", alphaText = "Y"),
            CalcButton("M+", shiftText = "M-", alphaText = "M")
        )
    )

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(spacing)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                row.forEach { btn ->
                    KeyButton(button = btn, onKeyPress = onKeyPress, modifier = Modifier.weight(1f).fillMaxHeight())
                }
            }
        }
    }
}

@Composable
fun NumericGrid(spacing: androidx.compose.ui.unit.Dp, onKeyPress: (CalcButton) -> Unit, modifier: Modifier = Modifier) {
    val rows = listOf(
        listOf(
            CalcButton("7", shiftText = "CONST", type = ButtonType.Normal),
            CalcButton("8", shiftText = "CONV", type = ButtonType.Normal),
            CalcButton("9", shiftText = "CLR", type = ButtonType.Normal),
            CalcButton("DEL", shiftText = "INS", type = ButtonType.Action),
            CalcButton("AC", shiftText = "OFF", type = ButtonType.Action)
        ),
        listOf(
            CalcButton("4", shiftText = "MATRIX", type = ButtonType.Normal),
            CalcButton("5", shiftText = "VECTOR", type = ButtonType.Normal),
            CalcButton("6", shiftText = "STAT", type = ButtonType.Normal),
            CalcButton("×", shiftText = "nPr", type = ButtonType.Normal),
            CalcButton("÷", shiftText = "nCr", type = ButtonType.Normal)
        ),
        listOf(
            CalcButton("1", shiftText = "CMPLX", type = ButtonType.Normal),
            CalcButton("2", shiftText = "BASE", type = ButtonType.Normal),
            CalcButton("3", type = ButtonType.Normal),
            CalcButton("+", shiftText = "Pol", type = ButtonType.Normal),
            CalcButton("-", shiftText = "Rec", type = ButtonType.Normal)
        ),
        listOf(
            CalcButton("0", shiftText = "Rnd", type = ButtonType.Normal),
            CalcButton(".", shiftText = "Ran#", alphaText = "RanInt", isAlphaTopRight = false, type = ButtonType.Normal),
            CalcButton("×10^x", shiftText = "π", alphaText = "e", isAlphaTopRight = false, type = ButtonType.Normal),
            CalcButton("Ans", shiftText = "DRG>", type = ButtonType.Normal),
            CalcButton("=", type = ButtonType.Equals)
        )
    )

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(spacing)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                row.forEach { btn ->
                    KeyButton(button = btn, onKeyPress = onKeyPress, modifier = Modifier.weight(1f).fillMaxHeight())
                }
            }
        }
    }
}

@Composable
fun KeyButton(button: CalcButton, onKeyPress: (CalcButton) -> Unit, modifier: Modifier = Modifier) {
    val bgColor = when (button.type) {
        ButtonType.Normal -> KeyNormal
        ButtonType.Function -> KeyFunction
        ButtonType.Action -> KeyActionBg
        ButtonType.Equals -> KeyEquals
    }
    
    val textColor = when (button.type) {
        ButtonType.Action -> KeyActionText
        ButtonType.Equals -> Color.White
        else -> Color.DarkGray
    }

    Box(
        modifier = modifier
            .shadow(1.dp, RoundedCornerShape(8.dp))
            .background(bgColor, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable { onKeyPress(button) },
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(1.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp).padding(top = 1.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row {
                    if (button.shiftText != null) {
                        Text(button.shiftText, color = ShiftColor, fontSize = 6.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                    if (!button.isAlphaTopRight && button.alphaText != null) {
                        Spacer(modifier = Modifier.width(1.dp))
                        Text(button.alphaText, color = AlphaColor, fontSize = 6.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }
                
                if (button.isAlphaTopRight && button.alphaText != null) {
                    Text(button.alphaText, color = AlphaColor, fontSize = 6.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }
            }
            
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = button.mainText,
                    color = if (button.mainText == "SHIFT") ShiftColor else if (button.mainText == "ALPHA") AlphaColor else textColor,
                    fontSize = if (button.type == ButtonType.Function && button.mainText.length > 2) 9.sp else 16.sp,
                    fontWeight = if (button.type == ButtonType.Equals || button.type == ButtonType.Action) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun HistoryList() {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(4) { idx ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                val expr = when(idx) {
                    0 -> "∫(0→π) sin(x) dx"
                    1 -> "d/dx (x³ + 2x) @ x=2"
                    2 -> "log₂(1024)"
                    else -> "√(144) + 5²"
                }
                val res = when(idx) {
                    0 -> "2"
                    1 -> "14"
                    2 -> "10"
                    else -> "37"
                }
                Text(expr, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(res, color = KeyEquals, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
