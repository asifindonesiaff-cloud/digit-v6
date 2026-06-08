package com.example.ui.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DigitTabHeader
import com.example.ui.student.Primary
import com.example.ui.student.PrimaryContainer
import com.example.ui.student.PrimaryFixed
import com.example.ui.student.SecondaryContainer
import com.example.ui.student.SurfaceCard
import com.example.ui.student.SurfaceContainerHighest
import com.example.ui.student.SurfaceContainerLow
import com.example.ui.student.SurfaceBg
import com.example.ui.student.OnSurface
import com.example.ui.student.OnSurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherProgressScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLessons: () -> Unit,
    onNavigateToQuizzes: () -> Unit,
    onNavigateToAlerts: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    Scaffold(
        containerColor = SurfaceBg,
        topBar = {
            TeacherHeader(
                onNavigateToAlerts = onNavigateToAlerts,
                onNavigateToProfile = onNavigateToProfile
            )
        },
        bottomBar = {
            TeacherBottomNavBar(
                currentRoute = "Progress",
                onNavigateToHome = onNavigateToHome,
                onNavigateToLessons = onNavigateToLessons,
                onNavigateToProgress = { },
                onNavigateToQuizzes = onNavigateToQuizzes
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 16.dp
            )
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Class Progress", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Primary, lineHeight = 40.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Analyze your students' performance.", fontSize = 14.sp, color = OnSurfaceVariant)
                }
            }
            
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    DashboardStatCard(
                        title = "AVERAGE SCORE",
                        value = "85%",
                        subtitle = "+5% from last week",
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        iconCol = PrimaryContainer,
                        iconTint = PrimaryFixed
                    )

                    DashboardStatCard(
                        title = "ASSIGNMENT COMPLETION",
                        value = "92%",
                        subtitle = "Needs attention for 3 students",
                        icon = Icons.Default.CheckCircle,
                        iconCol = SecondaryContainer,
                        iconTint = Primary
                    )
                }
            }

            item {
                Text("Recent Activity", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Primary, modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    StudentActivityCard(
                        studentName = "Rahul Das",
                        activity = "Completed 'Solar System Quiz'",
                        time = "2 hours ago",
                        icon = Icons.Default.Quiz
                    )

                    StudentActivityCard(
                        studentName = "Nisha Rahman",
                        activity = "Submitted Assignment 3",
                        time = "4 hours ago",
                        icon = Icons.Default.Assignment
                    )

                    StudentActivityCard(
                        studentName = "Aminul Islam",
                        activity = "Asked a question in Lesson Lab",
                        time = "5 hours ago",
                        icon = Icons.Default.QuestionAnswer
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun StudentActivityCard(studentName: String, activity: String, time: String, icon: ImageVector) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = SurfaceCard,
        shadowElevation = 0.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceContainerHighest)
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = SecondaryContainer.copy(alpha = 0.15f), modifier = Modifier.size(52.dp)) {
                Icon(icon, contentDescription = null, tint = Primary, modifier = Modifier.padding(14.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(studentName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OnSurface)
                Text(activity, fontSize = 13.sp, color = OnSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Text(time, fontSize = 11.sp, color = Primary)
            }
        }
    }
}
