package com.phoenix.phoenixnotes.ui.home

import android.app.Activity
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
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
import com.phoenix.phoenixnotes.utils.ColorUtil
import com.phoenix.phoenixnotes.utils.SpeechUtil
import com.phoenix.phoenixnotes.utils.views.StaggeredVerticalGrid
import org.joda.time.format.ISODateTimeFormat


@ExperimentalUnsignedTypes
@ExperimentalFoundationApi
@Composable
fun Home(navController: NavController) {
    val viewModel = hiltNavGraphViewModel<HomeViewModel>()
    viewModel.getAllNotes()

    val notesState by viewModel.homeState.collectAsState()

    val speechIntentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val result = it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            if (!result.isNullOrEmpty()) {
                val content = result[0]
                val newNote =
                    Note(
                        id = "",
                        title = "",
                        content,
                        dateCreated = "",
                        ColorUtil.noteColors()[0].color.value.toLong()
                    )
                newNote.isFromSpeech = true

                openCreateNotePage(navController, newNote)
            }
        }
    }

    Scaffold(
        Modifier.fillMaxSize(),
        backgroundColor = Black300,
        content = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                HomeTopBar()
                HomeContent(notes = notesState.notes) {
                    openCreateNotePage(navController, it)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openCreateNotePage(navController, null)
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
        bottomBar = {
            HomeBottomBar(onRecordingClicked = {
                SpeechUtil.getSpeechIntent(speechIntentLauncher)
            })
        })
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
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_btn),
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
                        start.linkTo(menuIcon.end, margin = 5.dp)
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
fun HomeBottomBar(onRecordingClicked: () -> Unit) {
    BottomAppBar(
        cutoutShape = CircleShape,
        backgroundColor = Black400
    ) {
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(onClick = onRecordingClicked, modifier = Modifier.padding(4.dp)) {
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
fun HomeContent(notes: List<Note>, onNoteClicked: (Note) -> Unit) {
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
            notes.sortedWith { note1, note2 ->
                val firstDate = ISODateTimeFormat.dateTime().parseDateTime(note1.dateCreated)
                val secondDate = ISODateTimeFormat.dateTime().parseDateTime(note2.dateCreated)

                secondDate.compareTo(firstDate)
            }.forEach {
                NoteItem(it, onNoteClicked)
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
fun NoteItem(note: Note, onNoteClicked: (Note) -> Unit) {
    Card(
        shape = RoundedCornerShape(7.dp),
        border = BorderStroke(0.5.dp, White200), modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color(note.color))
            .padding(5.dp)
            .clickable {
                onNoteClicked(note)
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
    //HomeBottomBar()
}

@ExperimentalUnsignedTypes
@ExperimentalFoundationApi
@Composable
@Preview(showBackground = true)
fun HomeContentPreview() {
    // HomeContent(listOf())
}

fun openCreateNotePage(navController: NavController, note: Note?) {
    navController.currentBackStackEntry?.arguments?.putSerializable("note", note)
    navController.navigate("createNote")
}