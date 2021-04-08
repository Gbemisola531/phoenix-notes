package com.phoenix.phoenixnotes.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.phoenix.phoenixnotes.R
import com.phoenix.phoenixnotes.data.model.Note
import com.phoenix.phoenixnotes.ui.theme.Black300
import com.phoenix.phoenixnotes.ui.theme.Black400
import com.phoenix.phoenixnotes.ui.theme.White200
import com.phoenix.phoenixnotes.utils.views.StaggeredVerticalGrid

@ExperimentalUnsignedTypes
@ExperimentalFoundationApi
@Composable
fun Home(navController: NavController) {
    val viewModel = hiltNavGraphViewModel<HomeViewModel>()
    viewModel.getAllNotes()

    val notesState by viewModel.homeState.collectAsState()

    Scaffold(
        Modifier.fillMaxSize(),
        backgroundColor = Black300,
        content = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                HomeTopBar()
                HomeContent(notes = notesState.notes)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("createNote")
                },
                shape = CircleShape,
                backgroundColor = Black400,
                modifier = Modifier.padding(3.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.create_note_icon)
                )
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = { HomeBottomBar() })
}

@Composable
fun HomeTopBar() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
    )

    Surface(
        Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(start = 15.dp, end = 15.dp, top = 20.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp, color = Black400
    ) {
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .clickable {

            }) {
            val (menuIcon, searchTxt, userIcon) = createRefs()

            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.menu_btn),
                modifier = Modifier.constrainAs(menuIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, margin = 5.dp)
                    bottom.linkTo(parent.bottom)
                }
            )

            Text(
                text = stringResource(R.string.search_your_notes),
                modifier = Modifier
                    .padding(start = 5.dp)
                    .constrainAs(searchTxt) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(menuIcon.end, margin = 10.dp)
                    }
            )

            Surface(
                Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .constrainAs(userIcon) {
                        end.linkTo(parent.end, margin = 5.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                Box(Modifier.background(Color.Red)) {
                    Text(text = "G", Modifier.align(Alignment.Center), fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun HomeBottomBar() {
    BottomAppBar(
        cutoutShape = CircleShape,
        backgroundColor = Black400
    ) {
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(onClick = {}, modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.Default.Mic, contentDescription = "Microphone icon")
        }
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(5.dp)

        )
        IconButton(onClick = {}, modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.Default.Image, contentDescription = "Image icon")
        }
    }
}

@ExperimentalUnsignedTypes
@ExperimentalFoundationApi
@Composable
fun HomeContent(notes: List<Note>) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(start = 5.dp)
    ) {
        Text(
            text = stringResource(R.string.notes), fontSize = 12.sp,
            modifier = Modifier.padding(start = 15.dp, top = 15.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )

        StaggeredVerticalGrid(
            maxColumnWidth = 220.dp,
            modifier = Modifier.padding(start = 2.dp, end = 5.dp)
        ) {
            notes.forEach {
                NoteItem(it)
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
    }
}

@ExperimentalUnsignedTypes
@Composable
fun NoteItem(note: Note) {
    Card(
        shape = RoundedCornerShape(7.dp),
        border = BorderStroke(0.5.dp, White200), modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color(note.color))
            .padding(5.dp)
            .clickable {
            },
        backgroundColor = Color(note.color.toULong())
    ) {
        Column(Modifier.padding(8.dp)) {
            if (note.title.isNotEmpty()) {
                Text(
                    text = note.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
            if (note.content.isNotEmpty()) {
                var content = note.content
                if (content.length > 200) content = content.substring(0, 200).plus("...")

                Text(text = content, fontSize = 14.sp)
            }

            if (note.title.isEmpty())
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeTopBarPreview() {
    HomeTopBar()
}

@Composable
@Preview(showBackground = true)
fun HomeBottomBarPreview() {
    HomeBottomBar()
}

@ExperimentalUnsignedTypes
@ExperimentalFoundationApi
@Composable
@Preview(showBackground = true)
fun HomeContentPreview() {
    HomeContent(listOf())
}