import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quizizz_app.R
import com.example.quizizz_app.models.QuizResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedCounterRank(
    titleList: List<String>,
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // Trạng thái cho dropdown
    var selectedItem by remember { mutableStateOf(titleList[0]) } // Mục đã chọn

    Column(
        modifier = modifier
            .border(3.dp, Color.White, shape = RoundedCornerShape(10.dp))
            .height(50.dp)
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedItem,
                modifier = Modifier.weight(1f), // Cân đều nội dung trong Row
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                ),
                color = Color.White
            )
            IconButton(
                onClick = { expanded = !expanded } // Xử lý khi nhấn vào biểu tượng
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            titleList.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        selectedItem = item // Cập nhật mục đã chọn
                        expanded = false // Đóng menu
                        onItemSelected(item)
                    },
                    text = {
                        Text(text = item, color = Color.Black)
                    }
                )
            }
        }
    }
}

@Composable
fun ItemRankUser(
    quizResult: QuizResult,
    index: Int,
    selectedChooseRank: String,
    currentUserId: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .border(1.dp, if (quizResult.userId == currentUserId) Color(0xff191837) else Color.Transparent, RoundedCornerShape(50.dp))
                .size(40.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "${index + 1}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Center
                ),
                color = Color(0xff797a99),
            )
        }
        Spacer(modifier = Modifier.padding(12.dp)) // Khoảng cách giữa ảnh và tên người chơi
        if (quizResult.imageUrl != "") {
            AsyncImage(
                model = quizResult.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White),
                contentScale = ContentScale.Crop
            )
        } else {
            // Hiển thị icon khác nếu imageUrl là null
            Image(
                painter = painterResource(id = R.drawable.petsss) ,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White)
            )
        }
        val value = when (selectedChooseRank) {
            "Điểm cao nhất" -> quizResult.score.toString()
            "Chuỗi trả lời" -> quizResult.highestStreak.toString()
            "Tổng câu đúng" -> quizResult.totalCorrectAnswers.toString()
            else -> ""
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Column {
            Text(
                text = "${quizResult.userName}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xff191837),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xff191837)
            )
        }

    }
}

//@Composable
//@Preview
//fun PreviewAnimatedCounterRank() {
//    ItemRankUser(
//        quizResult = QuizResult(
//            "01",
//            "02",
//            "Thien",
//            "01",
//            20,
//            200,
//            20,
//            21,
//            ""
//        ),
//        index = 1,
//        selectedChooseRank = "Điểm cao nhất"
//    )
//}

